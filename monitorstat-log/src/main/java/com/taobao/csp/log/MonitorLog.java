
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;



/**
 * @author xiaodu
 *
 * 下午4:56:48
 */
public class MonitorLog {
	
	private static Logger logger = Logger.getLogger(MonitorLog.class);
	
	public static final String USER_HOME = "user.home";
	
	public static final String DIR_NAME = "logs" + File.separator + "monitor";
	
	private static String DEFAULT_LOGFILE_NAME =MonitorLog.class.getClassLoader().getClass().getCanonicalName();;

	private static String DEFAULT_LOG_PATH = "/home/admin/logs/monitor";
	
	private static Map<String,MonitorLogNotes> logNoteMap = new ConcurrentHashMap<String, MonitorLogNotes>();
	
	private static final ReentrantLock logNoteLock = new ReentrantLock();
	
	private static final ReentrantLock timerLock = new ReentrantLock();
	
	private static final Condition condition = timerLock.newCondition();
	
	private static long waitTime =60L;
	
	private static Thread writerThread = null;
	
	private static boolean isCompress = false;
	
	static{
		/** 动态创建日志记录的配置 */
		String userHome = System.getProperty(USER_HOME);
		if (!userHome.endsWith(File.separator)) {
			userHome += File.separator;
		}
		
		DEFAULT_LOG_PATH = userHome + DIR_NAME + File.separator;
		File dir = new File(DEFAULT_LOG_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		runWriteThread() ;
		setupAutoFlushOnShutdown();
	}
	
	public static void addStat(String keyOne, String keyTwo, String keyThree) {
		addStat(new String[]{keyOne,keyTwo,keyThree}, new Long[]{1l});
	}
	
	public static void addStat(String keyOne, String keyTwo, String keyThree,long value1) {
		addStat(new String[]{keyOne,keyTwo,keyThree}, new Long[]{value1});
	}
	
	public static void addStat(String keyOne, String keyTwo, String keyThree,long value1, long value2) {
		addStat(new String[]{keyOne,keyTwo,keyThree}, new Long[]{value1,value2});
	}
	
	
	public static void addStat(String[] keys, Long[] values) {
		addStat(DEFAULT_LOGFILE_NAME, keys,  values);
	}
	
	public static void addStat(String[] keys, String[] values) {
		addStat(DEFAULT_LOGFILE_NAME, keys,  values);
	}
	
	public static void addStat(String fileName,String[] keys, Long[] values) {
		getNote(fileName).addStat(keys, values);
	}
	
	public static void addStat(String fileName,String[] keys, String[] values) {
		
		getNote(fileName).addStat(keys, values);
	}
	
	
	/**
	 * 设置统计数据输出的频率，单位是秒
	 * @param time
	 */
	public static void setWaitTime(long time){
		if(time >1){
			waitTime = time;
		}
	}
	
	/**
	 * 设置是否 对key 做压缩，true 是压缩，false 不压缩。默认是false
	 * @param compress
	 */
	public static void setCompress(boolean compress){
		isCompress = compress;
	}
	/**
	 * 设置在note 中最多缓存 key的个数，如果超过这个设置数量，将直接跳过不做统计
	 * @param size
	 */
	public static void setMaxKeySize(int size){
		
		logNoteLock.lock();
		try{//防止在写入磁盘的时候，有新的MonitorLogNotes 产生
			for(Map.Entry<String,MonitorLogNotes> entry:logNoteMap.entrySet() ){
				entry.getValue().setMaxKeySize(size);
			}
		}finally{
			logNoteLock.unlock();
		}
		
		
	}
	/**
	 * 设置最大压缩的key的个数，这个设置只有在 setCompress = true 有效
	 * @param size
	 */
	public static void setMaxCompressKeySize(int size){
		logNoteLock.lock();
		try{//防止在写入磁盘的时候，有新的MonitorLogNotes 产生
			for(Map.Entry<String,MonitorLogNotes> entry:logNoteMap.entrySet() ){
				entry.getValue().setMaxCompressKeySize(size);
			}
		}finally{
			logNoteLock.unlock();
		}
	}
	
	
	
	/**
	 * 获取一个MonitorLogNotes 
	 * @param fileName
	 * @return
	 */
	private static MonitorLogNotes getNote(String fileName){
		
		MonitorLogNotes lognote = logNoteMap.get(fileName);
		if(lognote == null){
			logNoteLock.lock();
			try {
				lognote = logNoteMap.get(fileName);
				if(lognote == null){
					lognote = new MonitorLogNotes(DEFAULT_LOG_PATH, fileName,isCompress);
					logNoteMap.put(fileName, lognote);
				}
			}finally{
				logNoteLock.unlock();
			}
		}
		
		return lognote;
		
	}
	
	/**
	 * 做一个Hook  未必有用。
	 */
	private static void setupAutoFlushOnShutdown() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				flush();
			}
		}));
	}
	
	/**
	 * 将MonitorLogNotes 中缓存统计的数据，写入到磁盘中
	 */
	private static void flush(){
		logNoteLock.lock();
		try{//防止在写入磁盘的时候，有新的MonitorLogNotes 产生
			for(Map.Entry<String,MonitorLogNotes> entry:logNoteMap.entrySet() ){
				entry.getValue().flushWriter();
			}
		}finally{
			logNoteLock.unlock();
		}
	}
	
	
	
	
	/**
	 * 执行写入线程
	 */
	private  static void runWriteThread() {
		if (null != writerThread) { // 如果线程还存在，先interrupt一下
			try {
				writerThread.interrupt();
			} catch (Exception e) {
				logger.error("interrupt write thread error", e);
			}
		}
		// 初始化线程
		writerThread = new Thread(new Runnable() {
			public void run() {
				// 等待waitTime秒
				while (true) {
					timerLock.lock();
					try {
						condition.await(waitTime, TimeUnit.SECONDS);
					} catch (Exception e) {
						logger.error("wait error", e);
					} finally {
						timerLock.unlock();
					}
					flush();
				}
			}
		});
		writerThread.setDaemon(true);
		writerThread.setName("MonitorLog - Thread");
		writerThread.start();
	}

	
	

}
