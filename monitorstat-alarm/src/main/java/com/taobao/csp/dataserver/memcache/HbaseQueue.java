
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.taobao.csp.dao.hbase.base.HBaseUtil;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.event.CspBdbQueue;
import com.taobao.monitor.MonitorLog;

/**
 * @author xiaodu
 *
 * 下午2:00:26
 */
public class HbaseQueue implements Runnable{
	private static final Logger logger = Logger.getLogger(HbaseQueue.class);
	
	private LinkedBlockingQueue<String[]> queue = new LinkedBlockingQueue<String[]>();

	private static int queue_size = 3;
	public static int MAX_QUEUE_SIZE = 300000;
	//可以入BDB的容量
	public static int BDB_QUEUE_SIZE = 200000;
	
	private static HbaseQueue[] hq = new HbaseQueue[queue_size];
	static {
		for(int i=0;i<queue_size;i++)
			hq[i] = new HbaseQueue(i);
	}
	
	private static Random random = new Random();
	
	public static HbaseQueue get(){
		return hq[random.nextInt(queue_size)];
	}
	
	private Thread thread = null;

	private HbaseQueue(int size){
		thread = new Thread(this);
		thread.setName("Thread - HbaseQueue -"+size);
		thread.start();

		CspBdbQueue.initConsume();
	}
	
	public void addData(String id,String prop,String value){

		String[] tmp = new String[3];
		tmp[0] = id;
		tmp[1] = prop;
		tmp[2] = value;
		
		addData(tmp);
	}
	
	public void addData(String[] data){
		
		if(queue.size() >BDB_QUEUE_SIZE){
			CspBdbQueue.insert(data);
			return ;
		}
		queue.add(data);
	}
	
	public int getQueueSize(){
		return queue.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true){
			String[]  v = null;
			try {
				while((v =queue.take())!=null){
					long time = System.currentTimeMillis();
					try {
						HBaseUtil.addRow(v[0], v[1], v[2]);
						//Thread.sleep(1000);
					} catch (Exception e) {
						MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"HbaseInsertException"}, new Long[]{1l,System.currentTimeMillis()-time});
						logger.error("插入Hbase异常，write to hbase exception",e);
					}
					MonitorLog.addStat(Constants.DATA_PERSISTENCE_LOG, new String[]{"HbaseInsertSuccess"}, new Long[]{1l,System.currentTimeMillis()-time});
				}
			} catch (InterruptedException e) {
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		//
		HbaseQueue.MAX_QUEUE_SIZE=300;
		CspBdbQueue.SleepTime=1000;
		CspBdbQueue.setBdbDeal(BdbDeal.DEAL_NOTHING);
		
		for(int i=0;i<1000;i++){
			String[] ss= new String[]{"k"+i,"kk"+i,"kkk"+i};
			HbaseQueue.get().addData(ss);
		}
		System.out.println(CspBdbQueue.keyIndex);
	}
}
