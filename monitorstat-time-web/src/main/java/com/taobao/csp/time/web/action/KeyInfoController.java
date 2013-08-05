package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.alarm.AlarmKeyContainer;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.PropNameDescUtil;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.po.CspKeyPropertyRelation;
import com.taobao.monitor.common.util.page.Pagination;




@Controller
@RequestMapping("/app/conf/key/show.do")
public class KeyInfoController extends BaseController {
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;

	@RequestMapping(params = "method=alarmKeyList")
	public ModelAndView alarmKeyList(HttpServletRequest request,
			HttpServletResponse response, int appId) throws IOException,
			JSONException {

		String keyNamePart = BLANK;
		int pageNo = 1;
		int pageSize = PAGE_SIZE;
		// 这样访问首页时，不用强制传pageNo、pageSize、keyNamePart
		String pageNoParam = request.getParameter("pageNo");
		String pageSizeParam = request.getParameter("pageSize");
		String keyNamePartParam = request.getParameter("keyNamePart");
		if (pageNoParam != null) {
			try {
				pageNo = Integer.parseInt(pageNoParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if (pageSizeParam != null) {
			if (pageSizeParam.trim().equals(""))
				pageSize = PAGE_SIZE;
			try {
				pageSize = Integer.parseInt(pageSizeParam);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				pageSize = PAGE_SIZE;
			}
		}

		if (keyNamePartParam != null) {
			keyNamePart = keyNamePartParam.trim();
		}

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		Pagination<CspKeyMode> page = KeyAo.get().findAlarmKeyListByAppIdPageable(
				appId, keyNamePart, pageNo, pageSize);

		ModelAndView view = new ModelAndView("time/conf/alarm_key_list");

		view.addObject("appInfo", appInfo);
		view.addObject("pagination", page);

		return view;
	}


	/**
	 *@author wb-lixing
	 *2012-4-12 下午05:01:41 
	 * @throws IOException 
	 */
	@RequestMapping(params = "method=updateAlarmKey")
	public void updateAlarmKey(CspKeyMode po, HttpServletResponse response,HttpServletRequest request,   int appId,String keyName) throws IOException {

		KeyAo.get().updateKeyMode(po);
		AlarmKeyContainer.updateMode(po.getId());
//		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		//成功后跳转到属性列表页面
		String url = request.getContextPath()+"/app/conf/key/show.do?method=keyPropsList&appId="+ appId +"&keyName="+URLEncoder.encode(keyName,"GBK");
		//如果从告警key列表页跳转过来，则修改后再跳转到告警列表页
		String from = request.getParameter("from");
		if("alarmKeyList".equals(from))
			url = request.getContextPath()+"/app/conf/key/show.do?method=alarmKeyList&appId="+ appId;
		logger.debug("---------from: "+ from);
		logger.debug("---------url: "+url);
		response.sendRedirect(url);


	}


	/**
	 *@author wb-lixing
	 *2012-4-12 下午04:02:16 
	 */
	@RequestMapping(params = "method=deleteAlarmKey")
	public void deleteAlarmKey(int id,   int appId,String keyName,HttpServletRequest request,  HttpServletResponse response) throws IOException{
		boolean res = KeyAo.get().deleteKeyMode(id);
		AlarmKeyContainer.removeMode(id);
		//成功后跳转到属性列表页面
		String url = request.getContextPath()+"/app/conf/key/show.do?method=keyPropsList&appId="+ appId +"&keyName="+URLEncoder.encode(keyName,"GBK");
		//如果从告警key列表页跳转过来，则修改后再跳转到告警列表页
		String from = request.getParameter("from");
		if("alarmKeyList".equals(from))
			url = request.getContextPath()+"/app/conf/key/show.do?method=alarmKeyList&appId="+ appId;
		response.sendRedirect(url);
	}



	/**
	 *@author wb-lixing
	 *2012-4-12 下午02:34:14 
	 *
	 *后面2个参数，是为成功后跳转服务的
	 * @throws IOException 
	 */
	@RequestMapping(params = "method=addAlarmKey")
	public void addAlarmKey(CspKeyMode po, HttpServletResponse response,HttpServletRequest request,  int appId,String keyName) throws IOException{
		KeyAo.get().addKeyMode(po);
		List<CspKeyMode> list = KeyAo.get().findKeyModes(po.getAppName(), po.getKeyName(), po.getPropertyName());
		if(list.size()!=0)AlarmKeyContainer.addMode(list.get(0).getId());
//		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		//成功后跳转到属性列表页面
		String url = request.getContextPath()+"/app/conf/key/show.do?method=keyPropsList&appId="+ appId +"&keyName="+URLEncoder.encode(keyName,"GBK");
		response.sendRedirect(url);
	}

	/**
	 *@author wb-lixing
	 *2012-4-12 上午10:38:43 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(params = "method=alarmKeyEdit")
	public ModelAndView alarmKeyEdit( int appId,String keyName, String propertyName, HttpServletRequest request) throws UnsupportedEncodingException{
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("/time/conf/alarm_key_edit");
		//根据keyName获取KeyInfoPo
		CspKeyInfo keyInfo = KeyAo.get().getKeyInfo(keyName);
		view.addObject("appInfo", appInfo);
		view.addObject("keyInfo",keyInfo);
		view.addObject("propertyName", propertyName);


		//告警记录id
		String idStr = request.getParameter("id");

		if(idStr!=null){
			int id = 0;
			id = Integer.parseInt(idStr);
			CspKeyMode keyMode = KeyAo.get().getKeyMode(id);
			view.addObject("keyMode",keyMode);
		}

		return view;
	}

	/**
	 * key属性列表
	 * @param keyName 主要用于导航显示
	 * 
	 * @author wb-lixing 2012-4-11 下午04:14:04
	 * @throws UnsupportedEncodingException 
	 * 
	 *调试demo appId=1 keyName like mbean`ds`JmsXA 有属性

	 */

	@RequestMapping(params = "method=keyPropsList")
	public ModelAndView keyPropsList(int appId, String keyName) throws UnsupportedEncodingException {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("/time/conf/key_props_list");

		List<CspKeyPropertyRelation> list =  KeyAo.get().findKeyPropertyRelation(keyName);
		List<CspKeyMode> keyModeList = KeyAo.get().findKeyModes(appInfo.getAppName(), keyName);

		view.addObject("appInfo", appInfo);
		view.addObject("keyName",keyName);



		List<Map<String,Object>> addAlarmList = new ArrayList<Map<String,Object>>();
		for(CspKeyPropertyRelation ckp : list){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("isAlarm", isAlarm(keyModeList,ckp.getPropertyName()) );
			map.put("keyModeId", getKeyModeId(keyModeList,ckp.getPropertyName()));
			map.put("po", ckp);
			map.put("pn", PropNameDescUtil.getDesc(ckp.getPropertyName()));
			addAlarmList.add(map);
		}
		view.addObject("addAlarmList",addAlarmList);

		return view;
	}

	/**
	 *@author wb-lixing
	 *2012-4-12 下午04:25:29
	 *@param keyModeList
	 *@param propertyName
	 *@return 
	 */
	private int getKeyModeId(List<CspKeyMode> keyModeList,
			String propertyName) {
		for(CspKeyMode mode : keyModeList){
			if(mode.getPropertyName().equals(propertyName))
				return mode.getId();
		}
		return -1;
	}



	private  boolean isAlarm(List<CspKeyMode> keyModeList, String propertyName){
		for(CspKeyMode mode : keyModeList){
			if(mode.getPropertyName().equals(propertyName))
				return true;
		}
		return false;
	}
	/**
	 *@author wb-lixing 2012-3-20 下午02:13:15
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(HttpServletRequest request,
			HttpServletResponse response, int appId)
					throws UnsupportedEncodingException {

		String keyNamePart = BLANK;
		int pageNo = 1;
		int pageSize = PAGE_SIZE;
		// 这样访问首页时，不用强制传pageNo、pageSize、keyNamePart
		String pageNoParam = request.getParameter("pageNo");
		String pageSizeParam = request.getParameter("pageSize");
		String keyNamePartParam = request.getParameter("keyNamePart");
		if (pageNoParam != null) {
			try {
				pageNo = Integer.parseInt(pageNoParam);
			} catch (NumberFormatException e) {
			}
		}
		if (pageSizeParam != null) {
			if (pageSizeParam.trim().equals(""))
				pageSize = PAGE_SIZE;
			try {
				pageSize = Integer.parseInt(pageSizeParam);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				pageSize = PAGE_SIZE;
			}
		}

		if (keyNamePartParam != null) {
			keyNamePart = keyNamePartParam.trim();
		}

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		Pagination<CspKeyInfo> page = KeyAo.get().findKeyListByAppIdPageable(
				appId, keyNamePart, pageNo, pageSize);

		ModelAndView view = new ModelAndView("time/conf/key_list");

		view.addObject("appInfo", appInfo);
		view.addObject("pagination", page);

		return view;
	}

	@RequestMapping(params = "method=showMoreKeyDetail")
	public ModelAndView showMoreKeyDetail(HttpServletRequest request,
			HttpServletResponse response, int appId, String keyName, String keyScope,
			String second)
					throws UnsupportedEncodingException {
		boolean isSecond=false;
		if(StringUtils.isNotBlank(second) && second.equals("true")){
			isSecond=true;
		}
		if(keyScope == null)
			keyScope = KeyScope.NO.toString();

		List<String> propertys = KeyAo.get().findKeyPropertyNames(keyName);
		String queryMainProperty = "";
		if(propertys.contains(PropConstants.E_TIMES)) {
			queryMainProperty = PropConstants.E_TIMES;
		} else if(propertys.size() > 0) {
			queryMainProperty = propertys.get(0);
		}
		
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		Map<String, Integer> orderMap = new HashMap<String, Integer>();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		SimpleDateFormat sdf = null;
		String[] timeArray=null;
		if(!isSecond){
			timeArray = new String[Constants.CACHE_TIME_INTERVAL];
			sdf = new SimpleDateFormat("HH:mm");
			
			for(int i=0; i<timeArray.length; i++) {
				timeArray[i] = sdf.format(cal.getTime());
				cal.add(Calendar.MINUTE, - 1);
				orderMap.put(timeArray[i], i);	//记录某个时间的序号
			}
		}else{
			sdf = new SimpleDateFormat("HH:mm:ss");
			timeArray = new String[180];
			
			for(int i=0; i<timeArray.length; i++) {
				timeArray[i] = sdf.format(cal.getTime());
				cal.add(Calendar.SECOND, - 1);
				orderMap.put(timeArray[i], i);	//记录某个时间的序号
			}
		}
		
		List<TimeDataInfo> appValueList = new ArrayList<TimeDataInfo>();
		List<SortEntry<TimeDataInfo>> ipValueList = new ArrayList<SortEntry<TimeDataInfo>>();
		if(keyScope.equals( KeyScope.APP.toString()) || keyScope.equals( KeyScope.ALL.toString())) {
			 appValueList = commonService.querySingleKeyData(appInfo.getAppName(), keyName, 
						  queryMainProperty,isSecond);
		}

		if(keyScope.equals( KeyScope.HOST.toString()) || keyScope.equals( KeyScope.ALL.toString())) {
			ipValueList = commonService.querykeyDataForHostBySort(appInfo.getAppName(), 
					keyName, queryMainProperty,isSecond);
		}
		ModelAndView view = new ModelAndView("time/conf/key_list_more");
		view.addObject("appInfo", appInfo);
		view.addObject("keyScope", keyScope);
		view.addObject("keyName", keyName);
		view.addObject("ipValueList", ipValueList);
		view.addObject("appValueList", appValueList);
		view.addObject("timeArray", timeArray);
		view.addObject("orderMap", orderMap);
		return view;
	}	
	
	@Deprecated
	@RequestMapping(params = "method=list")
	public void list(HttpServletResponse response, int appId,
			String keyNamePart, int pageNo, int pageSize) throws IOException,
			JSONException {
		if (keyNamePart == null) {
			keyNamePart = "";
		} else {
			keyNamePart = keyNamePart.trim();
		}

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		Pagination<CspKeyInfo> page = KeyAo.get().findKeyListByAppIdPageable(
				appId, keyNamePart, pageNo, pageSize);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (CspKeyInfo po : page.getList()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("po", po);
			list.add(map);
		}

		writeJSONToResponseJSONArray(response, page.getList());

	}

	@RequestMapping(params = "method=edit")
	public ModelAndView edit(int appId, HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView("time/conf/key_edit");
		Map model = new HashMap();
		model.put("appInfo", appInfo);
		view.addAllObjects(model);
		return view;
	}

	@RequestMapping(params = "method=add")
	public void add(CspKeyInfo po, HttpServletRequest request,
			HttpServletResponse response, int appId) throws IOException {
		CspKeyInfo keyInfo = KeyAo.get().addKeyInfo(po);
		boolean res = keyInfo != null;
		if (res) {
			// 添加key、app对应关系
			KeyAo.get().addAppKeyRelation(appId, keyInfo.getKeyId());
		}

		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", res ? 1 : 0);
		map.put("operate", "save");
		writeJSONToResponseJSONObject(response, map);

	}

	// !!是""，而不是null,因为要用于sql拼接
	private static final String BLANK = "";
	private static final int PAGE_SIZE = 30;
	private static final Logger logger = Logger
			.getLogger(ApacheInfoController.class);
}
