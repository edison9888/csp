/**
* <p>Title: HaBoAppNameMap.java</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: taobao</p>
* @author tom
* @下午04:51:37 - 2010-5-14
* @version 1.0
*/
package com.taobao.monitor.web.util;

public class HaBoAppNameMap {
	/**
	 * 
	* <p>Description: 获取哈勃对应的应用名称</p>
	* @param appName
	* @return
	* String
	* @author tom
	* @下午04:57:07 - 2010-5-14
	 */
	public static String getHaBoAppName(String appName){
		if(appName.equalsIgnoreCase("list"))appName = ApplicationName.LIST.getValue();
		else if(appName.equalsIgnoreCase("shopsystem"))appName = ApplicationName.SHOP_SYSTEM.getValue();
		else if(appName.equalsIgnoreCase("item"))appName = ApplicationName.ITEM.getValue();
		else if(appName.equalsIgnoreCase("buy"))appName = ApplicationName.BUY.getValue();
		else if(appName.equalsIgnoreCase("trademgr"))appName = ApplicationName.TRADEMGR.getValue();
		else if(appName.equalsIgnoreCase("ic"))appName = ApplicationName.IC.getValue();
		else if(appName.equalsIgnoreCase("tbuic"))appName = ApplicationName.TBUIC.getValue();
		else if(appName.equalsIgnoreCase("shopcenter"))appName = ApplicationName.SHOP_CENTER.getValue();
		else if(appName.equalsIgnoreCase("tc"))appName = ApplicationName.TC.getValue();
		else if(appName.equalsIgnoreCase("designcenter"))appName = ApplicationName.DESIGN_CENTER.getValue();
		else appName = appName;
		return appName;
	}
}
