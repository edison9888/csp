package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.alarm.service.UserService;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspUserInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspUserInfoPo;

/**
 * 告警配置页面，管理应用的告警接收人
 * @author root
 *
 */
@Controller
@RequestMapping("/app/conf/alarmReceiverManage.do")
public class AlarmReceiverManageController  extends BaseController{
	private static Logger logger = Logger.getLogger(AlarmReceiverManageController.class);
	/**
	 * 列出所有应用
	 * @author 李星
	 * 2013-4-16 下午2:51:06
	 * @param request
	 * @param response
	 * @param appId
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			JSONException {
		List<AppInfoPo> list = AppInfoAo.get().findAllAppInfo();
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("applist", list);
		ModelAndView view = new ModelAndView("time/conf/alarm_receiver_manage_index", model);
		return view;
	}
	
	@RequestMapping(params = "method=receiverList")
	public ModelAndView receiverList(HttpServletRequest request,
			HttpServletResponse response,int appId) throws IOException,
			JSONException {
	//	String appName = AppInfoAo.get().getAppInfoId2NameMap().get(appId);
		List<CspUserInfoPo> list = CspUserInfoAo.get().findAllCspUserInfo();
		//遍历列表，以应用为key，将告警人列出来
		List<CspUserInfoPo> receivers = new ArrayList<CspUserInfoPo>();
	
		for (CspUserInfoPo po : list) {
			String appIds = po.getAccept_apps();
			if (appIds != null) {
				appIds = appIds.trim();
				if (!appIds.equals("")) {
					String[] appIdStrArr = appIds.split(",");
					if (appIdStrArr.length > 0) {
						for (String appIdStr : appIdStrArr) {
							appIdStr = appIdStr.trim();
							if(appIdStr.equals(""))
								continue;
							//fix bug库中有"null"串
							appIdStr = appIdStr.replace("null", "");
							Integer loopAppId = Integer.parseInt(appIdStr);
							if (loopAppId.equals(appId)){
								receivers.add(po);
								break;
							}
						}
					}
				}
			}
		}
		//
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("userList", receivers);
	//	model.put("appName", appName);
		ModelAndView view = new ModelAndView("time/conf/alarm_receiver_manage_user_list", model);
		return view;

	}
	
	
	@RequestMapping(params = "method=addReceiverIndex")
	public ModelAndView addReceiverIndex(HttpServletRequest request,
			HttpServletResponse response, int appId, String email) {
		// 遍历用户表所有用户找到appidList字段，并添加，添加时appid要判重复;添加时要对email判断，判断是否存在
		ModelAndView view = new ModelAndView("time/conf/alarm_receiver_manage_add");
		return view;

	}
	
	
	@RequestMapping(params = "method=addReceiver")
	public ModelAndView addReceiver(HttpServletRequest request,
			HttpServletResponse response, int appId, String appName, String email) {
		// 遍历用户表所有用户找到appidList字段，并添加，添加时appid要判重复;添加时要对email判断，判断是否存在
		ModelAndView view = new ModelAndView("time/conf/alarm_receiver_manage_add");
		CspUserInfoPo cspUserInfoPo = CspUserInfoAo.get()
				.findCspUserInfoByMail(email);
		if (cspUserInfoPo == null || cspUserInfoPo.getMail() == null) {
			//返回modelandview，提示用户不存在，先让用户注册
			view.addObject("message", "添加失败，用户不存在，请先通知用户注册!");
			return view;
		}
		Set<Integer> appSet = new HashSet<Integer>();
		if (cspUserInfoPo.getAccept_apps() != null
				&& !"".equals(cspUserInfoPo.getAccept_apps())) {
			String appIds = cspUserInfoPo.getAccept_apps();
			for (String id : appIds.split(",")) {
				try {
					int idInt = Integer.parseInt(id);
					if(appId == idInt){
						//返回modelandview，提示已添加
						view.addObject("message", "添加失败，此用户已加入告警!");
						return view;
					}
					appSet.add(idInt);
				} catch (Exception e) {
					logger.info(e);
				}
			}
		}else{
			cspUserInfoPo.setAccept_apps("");
		}
		boolean appendComma = !cspUserInfoPo.getAccept_apps().trim().equals("") && !cspUserInfoPo.getAccept_apps().trim().endsWith(",");
		
		cspUserInfoPo.setAccept_apps(cspUserInfoPo.getAccept_apps()+(appendComma?",":"")+appId+",");
		CspUserInfoAo.get().updateCspUserInfo(cspUserInfoPo);
		view.addObject("message","添加成功");
		return view;

	}
	
	
	@RequestMapping(params = "method=deleteReceiver")
	public void deleteReceiver(HttpServletRequest request,
			HttpServletResponse response,int appId, String email,String appName) throws IOException  {
		//遍历用户表所有用户找到appidList字段，删除
		CspUserInfoPo cspUserInfoPo = CspUserInfoAo.get()
				.findCspUserInfoByMail(email);
		
		String apps = cspUserInfoPo.getAccept_apps();
		apps = apps.replace(appId+ ",", "");
		cspUserInfoPo.setAccept_apps(apps);
		CspUserInfoAo.get().updateCspUserInfo(cspUserInfoPo);
		String redirectUrl = request.getContextPath()+"/app/conf/alarmReceiverManage.do?method=receiverList&appId="+appId+"&appName="+appName;
		response.sendRedirect(redirectUrl);
	}
	/**
	 * {label:'花名',value:'邮箱地址'}
	 * @throws IOException 
	 * 
	 */
	@RequestMapping(params = "method=userAutoComplate")
	public void userAutoComplate(HttpServletRequest request,
			HttpServletResponse response) throws IOException  {
		List<CspUserInfoPo> list = CspUserInfoAo.get().findAllCspUserInfo();
		JSONArray arr = new JSONArray();
		for(CspUserInfoPo user: list){
			if(user.getMail()==null)
				continue;
			JSONObject obj =new JSONObject();
			obj.put("label", user.getMail()+"\t" + user.getWangwang());
			obj.put("value", user.getMail());
			arr.add(obj);
		}
		response.setCharacterEncoding("utf-8");
		writeToResponse(response, arr.toString());
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(",".split(",").length);
	}
}


