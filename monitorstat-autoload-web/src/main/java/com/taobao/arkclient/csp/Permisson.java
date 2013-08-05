package com.taobao.arkclient.csp;

/***
 * 压测权限常量
 * 
 * 权限设置的规则
 * addLoadrun:ALL;
 * editLoadrun:ALL(or appId sequence splitted by comma);
 * deleteLoadrun:ALL(or appId sequence splitted by comma);
 * autoLoadrun:ALL(or appId sequence splitted by comma);
 * manualLoadrun:ALL(or appId sequence splitted by comma);
 * 
 * @author youji.zj
 * 2011-10-20
 *
 */
public class Permisson {
	
	/***
	 *  增加压测应用（type）
	 */
	public static final String ADD_APP = "addLoadrun";
	
	/***
	 * 修改压测应用（type）
	 */
	public static final String EDIT_APP = "editLoadrun";
	
	/***
	 *  删除压测应用（type）
	 */
	public static final String DELETE_APP = "deleteLoadrun";
	
	/*** 
	 * 自动压测（type）
	 */
	public static final String AUTO_LOAD = "autoLoadrun";
	
	/***
	 * 手动压测（type）
	 */
	public static final String MANUAL_LOAD = "manualLoadrun";
	
	
	/***
	 * 权限的aim，针对某些应用的话aim是以逗号分隔的应用序列，如（1，2，3）
	 * aim是NO_AIM 则说明拥有所以应用的权限，或者该全下不针对具体应用
	 */
	public static final String NO_AIM = "ALL";

}
