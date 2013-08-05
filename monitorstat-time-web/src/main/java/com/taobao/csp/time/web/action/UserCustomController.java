package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.time.custom.ao.KeyAo;
import com.taobao.csp.time.custom.ao.NaviAo;
import com.taobao.csp.time.custom.ao.ViewAo;
import com.taobao.csp.time.util.AmlineFlash;
import com.taobao.csp.time.util.QueryUtilHelp;
import com.taobao.csp.time.util.Sort;
import com.taobao.csp.time.util.Urls;
import com.taobao.csp.time.web.po.UserCustomNaviKeyPo;
import com.taobao.csp.time.web.po.UserCustomNaviMainPo;
import com.taobao.csp.time.web.po.UserCustomNaviViewPo;
import com.taobao.csp.time.web.po.UserCustomTreeKeyPo;
import com.taobao.csp.time.web.session.SessionUtil;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyPropertyRelation;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/app/detail/custom/show.do")
public class UserCustomController extends BaseController{
	private static Logger logger = Logger.getLogger(UserCustomController.class);
	@RequestMapping(params = "method=updateNaviName")
	public void updateNaviName(Integer naviId,String naviName){
		if(naviName!=null&&!naviName.equals("")){
			NaviAo.get().updateNaviName(naviId, naviName);
		}
	}
	@RequestMapping(params = "method=queryHistory")
	public ModelAndView showHistory(String appName,String keyName) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<String> propertys = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyNames(keyName);
		Calendar time = Calendar.getInstance();
		Date cur = time.getTime();

		time.add(Calendar.DAY_OF_YEAR, -7);

		Date old = time.getTime();

		String cur_ftime = sdf.format(cur);

		String old_ftime = sdf.format(old);

		Map<Date,Map<String,String>> cur_result = QueryHistoryUtil.queryMultiProperty(
				appName, keyName,"", propertys.toArray(new String[]{}), cur);

		Map<Date,Map<String,String>> old_result = QueryHistoryUtil.queryMultiProperty(
				appName, keyName,"", propertys.toArray(new String[]{}), old);

		Map<String,AmlineFlash> map = new HashMap<String, AmlineFlash>();

		for(Map.Entry<Date,Map<String,String>> entry:cur_result.entrySet()){
			Date date = entry.getKey();
			Map<String,String> m = entry.getValue();
			for(Map.Entry<String,String> h:m.entrySet()){
				String key = h.getKey();
				String value = h.getValue();
				AmlineFlash am = map.get(key);
				if(am == null){
					am = new AmlineFlash();
					map.put(key, am);
				}
				try{
					am.addValue(key+"["+cur_ftime+"]", date.getTime(), Double.parseDouble(value));
				}catch (Exception e) {
				}
			}
		}

		for(Map.Entry<Date,Map<String,String>> entry:old_result.entrySet()){
			Date date = entry.getKey();
			Map<String,String> m = entry.getValue();
			for(Map.Entry<String,String> h:m.entrySet()){
				String key = h.getKey();
				String value = h.getValue();
				AmlineFlash am = map.get(key);
				if(am == null){
					am = new AmlineFlash();
					map.put(key, am);
				}
				try{
					am.addValue(key+"["+old_ftime+"]", date.getTime(), Double.parseDouble(value));
				}catch (Exception e) {
				}
			}
		}


		ModelAndView view = new ModelAndView("/time/history/history");
		view.addObject("flashMap", map);

		return view;
	}
	@RequestMapping(params="method=showIndex")
	public ModelAndView showIndex(HttpServletRequest request,Integer naviId,Integer appId){
		List<UserCustomNaviKeyPo> list = new ArrayList<UserCustomNaviKeyPo>();
		KeyAo.get().findInfoByNaviId(list, naviId);
		List<IFrame> iframes = new ArrayList<IFrame>();
		for(UserCustomNaviKeyPo po : list){
			String iframestr = po.getActionUrl()+"&appName="+po.getAppName()+"&keyName="+po.getKeyName()+"&viewMod="+po.getViewMod()+"&property="+po.getProperty();
			IFrame iframe = new IFrame();
			iframe.setUrl(iframestr);
			if(po.getViewMod().equals("mapPoListTable2")){
				iframe.setSize("50%");
			}else {
				iframe.setSize("100%");
			};
			iframes.add(iframe);
		}
		
		
		
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		
		ModelAndView view = new ModelAndView("/time/custom/index");
		view.addObject("iframes",iframes);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params="method=indexNavi")
	public ModelAndView indexNavi(HttpServletRequest request,Integer appId) throws Exception{
		ModelAndView view = new ModelAndView("/time/custom/indexNavi");
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		String userName = SessionUtil.getCspUserInfo(request).getMail();
		List<UserCustomNaviMainPo> navis = new ArrayList<UserCustomNaviMainPo>();
		NaviAo.get().findNavisByUserName(userName, navis);  			
		view.addObject("navis", navis);
		view.addObject("userName", userName);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params="method=indexNaviApp")
	public ModelAndView indexNaviApp(HttpServletRequest request,Integer appId){
		ModelAndView view = new ModelAndView("/time/custom/indexNaviApp");
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<UserCustomNaviMainPo> navis = new ArrayList<UserCustomNaviMainPo>();
		NaviAo.get().findNavisByAppName(appInfo.getAppName(), navis);  			
		view.addObject("navisapp", navis);

		//		view.addObject("userName", userName);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params="method=deleteNavi")
	public void deleteNavi(HttpServletRequest request,HttpServletResponse response,Integer appId){
		try {
			String[] naviIdStrs = request.getParameterValues("naviId");
			String userName = SessionUtil.getCspUserInfo(request).getMail();
			List<Integer> naviIds = new ArrayList<Integer>();
			for(String naviIdStr : naviIdStrs){
				naviIds.add(Integer.parseInt(naviIdStr));
			}
			NaviAo.get().deleteNavisByUserName(userName, naviIds);
			response.sendRedirect(request.getContextPath()+"/app/detail/custom/show.do?method=indexNavi&appId="+appId);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=deleteNaviApp")
	public void deleteNaviApp(HttpServletRequest request,HttpServletResponse response,Integer appId){
		try {
			String[] naviIdStrs = request.getParameterValues("naviId");
			AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
			List<Integer> naviIds = new ArrayList<Integer>();
			for(String naviIdStr : naviIdStrs){
				naviIds.add(Integer.parseInt(naviIdStr));
			}
			NaviAo.get().deleteNavisByAppName(appInfo.getAppName(), naviIds);
			response.sendRedirect(request.getContextPath()+"/app/detail/custom/show.do?method=indexNaviApp&appId="+appId);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=indexNaviKey")
	public ModelAndView indexNaviKey(HttpServletRequest request,Integer naviId,String naviName,Integer appId){
		ModelAndView view = new ModelAndView("/time/custom/indexNaviKey");
		//		String userName = SessionUtil.getCspUserInfo(request).getMail();
		List<UserCustomNaviKeyPo> list =new ArrayList<UserCustomNaviKeyPo>();
		KeyAo.get().findInfoByNaviId(list, naviId);
		transferToUserMode(list);
		try {
			naviName = new String(naviName.getBytes("gbk"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		view.addObject("userName", "");
		view.addObject("naviName", naviName);
		view.addObject("naviId",naviId);
		view.addObject("keys", list);
		view.addObject("appInfo", appInfo);
		return view;
	}
	@RequestMapping(params="method=indexNaviKeyApp")
	public ModelAndView indexNaviKeyApp(HttpServletRequest request,Integer naviId,String naviName,Integer appId){
		ModelAndView view = new ModelAndView("/time/custom/indexNaviKey");
		List<UserCustomNaviKeyPo> list =new ArrayList<UserCustomNaviKeyPo>();
		KeyAo.get().findInfoByNaviId(list, naviId);
		transferToUserMode(list);
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		view.addObject("userName", "");
		view.addObject("naviName", naviName);
		view.addObject("naviId",naviId);
		view.addObject("keys", list);
		view.addObject("appInfo", appInfo);
		return view;
	}
	private void transferToUserMode(List<UserCustomNaviKeyPo> list){
		for(UserCustomNaviKeyPo po : list){
			po.setActionUrl(DataViewModTransferMap.getSingle().get(po.getActionUrl()));
			po.setViewMod(DataViewModTransferMap.getSingle().get(po.getViewMod()));
		}
	}

	@RequestMapping(params="method=deleteNaviKey")
	public void deleteNaviKey(HttpServletRequest request,HttpServletResponse response,Integer naviId,String naviName,Integer appId){
		try {
			String[] keyIds = request.getParameterValues("keyId");
			List<Integer> list = new ArrayList<Integer>();
			for(String keyId : keyIds){
				list.add(Integer.parseInt(keyId));
			}
			KeyAo.get().deleteInfoByKeyId(list);
			response.sendRedirect(request.getContextPath()+"/app/detail/custom/show.do?method=indexNaviKey&naviId="+naviId+"&naviName="+naviName+"&appId="+appId);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=indexAddNaviKey")
	public ModelAndView indexAddNaviKey(Integer naviId,String naviName,Integer appId){
		ModelAndView view = new ModelAndView("/time/custom/addNaviKey");
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		view.addObject("naviId",naviId);
		view.addObject("naviName", naviName);
		view.addObject("appInfo", appInfo);
		view.addObject("appId",appId);
		return view;
	}
	@RequestMapping(params="method=addNaviKey")
	public void addNaviKey(HttpServletRequest request,HttpServletResponse response,Integer naviId,String naviName,Integer appId){
		try {
			String appName = AppInfoCache.getAppInfoById(appId).getAppName();
			String keyName = request.getParameter("keyName").trim();
			String actionUrl = request.getParameter("actionUrl").trim();
			String viewMod = request.getParameter("viewMod").trim();
			String property = request.getParameter("property").trim();
			List<UserCustomNaviKeyPo> list = new ArrayList<UserCustomNaviKeyPo>();
			UserCustomNaviKeyPo po = new UserCustomNaviKeyPo();
			po.setActionUrl(actionUrl);
			po.setAppName(appName);
			po.setKeyName(keyName);
			po.setNavId(naviId);
			po.setProperty(property);
			po.setViewMod(viewMod);
			list.add(po);
			KeyAo.get().insertInfoByNaviId(list);
			response.sendRedirect(request.getContextPath()+"/app/detail/custom/show.do?method=indexNaviKey&naviId="+naviId+"&naviName="+naviName+"&appId="+appId);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=addNavi")
	public void addNavi(String naviName,HttpServletRequest request,HttpServletResponse response,Integer appId){
		try {
			String userName = SessionUtil.getCspUserInfo(request).getMail();
			List<String> naviNames = new ArrayList<String>();
			naviNames.add(naviName);
			List<UserCustomNaviMainPo> navis = new ArrayList<UserCustomNaviMainPo>();
			NaviAo.get().findNavisByUserName(userName, navis);
			boolean flag = false;
			for(UserCustomNaviMainPo po : navis){
				if(po.getNaviName().equals(naviName)){
					flag = true;
					break;
				}
			}
			if(flag==false&&naviName!=null&&!naviName.equals(""))NaviAo.get().insertNavisByUserName(userName, naviNames);
			response.sendRedirect(request.getContextPath()+"/app/detail/custom/show.do?method=indexNavi&appId="+appId);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=addNaviApp")
	public void addNaviApp(String naviName,HttpServletRequest request,HttpServletResponse response,Integer appId){
		try {
			AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
			List<String> naviNames = new ArrayList<String>();
			naviNames.add(naviName);
			List<UserCustomNaviMainPo> navis = new ArrayList<UserCustomNaviMainPo>();
			NaviAo.get().findNavisByAppName(appInfo.getAppName(), navis);
			boolean flag = false;
			for(UserCustomNaviMainPo po : navis){
				if(po.getNaviName().equals(naviName)){
					flag = true;
					break;
				}
			}
			if(flag==false&&naviName!=null&&!naviName.equals(""))NaviAo.get().insertNavisByAppName(appInfo.getAppName(), naviNames);
			response.sendRedirect(request.getContextPath()+"/app/detail/custom/show.do?method=indexNaviApp&appId="+appId);
		} catch (Exception e) {
			logger.info(e);
		}
	}

	@RequestMapping(params="method=getKey")
	public void getKey(Integer appId,HttpServletResponse response){
		try {
			Map<String, List<JSONObject>> jsonMap = new HashMap<String, List<JSONObject>>();
			UserCustomTreeKeyPo po = new UserCustomTreeKeyPo();
			po.setCheckstate(0);
			po.setComplete(true);
			po.setHasChildren(false);
			po.setId("1");
			po.setIsexpand(true);
			po.setShowcheck(true);
			po.setText(KeyConstants.HSF_CONSUMER);
			po.setValue(KeyConstants.HSF_CONSUMER);
			UserCustomTreeKeyPo po2 = new UserCustomTreeKeyPo();
			po2.setCheckstate(0);
			po2.setComplete(true);
			po2.setHasChildren(true);
			po2.setId("0");
			po2.setIsexpand(true);
			po2.setShowcheck(true);
			po2.setText("detail");
			po2.setValue("detail");
			po2.setChildNodes(new UserCustomTreeKeyPo[]{po});
			JSONObject obj = JSONObject.fromObject(po2);
			List<JSONObject> list = new ArrayList<JSONObject>();
			list.add(obj);
			jsonMap.put("keyData", list);
			writeJSONToResponseJSONObject(response,jsonMap);
		} catch (IOException e) {
			logger.info(e);
		}
	}

	@RequestMapping(params="method=querySingleRealTime")
	public ModelAndView querySingleRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki =  com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "querySingleRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=querySingleRealTimeAjax")
	public void querySingleRealTimeAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			Map<String, Map<String, Object>> map = QueryUtil.querySingleRealTime(appName, keyName);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,Object>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				Map<String,Object> poMap = new HashMap<String,Object>();
				String ftime = format.format(new Date(Long.parseLong(entry.getKey())));
				poMap.put("ftime", ftime);
				poMap.putAll(entry.getValue());
				list.add(poMap);
			}
			list = Sort.poListSort(list, property, 10);
			Map<String, List<Map<String, Object>>> jsonMap = new HashMap<String, List<Map<String, Object>>>();
			jsonMap.put("list", list);
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=querySingleRealTimeLine")
	public ModelAndView querySingleRealTimeLine(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki =  com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "querySingleRealTimeLineAjax");
		return view;
	}
	@RequestMapping(params="method=querySingleRealTimeLineAjax")
	public void querySingleRealTimeLineAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			Map<String, Map<String, Object>> map = QueryUtil.querySingleRealTime(appName, keyName);
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,Object>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				Map<String,Object> poMap = new HashMap<String,Object>();
				String ftime = format.format(new Date(Long.parseLong(entry.getKey())));
				poMap.put("ftime", ftime);
				poMap.putAll(entry.getValue());
				list.add(poMap);
			}
			list = Sort.poListSort(list, "ftime", list.size());
			Map<String, List<Map<String, Object>>> jsonMap = new HashMap<String, List<Map<String, Object>>>();
			jsonMap.put("list", list);
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryRecentlySingleRealTimeAjax")
	public void queryRecentlySingleRealTimeAjax(HttpServletResponse response,String appName,String keyName){
		try {
			Map<String, DataEntry> map = QueryUtil.queryRecentlySingleRealTime(appName, keyName);
			CspKeyInfo cki =  com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
			List<CspKeyPropertyRelation> ckprList = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			boolean flag = false;
			Map<String,Object> poMap = new HashMap<String,Object>();
			for(CspKeyPropertyRelation ckpr : ckprList){
				String property;
				Object object;
				try {
					property = ckpr.getPropertyName();
					object = map.get(property).getValue();
					if(!flag){
						String ftime = format.format(new Date((map.get(property).getTime())));
						poMap.put("ftime", ftime);
						flag=true;
					}
					poMap.put(property, object);
				} catch (Exception e) {
					logger.info("normal:"+e);
				}
			}
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			list.add(poMap);
			Map<String, List<Map<String, Object>>> jsonMap = new HashMap<String, List<Map<String, Object>>>();
			jsonMap.put("list", list);
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryRecentlySingleRealTime")
	public ModelAndView queryRecentlySingleRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki =  com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "queryRecentlySingleRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryChildRealTime")
	public ModelAndView queryChildRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "queryChildRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryChildRealTimeAjax")
	public void queryChildRealTimeAjax(HttpServletResponse response,String appName,String keyName){
		try {
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, keyName);
			Map<String,List<Map<String,Object>>> jsonMap = new HashMap<String,List<Map<String,Object>>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					if(entry2.getValue()==null)continue;
					Map<String,Object> poMap = new HashMap<String,Object>();
					String ftime = format.format(new Date(Long.parseLong(entry2.getKey())));
					poMap.put("ftime", ftime);
					poMap.putAll(entry2.getValue());
					list.add(poMap);
				}
				String key = entry.getKey().substring(entry.getKey().lastIndexOf(Constants.S_SEPERATOR)+1);
				jsonMap.put(key, list);
			}
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryRecentlyChildRealTime")
	public ModelAndView queryRecentlyChildRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "queryRecentlyChildRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryRecentlyChildRealTimeAjax")
	public void queryRecentlyChildRealTimeAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			Map<String, Map<String, DataEntry>> map = QueryUtil.queryRecentlyChildRealTime(appName, keyName);
			Map<String,Map<String,Object>> jsonMap = new HashMap<String,Map<String,Object>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,DataEntry>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				String key = entry.getKey().substring(entry.getKey().lastIndexOf(Constants.S_SEPERATOR)+1);
				Map<String,Object> poMap =new HashMap<String,Object>();
				boolean flag = false;
				for(Map.Entry<String, DataEntry> entry2 : entry.getValue().entrySet()){
					if(entry2.getValue()==null)continue;
					poMap.put(entry2.getKey(), entry2.getValue().getValue());
					if(!flag){
						poMap.put("ftime", format.format(new Date(entry2.getValue().getTime())));
						flag=true;
					}
				}
				jsonMap.put(key, poMap);
			}
			jsonMap = Sort.mapPoSort(jsonMap, property, 10);
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=querySingleHostRealTime")
	public ModelAndView querySingleHostRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "querySingleHostRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=querySingleHostRealTimeAjax")
	public void querySingleHostRealTimeAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			List<String> ipList =CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryHostRealTime(appName, keyName,ipList);
			Map<String,List<Map<String,Object>>> jsonMap = new HashMap<String,List<Map<String,Object>>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					if(entry2.getValue()==null)continue;
					Map<String,Object> poMap = new HashMap<String,Object>();
					String ftime = format.format(new Date(Long.parseLong(entry2.getKey())));
					poMap.put("ftime", ftime);
					poMap.putAll(entry2.getValue());
					list.add(poMap);
				}
				String key = entry.getKey().substring(entry.getKey().lastIndexOf(Constants.S_SEPERATOR)+1);
				jsonMap.put(key, list);
			}
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryRecentlySingleHostRealTime")
	public ModelAndView queryRecentlySingleHostRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "queryRecentlySingleHostRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryRecentlySingleHostRealTimeAjax")
	public void queryRecentlySingleHostRealTimeAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			List<String> ipList =CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
			Map<String, Map<String, DataEntry>> map = QueryUtil.queryRecentlyHostRealTime(appName,keyName,ipList);
			Map<String,Map<String,Object>> jsonMap = new HashMap<String,Map<String,Object>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,DataEntry>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				String key = entry.getKey().substring(entry.getKey().lastIndexOf(Constants.S_SEPERATOR)+1);
				Map<String,Object> poMap =new HashMap<String,Object>();
				boolean flag = false;
				for(Map.Entry<String, DataEntry> entry2 : entry.getValue().entrySet()){
					if(entry.getValue()==null)continue;
					poMap.put(entry2.getKey(), entry2.getValue().getValue());
					if(!flag){
						poMap.put("ftime", format.format(new Date(entry2.getValue().getTime())));
						flag=true;
					}
				}
				jsonMap.put(key, poMap);
			}
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryChildHostRealTime2")
	public ModelAndView queryChildHostRealTime2(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}

		List<String> childKeys =  childKeyList(appName, keyName);
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("childKeys",childKeys);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "querySingleHostRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryChildHostRealTime3")
	public ModelAndView queryChildHostRealTime3(String appName,String keyName,String viewMod,String property,String ip){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "querySingleHostRealTimeAjax");
		view.addObject("ip",ip);
		return view;
	}

	@RequestMapping(params="method=queryChildHostRealTime")
	public ModelAndView queryChildHostRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "queryChildHostRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryChildHostRealTimeAjax")
	public void queryChildHostRealTimeAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			List<String> ipList =CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
			Map<String, Map<String, Map<String, Object>>> map = QueryUtilHelp.queryChildHostRealTime(appName, keyName,ipList);
			Map<String,List<Map<String,Object>>> jsonMap = new HashMap<String,List<Map<String,Object>>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
				for(Map.Entry<String, Map<String,Object>> entry2 : entry.getValue().entrySet()){
					if(entry2.getValue()==null)continue;
					Map<String,Object> poMap = new HashMap<String,Object>();
					String ftime = format.format(new Date(Long.parseLong(entry2.getKey())));
					poMap.put("ftime", ftime);
					poMap.putAll(entry2.getValue());
					list.add(poMap);
				}
				String[] keys = entry.getKey().split(Constants.S_SEPERATOR);
				String key = keys[keys.length-2]+Constants.S_SEPERATOR+keys[keys.length-1];
				jsonMap.put(key, list);
			}
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=queryRecentlyChildHostRealTime")
	public ModelAndView queryRecentlyChildHostRealTime(String appName,String keyName,String viewMod,String property){
		UserCustomNaviViewPo po = new UserCustomNaviViewPo();
		po.setViewMod(viewMod);
		ViewAo.get().findViewUrl(po);
		CspKeyInfo cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
		List<CspKeyPropertyRelation> list = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation((Integer)cki.getKeyId());
		String properties = "";
		for(CspKeyPropertyRelation relation : list){
			properties+=relation.getPropertyName()+",";
		}
		properties+="ftime";
		ModelAndView view =new ModelAndView(po.getViewUrl());
		view.addObject("appName",appName);
		view.addObject("keyName",keyName);
		view.addObject("property",property);
		view.addObject("properties",properties);
		view.addObject("method", "queryRecentlyChildHostRealTimeAjax");
		return view;
	}
	@RequestMapping(params="method=queryRecentlyChildHostRealTimeAjax")
	public void queryRecentlyChildHostRealTimeAjax(HttpServletResponse response,String appName,String keyName,String property){
		try {
			List<String> ipList =CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
			Map<String, Map<String, DataEntry>> map = new HashMap<String,Map<String,DataEntry>>();
			for(String ip : ipList){
				Map<String,Map<String,DataEntry>> tempMap = QueryUtil.queryRecentlyChildHostRealTime(appName, keyName, ip);
				map.putAll(tempMap);
			}
			Map<String,Map<String,Object>> jsonMap = new HashMap<String,Map<String,Object>>();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			for(Map.Entry<String, Map<String,DataEntry>> entry : map.entrySet()){
				if(entry.getValue()==null)continue;
				String[] keys = entry.getKey().split(Constants.S_SEPERATOR);
				String key = keys[keys.length-2]+Constants.S_SEPERATOR+keys[keys.length-1];
				Map<String,Object> poMap =new HashMap<String,Object>();
				boolean flag = false;
				for(Map.Entry<String, DataEntry> entry2 : entry.getValue().entrySet()){
					if(entry2.getValue()==null)continue;
					poMap.put(entry2.getKey(), entry2.getValue().getValue());
					if(!flag){
						poMap.put("ftime", format.format(new Date(entry2.getValue().getTime())));
						flag=true;
					}
				}
				jsonMap.put(key, poMap);
			}
			writeJSONToResponseJSONObject(response, jsonMap);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=getKeyName")
	public void getKeyName(HttpServletResponse response,Integer appId,HttpServletRequest request){
		String term = request.getParameter("term");
		try {
			List<CspKeyInfo>  listKeys = com.taobao.monitor.common.ao.center.KeyAo.get().findCspKeyLikeName(term, appId,20);
			JSONArray jsonArray = new JSONArray();
			for(CspKeyInfo keyPo : listKeys){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("label", keyPo.getKeyName());
				jsonObject.put("value", keyPo.getKeyName());
				jsonArray.add(jsonObject);
			}
			String jsonStr = jsonArray.toString();
			writeJSONToResponse(response, jsonStr);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=getActionUrl")
	public void getActionUrl(HttpServletResponse response,String keyName){
		try{
			CspKeyInfo cki = new CspKeyInfo();
			cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
			JSONObject jsonObject = getActionUrlJsonObject(cki); 
			String jsonStr = jsonObject.toString();
			writeJSONToResponse(response, jsonStr);
		}catch(Exception e){
			logger.info(e);
		}
	}
	private JSONObject getActionUrlJsonObject(CspKeyInfo cki){
		JSONObject jsonObject = new JSONObject();

		if(cki != null) {
			switch(KeyScope.valueOf(cki.getKeyScope())){
			case NO:break;
			case ALL:
			case HOST:	
				jsonObject.put(Urls.querySingleHostRealTime,DataViewModTransferMap.getSingle().get(Urls.querySingleHostRealTime));
				jsonObject.put(Urls.queryRecentlySingleHostRealTime,DataViewModTransferMap.getSingle().get(Urls.queryRecentlySingleHostRealTime));
				if(KeyScope.valueOf(cki.getKeyScope())==KeyScope.HOST)
					break;
			case APP: 	
				jsonObject.put(Urls.querySingleRealTime,DataViewModTransferMap.getSingle().get(Urls.querySingleRealTime));
				
			default:	
				jsonObject.put(Urls.queryChildHostRealTime2,DataViewModTransferMap.getSingle().get(Urls.queryChildHostRealTime2));
				jsonObject.put(Urls.queryChildRealTime,DataViewModTransferMap.getSingle().get(Urls.queryChildRealTime));
				jsonObject.put(Urls.queryRecentlyChildRealTime,DataViewModTransferMap.getSingle().get(Urls.queryRecentlyChildRealTime));
			}
			if(!(KeyScope.valueOf(cki.getKeyScope())==KeyScope.NO)){
				jsonObject.put(Urls.queryHistory,DataViewModTransferMap.getSingle().get(Urls.queryHistory));
			}		
		} 
		jsonObject.put(Urls.queryCustomerBusinessLog, DataViewModTransferMap.getSingle().get(Urls.queryCustomerBusinessLog));			
		return jsonObject;
	}
	@RequestMapping(params="method=getViewMod")
	public void getViewMod(HttpServletResponse response,String actionUrl){
		try {
			JSONObject jsonObject = new JSONObject();
			if(actionUrl.equals(Urls.queryHistory)){
				jsonObject.put("historyLine", DataViewModTransferMap.getSingle().get("historyLine"));
			}
			if(actionUrl.equals(Urls.querySingleRealTimeLine)){
				jsonObject.put(Urls.poListLine, DataViewModTransferMap.getSingle().get(Urls.poListLine));
				jsonObject.put(Urls.poListTable, DataViewModTransferMap.getSingle().get(Urls.poListTable));
			}
			if(actionUrl.equals(Urls.queryRecentlySingleRealTime)||actionUrl.equals(Urls.querySingleRealTime)){
				jsonObject.put(Urls.poListTable, DataViewModTransferMap.getSingle().get(Urls.poListTable));
				jsonObject.put(Urls.poListTop10, DataViewModTransferMap.getSingle().get(Urls.poListTop10));
			}
			if(actionUrl.equals(Urls.queryRecentlySingleHostRealTime)||actionUrl.equals(Urls.queryRecentlyChildRealTime)||actionUrl.equals(Urls.queryRecentlyChildHostRealTime)){
				jsonObject.put(Urls.mapPoTable, DataViewModTransferMap.getSingle().get(Urls.mapPoTable));
				jsonObject.put(Urls.mapPoTop10, DataViewModTransferMap.getSingle().get(Urls.mapPoTop10));
			}
			if(actionUrl.equals(Urls.queryChildHostRealTime)||actionUrl.equals(Urls.queryChildRealTime)||actionUrl.equals(Urls.querySingleHostRealTime))
				jsonObject.put(Urls.mapPoListTable, DataViewModTransferMap.getSingle().get(Urls.mapPoListTable));
			
			if(actionUrl.equals(Urls.queryChildHostRealTime2)){
				jsonObject.put(Urls.mapPoListTable2, DataViewModTransferMap.getSingle().get(Urls.mapPoListTable2));
			}
			
			if(actionUrl.equals(Urls.queryCustomerBusinessLog)){
				jsonObject.put(Urls.poListTable, DataViewModTransferMap.getSingle().get(Urls.poListTable));
			}
			
			String jsonStr = jsonObject.toString();
			writeJSONToResponse(response, jsonStr);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	@RequestMapping(params="method=getProperty")
	public void getProperty(HttpServletResponse response,String keyName){
		try {
			CspKeyInfo cki = new CspKeyInfo();
			JSONObject jsonObject = new JSONObject();
			cki = com.taobao.monitor.common.ao.center.KeyAo.get().getKeyInfo(keyName);
			List<CspKeyPropertyRelation> properties = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyPropertyRelation(cki.getKeyId());
			for(CspKeyPropertyRelation ckpr : properties){
				jsonObject.put(ckpr.getPropertyName(), ckpr.getPropertyName());
			}
			String jsonStr = jsonObject.toString();
			writeJSONToResponse(response, jsonStr);
		} catch (IOException e) {
			logger.info(e);
		}
	}
	private List<String> childKeyList(String appName, String parentKey) {
		AppInfoPo app = AppInfoCache.getAppInfoByAppName(appName);

		List<CspKeyInfo> keyList = com.taobao.monitor.common.ao.center.KeyAo.get().findKeyChildByApp(app.getAppId(), parentKey);
		List<String> keys = new ArrayList<String>();
		for (CspKeyInfo keyInfo : keyList) {
			keys.add(keyInfo.getKeyName());
		}
		return keys;
	}
	public class IFrame{
		private String url;
		private String size;

		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
	}
}
