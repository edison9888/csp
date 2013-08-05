package com.taobao.csp.day.config;

import com.taobao.csp.day.base.DataType;

public class DayConfig {
	
	public static long getFetchSize(DataType dataType) {
		long fetchSize = 1024 * 1024 * 10;
		if (dataType == DataType.TDDL) {
			fetchSize = 1024 * 1024 * 10;
		}
		
		if (dataType == DataType.SPH) {
			fetchSize = 1024 * 1024 * 10;
		}
		
		if (dataType == DataType.APACHE_SPECIAL) {
			fetchSize = 1024 * 1024 * 20;
		}
		
		if (dataType == DataType.TDOD) {
			fetchSize = 1024 * 1024 * 10;
		}
		
		if (dataType == DataType.PINKIE_ACCESS) {
			fetchSize = 1024 * 1024 * 8;
			
//			fetchSize = 1024 * 10;
		}
		
		return fetchSize;
	}
	
	public static int getBackLogGeneratePeriod(DataType dataType) {
		int period = 10;
		if (dataType == DataType.TDDL) {
			period = 10;
		}
		
		if (dataType == DataType.SPH) {
			period = 1;
		}
		
		if (dataType == DataType.APACHE_SPECIAL) {
			period = 1;
		}
		
		if (dataType == DataType.TDOD) {
			period = 1;
		}
		
		return period;
	}
	
	public static int getCurrentLogGeneratePeriod(DataType dataType) {
		int period = 30;
		if (dataType == DataType.TDDL) {
			period = 30;
		}
		
		if (dataType == DataType.SPH) {
			period = 1;
		}
		
		if (dataType == DataType.TDOD) {
			period = 1;
		}
		
		return period;
	}
}
