
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author xiaodu
 *
 * 下午6:52:12
 */
public abstract class DailyRolingFiles {
	
	public static final String YYYY_MM_DD = "'.'yyyy-MM-dd";
	
	public static final String GBK = "GBK";	
	
	public static final String LOG_DATE_FORMAT = "HH:mm:ss"; //日志输出的时间
	
	private String filePath;
	
	private String fileName;
	public String getFilePath() {
		return filePath;
	}
	public String getFileName() {
		return fileName;
	}

	public DailyRolingFiles(String filePath,String fileName){
		this.fileName = fileName;
		this.filePath = filePath;
	}
	
	
	protected String getFormatTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(LOG_DATE_FORMAT);
		String time = dateFormat.format(Calendar.getInstance().getTime());
		return time;
	}
	
	public  abstract void writerInfo(Keys key,NoteLog values);
	
	
	public abstract void close();
	

}
