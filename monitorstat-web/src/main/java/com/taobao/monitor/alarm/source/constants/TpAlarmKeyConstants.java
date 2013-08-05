package com.taobao.monitor.alarm.source.constants;

import java.util.ArrayList;
import java.util.List;

public class TpAlarmKeyConstants {
	public static List<String> alipayKeyList = new ArrayList<String>();
	public static List<String> mainDBKeyList  = new ArrayList<String>();
	public static List<String> readDBMasterKeyList  = new ArrayList<String>();
	public static List<String> uicFinalKeyList  = new ArrayList<String>();
	//������𣬰�������ϵͳ����𣬽�������key�б�
	//��������Ҫ�滮���� �ͽ������ݿ���������ɿ�ά�����������
	//������Ҫ���϶��������ģ�����IC logistics  notify

	static{
		
		for(String keyDefine:KeyDefineConstants.keyDefineSuffixList){
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "cae_charge_agent" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "close_trade" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "logistic_sign_in" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "send_goods_confirm_by_platform" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "single_trade_query" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "batchConfirmReceiveGoods" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "confirmDisburse" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "confirmReceiveGoods" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "modifyCODFee" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "modifyTradeFee" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "applyRefund" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "refundDisburse" + keyDefine);
			alipayKeyList.add(KeyDefineConstants.keyDefinePrefix + "trade_create" + keyDefine);
		}
		
		
		
		for(int i=0;i<32;i++){
			for(String keyDefine:KeyDefineConstants.keyDefineSuffixList){
				mainDBKeyList.add(KeyDefineConstants.keyDefinePrefix + "tcmain" + i + keyDefine);
			}
		}
		
		for(int i=0;i<32;i++){
			for(String keyDefine:KeyDefineConstants.keyDefineSuffixList){
				readDBMasterKeyList.add(KeyDefineConstants.keyDefinePrefix + "readDb" + i + keyDefine);
			}
		}
		
		for(String keyDefine:KeyDefineConstants.keyDefineSuffixList){
			uicFinalKeyList.add(KeyDefineConstants.keyDefinePrefix + "P1-Uic" +keyDefine );
			uicFinalKeyList.add(KeyDefineConstants.keyDefinePrefix + "P1-UicDeliveryAddress" + keyDefine);
		}
		
		
	}
	
	
}
