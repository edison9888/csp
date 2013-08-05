package com.taobao.csp.assign.util;

public class LocalUtil {
	
	public static String transerThrowableInfo(Throwable e) {
		StringBuffer sb = new StringBuffer();
		if (e.getStackTrace() == null) {
			return e.toString();
		}
		for (int i = 0; i < e.getStackTrace().length; i++) {
			StackTraceElement element = e.getStackTrace()[i];
			sb.append(element.getClassName()).append(":");
			sb.append(element.getMethodName()).append(":").append(element.getLineNumber()).append("\n");
		}
		return sb.toString();
	}

}
