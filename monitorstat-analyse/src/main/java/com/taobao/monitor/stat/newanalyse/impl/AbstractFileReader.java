
package com.taobao.monitor.stat.newanalyse.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2010-5-10 ÏÂÎç07:44:28
 */
public abstract class AbstractFileReader {
	
	private static final Logger logger =  Logger.getLogger(AbstractFileReader.class);	
	
	private File filePath;
	
	private char recordSeparator;
	
	private String fieldSeparator;
	
	private String collectTimePattern;
	
	private String collectTimeFormat;
	
	
	
	public AbstractFileReader(File filePath){
		this.filePath = filePath;
	}

	public File getFilePath() {
		return filePath;
	}

	public void setFilePath(File filePath) {
		this.filePath = filePath;
	}

	public char getRecordSeparator() {
		return recordSeparator;
	}

	public void setRecordSeparator(char recordSeparator) {
		this.recordSeparator = recordSeparator;
	}

	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	public String getCollectTimePattern() {
		return collectTimePattern;
	}

	public void setCollectTimePattern(String collectTimePattern) {
		this.collectTimePattern = collectTimePattern;
	}

	public String getCollectTimeFormat() {
		return collectTimeFormat;
	}

	public void setCollectTimeFormat(String collectTimeFormat) {
		this.collectTimeFormat = collectTimeFormat;
	}
	
	
	
	
	public Date doFetchTime(String logRecord){
		SimpleDateFormat sdf = new SimpleDateFormat(this.getCollectTimeFormat(),Locale.ENGLISH);
		
		Pattern pattern = Pattern.compile(this.getCollectTimePattern());		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			String timeStr =  m.group(1);
			
			try {
				return sdf.parse(timeStr);
			} catch (ParseException e) {
				logger.error("", e);
			}
		}
		
		return null;
		
	}
	
	
	

}
