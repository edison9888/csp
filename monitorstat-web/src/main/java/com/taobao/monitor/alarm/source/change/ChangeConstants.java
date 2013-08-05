package com.taobao.monitor.alarm.source.change;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;


public class ChangeConstants {
	private static final Logger logger = Logger.getLogger(ChangeConstants.class);
	
	public static final String artooUrl = "http://artoo2.taobao.net:9999/api/getPlanListByCondition.htm?";
	
	private static final String CHANGEFREE_URL_ONLINE = "changefree.url.online";
	private static final String CHANGEFREE_URL_DAILY = "changefree.url.daily";
	
	public static String changeFreeUrl;
	
	//通过artoo平台发布应用的名称
	public static List<String> appFromArtooList = new ArrayList<String>();
	//通过changefree平台发生变更的系统名称，包括数据库
	public static List<String> systemFromChangeFreeList = new ArrayList<String>();
	
	//添加appkey名称
	static{
		appFromArtooList.add("tradeplatform");
		appFromArtooList.add("tf_buy");
		appFromArtooList.add("tf_tm");
		appFromArtooList.add("cart");
		appFromArtooList.add("misccenter");
		appFromArtooList.add("uicfinal");
		
		appFromArtooList.add("detail");
		appFromArtooList.add("login");
		appFromArtooList.add("shopsystem");
		
		appFromArtooList.add("itemcenter");
		appFromArtooList.add("misccenter");
		appFromArtooList.add("tradelogs");
		appFromArtooList.add("ump");
		appFromArtooList.add("logisticscenter");
		appFromArtooList.add("delivery");
		
		appFromArtooList.add("shopcenter");
		appFromArtooList.add("uiclogin");
		appFromArtooList.add("communityuis");
		appFromArtooList.add("wwposthouse");
		appFromArtooList.add("htm");
		appFromArtooList.add("timeoutcenter");
		appFromArtooList.add("pointcenter");
		
		systemFromChangeFreeList.addAll(appFromArtooList);
		systemFromChangeFreeList.add("mysql_taobao");
		
		Properties pro = new Properties();
		InputStream in = ChangeConstants.class.getClassLoader().getResourceAsStream("changefree.properties");
		try {
			pro.load(in);
			String onlineChangeFreeURL = pro.getProperty(CHANGEFREE_URL_ONLINE);
			String dailyChangeFreeURL = pro.getProperty(CHANGEFREE_URL_DAILY);
			//判断运行程序的机器是线上还是测试环境 查询本机地址
			InetAddress address = InetAddress.getLocalHost();
			
			changeFreeUrl = onlineChangeFreeURL;
			
			}catch (Exception e) {
				logger.warn("load changefree.properties fail! ", e);
				throw new RuntimeException(e);
				}
	}
}
