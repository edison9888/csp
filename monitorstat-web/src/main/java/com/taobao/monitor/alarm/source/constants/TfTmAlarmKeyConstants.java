package com.taobao.monitor.alarm.source.constants;

import java.util.ArrayList;
import java.util.List;

public class TfTmAlarmKeyConstants {
	public static List<String> tpKeyList  = new ArrayList<String>();
	public static List<String> itemCenterKeyList  = new ArrayList<String>();
	public static List<String> logitiscsCenterKeyList  = new ArrayList<String>();
	public static List<String> promotionCenterKeyList  = new ArrayList<String>();
	public static List<String> miscCenterKeyList  = new ArrayList<String>();
	public static List<String> shopCenterKeyList  = new ArrayList<String>();
	public static List<String> messengeCenterKeyList  = new ArrayList<String>();
	public static List<String> htmKeyList  = new ArrayList<String>();
	
	static{
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Common-Ic"));
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-Common-Ic-Spu"));
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-ItemService-Proxy"));
		itemCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Common-VicQuery"));
		
		miscCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Common-MiscCenter"));
		
		promotionCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Buy-AsyncPromotion"));
		promotionCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Buy-PromotionCenter"));
		
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-TM-ModifyOrder"));
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P1-TM-Pay"));
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-UserTPQuery"));
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-TM-ConfirmGoods"));
		tpKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-TM-Ship"));
		
		shopCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Common-ShopCenter"));
		
		messengeCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-MessageCenter"));
		
		logitiscsCenterKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-LogisticsCenter"));
		
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmBatchRestoreOrder"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmBatchSoftDeleteOrder"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmBatchSoftPermanentlyDeleteOrder"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmBoughtList"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmBoughtListCountByHSF"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmBoughtListCountByTair"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmQueryTradeIgnoreSnap"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-HtmUpdateBuyerMemo"));
		htmKeyList.addAll(KeyDefineConstants.getStandardKeyNameList("P2-Tm-UpdateHtmBoughtListCountTair"));
		
	}
}
