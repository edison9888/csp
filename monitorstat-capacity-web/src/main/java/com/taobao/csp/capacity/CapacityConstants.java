package com.taobao.csp.capacity;

/***
 * store const values of capacity system
 * the last day of 2011
 * @author youji.zj
 *
 */
public class CapacityConstants {
	
	/*** Threshold of dependency qps, if below this threshold, dependency 
	 * qps record will be filtered, otherwise it will be synchronized in 
	 * capacity system
	 */
	public static final double DEP_QPS_THRESHOD = 1.0d;

}
