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
	
	//ͨ��artooƽ̨����Ӧ�õ�����
	public static List<String> appFromArtooList = new ArrayList<String>();
	//ͨ��changefreeƽ̨���������ϵͳ���ƣ��������ݿ�
	public static List<String> systemFromChangeFreeList = new ArrayList<String>();
	
	//���appkey����
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
			//�ж����г���Ļ��������ϻ��ǲ��Ի��� ��ѯ������ַ
			InetAddress address = InetAddress.getLocalHost();
			
			changeFreeUrl = onlineChangeFreeURL;
			
			}catch (Exception e) {
				logger.warn("load changefree.properties fail! ", e);
				throw new RuntimeException(e);
				}
	}
}
