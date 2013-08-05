package com.taobao.csp.hadoop.util;

import java.io.Closeable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;

public class LocalUtil {
	
	public static String cleanUrl(String str) {
		if (str.indexOf("?") > -1) {
			return str.substring(0, str.indexOf("?"));
		}
		return str;
	}
	
	public static String getThrowableTrace(Throwable e) {
		StringBuffer sb = new StringBuffer(); 
		if (e.getStackTrace() != null) {
			for (int i = 0; i < e.getStackTrace().length; i++) { 
				StackTraceElement element = e.getStackTrace()[i]; 
				sb.append(element.getClassName()).append(":"); 
				sb.append(element.getMethodName()).append(":").append(element.getLineNumber()).append("\n"); 
			} 
		}
		
		return sb.toString();
	}

	/***
	 * 递归获取目录的所有文件
	 * @param conf
	 * @param directory 必须为目录
	 * @param inpaths 查询结果插入该List
	 */
	public static void getAllFiles(Configuration conf, Path directory, List<Path> inpaths) {
		try {
			FileSystem fstm = FileSystem.get(conf);
			FileStatus[] fsArr = fstm.listStatus(directory);
			
			for (FileStatus fs : fsArr) {
				if ((fs.isDir())) {
					getAllFiles(conf, fs.getPath(), inpaths);
				} else {
					inpaths.add(fs.getPath());
				}
			}
		} catch (Exception e) {
			System.err.println(LocalUtil.getThrowableTrace(e));
		}
	}
	
	public static void closeStream(Closeable obj) {
		try {
			if (obj != null) {
				obj.close();
			}
		} catch (Exception e) {
			System.err.println(LocalUtil.getThrowableTrace(e));
		}
	}
	
	public static String getDomainFromUrl(String url) {
		String domain = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(url)) {
			int begin = url.indexOf("//") + 2;
			String sub = url.substring(begin);
			int end = sub.indexOf("/");
			
			if (end == -1) {
				end = sub.length();
			}
			
			domain = sub.substring(0, end);
		}
		
		return domain;
	}
	
	public static String truncateWithEllipsis(String domain, int limitSize) {
		if (domain != null && domain.length() > limitSize) {
			return domain.substring(0, limitSize) + "...";
		}
		
		return domain;
	}
}
