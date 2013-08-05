package com.taobao.monitor.time.ao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.monitor.time.po.AppPvPO;
import com.taobao.monitor.time.po.TimeDataInfo;
import com.taobao.monitor.time.util.DataUtil;
import com.taobao.monitor.time.util.TimeUtil;
import com.taobao.util.CollectionUtil;

public class AppHSFQueryAo {
	private static final Logger logger =  Logger.getLogger(AppHSFQueryAo.class);
	private static AppHSFQueryAo ao = new AppHSFQueryAo();
	public static String LOGIN_URL = "http://login.taobao.com/member/login.jhtml";
	public static String DETAIL_URL = "http://item.taobao.com/item.htm";
	public static String MALL_DETAIL_URL = "http://detail.tmall.com/item.htm";
	public static String CART_URL = "http://cart.taobao.com/my_cart.htm";
	
	public static String TF_BUY_URL1 = "http://buy.taobao.com/auction/buy_now.jhtml";
	public static String TF_BUY_URL2 = "http://buy.taobao.com/auction/buy_now.htm";
	public static String TF_BUY_URL3 = "http://buy.taobao.com/auction/buy.htm";
	
	public static String TMALL_BUY_URL1 = "http://cart.tmall.com/cart/myCart.htm";
	public static String TMALL_BUY_URL2 = "http://buy.tmall.com/order/confirm_order.htm";
	
	public static String TAOBAO_CREATING_KEY = "HSF-provider`com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0`createOrders";
	public static String TMALL_CREATING_KEY = "HSF-provider`com.taobao.tc.service.TcTradeService:1.0.0`syncCreate~iBLPS";
	public static String TAOBAO_PAY_KEY = "HSF-provider`com.taobao.trade.platform.api.pay.PayOrderService:1.0.0`pay~lF";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static AppHSFQueryAo get(){
		return ao;
	}
	
	private AppHSFQueryAo(){
	}
	
	/**
	 * 通过实时数据查询
	 * 组装集市购买路径flash金字塔的xml数据
	 * @param i 第i分钟数据 0<i<=10
	 * @return String
	 */
	public String getTaobaoFlashXML(int i){
		StringBuffer xml = new StringBuffer();
		
		long payHSF = 0L;
		long creatingHSF = 0L;
		long buyPV = 0L;
		long cartPV = 0L;
		long detailPV = 0L;
		long loginPV = 0L;
		
		try{
			payHSF = queryAppKeyNameCount("tradeplatform", TAOBAO_PAY_KEY, i);
			creatingHSF = queryAppKeyNameCount("tradeplatform", TAOBAO_CREATING_KEY, i);
			//tf_buy三个链接累加
			buyPV = queryAppKeyNameCount("tf_buy", KeyConstants.PV+Constants.S_SEPERATOR+TF_BUY_URL1, i);
			buyPV += queryAppKeyNameCount("tf_buy", KeyConstants.PV+Constants.S_SEPERATOR+TF_BUY_URL2, i);
			buyPV += queryAppKeyNameCount("tf_buy", KeyConstants.PV+Constants.S_SEPERATOR+TF_BUY_URL3, i);
			cartPV = queryAppKeyNameCount("cart", KeyConstants.PV+Constants.S_SEPERATOR+CART_URL, i);
			detailPV = queryAppKeyNameCount("detail", KeyConstants.PV+Constants.S_SEPERATOR+DETAIL_URL, i);
			loginPV = queryAppKeyNameCount("login", KeyConstants.PV+Constants.S_SEPERATOR+LOGIN_URL, i);
		}catch(Exception e){
			logger.warn("getTaobaoFlashXML error:", e);
		}
		
		xml.append("<chart manageResize='1' origW='450' origH='250' caption='Taobao用户");
		xml.append( sdf.format(new Date()) );
		xml.append("时刻pv和购买链路转化监控图' baseFontSize='14' showLegend='1' legendPosition='Right' showLabels='1' showBorder='0' decmials='2' formatNumberScale='0'><set value='");
		xml.append(payHSF);
		xml.append("' label='Pay' color='980101' /><set value='");
		xml.append(creatingHSF);
		xml.append("' label='createOrders' color='FF8F45' /><set value='");
		xml.append(buyPV);
		xml.append("' label='Buy' color='FFB384' /><set value='");
		xml.append(cartPV);
		xml.append("' label='Cart' color='FFFF00' /><set value='");
		xml.append(detailPV);
		xml.append("' label='Detail' color='79C833' isSliced='1' /><set value='");
		xml.append(loginPV);
		xml.append("' label='Login' color='5EBCFF' /></chart>");
		return xml.toString();
	}
	
	/**
	 * 通过实时数据查询
	 * 组装天猫购买路径flash金字塔的xml数据
	 * @param i 第i分钟数据 0<i<=10
	 * @return String
	 */
	public String getTmallFlashXML(int i){
		StringBuffer xml = new StringBuffer();
		
		long payHSF = 0L;
		long tmallCreatingHSF = 0L;
		long tmallBuyPV = 0L;
		long mallDetailPV = 0L;
		long loginPV = 0L;
		try{
			payHSF = queryAppKeyNameCount("tradeplatform", TAOBAO_PAY_KEY, i);
			tmallCreatingHSF = queryAppKeyNameCount("tradeplatform", TMALL_CREATING_KEY, i);
			tmallBuyPV = queryAppKeyNameCount("tmallbuy", KeyConstants.PV+Constants.S_SEPERATOR+TMALL_BUY_URL1, i);
			tmallBuyPV += queryAppKeyNameCount("tmallbuy", KeyConstants.PV+Constants.S_SEPERATOR+TMALL_BUY_URL2, i);
			mallDetailPV = queryAppKeyNameCount("malldetail", KeyConstants.PV+Constants.S_SEPERATOR+MALL_DETAIL_URL, i);
			loginPV = queryAppKeyNameCount("login", KeyConstants.PV+Constants.S_SEPERATOR+LOGIN_URL, i);
		}catch(Exception e){
			logger.warn("getTmallFlashXML error:", e);
		}
		
		xml.append("<chart manageResize='1' origW='450' origH='250' caption='Taobao用户");
		xml.append( sdf.format(new Date()) );
		xml.append("时刻pv和购买链路转化监控图' baseFontSize='14' showLegend='1' legendPosition='Right' showLabels='1' showBorder='0' decmials='2' formatNumberScale='0'><set value='");
		xml.append(payHSF);
		xml.append("' label='Pay' color='980101' /><set value='");
		xml.append(tmallCreatingHSF);
		xml.append("' label='syncCreate' color='FF8F45' /><set value='");
		xml.append(tmallBuyPV);
		xml.append("' label='tmallBuy' color='FFFF00' /><set value='");
		xml.append(mallDetailPV);
		xml.append("' label='mallDetail' color='79C833' isSliced='1' /><set value='");
		xml.append(loginPV);
		xml.append("' label='Login' color='5EBCFF' /></chart>");
		return xml.toString();
	}
	
	/**
	 * 通过appName和keyName名称来查询单个接口的10分钟的数据
	 * 获取上一分钟数据
	 * @param appName
	 * @param serviceName
	 * @return long
	 */
	public long queryAppKeyNameCount(String appName, String keyName, int i){
		long count = 1000L;
		try {
			Map<String, Map<String, Object>> map = QueryUtil.querySingleRealTime(appName, keyName);
			List<TimeDataInfo> timeList = new ArrayList<TimeDataInfo>();
			
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				Map<String, Object> timeMap = entry.getValue();
				TimeDataInfo info = new TimeDataInfo();
				String time = entry.getKey();
				info.setTime(Long.parseLong(time));
				info.getOriginalPropertyMap().putAll(timeMap);
				timeList.add(info);
			}
			
			Collections.sort(timeList,new Comparator<TimeDataInfo>() {
				@Override
				public int compare(TimeDataInfo o1, TimeDataInfo o2) {
					if(o1.getTime()>o2.getTime()){
						return -1;
					}else if(o1.getTime()<o2.getTime()){
						return 1;
					}
					return 0;
				}
			});
			if(i<10 && i>0){
				count = DataUtil.transformLong( timeList.get(i-1).getOriginalPropertyMap().get(PropConstants.E_TIMES) );
			}
		} catch (Exception e) {
			logger.error("queryAppKeyNameCount 查询 appName：" + appName + " keyName:" + keyName + " i:" + i, e);
		}
		return count;
	}
	
	/**
	 * 通过查询单个应用URL的pv情况
	 * @param appName
	 * @param urlList
	 * @return List<AppPvPO>
	 */
	public List<AppPvPO> queryAppPv(String appName, List<String> urlList){
		List<AppPvPO> appPvList = new ArrayList<AppPvPO>();
		if(CollectionUtil.isEmpty(urlList)){
			logger.warn("call queryAppPv(String appName="+appName+", List<String> urlList) failed for urlList is Empty!");
			return appPvList;
		}
		try{
			for(String url:urlList){
				String key = KeyConstants.PV + Constants.S_SEPERATOR + url;
				
				Map<String, Map<String, Object>> map  = QueryUtil.querySingleRealTime(appName, key);
				for(Map.Entry<String, Map<String,Object>> entry : map.entrySet()){
					if(entry.getValue()==null){
						continue;
					}
					AppPvPO po = new AppPvPO();
					po.setAppName(appName);
					po.setUrlName(url);
					po.setPvTime( new Date(Long.parseLong(entry.getKey())) );
					Map<String, Object> tMap = entry.getValue();
					po.setPvCount( (Integer)tMap.get( PropConstants.E_TIMES ) );
					appPvList.add(po);
				}
	
			}
		} catch(Exception e){
			logger.warn("call QueryUtil.querySingleRealTime() error", e);
		}
		
		Collections.sort(appPvList, new Comparator<AppPvPO>() {
			@Override
			public int compare(AppPvPO A1, AppPvPO A2) {
				if( A1.getPvTime().getTime()> A2.getPvTime().getTime() ){
					return -1;
				}else if(A1.getPvTime().getTime() < A2.getPvTime().getTime() ){
					return 1;
				}
				return 0;
			}
		});
		
		return appPvList;
	}
	
	/**
	 * 通过appName和service名称来查询单个接口的数据
	 * @param appName
	 * @param serviceName
	 * @return Map<String, List<TimeDataInfo>>
	 */
	public Map<String, List<TimeDataInfo>> queryAppHSFInterface(String appName, String keyName){
		Map<String, List<TimeDataInfo>> childMap = new HashMap<String, List<TimeDataInfo>>();
		try {
			Map<String, Map<String, Object>> map = QueryUtil.querySingleRealTime(appName, keyName);
			List<TimeDataInfo> timeList = new ArrayList<TimeDataInfo>();
			
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				Map<String, Object> timeMap = entry.getValue();
				TimeDataInfo info = new TimeDataInfo();
				info.setAppName(appName);
				info.setKeyName(keyName);
				info.setMainProp( PropConstants.E_TIMES );
				String time = entry.getKey();
				info.setTime(Long.parseLong(time));
				info.setFtime(TimeUtil.formatMillisTime(Long.parseLong(time), "HH:mm"));
				info.getOriginalPropertyMap().putAll(timeMap);
				info.setMainValue(DataUtil.transformDouble(timeMap.get( PropConstants.E_TIMES )));
				timeList.add(info);
			}
			
			Collections.sort(timeList,new Comparator<TimeDataInfo>() {
				@Override
				public int compare(TimeDataInfo o1, TimeDataInfo o2) {
					if(o1.getTime()>o2.getTime()){
						return -1;
					}else if(o1.getTime()<o2.getTime()){
						return 1;
					}
					return 0;
				}
			});
			childMap.put(keyName, timeList);
		} catch (Exception e) {
			logger.error("querykeyDataForHost 查询 appName：" + appName + " keyName:" + keyName, e);
		}
		return childMap;
	}
}
