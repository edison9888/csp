package com.taobao.csp.config.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.common.po.DatabaseAppRelPo;
import com.taobao.monitor.common.po.HostPo;

@Controller
@RequestMapping( { "/dbconf/list.do", "/dbconf/delete.do", "/dbconf/view.do",
		"/dbconf/get.do", "/dbconf/no_rel_apps.do", "/dbconf/rel_apps.do",
		"/dbconf/rel_app_delete.do", "/dbconf/rel_app_add.do",
		"/dbconf/edit.do", "/dbconf/add_or_update.do" })
public class DBConfigController extends BaseController {
	@RequestMapping("edit.do")
	public ModelAndView edit(HttpServletRequest request) {
		Object id = request.getParameter("id");
		ModelAndView view = new ModelAndView("config/dbconf/edit");
		// 新增
		if (id == null)
			return view;

		DataBaseInfoPo po = DataBaseInfoAo.get().findDataBaseInfoById(
				Integer.parseInt((String) id));
		// 修改
		Map model = new HashMap();
		model.put("po", po);
		view.addAllObjects(model);

		return view;
	}

	@RequestMapping("add_or_update.do")
	public void addOrUpdate(DataBaseInfoPo po, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		boolean res = false;
		String update = request.getParameter("update");
		boolean updateBool = "true".equals(update) ? true : false;
		if (updateBool) {
			// System.out.println("updateBool: "+ updateBool);
			res = DataBaseInfoAo.get().updateDataBaseInfo(po);
		} else {
			// System.out.println("do save!");
			res = DataBaseInfoAo.get().addDataBaseInfoData(po);
		}
		// System.out.println(po.getDbName());

		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", res ? 1 : 0);
		map.put("operate", updateBool ? "update" : "save");
		JSONObject jsonObj = new JSONObject();

		writeJSONToResponseJSONObject(response, map);

	}

	@RequestMapping("/dbconf/rel_app_add.do")
	public void add(String[] appId, int dbId, HttpServletResponse response)
			throws IOException {
		// System.out.println("----------delete: "+id);
		System.out.println("appId: " + Arrays.toString(appId));
		List<DatabaseAppRelPo> selectedAppList = new ArrayList<DatabaseAppRelPo>();
		for (String id : appId) {

			DatabaseAppRelPo po = new DatabaseAppRelPo();
			po.setAppId(Integer.parseInt(id));
			po.setAppType("time");
			po.setDatabaseId(dbId);
			selectedAppList.add(po);
		}

		boolean b = AppInfoAo.get().addDatabaseRelList(selectedAppList);
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
	@RequestMapping( { "/dbconf/no_rel_apps.do" })
	public ModelAndView noRelApps(HttpServletResponse response)
			throws IOException {
		List<AppInfoPo> list = AppInfoAo.get().findAllAppWithoutDatabaseRel(
				"time");

		Map model = new HashMap();
		model.put("list", list);
		ModelAndView view = new ModelAndView(
				"config/dbconf/no_rel_apps_checkbox", model);
		return view;
		// JSONArray ja = JSONArray.fromObject(list);
		// String jsonStr = ja.toString();
		// writeJSONToResponse(response, jsonStr);

	}

	@RequestMapping("/dbconf/rel_app_delete.do")
	public void delete(int id, int dbId, HttpServletResponse response)
			throws IOException {
		// System.out.println("----------delete: "+id);

		DatabaseAppRelPo relPo = new DatabaseAppRelPo();
		relPo.setAppId(id);
		relPo.setAppType("time");
		relPo.setDatabaseId(dbId);
		AppInfoAo.get().deleteRel(relPo);

		writeToResponse(response, "");
	}

	/**
	 * 
	 *获取和当前 DB关联的app
	 */
	@RequestMapping( { "/dbconf/rel_apps.do" })
	public void relApps(HttpServletResponse response, int id)
			throws IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<AppInfoPo> appList = AppInfoAo.get()
				.findAllAppByDatabaseIdAndAppType(id, "time");
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

		DataBaseInfoAo.get().deleteDataBaseInfo(id);

		writeToResponse(response, "");
	}

	@RequestMapping("list.do")
	public void list(HttpServletResponse response) throws IOException,
			JSONException {
		// System.out.println("/dbconfig/"+"\t"+"list.do \t"+ new Date());
		List<DataBaseInfoPo> dblist = DataBaseInfoAo.get()
				.findAllDataBaseInfo();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (DataBaseInfoPo po : dblist) {
			Map<String, Object> map = new HashMap<String, Object>();
			long type1num = 0;
			if (po.getDbType() == 1)
				type1num = DataBaseInfoAo.get().findDataBaseRecordNum(
						po.getDbId(), getPreDay());
			map.put("type1num", type1num);
			map.put("po", po);
			list.add(map);
		}
		JSONArray ja = JSONArray.fromObject(list);
		String jsonStr = ja.toString();
		writeJSONToResponse(response, jsonStr);

	}

	@RequestMapping("view.do")
	public ModelAndView view(HttpServletResponse response,
			HttpServletRequest request) {
		Object id = request.getParameter("id");
		DataBaseInfoPo po = DataBaseInfoAo.get().findDataBaseInfoById(
				Integer.parseInt((String) id));
		// 修改
		Map model = new HashMap();
		model.put("po", po);
		ModelAndView view = new ModelAndView("config/dbconf/view", model);
		return view;
	}

	public DBConfigController() {
	//	log.debug("------------instance " + getClass());
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
	public void get(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Object id = request.getParameter("id");
		DataBaseInfoPo po = DataBaseInfoAo.get().findDataBaseInfoById(
				Integer.parseInt((String) id));

		JSONObject jsonObj = JSONObject.fromObject(po);

		String jsonStr = jsonObj.toString();
		writeJSONToResponse(response, jsonStr);
	}

}
