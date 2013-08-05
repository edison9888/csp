package com.taobao.monitor.alarm.source.constants;

import java.util.ArrayList;
import java.util.List;

public class TfBuyAlarmKeyConstants {
	public static List<String> tpKeyList  = new ArrayList<String>();
	public static List<String> itemCenterKeyList  = new ArrayList<String>();
	public static List<String> messengeCenterKeyList  = new ArrayList<String>();
	
	public static List<String> deliveryKeyList  = new ArrayList<String>();
	public static List<String> promotionCenterKeyList  = new ArrayList<String>();
	public static List<String> miscCenterKeyList  = new ArrayList<String>();
	public static List<String> shopCenterKeyList  = new ArrayList<String>();
	public static List<String> logitiscsCenterKeyList  = new ArrayList<String>();
	static{
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Common-Ic"));
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Common-Ic-Spu"));
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-ItemService-Proxy"));
		
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Buy-CreateOrder"));
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Buy-SplitOrder"));
		
		messengeCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-MessageCenter"));
		
		deliveryKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Buy-DeliverFee"));
		deliveryKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Buy-DeliverTemplate"));
		
		promotionCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Buy-AsyncPromotion"));
		promotionCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Buy-ComboPromotion"));
		promotionCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Buy-PromotionCenter"));

		miscCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Common-MiscCenter"));
		
		shopCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Common-ShopCenter"));
		
		logitiscsCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-LogisticsCenter"));
	}
	
}
