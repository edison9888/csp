package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.AmchartPo;
import com.taobao.csp.time.web.po.AmchartTwoYPo;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.csp.time.web.po.AmchartTwoYPo.ValuePo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.TreeGridPoNew;

/**
 * 用来展示b2b应用的action
 * @author zhongting.zy
 */
@Controller
@RequestMapping("/app/b2b/show.do")
public class B2BAppController extends BaseController {

	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	private static final Logger logger = Logger .getLogger(B2BAppController.class);

	@RequestMapping(params="method=gotoOceanRoot")
	public ModelAndView gotoOceanRoot(int appId, String keyName){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		String appName = appInfo.getAppName();

		ModelAndView view = new ModelAndView("/time/b2b/b2bocean");
		view.addObject("appInfo", appInfo);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("timeinterval", Constants.CACHE_TIME_INTERVAL);
		return view;
	}
	/**
	 * 显示ocean openapi应用的详细信息
	 * @param response
	 */
	@RequestMapping(params = "method=showOceanRoot")
	public void showOceanRoot(HttpServletResponse response, String appName, String keyName) {
		//KeyConstants.B2B_OCEAN_APPLICATION;
		//KeyConstants.B2B_OCEAN_EXCEPTION;
		Map<String, List<TimeDataInfo>> dataMap = commonService.querykeyDataForChild(appName, keyName,"E-times");
		//TreeGridPoNew[] childArray = new TreeGridPoNew[dataMap.size() + 1]; //第一个放时间
		List<TreeGridPoNew> childList = new ArrayList<TreeGridPoNew>();
		TreeGridPoNew root = new TreeGridPoNew();
		root.getMap().put("keyName", keyName);
		root.setUuid(UUID.randomUUID().toString());
		//root.setChildren(childArray);
		for(int i=0; i<Constants.CACHE_TIME_INTERVAL; i++) {
			root.getMap().put("time" + i, new String[]{"0","0","0"});  //HH:mm:ss callnum calltime
		}
		Map<String, String> standardMap = new HashMap<String, String>();
		TreeGridPoNew timePo = new TreeGridPoNew();
		timePo.setUuid(UUID.randomUUID().toString());
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		timePo.getMap().put("keyName", "当前时间");
		for(int i=0; i<Constants.CACHE_TIME_INTERVAL; i++) {
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - 1);
			String tmp = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
			timePo.getMap().put("time" + i, new String[]{tmp,"0","0"});
			standardMap.put(tmp, "time" + i);
		}
		childList.add(timePo);
		for(Entry<String, List<TimeDataInfo>> entry: dataMap.entrySet()) {
			TreeGridPoNew po = new TreeGridPoNew();
			po.setUuid(UUID.randomUUID().toString());
			po.getMap().put("keyName", "data not ready");
			List<TimeDataInfo> dataList = entry.getValue();
			if(dataList == null || dataList.size() == 0)
				continue;
			for(TimeDataInfo data: dataList) {
				po.getMap().put("keyName", data.getFullKeyName());
				if(!standardMap.containsKey(data.getFtime()))
					continue;
				String timeIndex = standardMap.get(data.getFtime());
				Map<String, Object> propertyMap = data.getPropertyMap();
				
				long callNum;
				long callTime;
				try {
					callNum = (Long) propertyMap.get("请求量");
				} catch (Exception e) {
					logger.error("",e);
					callNum = 0;
				}
				try {
					callTime = (Long) propertyMap.get("响应时间(ms)");
				} catch (Exception e) {
					logger.error("",e);
					callTime = 0;
				}				

				String[] rootValueArray = (String[]) root.getMap().get(timeIndex);
				po.getMap().put(timeIndex, new String[]{data.getFtime(), callNum + "", callTime + ""});   
				po.setState("closed");
				if(rootValueArray == null) {
					root.getMap().put(timeIndex, new String[]{data.getFtime(), callNum + "", callTime + ""});          
				} else {
					try {
						root.getMap().put(timeIndex,
								new String[] { data.getFtime(),
								Long.parseLong(rootValueArray[1]) + callNum + "",
								Long.parseLong(rootValueArray[2]) + callTime + "" });
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			}
			childList.add(po);
//			childArray[iIndexOfChild++] = po;
//			if(po.getMap().get("keyName").equals("data not ready")) {
//				po.getMap().remove("keyName");			
//			}
		}
		root.setChildren(childList.toArray(new TreeGridPoNew[0]));	//屏蔽掉没有数据的情况
		TreeGridPoNew[] array = new TreeGridPoNew[]{root};
		try {
			this.writeJSONToResponseJSONArray(response, array);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@RequestMapping(params = "method=showOceanSub")
	public void showOceanSub(HttpServletResponse response, String appName, String keyName) {
		Map<String, List<TimeDataInfo>> dataMap = commonService.querykeyDataForChild(appName, keyName,"E-times");
		if(dataMap.size() == 0) {
			try {
				this.writeJSONToResponseJSONArray(response, new TreeGridPoNew[0]);
			} catch (IOException e) {
				logger.error("", e);
			}      
			return;
		}
		//TreeGridPoNew[] childArray = new TreeGridPoNew[dataMap.size() + 1]; //第一个放时间
		List<TreeGridPoNew> childList = new ArrayList<TreeGridPoNew>();
		TreeGridPoNew timePo = new TreeGridPoNew();
		timePo.setUuid(UUID.randomUUID().toString());

		Map<String, String> standardMap = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		timePo.getMap().put("keyName", "当前时间");
		for(int i=0; i<Constants.CACHE_TIME_INTERVAL; i++) {
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - 1);
			String tmp = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));			
			timePo.getMap().put("time" + i, new String[]{tmp,"0","0"});
			standardMap.put(tmp, "time" + i);
		}
		childList.add(timePo);

		for(Entry<String, List<TimeDataInfo>> entry: dataMap.entrySet()) {
			TreeGridPoNew po = new TreeGridPoNew();
			po.getMap().put("keyName", "data not ready");
			po.setUuid(UUID.randomUUID().toString());
			List<TimeDataInfo> dataList = entry.getValue();
			if(dataList == null || dataList.size() == 0)
				continue;
			for(TimeDataInfo data: dataList) {
				po.getMap().put("keyName", data.getFullKeyName());
				if(!standardMap.containsKey(data.getFtime()))
					continue;        
				String timeIndex = standardMap.get(data.getFtime());

				Map<String, Object> propertyMap = data.getPropertyMap();
				long callNum;
				long callTime;
				
				try {
					callNum = (Long) propertyMap.get("请求量");
				} catch (Exception e) {
					logger.error("",e);
					logger.info(propertyMap.keySet().toString());
					callNum = 0;
				}
				try {
					callTime = (Long) propertyMap.get("响应时间(ms)");
				} catch (Exception e) {
					logger.error("",e);
					callTime = 0;
				}

				po.getMap().put(timeIndex, new String[]{data.getFtime(), callNum + "", callTime + ""});   
				po.setState("closed");
			}
			childList.add(po);
		}
		try {
			this.writeJSONToResponseJSONArray(response, childList.toArray(new TreeGridPoNew[0]));
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	@RequestMapping(params="method=gotoOceanRootHistory")
	public ModelAndView gotoOceanRootHistory(int appId, String keyName, String queryTime){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView("/time/b2b/b2boceanhistory");
		view.addObject("appName", appInfo.getAppName());
		view.addObject("appInfo", appInfo);
		view.addObject("keyName", keyName);
		view.addObject("queryTime", queryTime);
		return view;
	}	

	@RequestMapping(params = "method=showOceanRootHistory")
	public void showOceanRootHistory(HttpServletResponse response, String appName, String keyName, String queryTime) {
		AppInfoPo appInfo = AppInfoAo.get().getAppInfoByOpsName(appName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(queryTime);
			time.setTime(date1);
		} catch (Exception e1) {
		}
		Date queryDate = time.getTime();
		TreeGridPoNew root = new TreeGridPoNew();
		root.getMap().put("keyName", keyName);
		if(appInfo != null) {
			List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);
			List<CspKeyInfo> childKeyList = KeyAo.get().findKeyChildByApp(appInfo.getAppId(), keyName);
			TreeGridPoNew[] childArray = new TreeGridPoNew[childKeyList.size()]; //第一个放时间
			root.setUuid(UUID.randomUUID().toString());
			root.setChildren(childArray);
			int iIndexOfChild = 0;
			long totalCallNum = 0;
			long totalCallTime = 0;

			for(CspKeyInfo childKeyInfo: childKeyList) {
				try {
					TreeGridPoNew po = new TreeGridPoNew();
					po.setUuid(UUID.randomUUID().toString());
					po.getMap().put("keyName", childKeyInfo.getKeyName());
					po.setState("closed");
					Map<String, String> valueMap = getValueMap(appName,
							childKeyInfo.getKeyName(), queryDate,
							propertys.toArray(new String[] {}));
					long callNum = Long.parseLong(valueMap.get("E-times"));
					long callTime = Long.parseLong(valueMap.get("C-time"));

					totalCallNum += callNum;
					totalCallTime += callTime;

					po.getMap().put("value",
							new String[] { callNum + "", callTime + "" });
					childArray[iIndexOfChild++] = po;
				} catch (Exception e) {
					logger.error("", e);
				}
			}		
			root.getMap().put("value", new String[]{totalCallNum + "",totalCallTime + ""});//callnum calltime
		}

		TreeGridPoNew[] array = new TreeGridPoNew[]{root};
		try {
			this.writeJSONToResponseJSONArray(response, array);
		} catch (IOException e) {
			logger.error("", e);
		}		
	}

	@RequestMapping(params = "method=showOceanSubHistory")
	public void showOceanSubHistory(HttpServletResponse response, String appName, String keyName, String queryTime) {
		AppInfoPo appInfo = AppInfoAo.get().getAppInfoByOpsName(appName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(queryTime);
			time.setTime(date1);
		} catch (Exception e1) {
		}
		Date queryDate = time.getTime();
		TreeGridPoNew[] childArray = new TreeGridPoNew[0]; //默认没有数据

		if(appInfo != null) {
			List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);
			List<CspKeyInfo> childKeyList = KeyAo.get().findKeyChildByApp(appInfo.getAppId(), keyName);
			childArray = new TreeGridPoNew[childKeyList.size()]; //第一个放时间
			int iIndexOfChild = 0;
			for(CspKeyInfo childKeyInfo: childKeyList) {
				try {
					TreeGridPoNew po = new TreeGridPoNew();
					po.setUuid(UUID.randomUUID().toString());
					po.getMap().put("keyName", childKeyInfo.getKeyName());
					po.setState("closed");
					Map<String, String> valueMap = getValueMap(appName,
							childKeyInfo.getKeyName(), queryDate,
							propertys.toArray(new String[] {}));
					long callNum = Long.parseLong(valueMap.get("E-times"));
					long callTime = Long.parseLong(valueMap.get("C-time"));
					po.getMap().put("value",
							new String[] { callNum + "", callTime + "" });
					childArray[iIndexOfChild++] = po;
				} catch (Exception e) {
					logger.error("", e);
				}
			}	
		}

		try {
			this.writeJSONToResponseJSONArray(response, childArray);
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	private Map<String, String> getValueMap(String appName, String keyName, Date queryDate, String[] propertyArray) {
		Map<Date,Map<String,String>> result = QueryHistoryUtil.queryMultiProperty(
				appName, keyName,"", propertyArray, queryDate);
		final Map<String, String> valueMap = new HashMap<String, String>();
		for(Map.Entry<Date,Map<String,String>> entry:result.entrySet()){
			Map<String,String> m = entry.getValue();
			if(m==null||m.size()==0){
				continue;
			}
			for(Map.Entry<String,String> h:m.entrySet()){
				String key = h.getKey();
				String value = h.getValue();
				if(NumberUtils.isNumber(value)){
					try{
						long longValue = Long.parseLong(value);
						if(valueMap.containsKey(key)) {
							longValue += Long.parseLong(valueMap.get(key));
						}
						valueMap.put(key, longValue + "");
					}catch (Exception e) {
					}
				}
			}
		}
		return valueMap;
	}

	/**
	 * 经过测试，可用的统一模板
	 * @return
	 */
	@RequestMapping(params = "method=amchartDemo")
	public ModelAndView amchartDemo() {
		AmchartPo chart = new AmchartPo();
		Map<String, String> fieldMap = new HashMap<String, String>();
		fieldMap.put("date", "时间");
		fieldMap.put("callnum", "调用量");
		fieldMap.put("baseline", "基线调用量");

		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR)-1);
		for(int i=0; i<1440; i++) {
			Map<String, Object> valueMap = new HashMap<String, Object>();
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
			valueMap.put("date", cal.getTimeInMillis());
			valueMap.put("callnum", Math.round(Math.random() * 40) + 20);
			valueMap.put("baseline", Math.round(Math.random() * 30) + 20);
			valueList.add(valueMap);
		}
		chart.setFieldMap(fieldMap);
		chart.setTimeUnit("mm");
		chart.setValueList(valueList);
		chart.setxField("date");
		chart.setyTitle("调用量");

		ModelAndView view = new ModelAndView("/time/chartreport/amchart_mul");
		view.addObject("chart", chart);
		return view;
	}

	//	@RequestMapping(params = "method=showHistoryInfo")
	//	public ModelAndView showHistoryInfo(String appName,String keyName,String time1,
	//			String selectProperty) {
	//		Map<String, String> fieldMap = new HashMap<String, String>();
	//		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
	//		
	//		AmchartPo chart = new AmchartPo();
	//		chart.setFieldMap(fieldMap);
	//		chart.setTimeUnit("mm");
	//		chart.setxField("date");
	//		chart.setyTitle("调用量");
	//		chart.setValueList(valueList);		
	//		fieldMap.put("date", "时间"); //important
	//		List<String> listProperty = null;
	//		if(selectProperty != null && !selectProperty.equals("allfield")) {
	//			listProperty = new ArrayList<String>();
	//			String title = PropNameDescUtil.getDesc(selectProperty);
	//			listProperty.add(selectProperty);
	//			fieldMap.put(selectProperty.replace("-", ""), title);
	//		} else {
	//			List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);
	//			listProperty = new ArrayList<String>();
	//			for(String prop : propertys){
	//				String title = PropNameDescUtil.getDesc(prop);
	//				listProperty.add(prop);
	//				fieldMap.put(prop.replace("-", ""), title);	
	//			}
	//		}
	//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	//		Calendar time = Calendar.getInstance();
	//		try {
	//			Date date1 = sdf.parse(time1);
	//			time.setTime(date1);
	//		} catch (Exception e1) {
	//		}
	//		Date cur = time.getTime();
	//		time1 = sdf.format(cur);
	//		
	//		//SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//		Map<Date,Map<String,String>> cur_result = QueryHistoryUtil.queryMultiProperty(appName, keyName, listProperty.toArray(new String[]{}), cur);
	//		
	//		for(Map.Entry<Date,Map<String,String>> entry:cur_result.entrySet()){
	//			Date date = entry.getKey();
	//			Map<String,String> m = entry.getValue();
	//
	//			if(m==null||m.size()==0){
	//				continue;
	//			}
	//			for(Map.Entry<String,String> h:m.entrySet()){
	//				String key = h.getKey();	//就是显示的field
	//				key = key.replace("-", "");
	//				String value = h.getValue();
	//				Map<String, Object> valueMap = new HashMap<String, Object>();
	//				valueMap.put("date", date.getTime());
	//				if(!StringUtil.isNumeric(value)) {
	//					value = "0";
	//				}
	//				valueMap.put(key, Double.parseDouble(value));
	//				valueList.add(valueMap);
	//			}
	//		}
	//		ModelAndView view = new ModelAndView("/time/chartreport/amchart_mul");
	//		view.addObject("chart", chart);
	//		return view;
	//	}	
	@RequestMapping(params = "method=showHistoryInfo")
	public ModelAndView showHistoryInfo(String appName,String keyName,String time1,
			String selectProperty) {
		AmchartTwoYPo chart = new AmchartTwoYPo();
		chart.setTimeUnit("mm");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(time1);
			time.setTime(date1);
		} catch (Exception e1) {
		}
		Date cur = time.getTime();
		time1 = sdf.format(cur);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.setTime(cur);

		Map<Date,Map<String,String>> cur_result = QueryHistoryUtil.queryMultiProperty(
				appName, keyName,"", new String[]{PropConstants.E_TIMES, PropConstants.C_TIME}, cur);

		long totalNum = 0;
		float totalTime = 0;
		
		for(int i=0; i<24*60; i++) {
			cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + 1);
			Date date = cal.getTime();
			Map<String,String> m = cur_result.get(date);
			if(m == null) {
				chart.addNewValueToList(0, 0, cal.getTimeInMillis());
				continue;
			}

			long executeSum = 0;
			try {
				executeSum = Long.parseLong(m.get(PropConstants.E_TIMES));
			} catch (Exception e) {
				logger.error("", e);
			}
			float timeNum = 0;
			try {
				timeNum = Float.parseFloat(m.get(PropConstants.C_TIME));
			} catch (Exception e) {
				logger.error("", e);
			}
			
			totalNum += executeSum;
			totalTime += timeNum;
			
			chart.addNewValueToList(executeSum, timeNum, cal.getTimeInMillis());
		}
//
//		for(Map.Entry<Date,Map<String,String>> entry:cur_result.entrySet()){
//			Date date = entry.getKey();
//			long collectTimeL = date.getTime();
//			Map<String,String> m = entry.getValue();
//			if(m==null||m.size()==0){
//				continue;
//			}
//			long executeSum = 0;
//			try {
//				executeSum = Long.parseLong(m.get(PropConstants.E_TIMES));
//			} catch (Exception e) {
//				logger.error("", e);
//			}
//			float timeNum = 0;
//			try {
//				timeNum = Float.parseFloat(m.get(PropConstants.C_TIME));
//			} catch (Exception e) {
//				logger.error("", e);
//			}
//			String s1 = Math.random()*3000 + "";
//			s1 = s1.substring(0, s1.indexOf('.'));
//
//			String s2 = Math.random()*1000 + "";
//			s2 = s2.substring(0, s2.indexOf('.'));
//			chart.addNewValueToList(Long.parseLong(s1),Float.parseFloat(s2), collectTimeL);
//		}

		ModelAndView view = new ModelAndView("/time/chartreport/two_y_seperate");
		view.addObject("chart", chart);
		view.addObject("totalNum", totalNum);
		view.addObject("totalTime", totalTime);
		return view;
	}		
}

