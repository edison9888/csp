package com.taobao.csp.config.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.config.ao.MonitorExtraKeyAo;
import com.taobao.csp.config.ao.MonitorUserAo;
import com.taobao.csp.config.po.LoginUserPo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 用户管理的controller
 * @author denghaichuan.pt
 * @version 2012-1-6
 */
@Controller
@RequestMapping("/show/UserConf.do")
public class UserConfigAction {
	
private static final Logger logger =  Logger.getLogger(UserConfigAction.class);
	
	@Resource(name = "appInfoAo")
	private AppInfoAo appInfoAo;
	
	@RequestMapping(params = "method=gotoUserConf")
	public ModelAndView gotoUserConf(String searchUserName, String appNameSelect, String parentGroupSelect) {
		
		List<LoginUserPo> listUser = MonitorUserAo.get().findAllUser();
		List<LoginUserPo> filterList = listUser;

		if(!"".equals(searchUserName) && null != searchUserName) {
			
			filterList = MonitorUserAo.get().findMatcherUser(filterList, searchUserName);
		}

		if(!"".equals(appNameSelect) && !"".equals(parentGroupSelect) && null != appNameSelect && null != parentGroupSelect) {
			
			filterList = MonitorUserAo.get().findMatcherAppName(filterList, appNameSelect);
		}
		
		List<AppInfoPo> appList = appInfoAo.findAllEffectiveAppInfo();
		
		ModelAndView view = new ModelAndView("config/userconf/user_config");
		
		view.addObject("filterList", filterList);
		view.addObject("appList", appList);
		view.addObject("searchUserName", searchUserName);
		view.addObject("selectAppName", appNameSelect);
		view.addObject("selectGroupName", parentGroupSelect);
		
		return view;
	}
	
	@RequestMapping(params = "method=deleteUser")
	public ModelAndView deleteUser(int userId) {
		boolean issuccess = MonitorUserAo.get().deleteLoginUserPo(userId);
		return gotoUserConf("","","");
	}
	
	
	@RequestMapping(params = "method=gotoUserCheck")
	public ModelAndView gotoUserCheck(int userId) {
		
		List<AppInfoPo> appList = new ArrayList<AppInfoPo>();	//用来存放已经选择的app
		
		LoginUserPo loginUserPo = MonitorUserAo.get().getLoginUserPo(userId);
		
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		String phoneFeature = loginUserPo.getSendPhoneFeature();
		String[] week = new String[7];
		String[] num = new String[7];			
		if(phoneFeature!=null){
			String[] featrues = phoneFeature.split("\\$");
			for(String featrue:featrues){
				String[]  _tmp= featrue.split("\\#");
				if(_tmp.length==4){						
					int w = Integer.parseInt(_tmp[1]);
					String time1 = _tmp[2];
					String time2 = _tmp[3];
					week[w-1] = time1+"#"+time2;
					num[w-1] = _tmp[0];
				}
			}
		}
		
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		String wangwangFeature = loginUserPo.getSendWwFeature();
		String[] week1 = new String[7];
		String[] num1 = new String[7];				
		if(wangwangFeature!=null){
			String[] featrues = wangwangFeature.split("\\$");
			for(String featrue:featrues){
				String[]  _tmp= featrue.split("\\#");
				if(_tmp.length==4){						
					int w = Integer.parseInt(_tmp[1]);
					String time1 = _tmp[2];
					String time2 = _tmp[3];
					week1[w-1] = time1+"#"+time2;
					num1[w-1] = _tmp[0];
				}
			}
		}
		
		String appGroup = loginUserPo.getGroup();	//alarmKey:98,97,2,96,1,; 或者 alarmKey:;
		String initSelectedApp = "";						//用户已经选择的app
		if(appGroup != null) {
			
			String[] apps = appGroup.split(",");

			for(String a : apps) {
				if(!a.equals("")) {
					AppInfoPo po = appInfoAo.findAppInfoById(Integer.parseInt(a));
					initSelectedApp += a + ",";
					appList.add(po);
				}
			}
		}
		
		//拼接成  appId:keyId,keyId;
		String initAppKeyRel = "";
		for(AppInfoPo a : appList) {

			List<Integer> key = MonitorExtraKeyAo.get().findUserAndKeyRel(userId, a.getAppId());

			if(key.size() != 0) {
				initAppKeyRel += a.getAppId() + ":";
				for(int i =0; i < key.size();i++) {
					System.out.println(key.size());
					if(i != key.size()-1) {
						
						initAppKeyRel += key.get(i) + ",";
					} else {
						
						initAppKeyRel += key.get(i) + ";";
					}
				}
			}
		}
		
		ModelAndView view = new ModelAndView("config/userconf/user_config_check");
		
		view.addObject("week", week);
		view.addObject("num", num);
		view.addObject("week1", week1);
		view.addObject("num1", num1);
		view.addObject("loginUserPo", loginUserPo);
		view.addObject("appList", appList);
		view.addObject("initAppKeyRel", initAppKeyRel);
		view.addObject("initSelectedApp", initSelectedApp);
		return view;
	}
	
	@RequestMapping(params = "method=getSelectedKey")
	public void getSelectedKey(String selectedAppId, String relKey, HttpServletResponse response) {
		
		//以下是为了第二次选择的时候会把以前选择的勾选了
		String[] appAndKeys = relKey.split(";");			//9:34,54;7:23,53;
		List<String> keyIdList = new ArrayList<String>(); 
		for(String s : appAndKeys) {
			
			String[] a = s.split(":");	//9:34,54
			if(a[0].equals(selectedAppId)) {
				
				String[] b = a[1].split(",");	//34,54
				for(String c : b) {
					keyIdList.add(c);		//存放当前应用id
				}
			}
		}
		
		PrintWriter pw = null;
		try{
			response.setContentType("application/json");
			pw = response.getWriter();
			JSONArray resultJSON = JSONArray.fromObject(keyIdList);
			pw.write(resultJSON.toString());
			response.flushBuffer();
		}catch (Exception e) {
			logger.error("error", e);
			e.printStackTrace();
		}
		finally {
			pw.close();
		}
	}
	
	@RequestMapping(params = "method=gotoUserUpdate")
	public ModelAndView gotoUserUpdate(int userId) {
		
		List<AppInfoPo> appList = new ArrayList<AppInfoPo>();	//用来存放已经选择的app
		
		LoginUserPo loginUserPo = MonitorUserAo.get().getLoginUserPo(userId);
		
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		String phoneFeature = loginUserPo.getSendPhoneFeature();
		String[] week = new String[7];
		String[] num = new String[7];			
		if(phoneFeature!=null){
			String[] featrues = phoneFeature.split("\\$");
			for(String featrue:featrues){
				String[]  _tmp= featrue.split("\\#");
				if(_tmp.length==4){						
					int w = Integer.parseInt(_tmp[1]);
					String time1 = _tmp[2];
					String time2 = _tmp[3];
					week[w-1] = time1+"#"+time2;
					num[w-1] = _tmp[0];
				}
			}
		}
		
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		String wangwangFeature = loginUserPo.getSendWwFeature();
		String[] week1 = new String[7];
		String[] num1 = new String[7];				
		if(wangwangFeature!=null){
			String[] featrues = wangwangFeature.split("\\$");
			for(String featrue:featrues){
				String[]  _tmp= featrue.split("\\#");
				if(_tmp.length==4){						
					int w = Integer.parseInt(_tmp[1]);
					String time1 = _tmp[2];
					String time2 = _tmp[3];
					week1[w-1] = time1+"#"+time2;
					num1[w-1] = _tmp[0];
				}
			}
		}
		
		String appGroup = loginUserPo.getGroup();	//alarmKey:98,97,2,96,1,; 或者 alarmKey:;
		String initSelectedApp = "";						//用户已经选择的app
		if(appGroup != null) {
			
			String[] apps = appGroup.split(",");

			for(String a : apps) {
				if(!a.equals("")) {
					AppInfoPo po = appInfoAo.findAppInfoById(Integer.parseInt(a));
					initSelectedApp += a + ",";
					appList.add(po);
				}
			}
		}
		
		//拼接成  appId:keyId,keyId;
		String initAppKeyRel = "";
		for(AppInfoPo a : appList) {

			List<Integer> key = MonitorExtraKeyAo.get().findUserAndKeyRel(userId, a.getAppId());

			if(key.size() != 0) {
				initAppKeyRel += a.getAppId() + ":";
				for(int i =0; i < key.size();i++) {
					System.out.println(key.size());
					if(i != key.size()-1) {
						
						initAppKeyRel += key.get(i) + ",";
					} else {
						
						initAppKeyRel += key.get(i) + ";";
					}
				}
			}
		}
		
		ModelAndView view = new ModelAndView("config/userconf/user_config_update");
		
		view.addObject("week", week);
		view.addObject("num", num);
		view.addObject("week1", week1);
		view.addObject("num1", num1);
		view.addObject("loginUserPo", loginUserPo);
		view.addObject("appList", appList);
		view.addObject("initAppKeyRel", initAppKeyRel);
		view.addObject("initSelectedApp", initSelectedApp);
		return view;
	}
	
	@RequestMapping(params = "method=gotoRelAppAdd")
	public ModelAndView gotoRelAppAdd(String selectedApp) {
		
		if(selectedApp == null) {
			selectedApp = "";
		}
		
		String[] apps = selectedApp.split(",");
		List<String> appIdList = new ArrayList<String>();
		for(String id : apps) {
			appIdList.add(id);
		}
		
		List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
		
		ModelAndView view = new ModelAndView("config/userconf/user_alarmApp_rel");
		view.addObject("selectedApp", selectedApp);
		view.addObject("listApp", listApp);
		view.addObject("appIdList", appIdList);
		
		return view;
	}
	
	@RequestMapping(params = "method=addAppInPage")
	public void addAppInPage(String appGroups, HttpServletResponse response) {
		String[] alarmApps = appGroups.split(",");
		Map<String,String> responseMap = new HashMap<String, String>();

		for(String appId : alarmApps) {
			
			responseMap.put(appId, appInfoAo.findAppInfoById(Integer.parseInt(appId)).getAppName());
			//alarmAppList.add(AppCache.get().getKey(Integer.parseInt(appId)).getAppName());
		}
		JSONObject json = JSONObject.fromObject(responseMap);

		//JSONArray json = JSONArray.fromObject(alarmAppList);
		response.setContentType("application/json");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();
		} catch (IOException e) {
			
		}
	}
	
	@RequestMapping(params = "method=updateUser")
	public ModelAndView updateUser(HttpServletRequest request, int id) {
		
		String selectApp = request.getParameter("selectApp");					//拼接好告警的应用：   appId,appId,appId,
		String name = request.getParameter("name");
		String wangwang = request.getParameter("wangwang");
		String phone =request.getParameter("phone");
		String permissionDesc =request.getParameter("permissionDesc");
		String mail =request.getParameter("mail");
		
		String phoneDesc = "";

		
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		for(int i=1;i<=7;i++){
			String phone_week = request.getParameter("phone_week_"+i);
			String phone_num = request.getParameter("phone_num_"+i);
			if(phone_week!=null&&!"".equals(phone_week.trim())&&phone_num!=null&&!"".equals(phone_num.trim())){
				phoneDesc+=phone_num+"#"+i+"#"+phone_week+"$";
			}
		}
		String wangwangDesc = "";
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		for(int i=1;i<=7;i++){
			String ww_week = request.getParameter("wangwang_week_"+i);
			String ww_num = request.getParameter("wangwang_num_"+i);
			if(ww_week!=null&&!"".equals(ww_week.trim())&&ww_num!=null&&!"".equals(ww_num.trim())){
				wangwangDesc+=ww_num+"#"+i+"#"+ww_week+"$";
			}
		}
		
		
		LoginUserPo loginUserPo = new LoginUserPo();
		loginUserPo.setName(name);
		loginUserPo.setPhone(phone);
		loginUserPo.setWangwang(wangwang);
		loginUserPo.setSendPhoneFeature(phoneDesc);
		loginUserPo.setSendWwFeature(wangwangDesc);
		loginUserPo.setMail(mail);
		loginUserPo.setId(id);
		if(permissionDesc != null) {
			loginUserPo.setPermissionDesc(permissionDesc);
		}
		loginUserPo.setGroup(selectApp);
		if(loginUserPo.getPermissionDesc()==null){
			loginUserPo.setPermissionDesc("alarmKey:"+selectApp+";");
		}else if(!permissionDesc.contains("alarmKey:ALL;")){
			loginUserPo.setPermissionDesc(loginUserPo.getPermissionDesc().replaceAll("alarmKey:[\\w,]*;","alarmKey:"+selectApp+";"));
		}
		
		String report="";
		loginUserPo.setReportDesc(report);
		boolean b = MonitorUserAo.get().updateLoginUserPo(loginUserPo);

//		boolean bb = false;
//		
//		if(b) {
//			int userId = MonitorUserAo.get().getLoginUserPo(name).getId();
//			boolean del = MonitorExtraKeyAo.get().deleteRelWithUserId(userId);	//删除user以前的关联
//
//			
//
//			if(!relKey.equals("")) {
//				String[] appKeyStrs = relKey.split(";");
//				for(String  appKeyStr: appKeyStrs) {
//					
//					//appKeyStr的值为  appId:keyId,keyId;
//					String[] appAndKey = appKeyStr.split(":");
//					String app = appAndKey[0];			//这是appId
//					String[] keyIds = appAndKey[1].split(",");
//					List<String> keyList = new ArrayList<String>();
//					for(String key : keyIds) {
//						
//						keyList.add(key);
//					}
//					bb = MonitorExtraKeyAo.get().addUserAndKeyRel(userId, Integer.parseInt(app),keyList);
//				}
//			} else {
//				bb = true;		//这是当没有key关联的时候那么就是把以前的删除掉，然后标记为更新成功
//			}
//
//		}
		
		return gotoUserConf("","","");
	}
	
	@RequestMapping(params = "method=gotoAddUser")
	public ModelAndView gotoAddUser() {
		ModelAndView view = new ModelAndView("config/userconf/user_config_add");
		return view;
	}
	
	@RequestMapping(params = "method=addUser")
	public ModelAndView addUser(HttpServletRequest request) {
		String selectApp = request.getParameter("selectApp");					//拼接好告警的应用：   appId,appId,appId,
		String name = request.getParameter("name");
		String wangwang = request.getParameter("wangwang");
		String phone =request.getParameter("phone");
		String permissionDesc =request.getParameter("permissionDesc");
		String mail =request.getParameter("mail");
		
		String phoneDesc = "";

		
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		for(int i=1;i<=7;i++){
			String phone_week = request.getParameter("phone_week_"+i);
			String phone_num = request.getParameter("phone_num_"+i);
			if(phone_week!=null&&!"".equals(phone_week.trim())&&phone_num!=null&&!"".equals(phone_num.trim())){
				phoneDesc+=phone_num+"#"+i+"#"+phone_week+"$";
			}
		}
		String wangwangDesc = "";
		//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		for(int i=1;i<=7;i++){
			String ww_week = request.getParameter("wangwang_week_"+i);
			String ww_num = request.getParameter("wangwang_num_"+i);
			if(ww_week!=null&&!"".equals(ww_week.trim())&&ww_num!=null&&!"".equals(ww_num.trim())){
				wangwangDesc+=ww_num+"#"+i+"#"+ww_week+"$";
			}
		}
		
		
		LoginUserPo loginUserPo = new LoginUserPo();
		loginUserPo.setName(name);
		loginUserPo.setPhone(phone);
		loginUserPo.setWangwang(wangwang);
		loginUserPo.setSendPhoneFeature(phoneDesc);
		loginUserPo.setSendWwFeature(wangwangDesc);
		loginUserPo.setMail(mail);
		if(permissionDesc != null) {
			loginUserPo.setPermissionDesc(permissionDesc);
		}
		loginUserPo.setGroup(selectApp);
		if(loginUserPo.getPermissionDesc()==null){
			loginUserPo.setPermissionDesc("alarmKey:"+selectApp+";");
		}else if(!permissionDesc.contains("alarmKey:ALL;")){
			loginUserPo.setPermissionDesc(loginUserPo.getPermissionDesc().replaceAll("alarmKey:[\\w,]*;","alarmKey:"+selectApp+";"));
		}
		
		String report="";
		loginUserPo.setReportDesc(report);
		boolean b = MonitorUserAo.get().addLoginUserPo(loginUserPo);
		return gotoUserConf("","","");
	}
}
