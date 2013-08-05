package com.taobao.arkclient.csp;

/***
 * 容量规划权限常量
 * 
 * 权限设置的规则
 * addCapacityApp:ALL;
 * editCapacityApp:ALL(or appId sequence splitted by comma);
 * deleteCapacityApp:ALL(or appId sequence splitted by comma);
 * reinsertCapacityPv:ALL;
 * reflushCapacityRank:ALL;
 * 
 * @author youji.zj
 * 2011-10-20
 *
 */
public class Permisson {
	
	/***
	 *  增加参与容量规划的应用（type）
	 */
	public static final String ADD_APP = "addCapacityApp";
	
	/***
	 * 修改参与容量规划的应用（type）
	 */
	public static final String EDIT_APP = "editCapacityApp";
	
	/***
	 *  删除参与容量规划的应用（type）
	 */
	public static final String DELETE_APP = "deleteCapacityApp";
	
	/*** 
	 * 重新计算插入pv（type）
	 */
	public static final String REINSERT_PV = "reinsertCapacityPv";
	
	/***
	 * 重新计算容量排行（type）
	 */
	public static final String REFLUSH_RANK = "reflushCapacityRank";
	
	
	/***
	 * 权限的aim，针对某些应用的话aim是以逗号分隔的应用序列，如（1，2，3）
	 * aim是NO_AIM 则说明拥有所以应用的权限，或者该全下不针对具体应用
	 */
	public static final String NO_AIM = "ALL";

}
