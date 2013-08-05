package com.taobao.csp.config.web.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.config.ao.MonitorAlarmAo;
import com.taobao.csp.config.po.AlarmDataPo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.ao.center.ServerInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.po.ServerAppRelPo;
import com.taobao.monitor.common.po.ServerInfoPo;

@Controller
@RequestMapping( { "/keyconf/list.do", "/keyconf/delete.do",
		"/keyconf/view.do", "/keyconf/get.do", "/keyconf/no_rel_apps.do",
		"/keyconf/rel_apps.do", "/keyconf/rel_app_delete.do",
		"/keyconf/rel_app_add.do", "/keyconf/app_list.do", "/keyconf/edit.do",
		"/keyconf/add_or_update.do" })
public class KeyConfigController extends BaseController {

	public static final Logger log = LoggerFactory.getLogger(KeyConfigController.class);
	@RequestMapping("edit.do")
	public ModelAndView edit(HttpServletRequest request) {
		String id = request.getParameter("id");
		//System.out.println("hi!");

		ModelAndView view = new ModelAndView("config/keyconf/edit");
		// 新增
		if (id == null)
			return view;

		KeyPo po = com.taobao.csp.config.ao.KeyAo.get().getKeyPo(Integer.parseInt(id));
		// 修改
		Map model = new HashMap();
		model.put("po", po);
		view.addAllObjects(model);

		return view;
	}

	@RequestMapping("add_or_update.do")
	public void addOrUpdate(KeyPo po, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean res = false;
		String update = request.getParameter("update");
		boolean updateBool = "true".equals(update) ? true : false;
		if (updateBool) {
			// System.out.println("updateBool: "+ updateBool);
			
			res =  KeyAo.get().updateKeyInfo(po);
			log.debug( po.getKeyId() + "\t" + po.getAliasName());
		} else {
			// System.out.println("do save!");
			//res = ServerInfoAo.get().addServerInfoData(po);
		}
		// System.out.println(po.getDbName());

		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", res ? 1 : 0);
		map.put("operate", updateBool ? "update" : "save");
		JSONObject jsonObj = new JSONObject();

		writeJSONToResponseJSONObject(response, map);

	}

	@RequestMapping("rel_app_add.do")
	public void add(String[] appId, int serverId, HttpServletResponse response)
			throws IOException {
		// System.out.println("----------delete: "+id);
		//System.out.println("appId: " + Arrays.toString(appId));

		List<ServerAppRelPo> selectedAppList = new ArrayList<ServerAppRelPo>();
		for (String id : appId) {

			ServerAppRelPo po = new ServerAppRelPo();
			po.setAppId(Integer.parseInt(id));
			po.setAppType("time");
			po.setServerId(serverId);

			selectedAppList.add(po);
		}

		boolean b = AppInfoAo.get().addServerRelList(selectedAppList);

		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", b ? 1 : 0);
		JSONObject jo = JSONObject.fromObject(map);

		String jsonStr = jo.toString();
		writeJSONToResponse(response, jsonStr);

		writeToResponse(response, "");
	}

	/**
	 * 
	 *获取所有未和 DB关联的app<br/>
	 * 
	 */
	@RequestMapping( { "no_rel_apps.do" })
	public ModelAndView noRelApps(HttpServletResponse response)
			throws IOException {
		List<AppInfoPo> list = AppInfoAo.get().findAllAppWithoutServerRel(
				"time");

		Map model = new HashMap();
		model.put("list", list);
		ModelAndView view = new ModelAndView(
				"config/keyconf/no_rel_apps_checkbox", model);
		return view;
		// JSONArray ja = JSONArray.fromObject(list);
		// String jsonStr = ja.toString();
		// writeJSONToResponse(response, jsonStr);

	}

	@RequestMapping("rel_app_delete.do")
	public void delete(int id, int serverId, HttpServletResponse response)
			throws IOException {
		// System.out.println("----------delete: "+id);
		ServerAppRelPo relPo = new ServerAppRelPo();
		relPo.setAppId(id);
		relPo.setAppType("time");
		relPo.setServerId(serverId);
		boolean res = AppInfoAo.get().deleteRel(relPo);
		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", res ? 1 : 0);

		JSONObject jo = JSONObject.fromObject(map);
		String jsonStr = jo.toString();
		writeJSONToResponse(response, jsonStr);
		writeToResponse(response, "");
	}

	/**
	 * 
	 *获取和当前 DB关联的app
	 */
	@RequestMapping( { "rel_apps.do" })
	public void relApps(HttpServletResponse response, int id)
			throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<AppInfoPo> appList = AppInfoAo.get()
				.findAllAppByServerIdAndAppType(id, "time");
		int number = 0;
		for (AppInfoPo po : appList) {

			number++;
			int limitData = 0; // 临时表
			int saveData = 0; // 持久表
			for (HostPo hostPo : AppInfoAo.get().findAppWithHostListByAppId(
					po.getAppId()).getHostList()) {

				if (hostPo.getSavedata().charAt(0) == '1') {

					limitData++;
				}
				if (hostPo.getSavedata().charAt(1) == '1') {

					saveData++;
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("number", number);
			map.put("limitData", limitData);
			map.put("saveData", saveData);
			map.put("po", po);
			list.add(map);
		}

		JSONArray ja = JSONArray.fromObject(list);
		String jsonStr = ja.toString();
		writeJSONToResponse(response, jsonStr);

	}

	@RequestMapping("delete.do")
	public void delete(int id, HttpServletResponse response) throws IOException {
		// System.out.println("----------delete: "+id);

		ServerInfoAo.get().deleteHostData(id);

		writeToResponse(response, "");
	}

	@RequestMapping("list.do")
	public void list(HttpServletResponse response, String keyName, String appId)
			throws IOException, JSONException {

		// 获取key列表
		List<KeyPo> keyList = KeyAo.get().findKeyLikeName(keyName,
				appId == null ? null : Integer.valueOf(appId));

		// 获取告警key列表
		List<AlarmDataPo> allalarmKeyList = MonitorAlarmAo.get()
				.findAllAlarmKeyByAimAndLikeName(Integer.parseInt(appId),
						keyName);

		Map<Integer, AlarmDataPo> alarmKeyMap = new HashMap<Integer, AlarmDataPo>();

		// 将告警key列表，改装成Map类型
		for (AlarmDataPo po : allalarmKeyList) {
			alarmKeyMap.put(Integer.parseInt(po.getKeyId()), po);
		}

		// 判断是否告警
		List<Map<String, Object>> handledKeyList = new ArrayList<Map<String, Object>>();
		for (KeyPo kp : keyList) {
			int keyId = kp.getKeyId();
			boolean isAlarm = alarmKeyMap.containsKey(keyId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isAlarm", isAlarm);
			map.put("po", kp);
			handledKeyList.add(map);
		}

		AppInfoPo currAppPo = AppInfoAo.get().findAppWithHostListByAppId(
				Integer.parseInt(appId));

		Map model = new HashMap();
		model.put("list", handledKeyList);
		model.put("currAppPo", currAppPo);

		// ModelAndView view = new ModelAndView("config/keyconf/list", model);
		//
		writeJSONToResponseJSONObject(response, model);
	}

	@RequestMapping("view.do")
	public ModelAndView view(HttpServletResponse response,
			HttpServletRequest request, int id) {
		ServerInfoPo po = ServerInfoAo.get().findAllServerInfoById(id);
		// 修改
		Map model = new HashMap();
		model.put("po", po);
		ModelAndView view = new ModelAndView("config/keyconf/view", model);
		return view;
	}

	public KeyConfigController() {
		//System.out.println("------------instance " + getClass());
	}

	public int getPreDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return Integer.parseInt(sdf.format(cal.getTime()));
	}

	/**
	 *获取某条记录，json格式
	 * 
	 * @throws IOException
	 */
	@RequestMapping("get.do")
	public void get(HttpServletRequest request, HttpServletResponse response,
			int id) throws IOException {
		ServerInfoPo po = ServerInfoAo.get().findAllServerInfoById(id);

		JSONObject jsonObj = JSONObject.fromObject(po);

		String jsonStr = jsonObj.toString();
		writeJSONToResponse(response, jsonStr);
	}

	@RequestMapping("app_list.do")
	public void appList(HttpServletResponse response) throws IOException {
		// 获取App列表
		List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
		writeJSONToResponseJSONArray(response, appList);
	}

}
