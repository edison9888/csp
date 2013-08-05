
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.util.AmlineFlash;
import com.taobao.csp.time.util.PropNameDescUtil;
import com.taobao.monitor.common.ao.center.KeyAo;

/**
 * @author xiaodu
 *
 * 下午4:41:16
 */
@Controller
@RequestMapping("/app/detail/history.do")
public class HistoryController {



	/**
	 * 获取应用级别的历史数据
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param time1
	 * @param time2
	 * @return
	 *TODO
	 */
	@RequestMapping(params = "method=showHistory")
	public ModelAndView showHistory(String appName,String keyName,String time1,String time2) {

		List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);

		Map<String,AmlineFlash> ammap = doHandHistoryByTime(appName,keyName,"",propertys,time1,time2);

		ModelAndView view = new ModelAndView("/time/history/history");
		view.addObject("flashMap", ammap);
		view.addObject("time1", time1);
		view.addObject("time2", time2);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("method", "showHistory");
		return view;

	}
	/**
	 * 获取某台机器的历史数据，这个keyName 不包含IP
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param ip
	 * @param time1
	 * @param time2
	 * @return
	 * modify by zhongting.zy 新增按ip对比，支持查询单个key
	 */
	@RequestMapping(params = "method=showHistoryByip")
	public ModelAndView showHistoryByip(String appName,String keyName,String ip,String time1,String time2,
			String selectProperty, String comparetype) {
		if(comparetype == null)
			comparetype = "time";
		if(ip == null)
			ip = "";

		List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);
		List<String> listProperty = null;
		if(selectProperty != null && !selectProperty.equals("allfield")) {
			listProperty = new ArrayList<String>();
			listProperty.add(selectProperty);
		} else {
			listProperty = new ArrayList<String>(propertys);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(time1);
			time.setTime(date1);
		} catch (Exception e1) {
		}
		Date cur = time.getTime();
		time1 = sdf.format(cur);
		try {
			Date date2 = sdf.parse(time2);
			time.setTime(date2);
		} catch (Exception e1) {
			time.add(Calendar.DAY_OF_YEAR, -7);
		}
		Date old = time.getTime();
		time2 =  sdf.format(old);		

		Map<String,AmlineFlash> ammap = null; 
		if(comparetype.equals("time")) {
			ip = ip.split(",")[0];	//默认按第一位有效
			ammap = doHandHistoryByTime(appName,keyName,ip,listProperty,time1,time2);	
		} else {
			ammap = doHandHistoryByIps(appName,keyName,
					listProperty,time1,ip);
		}	

		ModelAndView view = new ModelAndView("/time/history/historycompare");
		view.addObject("flashMap", ammap);
		view.addObject("time1", time1);
		view.addObject("time2", time2);
		view.addObject("appName", appName);
		view.addObject("keyName", keyName);
		view.addObject("method", "showHistoryByip");
		view.addObject("propertys", propertys);
		view.addObject("selectProperty", selectProperty);
		view.addObject("comparetype", comparetype);
		view.addObject("ip", ip);
		return view;
	}

	private Map<String,AmlineFlash>  doHandHistoryByTime(String appName,String keyName,String ip,
			List<String> propertys,String time1,String time2){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(time1);
			time.setTime(date1);
		} catch (Exception e1) {

		}
		Date cur = time.getTime();

		time1 = sdf.format(cur);

		try {
			Date date2 = sdf.parse(time2);
			time.setTime(date2);
		} catch (Exception e1) {
			time.add(Calendar.DAY_OF_YEAR, -7);
		}
		Date old = time.getTime();

		time2 =  sdf.format(old);

		String cur_ftime = sdf.format(cur);

		String old_ftime = sdf.format(old);

		Map<Date,Map<String,String>> cur_result = QueryHistoryUtil.queryMultiProperty(
				appName, keyName,ip, propertys.toArray(new String[]{}), cur);
		Map<Date,Map<String,String>> old_result = QueryHistoryUtil.queryMultiProperty(
				appName, keyName,ip, propertys.toArray(new String[]{}), old);

		Map<String,AmlineFlash> ammap = new HashMap<String, AmlineFlash>();
		for(String prop:propertys){
			Map<String,Double> map = BaseLineCache.get().getBaseLine(appName, keyName, prop);
			if(map==null||map.size()==0){
				continue;
			}

			for(Map.Entry<String,Double> entry:map.entrySet()){

				AmlineFlash am = ammap.get(prop);
				if(am == null){
					am = new AmlineFlash();
					ammap.put(prop, am);
				}

				String tmp = cur_ftime+" "+entry.getKey();

				try{
					am.addValue(PropNameDescUtil.getDesc(prop)+"[基线]", sdfTmp.parse(tmp).getTime(), entry.getValue());
				}catch (Exception e) {
				}

			}
		}

		for(Map.Entry<Date,Map<String,String>> entry:cur_result.entrySet()){
			Date date = entry.getKey();
			Map<String,String> m = entry.getValue();

			if(m==null||m.size()==0){
				continue;
			}

			for(Map.Entry<String,String> h:m.entrySet()){
				String key = h.getKey();
				String value = h.getValue();
				if(NumberUtils.isNumber(value)){
					AmlineFlash am = ammap.get(key);
					if(am == null){
						am = new AmlineFlash();
						ammap.put(key, am);
					}
					try{
						am.addValue(PropNameDescUtil.getDesc(key)+"["+cur_ftime+"]", date.getTime(), Double.parseDouble(value));
					}catch (Exception e) {
					}
				}
			}
		}

		for(Map.Entry<Date,Map<String,String>> entry:old_result.entrySet()){
			Date date = entry.getKey();
			Map<String,String> m = entry.getValue();
			if(m==null||m.size()==0){
				continue;
			}

			for(Map.Entry<String,String> h:m.entrySet()){
				String key = h.getKey();
				String value = h.getValue();
				if(NumberUtils.isNumber(value)){
					AmlineFlash am = ammap.get(key);
					if(am == null){
						am = new AmlineFlash();
						ammap.put(key, am);
					}
					try{
						am.addValue(PropNameDescUtil.getDesc(key)+"["+old_ftime+"]", date.getTime(), Double.parseDouble(value));
					}catch (Exception e) {
					}
				}
			}
		}


		return ammap;
	}


	private Map<String,AmlineFlash>  doHandHistoryByIps(String appName,String keyName,
			List<String> propertys,String time1, String ips){
		

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Calendar time = Calendar.getInstance();
		try {
			Date date1 = sdf.parse(time1);
			time.setTime(date1);
		} catch (Exception e1) {

		}
		Date cur = time.getTime();

		time1 = sdf.format(cur);

		String cur_ftime = sdf.format(cur);

		Map<String,AmlineFlash> ammap = new HashMap<String, AmlineFlash>();
		for(String prop:propertys){
			Map<String,Double> map = BaseLineCache.get().getBaseLine(appName, keyName, prop);
			if(map==null||map.size()==0){
				continue;
			}

			for(Map.Entry<String,Double> entry:map.entrySet()){

				AmlineFlash am = ammap.get(prop);
				if(am == null){
					am = new AmlineFlash();
					ammap.put(prop, am);
				}

				String tmp = cur_ftime+" "+entry.getKey();

				try{
					am.addValue(PropNameDescUtil.getDesc(prop)+"[基线]", sdfTmp.parse(tmp).getTime(), entry.getValue());
				}catch (Exception e) {
				}

			}
		}

		String[] ipArray = ips.split(",");
		for(String ip : ipArray) {
			Map<Date,Map<String,String>> cur_result = QueryHistoryUtil.queryMultiProperty(
					appName, keyName,ip, propertys.toArray(new String[]{}), cur);		
			for(Map.Entry<Date,Map<String,String>> entry:cur_result.entrySet()){
				Date date = entry.getKey();
				Map<String,String> m = entry.getValue();
				if(m==null||m.size()==0){
					continue;
				}
				for(Map.Entry<String,String> h:m.entrySet()){
					String key = h.getKey();
					String value = h.getValue();
					if(NumberUtils.isNumber(value)){
						AmlineFlash am = ammap.get(key);
						if(am == null){
							am = new AmlineFlash();
							ammap.put(key, am);
						}
						try{
							am.addValue(PropNameDescUtil.getDesc(key)+"["+ip +"]", date.getTime(), Double.parseDouble(value));
						}catch (Exception e) {
						}
					}
				}
			}			
		}
		return ammap;
	}	

//	/** 
//	 * 这个是取得主机的相关历史数据，其中keyName 包含了IP
//	 *@author xiaodu
//	 * @param appName
//	 * @param keyName
//	 * @param time1
//	 * @param time2
//	 * @return
//	 *TODO
//	 */
//	@RequestMapping(params = "method=showHistoryHost")
//	public ModelAndView showHistoryHost(String appName,String keyName,String time1,String time2) {
//		String queryKey = "";
//		if(keyName.lastIndexOf(Constants.S_SEPERATOR) >= 0) {	//or there will be out of index error
//			queryKey = keyName.substring(0,keyName.lastIndexOf(Constants.S_SEPERATOR));
//		} else {
//			queryKey = keyName;
//		}
//		List<String> propertys = KeyAo.get().findKeyPropertyNames(queryKey);
//
//		Map<String,AmlineFlash> ammap = doHandHistoryByTime(appName,keyName,propertys,time1,time2);
//
//		ModelAndView view = new ModelAndView("/time/history/history");
//		view.addObject("flashMap", ammap);
//		view.addObject("time1", time1);
//		view.addObject("time2", time2);
//		view.addObject("appName", appName);
//		view.addObject("keyName", keyName);
//		view.addObject("method", "showHistoryHost");
//		return view;
//	}

}
