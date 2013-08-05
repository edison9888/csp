
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.io.IOException;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * @author xiaodu
 *
 * ÏÂÎç6:54:55
 */
public class Log4jDailyRollingFiles extends DailyRolingFiles{
	
	
	private Logger appStatLog;
	
	public static final String M = "%m%n";
	
	
	public Log4jDailyRollingFiles(String logPath,String logFileName){
		super(logPath,logFileName);
		
		appStatLog = Logger.getLogger("MonitorLogNotes -" + getFileName());
		PatternLayout layout = new PatternLayout(M);
		FileAppender appender = null;
		try {
			appender = new DailyRollingFileAppender(layout, getFilePath()+getFileName(), YYYY_MM_DD);
			appender.setAppend(true);
			appender.setEncoding(GBK);
		} catch (IOException e) {
			//logger.error("MonitorLog initLog4j error", e);
		}
		if (appender != null) {
			appStatLog.removeAllAppenders();
			appStatLog.addAppender(appender);
		}
		appStatLog.setLevel(Level.INFO);
		appStatLog.setAdditivity(false);
		
	}
	

	/* (non-Javadoc)
	 * @see com.taobao.csp.log.DailyRolingFiles#writerInfo(com.taobao.csp.log.Keys, com.taobao.csp.log.NoteLog)
	 */
	@Override
	public void writerInfo(Keys key, NoteLog values) {
		if(appStatLog != null){
			StringBuilder sb = new StringBuilder();
			sb.append(getFormatTime()).append(MonitorConstants.SPLITTER_CHAR);
			sb.append( key.getString()).append( values.getString());
			appStatLog.info(sb.toString());
		}
		
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.log.DailyRolingFiles#close()
	 */
	@Override
	public void close() {
		if(appStatLog != null){
			appStatLog.removeAllAppenders();
		}
	}

}
