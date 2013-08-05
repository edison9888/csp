
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.url;

import java.math.BigDecimal;

/**
 * @author xiaodu
 *
 * обнГ3:17:55
 */
public abstract class BaseDataCheck {
	
	
	public abstract boolean checking();
	
	public abstract StringBuffer report();
	
	public abstract StringBuffer reportReferAffect();
	
	protected double rate(double s,double t){
		
		BigDecimal b1 = new BigDecimal(s);
		BigDecimal b2 = new BigDecimal(t);
		return b1.subtract(b2).divide(b2).doubleValue();
	}
	
	
	
	

}
