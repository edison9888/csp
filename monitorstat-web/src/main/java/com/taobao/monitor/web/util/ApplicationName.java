/**
* <p>Title: ApplicationName.java</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: taobao</p>
* @author tom
* @上午11:13:06 - 2010-5-14
* @version 1.0
*/
package com.taobao.monitor.web.util;

public enum ApplicationName {
	LIST("listhost"),//搜索结果页面
	SHOP_SYSTEM("shopsystemhost"),//店铺系统
	ITEM("itemhost"),
	BUY("buyhost"),
	TRADEMGR("trademgrhost"),//交易管理
	IC("ichost"),//商品中心
	TBUIC("tbuichost"),//用户中心
	SHOP_CENTER("shopcenterhost"),//店铺中心
	TC("tchost"),//交易中心
	DESIGN_CENTER("designcenter");
	
	private final String value;

	public String getValue() {
		return value;
	}
	ApplicationName(String value){
		this.value = value;
	}
	
}
