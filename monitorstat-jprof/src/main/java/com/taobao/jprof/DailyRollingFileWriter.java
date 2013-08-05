package com.taobao.jprof;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 
 * @author xiaodu
 * @version 2010-7-8 下午01:20:45
 */
public class DailyRollingFileWriter {

	private SimpleDateFormat sdf = new SimpleDateFormat("'.'yyyy-MM-dd");

	private static String FILE_NAME = "jprof.txt";

	private String rollingFileName;

	private String fileName;

	private RollingCalendar rollingCalendar = new RollingCalendar();
	
	private long nextRollingTime = rollingCalendar.getNextRollingMillis(new Date());

	private BufferedWriter bufferedWriter;
	
	
	public DailyRollingFileWriter() {
		this.fileName = FILE_NAME;
		createWriter(FILE_NAME, 8 * 1024);
		rollingFileName = FILE_NAME + sdf.format(new Date());
	}

	public DailyRollingFileWriter(String filePath) {
		this.fileName = filePath;
		createWriter(filePath, 8 * 1024);
		rollingFileName = this.fileName + sdf.format(new Date());
	}

	public void append(String log) {
		long time  = System.currentTimeMillis();
		if (time > nextRollingTime) {//是否已经到达 rolling 时间								
			nextRollingTime = rollingCalendar.getNextRollingMillis(new Date());
			rolling();
		}
		subappend(log);
	}
	
	public void flushAppend(){
		if(this.bufferedWriter!=null){
			try {
				this.bufferedWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void subappend(String log){		
		try {
			this.bufferedWriter.write(log);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private void closeFile() {
		if(this.bufferedWriter!=null){
			try {
				this.bufferedWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void rolling() {
		Date now = new Date();
		String datedFilename = this.fileName + sdf.format(now);//这个新 日志文件
		if (rollingFileName.equals(datedFilename)) {//
			return;
		}
		this.closeFile();
		File target = new File(rollingFileName);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(fileName);
		file.renameTo(target);
		this.createWriter(fileName, 8 * 1024);
		rollingFileName = datedFilename;
	}

	private void createWriter(String filename, int bufferSize) {
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(filename), bufferSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class RollingCalendar {
		private static final long serialVersionUID = -3560331770601814177L;

		RollingCalendar() {
			super();
		}

		public long getNextRollingMillis(Date now) {
			return getNextRollingDate(now).getTime();
		}

		public Date getNextRollingDate(Date now) {
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(now);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DATE, 1);
			return cal.getTime();
		}
	}
}
