package com.taobao.csp.loadrun.core.control;

/***
 * 压测控制属性的key
 * @author youji.zj
 * @version 2012-06-25
 *
 */
public enum ControlAtrribute {
	
	/*** 压测目标机器的ip ***/
	LOAD_IP,
	
	/*** apache 分流本地比例 ***/
	RATIO_LOCAL,
	
	/*** apache 分流目标比例 ***/
	RATIO_TARGET,
	
	/*** apache 分流本地比例 ***/
	PROXY_LOCAL,
	
	/*** apache 分流目标比例 ***/
	PROXY_TARGET,
	
	/*** 临时文件名 ***/
	TEMP_FILE_NAME,
	
	/*** 备份文件名 ***/
	BACK_FILE_NAME,
	
	/*** 压测机器存放从线上拉下来的配置路径 ***/
	LOCAL_PATH,
	
	/*** 线上配置路径 ***/
	CONFIG_PATH,
	
	/*** 启动路径 ***/
	BIN_PATH,
	
	/*** 备份脚本 ***/
	SCRIPT_BACK,
	
	/*** 运行脚本 ***/
	SCRIPT_RUN,
	
	/*** 恢复脚本 ***/
	SCRIPT_RESET;
}
