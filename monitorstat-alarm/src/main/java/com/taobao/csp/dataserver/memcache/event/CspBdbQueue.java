package com.taobao.csp.dataserver.memcache.event;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.BdbDeal;
import com.taobao.csp.dataserver.memcache.HbaseQueue;
import com.taobao.csp.dataserver.util.Util;
import com.taobao.monitor.MonitorLog;

/**
 * BDB本地缓存队列
 * 
 * 单写，3个线程 50W数据，insert cost time:37085,tps4W
 * 单线程读,			read cost time:86660,qps 1.7W
 * 读写混合			insert cost time:123755,tps 1.2W
 * 					read cost time:104592,qps	1.4W	
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-11-13
 */
public class CspBdbQueue {
	private static final Logger logger = Logger.getLogger(CspBdbQueue.class);

	private static String FILE_PATH = "/home/admin/bdb/bdata";
	private static String SOURCE_DB = "csp_source_db";

	private static EnvironmentConfig envConfig;
	private static Environment env;
	private static DatabaseConfig dbConfig;
	private static Database database;

	private static BdbDeal bdbDeal=new BdbDeal() {
		public void doValue(byte[] vdata) {
			try {
				String[] v = (String[])JavaCodec.decodeObject(vdata);
				if(v!=null){
					HbaseQueue.get().addData(v);
				}
			} catch (Exception e) {
				logger.warn("HbaseQueue bdb addData exception",e);
			}
		}
	};

	/**
	 * 每个value给一个自增的key，可以用来表示写入的记录数
	 */
	public static AtomicLong keyIndex = new AtomicLong(0);
	public static long totalRecovery =0;
	public static int SleepTime=60 * 1000*2;
	public static boolean isInit=false;
	
	/**
	 * 初始化消费者线程
	 * 初始化多次没有影响
	 */
	public static synchronized void initConsume(){
		if(!isInit){
			isInit=true;
			new Thread() {
				public void run() {
					while (true) {
						try {
							int getFreeSpace=Util.getHomeFreeSpace();
							//小于10个G，不允许入本地磁盘，丢数据
							if(getFreeSpace<10){
								Constants.IN_BDB=false;
							}else{
								Constants.IN_BDB=true;
							}
							
							double halfQueueSize=HbaseQueue.MAX_QUEUE_SIZE*0.5;
							long count=0;
							if(HbaseQueue.get().getQueueSize()<halfQueueSize ){
								count=readByCursor();
							}
							logger.warn("=================read "+count+","+
									HbaseQueue.get().getQueueSize()+" record,sleep 1 minute=============");
							
							Thread.sleep(SleepTime);
						} catch (Exception e) {
							logger.warn("recovry from queue exception",e);
						}
					}
				}
			}.start();
		}
	}
	
	/**
	 * 写入数据
	 * @param obj
	 */
	public static void insert(Object obj) {
		if(!Constants.IN_BDB){
			MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"Bdb Ingore"}, new Long[]{1l});
			return;
		}
		Long kIndex = keyIndex.getAndIncrement();
		try {
			byte[] vbytes = serialData(obj);
			byte[] kbytes = serialData("j"+kIndex);

			DatabaseEntry keyEntry = new DatabaseEntry(kbytes);
			DatabaseEntry valueEntry = new DatabaseEntry(vbytes);

			OperationStatus rtn = database.put(null, keyEntry, valueEntry);
			if (rtn != OperationStatus.SUCCESS) {
				logger.warn("write to bdb fail" + rtn.name());
				MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"Bdb Write Fail"}, new Long[]{1l});
			} else {
				MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"Bdb Success"}, new Long[]{1l});
			}
		} catch (Exception e) {
			logger.error("write to bdb exception", e);
			MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"Bdb Write Exception"}, new Long[]{1l});
		}
	}

	/**
	 * 序列化数据 暂时就只有JAVA序列化
	 * @param obj
	 * @return
	 */
	public static byte[] serialData(Object obj){
		try {
			return JavaCodec.encodeObject(obj);
		} catch (IOException e) {
			logger.warn("HbaseQueue serialData data exception",e);
		}
		return null;
	}
	/**
	 * 处理数据
	 * @param vdata
	 */
	public static void doValue(byte[] vdata) {
		try {
			bdbDeal.doValue(vdata);
		} catch (Exception e) {
			logger.warn("HbaseQueue bdb addData exception",e);
		}
	}
	//private static DiskOrderedCursorConfig docc=new DiskOrderedCursorConfig();

	private static long readByCursor() {
		long count=0;
		 
		//性能高，但是抛异常，未解
		//DiskOrderedCursor  cursor = database.openCursor(docc);
		Cursor cursor = database.openCursor(null, null);
		DatabaseEntry curkey = new DatabaseEntry();
		DatabaseEntry curval = new DatabaseEntry();

		boolean isGoingFull=false;
		try {
			while (cursor.getNext(curkey, curval, null) == OperationStatus.SUCCESS) {
				/*队列接近饱和，停止恢复*/
				if(HbaseQueue.get().getQueueSize()>HbaseQueue.BDB_QUEUE_SIZE){
					isGoingFull=true;
					break;
				}
				byte[] vdata = curval.getData();
				if (null == vdata || vdata.length < 1) {
					continue;
				}
				doValue(vdata);
				OperationStatus ops=cursor.delete();
				//OperationStatus ops=database.delete(null, curkey);
				if(ops!=OperationStatus.SUCCESS){
					logger.warn("delete data faile,"+ops);
				}
				count++;
			}
		} catch (Exception e) {
			logger.warn("read data from bdb Exception,", e);
		} finally {
			if (null != cursor)
				cursor.close();
		}
		if(isGoingFull){
			try {
				logger.warn("queue going full sleep 5min,"+totalRecovery);
				Thread.sleep(1000*60*3);
			} catch (InterruptedException e) {
				//TODO INGORE
			}
			
		}
		totalRecovery=totalRecovery+count;
		return count;
	}
	
	public static void close() {
		long startTs = System.currentTimeMillis();

		try {
			if(database!=null){
				database.close();// 会调用sync
			}
			env.cleanLog();
			env.close();
		} catch (Exception e) {
			//ingore
			logger.warn("close exception " ,e);
		}

		logger.warn("close takes " + (System.currentTimeMillis() - startTs));
	}

	static {
		long stime=System.currentTimeMillis();
		
		File f = new File(FILE_PATH);
		if (!f.exists()) {
			f.mkdirs();
		}
		/**
		 * =====================env配置=======================
		 */
		envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(false);
		/**
		 * 缓存40M
		 * 日志文件最大为20M，默认是10M 
		 */
		envConfig.setCacheSize(1024*1024*40);
		envConfig.setConfigParam("je.log.fileMax",String.valueOf(1024*1024*20)); 
		
		/**
		 * 修改写缓存比例，总共6M
		 */
		envConfig.setConfigParam("je.log.bufferSize",String.valueOf(1024*1024*2)); 
		envConfig.setConfigParam("je.log.totalBufferBytes",String.valueOf(1024*1024*6)); //
		env = new Environment(new File(FILE_PATH), envConfig);
		env.cleanLog();
		/**
		 * db配置
		 */
		dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		//dbConfig.setDeferredWrite(true);
		
		database = env.openDatabase(null, SOURCE_DB, dbConfig);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				close();
				logger.warn("close db and env");
			}
		});
		
		logger.warn("init bdb success " + (System.currentTimeMillis()-stime));
	}
	
	public static void setBdbDeal(BdbDeal bdbDeal) {
		CspBdbQueue.bdbDeal = bdbDeal;
	}

	public static void main(String[] args) throws InterruptedException {
		
		CspBdbQueue.setBdbDeal(BdbDeal.DEAL_NOTHING);
		CspBdbQueue.isInit=true;
		long count=-1;
		while(count!=0){
			count=CspBdbQueue.readByCursor();
			logger.warn("==read "+count+","+" record");
		}
		
		long stime=System.currentTimeMillis();
		final CountDownLatch cdl=new CountDownLatch(3);

		System.out.println("start insert");
		//只有写==========================================
		for(int i=0;i<3;i++){
			new Thread(){
				public void run(){
					for (int i = 0; i < 500000; i++) {
						String[] v = new String[] { "asdasdasdasdasdasdasdsadabc" + i, "deadadqweqwewqef" + i, 
								"ghqweqweqweqwej" + i };

						CspBdbQueue.insert(v);
					}
					cdl.countDown();
				}
			}.start();
		}
		cdl.await();
		
		long send=System.currentTimeMillis();
		System.out.println("insert cost time:"+(send-stime));

//		//测试150W数据下的启动时间
//		if(true){
//			return;
//		}
		
		//只有读==========================================
		count=CspBdbQueue.readByCursor();

		logger.warn("==read "+count+","+" record");
		System.out.println("read cost time:"+(System.currentTimeMillis()-send));
		
		//=====================读写混合===================
		System.out.println("start insert");
		for(int i=0;i<3;i++){
			new Thread(){
				public void run(){
					long s=System.currentTimeMillis();
					
					for (int i = 0; i < 500000; i++) {
						String[] v = new String[] { "asdasdasdasdasdasdasdsadabc" + i, "deadadqweqwewqef" + i, 
								"ghqweqweqweqwej" + i };

						CspBdbQueue.insert(v);
					}
					System.out.println("insert cost time:"+(System.currentTimeMillis()-s));
				}
			}.start();
		}
		
		count=-1;
		while(count!=0){
			count=CspBdbQueue.readByCursor();
			
			logger.warn("==read "+count+","+" record");
		}
		System.out.println("read cost time:"+(System.currentTimeMillis()-send));

		close();
		System.out.println("===end===");
	}
}
