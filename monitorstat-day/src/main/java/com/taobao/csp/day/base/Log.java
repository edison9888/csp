package com.taobao.csp.day.base;

import java.util.Date;

/***
 * 抽象的日记对象
 * @author youji.zj 2012-08-20
 * 
 * @version 1.0
 *
 */
public interface Log {
	
	Object [] getValues();
	
	Date getDate();

}
