package com.taobao.csp.time.web.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.time.util.Urls;

public class DataViewModTransferMap {
	private Map<String,String> keyMap = new ConcurrentHashMap<String,String>();
	private static DataViewModTransferMap transferMap = new DataViewModTransferMap();
	private DataViewModTransferMap(){
		keyMap.put(Urls.querySingleHostRealTime,"�����������ʮ��������");
		keyMap.put(Urls.queryRecentlySingleHostRealTime,"�����������һ���ӵ�����");
		keyMap.put(Urls.queryChildHostRealTime,"�����������ʮ������Key����");
		keyMap.put(Urls.queryRecentlyChildHostRealTime,"�����������һ������Key����");
		keyMap.put(Urls.querySingleRealTime,"Ӧ�ü������ʮ��������");
		keyMap.put(Urls.queryCustomerBusinessLog,"Ӧ�ü������ʮ����ҵ����־����");
		keyMap.put(Urls.querySingleRealTimeLine,"Ӧ�ü������ʮ��������,ʱ������");
		keyMap.put(Urls.queryRecentlySingleRealTime,"Ӧ�ü������һ��������");
		keyMap.put(Urls.queryChildRealTime,"Ӧ�ü������ʮ������Key����");
		keyMap.put(Urls.queryRecentlyChildRealTime,"Ӧ�ü������һ������Key����");
		
		keyMap.put(Urls.queryHistory, "��ʷ����");
		keyMap.put(Urls.poListTable, "�����ʽ������������");
		keyMap.put(Urls.poListLine, "����ͼչ��");
		keyMap.put(Urls.poListTop10, "�����ʽ������������top10");
		keyMap.put(Urls.mapPoTable, "�����ʽ������������");
		keyMap.put(Urls.mapPoTop10, "�����ʽ������������top10");
		keyMap.put(Urls.mapPoListTable, "�����ʽ");
		keyMap.put(Urls.historyLine,"����ͼ");
		keyMap.put(Urls.mapPoListTable2, "�����ʽ");
	}
	public  static DataViewModTransferMap getSingle(){
		return transferMap;
	}
	public String get(String key){
		return keyMap.get(key);
	}
}
