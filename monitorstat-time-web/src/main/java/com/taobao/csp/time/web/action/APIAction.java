package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONArray;
import com.taobao.csp.alarm.po.api.HsfProviderReferPo;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.TimeUtil;
import com.taobao.csp.time.web.po.APINotify;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ReleaseInfo;

@Controller
@RequestMapping("/api.do")
public class APIAction extends BaseController {
	private static Logger logger = Logger.getLogger(APIAction.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
	/**
	 *	为notify提供查询按key查询。时间格式为selectDate为"yyyy-MM-dd" 
	 */
	@RequestMapping(params="method=notifyDataByDay")
	public void notifyDataByDay(HttpServletResponse response, String appName, String key1, String key2, String key3, String selectDate) {
		//final Map<String, String> propertiesMap = new HashMap<String, String>();
		//propertiesMap.put("errormsg", "");

		APINotify notifyPo = new APINotify();
		try {
			AppInfoPo info = AppInfoCache.getAppInfoByAppName(appName);
			if(info == null)
				throw new Exception("appName not exists->" + appName);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			final Date date = sdf.parse(selectDate);

			String today = sdf.format(new Date());
			selectDate = sdf.format(date);	//避免 2012-7-23 和  2012-07-23 这种情况
			int searchType = today.compareTo(selectDate);
			if(searchType < 0) {
				throw new Exception("selectDate > now()->" + selectDate);
			}

			String keyNameFull = getKeyForNotify(key1, key2, key3);

			List<String> propertys = KeyAo.get().findKeyPropertyNames(keyNameFull);
			Map<Date,Map<String,String>> query_Result = QueryHistoryUtil.queryMultiProperty(appName,
					keyNameFull,"", propertys.toArray(new String[]{}), date);
			for(Map<String,String> valueMap : query_Result.values()) {
				for(Entry<String, String> entry :valueMap.entrySet()) {
					try {
						String property = entry.getKey();

						String value = entry.getValue();
						Long longValue = Long.parseLong(value);
						if("C-time".equals(property)){
							notifyPo.setcTimes(longValue + notifyPo.getcTimes());
						} else if ("E-times".equals(property)) {
							notifyPo.seteTimes(longValue + notifyPo.geteTimes());
						}
					} catch (Exception e) {
						logger.error("", e);
					}
				} 
			}
		} catch (Exception e) {
			notifyPo.setErrorMsg(e.toString());
		}
		try {
			this.writeJSONToResponseJSONObject(response, notifyPo);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	private String getKeyForNotify(String key1, String key2, String key3) throws Exception {
		String keyNameFull = KeyConstants.NOTIFY_PROVIDER;
		if(key1 != null && !key1.trim().equals("")) {
			key1 = key1.trim();
			keyNameFull += Constants.S_SEPERATOR + key1;
			if(key2 != null && !key2.trim().equals("")) {
				key2 = key2.trim();
				keyNameFull += Constants.S_SEPERATOR + key2;
				if(key3 != null && !key3.trim().equals("")) {
					key3 = key3.trim();
					keyNameFull += Constants.S_SEPERATOR + key3;
				}
			}
		} else {
			throw new Exception("key1 is null");
		}		
		return keyNameFull;
	}

	/**
	 * 按天汇总
	 */
	@RequestMapping(params="method=notifyDataRealTime")
	public void notifyDataRealTime(HttpServletResponse response, String appName, String key1, String key2, String key3, String selectDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(selectDate);
			time.setTime(date1);
		} catch (Exception e1) {
		}
		Date cur = time.getTime();
		String keyNameFull;
		final List<APINotify> notifyList = new ArrayList<APINotify>();
		try {
			AppInfoPo info = AppInfoCache.getAppInfoByAppName(appName);
			if(info == null)
				throw new Exception("appName not exists->" + appName);
			
			keyNameFull = getKeyForNotify(key1, key2, key3);
		} catch (Exception e) {
			APINotify errorNotify = new APINotify();
			errorNotify.setErrorMsg(e.toString());
			notifyList.add(errorNotify);
			try {
				this.writeJSONToResponseJSONArray(response, notifyList);
			} catch (IOException err) {
				logger.error("", err);
			}
			return;
		}
		//List<String> propertys = KeyAo.get().findKeyPropertyNames(keyNameFull);
		Map<Date,Map<String,String>> query_Result = QueryHistoryUtil.queryMultiProperty
				(appName, keyNameFull,"", new String[]{"E-times", "C-time"}, cur);

		for(Map.Entry<Date,Map<String,String>> entry:query_Result.entrySet()){
			Date date = entry.getKey();
			Map<String,String> m = entry.getValue();

			if(m==null||m.size()==0){
				continue;
			}
			APINotify notifyPo = new APINotify();
			notifyPo.setDate(date.getTime());
			for(Map.Entry<String,String> h:m.entrySet()){
				String key = h.getKey();
				String value = h.getValue();
				if(NumberUtils.isNumber(value)){
					long valueL = Long.parseLong(value);
					if("C-time".equals(key)){
						notifyPo.setcTimes(valueL);
					} else if ("E-times".equals(key)) {
						notifyPo.seteTimes(valueL);
					}
				} else {
					logger.info("value = " + value);
				}
 			}
			notifyList.add(notifyPo);
		}

		try {
			this.writeJSONToResponseJSONArray(response, notifyList);
		} catch (IOException err) {
			logger.error("", err);
		}
	}
	/**
	 * 按照分钟查询 
	 */
	@RequestMapping(params="method=notifyDataByMinute")
	public void notifyDataByMinute(HttpServletResponse response, String appName, String key1, String key2, String key3, String selectDate) {
		
		APINotify notifyPo = new APINotify();
		notifyPo.setErrorMsg("未初始化");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar time = Calendar.getInstance();
			Date dSelectDate = null;
			
			AppInfoPo info = AppInfoCache.getAppInfoByAppName(appName);
			if(info == null)
				throw new Exception("appName not exists->" + appName);
			
			Date date1 = sdf.parse(selectDate);
			time.setTime(date1);
			time.set(Calendar.SECOND, 0);
			time.set(Calendar.MILLISECOND, 0);
			dSelectDate = time.getTime();
			String keyNameFull = getKeyForNotify(key1, key2, key3);
			
			if(System.currentTimeMillis() - dSelectDate.getTime() < Constants.CACHE_TIME_INTERVAL*1000*60) {	
				//小于CACHE_TIME_INTERVAL 分钟,查缓存
				List<TimeDataInfo> list = commonService.querySingleKeyData(appName, keyNameFull, PropConstants.E_TIMES);
				for(TimeDataInfo po : list ) {
					if(po.getTime() == dSelectDate.getTime()) {
						Map<String, Object> properMap = po.getOriginalPropertyMap();
						long eTime = TimeUtil.getLongValueOfObj(properMap.get(PropConstants.E_TIMES));
						long cTime = TimeUtil.getLongValueOfObj(properMap.get(PropConstants.C_TIME));
						notifyPo.setcTimes(cTime);
						notifyPo.seteTimes(eTime);
						break;
					}
				}
			} else {	//查历史
				Map<String,String> resultMap = QueryHistoryUtil.queryMultiPropertyBySingleKey(appName, keyNameFull, new String[]{PropConstants.E_TIMES, PropConstants.C_TIME}, dSelectDate);
				long eTime = TimeUtil.getLongValueOfObj(resultMap.get(PropConstants.E_TIMES));
				long cTime = TimeUtil.getLongValueOfObj(resultMap.get(PropConstants.C_TIME));
				notifyPo.setcTimes(cTime);
				notifyPo.seteTimes(eTime);
			}
			
		} catch (Exception e) {
			APINotify errorNotify = new APINotify();
			errorNotify.setErrorMsg(e.toString());
			try {
				this.writeJSONToResponseJSONObject(response, notifyPo);
			} catch (IOException err) {
				logger.error("", err);
			}
			return;
		}
		try {
			this.writeJSONToResponseJSONObject(response, notifyPo);
		} catch (IOException err) {
			logger.error("", err);
		}
	}	
	
	@RequestMapping(params="method=getChildKeyByApp")
	public void getChildKeyByApp(HttpServletResponse response, String appName, String parentKey) {
		List<String> list = commonService.childKeyList(appName, parentKey);
		try {
			this.writeJSONToResponseJSONArray(response, list);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 
	 * @param response
	 * @param providerAppName
	 * @param consumeAppNames
	 */
	@RequestMapping(params="method=getRealTimeConsumeData")
	public void getRealTimeConsumeData(HttpServletResponse response, String providerAppName, String consumeAppNames) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -2);
		Date requestTime = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String fTime = sdf.format(requestTime);
		
		HsfProviderReferPo po = new HsfProviderReferPo();
		po.setTime(requestTime.getTime());
		po.setTotalnum(0);
		
		AppInfoPo appInfo = AppInfoCache.getAppInfoByAppName(providerAppName);
		if(appInfo != null) {
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForChildBySort(appInfo.getAppName(), KeyConstants.HSF_REFER, PropConstants.E_TIMES);
			long totalValue = 0;
			Set<String> appFilter = new HashSet<String>();
			if(consumeAppNames != null) {
				consumeAppNames = consumeAppNames.toLowerCase().trim();
				String[] appNameArray = consumeAppNames.split(",");
				for(String appReqeust: appNameArray) {
					appFilter.add(appReqeust);					
				}
			}
			for(SortEntry<TimeDataInfo> entry : list) {
				Map<String, TimeDataInfo> map = entry.getObjectMap();
				for(String fTimeString : map.keySet()) {
					if(fTimeString.equals(fTime)) {
						TimeDataInfo info = map.get(fTime);
						long value = DataUtil.transformLong(info.getMainValue());
						String referAppName = entry.getKeyName();
						totalValue += value;
						if(appFilter.size() == 0 || appFilter.contains(referAppName))
							po.addToList(referAppName, value);
					}
				}
			}
			po.setTotalnum(totalValue);	
		}
		try {
			this.writeFastJsonForObject(response, po);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	@RequestMapping(params="method=addReleaseInfoToDb")
	public void addReleaseInfoToDb(HttpServletResponse response, ReleaseInfo info) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			CspDependInfoAo.get().addReleaseInfoToDb(info);
		} catch (SQLException e) {
			map.put("result", "failed");
			map.put("message", "");
		}
		if(map.size() == 0) {
			map.put("result", "success");
			map.put("message", "");
		}
		try {
			this.writeFastJsonForArray(response, map);
		} catch (IOException e) {
			logger.error("");
		}
	}
//	public void 
	public static void main(String[] args) {
		new APIAction().getRealTimeConsumeData(null, "itemcenter", "mercury,cart");
		
	}
}
