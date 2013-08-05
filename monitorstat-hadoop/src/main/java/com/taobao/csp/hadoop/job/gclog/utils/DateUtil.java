package com.taobao.csp.hadoop.job.gclog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class DateUtil {
	public static final String PATTERN_MINUTE = "yyyy-MM-dd'T'hh:mm";
	public static final SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_MINUTE);
	private static Logger logger = Logger.getLogger(DateUtil.class);
	
	public static long getDateFromLine(String line) throws Exception {
		if(line != null) {
			Date date = sdf.parse(line.substring(0, 16));
			return date.getTime();
		}
		throw new Exception("parse time error");
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		final String date = "2013-01-24T10:31:02";
//		final String date2 = "2013-01-24T10:31:30";
//		final String pattern = "yyyy-MM-dd'T'hh:mm";
//		String line = "2013-01-24T10:31:02.374+0800: 25.723: [GC 25.723: [ParNew: 1462912K->34317K(1755456K), 0.0603830 secs] 1462912K->34317K(4950336K), 0.0605320 secs] [Times: user=0.17 sys=0.01, real=0.06 secs]";
		String line2 = "2013-02-02T00:41:09.186+0800: 48023.101:";
		System.out.println(DateUtil.getDateFromLine(line2));
//		System.out.println(DateUtil.getDateFromLine(line2));
		TimeZone time = TimeZone.getTimeZone("GMT+8"); //设置为东八区
		time = TimeZone.getDefault();// 这个是国际化所用的
		System.out.println(time);
		TimeZone.setDefault(time);// 设置时区
		
		long[] array = new long[]{1359856980000l,1359857040000l,1359650640000l,1359650820000l};
		for(long value : array)
			System.out.println(new Date(value));
	}

}
