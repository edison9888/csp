package com.taobao.sentinel.constant;

import com.taobao.sentinel.client.SentinelConfig;

/***
 * sentinel constant values
 * @author youji.zj
 *
 */
public class Constants {
	
	/***  database flag ***/
	public static final String SENTINEL_DATABASE = SentinelConfig.DATABASE_NAME;
	
	/*** dataId prefix ***/
	public static final String CONFIG_PREFIX = "sentinel_";
	
	/*** data group ***/
	public static final String GROUP = "sentinel";
	
	/*** administrator flag ***/
	public static final int ADMINISTRATOR = 0;
	
	/*** common user flag ***/
	public static final int USER = 1;
	
	/*** enable flag ***/
	public static final int ENABLE = 1;
	
	/*** disable flag ***/
	public static final int DISABLE = 0;
	
	/*** default period ***/
	public static final int DEFAULT_PERIOD = 10;

}
