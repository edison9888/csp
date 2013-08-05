package com.taobao.monitor.web.ao;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.dependent.dao.BeiDouDao;
import com.taobao.monitor.web.util.aggregation.PageViewJSSplitConstants;
import com.taobao.monitor.web.vo.beidou.BeidouAlarmDataPo;
import com.taobao.util.CollectionUtil;

import java.util.*;

public class BeidouAlarmAO {
	private BeiDouDao dao = new BeiDouDao();
	private static BeidouAlarmAO  ao = new BeidouAlarmAO();
	private static HashSet<String> allDbGroupNameList = new HashSet<String>();
	
	private static List<String> tradeRelateDBGroupList = new ArrayList<String>();
	private static List<String> cartRelateDBGroupList = new ArrayList<String>();
	private static List<String> tradeplatformRelateDBGroupList = new ArrayList<String>();
	private static List<String> tfBuyRelateDBGroupList = new ArrayList<String>();
	private static List<String> tfTmRelateDBGroupList = new ArrayList<String>();
    private static List<String> auctionplatformDBGroupList = new ArrayList<String>();
	
	static{
		tradeRelateDBGroupList.add("tcmaindb");
		tradeRelateDBGroupList.add("tcdb");
		tradeRelateDBGroupList.add("cartdb");
		tradeRelateDBGroupList.add("tcmiscdb");
		
		tradeplatformRelateDBGroupList.add("icdb_mysqldb");
		tradeplatformRelateDBGroupList.add("logdb");
		tradeplatformRelateDBGroupList.add("notify_ha_tcdb");
		tradeplatformRelateDBGroupList.add("notify_tradedb");
		tradeplatformRelateDBGroupList.add("tcmaindb");
		tradeplatformRelateDBGroupList.add("tcdb");
		tradeplatformRelateDBGroupList.add("ecarddb");  //misc center
		tradeplatformRelateDBGroupList.add("yunhaodb");
		tradeplatformRelateDBGroupList.add("marketingdb");
		tradeplatformRelateDBGroupList.add("mcdb");  //message center
		tradeplatformRelateDBGroupList.add("tocdb");
		tradeplatformRelateDBGroupList.add("tcpointdb");
		
		tfBuyRelateDBGroupList.add("marketingdb");
		tfBuyRelateDBGroupList.add("ecarddb");  //misc center
		tfBuyRelateDBGroupList.add("yunhaodb");
		tfBuyRelateDBGroupList.add("logdb");
		tfBuyRelateDBGroupList.add("icdb_mysqldb");
		tfBuyRelateDBGroupList.add("tcmaindb");
		tfBuyRelateDBGroupList.add("tcdb");
		
		tfTmRelateDBGroupList.add("icdb_mysqldb");
		tfTmRelateDBGroupList.add("tcmaindb");
		tfTmRelateDBGroupList.add("tcdb");
		tfTmRelateDBGroupList.add("ecarddb");  //misc center
		tfTmRelateDBGroupList.add("yunhaodb");
		tfTmRelateDBGroupList.add("mcdb");  //message center
		tfTmRelateDBGroupList.add("snsdb");  //misc center
		tfTmRelateDBGroupList.add("uic_logindb");
		tfTmRelateDBGroupList.add("supudb");  //message center
		
		cartRelateDBGroupList.add("supudb");
		cartRelateDBGroupList.add("tcmaindb");
		cartRelateDBGroupList.add("tcdb");
		cartRelateDBGroupList.add("icdb_mysqldb");
		cartRelateDBGroupList.add("cartdb");
		cartRelateDBGroupList.add("marketingdb");
		
        auctionplatformDBGroupList.add("biddb");
        auctionplatformDBGroupList.add("gov_biddb"); //govauction

		allDbGroupNameList.addAll(tradeRelateDBGroupList);
		allDbGroupNameList.addAll(cartRelateDBGroupList);
		allDbGroupNameList.addAll(tradeplatformRelateDBGroupList);
		allDbGroupNameList.addAll(tfBuyRelateDBGroupList);
		allDbGroupNameList.addAll(tfTmRelateDBGroupList);
		allDbGroupNameList.addAll(auctionplatformDBGroupList);
	}
	
	
	public static HashSet<String> getAllDbGroupNameList() {
		return allDbGroupNameList;
	}

	public static List<String> getTradeRelateDBGroupList() {
		return tradeRelateDBGroupList;
	}

	public static List<String> getCartRelateDBGroupList() {
		return cartRelateDBGroupList;
	}

	public static List<String> getTradeplatformRelateDBGroupList() {
		return tradeplatformRelateDBGroupList;
	}

    public static List<String> getAuctionplatformDBGroupList() {
        return auctionplatformDBGroupList;
    }

	public static List<String> getTfBuyRelateDBGroupList() {
		return tfBuyRelateDBGroupList;
	}

	public static List<String> getTfTmRelateDBGroupList() {
		return tfTmRelateDBGroupList;
	}

	public static  BeidouAlarmAO get(){
		return ao;
	}
	
	/**
	 * 查询北斗监控db的数据
	 * @param start  开始时间
	 * @param end   结束时间
	 * @return
	 */
	public Map<String,List<BeidouAlarmDataPo>> getBeidouAlarmDataMapByTime(Date start,Date end){
		Map<String,List<BeidouAlarmDataPo>> beidouAlarmDataMap = new HashMap<String,List<BeidouAlarmDataPo>>();
		for(String groupName:allDbGroupNameList){
			//去掉加的关键字db,beidou的DB里保存的groupName没有
			List<BeidouAlarmDataPo> beidouAlarmDataList = dao.findBeidouAlarmData(groupName.substring(0, groupName.length()-2), start, end);
			if(CollectionUtil.isNotEmpty(beidouAlarmDataList)){
				beidouAlarmDataMap.put(groupName, beidouAlarmDataList);
			}
		}
		return  beidouAlarmDataMap;
	}
	
	
	//获取页面展示需要的字符串
	public String getStringFormatByBeidouAlarmData(List<BeidouAlarmDataPo> beidouAlarmDataPoList){
		if(CollectionUtil.isEmpty(beidouAlarmDataPoList)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(BeidouAlarmDataPo beidouAlarmDataPo:beidouAlarmDataPoList){
			sb.append(beidouAlarmDataPo.getKeyString()).append(PageViewJSSplitConstants.keyValueSplit).
			append(beidouAlarmDataPo.getValueString()).append(PageViewJSSplitConstants.ObjectSplit);
		}
		return sb.toString();
	}

	public boolean isAppRelatedDBAlarm(String appName, String dbName){
		if( StringUtil.isBlank(dbName) ){
			return false;
		}
		if("all".equals( appName )){
			return tradeRelateDBGroupList.contains(dbName);
		}
		if("tradeplatform".equals( appName )){
			return tradeplatformRelateDBGroupList.contains(dbName);
		}
		if("tf_buy".equals(appName)){
			return tfBuyRelateDBGroupList.contains(dbName);
		}
		if("tf_tm".equals(appName)){
			return tfTmRelateDBGroupList.contains(dbName);
		}
		if("cart".equals(appName)){
			return cartRelateDBGroupList.contains(dbName);
		}
        if("auctionplatform".equals(appName)) {
            return auctionplatformDBGroupList.contains(dbName);
        }
		return false;
	}
}
