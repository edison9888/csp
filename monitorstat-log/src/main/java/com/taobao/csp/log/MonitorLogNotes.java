
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

/**
 * @author xiaodu
 *
 * 下午1:10:29
 */
public class MonitorLogNotes {
	
	private static Logger logger = Logger.getLogger(MonitorLogNotes.class);
	
	private Map<Keys,NoteLog> keyMap = new ConcurrentHashMap<Keys, NoteLog>();
	
	private DailyRolingFiles writerLog;//统计数据输出类
	
	public int MAX_KEY_SIZE = 5000; //最大的key 缓存数量，如果超过直接抛弃
	
	private  ReentrantLock logNoteLock = new ReentrantLock();
	
	private String logPath;
	
	private String fileName;
	
	private boolean compress = false;
	
	public MonitorLogNotes(String path,String fileName,boolean isCompress){
		this.logPath = path;
		this.fileName = fileName;
		this.compress = isCompress;
		if(isCompress)
			this.writerLog = new CompressedKeyDailyRollingFiles(path,fileName);
		else
			this.writerLog = new Log4jDailyRollingFiles(path,fileName);
	}
	
	public void setMaxKeySize(int size){
		logNoteLock.lock();
		try {
			 if(size >1){
				 MAX_KEY_SIZE = size;
				 logger.info("更新"+this.fileName+" MAX_KEY_SIZE="+size);
			 }else{
				 logger.info("更新"+this.fileName+" MAX_KEY_SIZE="+size+" 数量异常不做更新");
			 }
		}finally{
			logNoteLock.unlock();
		}
	}
	
	
	public void setMaxCompressKeySize(int size){
		if(compress){
			if(this.writerLog instanceof CompressedKeyDailyRollingFiles){
				CompressedKeyDailyRollingFiles w = (CompressedKeyDailyRollingFiles)this.writerLog;
				w.setMaxCompressKeySize(size);
			}
		}
	}

	
	public void addStat(String[] keys,Long[] values){
		Keys tmpKey = new Keys(keys);
		addStat(tmpKey,values);
	}
	
	public void addStat(String[] keys,String[] values){
		Keys tmpKey = new Keys(keys);
		addStat(tmpKey,values);
	}
	
	public void addStat(Keys keys,Long[] values){
		NoteLog log = keyMap.get(keys);
		if(log == null){
			logNoteLock.lock();
			try {//防止在flushWriter过程中出现新的KEY
				 log = keyMap.get(keys);
				 if(log == null){
					 
					 if(!checkKeySize()){
						 return;
					 }
					 
					 log = new NoteLogLong(values.length);
					 keyMap.put(keys, log);
				 }
			}finally{
				logNoteLock.unlock();
			}
		}
		if(log.getNoteLogType() != NoteLog.NoteLongType){
			return ;
		}
		log.addNote(values);
	}
	
	
	public void addStat(Keys keys,String[] values){
		NoteLog log = keyMap.get(keys);
		if(log == null){
			logNoteLock.lock();
			try{//防止在flushWriter过程中出现新的KEY
				 log = keyMap.get(keys);
				 if(log == null){
					 
					 if(!checkKeySize()){
						 return;
					 }
					 log = new NoteLogString();
					 keyMap.put(keys, log);
				 }
			}finally{
				logNoteLock.unlock();
			}
		}
		if(log.getNoteLogType() != NoteLog.NoteStringType){
			return ;
		}
		log.addNote(values);
	}
	
	
	
	private boolean checkKeySize(){
		
		if(keyMap.size() >MAX_KEY_SIZE){
			return false;
		}
		return true;
	}
	
	
	protected void  flushWriter() {
		logNoteLock.lock();
		try {
			for(Map.Entry<Keys,NoteLog> entry:keyMap.entrySet()){
				writerLog.writerInfo(entry.getKey(), entry.getValue());
			}
		}finally{
			logNoteLock.unlock();
		}
	}
	
	
	
	

}
