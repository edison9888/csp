
package com.taobao.monitor.stat.analyse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.exception.NoAppLogFileException;
import com.taobao.monitor.stat.util.Config;
import com.taobao.monitor.common.util.Constants;

/**
 * 
 * @author xiaodu
 * @version 2010-4-2 ����06:02:19
 */
public abstract class AnalyseFile extends Analyse{
	
	private static final Logger logger =  Logger.getLogger(AnalyseFile.class);
	
	private String logFileName;  //��־����
	private String logFileDir;	//��־����Ŀ¼
	
	private char sp = '\n';
	
	public AnalyseFile(String appName,String logFileName,char sp) throws Exception{		
		this(appName,logFileName);
		this.sp = sp;
	}

	public AnalyseFile(String appName,String logFileName) throws Exception{
		super(appName);
		if(appName==null||logFileName==null){
			throw new Exception("������Ч����  appName:"+this.getAppName()+" logName:"+logFileName);
		}		
		
		this.logFileName = logFileName;
		this.logFileDir = Config.getValue("LOG_PATH")+"/"+this.getAppName();
		
		File logPath = new File(this.logFileDir);
		if(!logPath.exists()||!logPath.isDirectory()){
			throw new NoAppLogFileException("Ӧ��:"+this.getAppName()+" �޶�Ӧ����־Ŀ¼");
		}
		
		if(logPath.list().length<1){
			throw new NoAppLogFileException("Ӧ��:"+this.getAppName()+" Ŀ¼����־����Ϊ��");
		}
	}
	
	
	public String getLogFileDir() {
		return logFileDir;
	}
	
		
	public String getLogFileName() {
		return logFileName;
	}	
	/**
	 * ������־�ļ�
	 * ͨ������ʱ�����������ʱ���ڵļ�¼ д����ʱ�ļ�tmp_log ��
	 * Ȼ��ͨ��parseMinFile ����ͳ�Ʋ�������
	 * Ȼ��ɾ����ʱ�ļ�
	 * 
	 */
	private void analyse(ReportContentInterface content){		
		String logPath = this.getLogFileDir();
		
		
		File logFies = new File(logPath);
		
		File[] _logs = logFies.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {				
				if(name.indexOf(AnalyseFile.this.getLogFileName())>-1){
					return true;
				}
				return false;
			}});
		this.setLogFileNumber(_logs.length);
		
		logger.info("��ʼ ����"+this.getAppName()+" "+this.getLogFileName()+" ����:"+_logs.length);
		long readerCount = 0;
		long readerTime = System.currentTimeMillis();
		int i=0;
		for(File log:_logs){			
			logger.info(log.getPath());
			try {
				BufferedReader2 reader = new BufferedReader2(new InputStreamReader(new FileInputStream(log),"gbk"),this.sp);
				String str = null;
				while((str=reader.readLine())!=null){
					if(this.getCollectDate().equals(parseLogLineCollectDate(str))){
						parseLogLine(str);			
						readerCount++;					
						if(readerCount%100000==0){						
							logger.info("����"+this.getAppName()+" "+this.getLogFileName()+" ["+_logs.length+":"+i+"]"+log.getPath()+"100000��:"+(System.currentTimeMillis()-readerTime));
							readerTime = System.currentTimeMillis();
						}
					}else{
						logger.debug("��¼:"+str+" ����ͳ��ʱ����");
					}
				}
				reader.close();
				i++;
			} catch (Exception e) {
				logger.error("log.getPath",e);
			} 
			
			log.delete();
			logger.info("��������  ɾ����־:"+log.getPath());
		}
		logger.info("�����������"+this.getAppName()+" "+this.getLogFileName()+" ����:"+_logs.length);
	
	}
	
	final public void analyseLogFile(ReportContentInterface content){
		analyse(content);
		insertToDb(content);
	}	


	/**
	 * ����ÿ����¼ ���ؽ��
	 * @param logRecord
	 * @return
	 */
	protected abstract void parseLogLine(String logRecord);
	/**
	 * 
	 * @param logRecord
	 * @return yyyy-MM-dd HH:mm
	 */
	protected abstract String parseLogLineCollectTime(String logRecord);
	/**
	 * 
	 * @param logRecord
	 * @return yyyy-MM-dd
	 */
	protected abstract String parseLogLineCollectDate(String logRecord);
	
	
	
	
	
	

	

}
