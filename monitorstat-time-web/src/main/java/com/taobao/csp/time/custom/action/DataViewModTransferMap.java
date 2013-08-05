package com.taobao.csp.time.custom.action;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.dataserver.PropConstants;

public class DataViewModTransferMap {
	private Map<String,String> keyMap = new ConcurrentHashMap<String,String>();
	private static DataViewModTransferMap transferMap = new DataViewModTransferMap();
	private DataViewModTransferMap(){
		keyMap.put("/app/detail/custom/show.do?method=querySingleHostRealTime","�����������ʮ��������");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlySingleHostRealTime","�����������һ���ӵ�����");
		keyMap.put("/app/detail/custom/show.do?method=queryChildHostRealTime","�����������ʮ������Key����");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlyChildHostRealTime","�����������һ������Key����");
		keyMap.put("/app/detail/custom/show.do?method=querySingleRealTime","Ӧ�ü������ʮ��������");
		keyMap.put("/app/detail/custom/show.do?method=querySingleRealTimeLine","Ӧ�ü������ʮ��������,ʱ������");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlySingleRealTime","Ӧ�ü������һ��������");
		keyMap.put("/app/detail/custom/show.do?method=queryChildRealTime","Ӧ�ü������ʮ������Key����");
		keyMap.put("/app/detail/custom/show.do?method=queryRecentlyChildRealTime","Ӧ�ü������һ������Key����");
		keyMap.put("/app/detail/custom/show.do?method=queryHistory", "��ʷ����");
		keyMap.put("poListTable", "�����ʽ������������");
		keyMap.put("poListLine", "����ͼչ��");
		keyMap.put("poListTop10", "�����ʽ������������top10");
		keyMap.put("mapPoTable", "�����ʽ������������");
		keyMap.put("mapPoTop10", "�����ʽ������������top10");
		keyMap.put("mapPoListTable", "�����ʽ");
		keyMap.put("historyLine","����ͼ");
	}
	public  static DataViewModTransferMap getSingle(){
		return transferMap;
	}
	public String get(String key){
		return keyMap.get(key);
	}
}
