
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
 * ����4:56:48
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
		/** ��̬������־��¼������ */
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
	 * ����ͳ�����������Ƶ�ʣ���λ����
	 * @param time
	 */
	public static void setWaitTime(long time){
		if(time >1){
			waitTime = time;
		}
	}
	
	/**
	 * �����Ƿ� ��key ��ѹ����true ��ѹ����false ��ѹ����Ĭ����false
	 * @param compress
	 */
	public static void setCompress(boolean compress){
		isCompress = compress;
	}
	/**
	 * ������note ����໺�� key�ĸ���������������������������ֱ����������ͳ��
	 * @param size
	 */
	public static void setMaxKeySize(int size){
		
		logNoteLock.lock();
		try{//��ֹ��д����̵�ʱ�����µ�MonitorLogNotes ����
			for(Map.Entry<String,MonitorLogNotes> entry:logNoteMap.entrySet() ){
				entry.getValue().setMaxKeySize(size);
			}
		}finally{
			logNoteLock.unlock();
		}
		
		
	}
	/**
	 * �������ѹ����key�ĸ������������ֻ���� setCompress = true ��Ч
	 * @param size
	 */
	public static void setMaxCompressKeySize(int size){
		logNoteLock.lock();
		try{//��ֹ��д����̵�ʱ�����µ�MonitorLogNotes ����
			for(Map.Entry<String,MonitorLogNotes> entry:logNoteMap.entrySet() ){
				entry.getValue().setMaxCompressKeySize(size);
			}
		}finally{
			logNoteLock.unlock();
		}
	}
	
	
	
	/**
	 * ��ȡһ��MonitorLogNotes 
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
	 * ��һ��Hook  δ�����á�
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
	 * ��MonitorLogNotes �л���ͳ�Ƶ����ݣ�д�뵽������
	 */
	private static void flush(){
		logNoteLock.lock();
		try{//��ֹ��д����̵�ʱ�����µ�MonitorLogNotes ����
			for(Map.Entry<String,MonitorLogNotes> entry:logNoteMap.entrySet() ){
				entry.getValue().flushWriter();
			}
		}finally{
			logNoteLock.unlock();
		}
	}
	
	
	
	
	/**
	 * ִ��д���߳�
	 */
	private  static void runWriteThread() {
		if (null != writerThread) { // ����̻߳����ڣ���interruptһ��
			try {
				writerThread.interrupt();
			} catch (Exception e) {
				logger.error("interrupt write thread error", e);
			}
		}
		// ��ʼ���߳�
		writerThread = new Thread(new Runnable() {
			public void run() {
				// �ȴ�waitTime��
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
