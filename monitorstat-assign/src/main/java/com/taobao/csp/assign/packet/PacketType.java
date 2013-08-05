
package com.taobao.csp.assign.packet;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-8-10 обнГ04:14:02
 */
public enum PacketType implements Serializable{
	
	Clazz,
	Jar,
	File,
	ClassCache,
	Slave,
	JobReport,
	ClassLoader,
	Job,
	SlaveHeart,
	CleanCache,
	AssignClient,
	stopJob
	

}
