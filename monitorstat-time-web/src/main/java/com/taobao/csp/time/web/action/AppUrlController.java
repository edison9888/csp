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
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.AppUrlRelation;
import com.taobao.monitor.common.po.DataBaseInfoPo;

@Controller
@RequestMapping("/app/conf/url/show.do")
public class AppUrlController extends BaseController {

	/**
	 *@author wb-lixing 2012-3-20 下午02:13:15
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(int appId, HttpServletRequest request)
			throws UnsupportedEncodingException {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		ModelAndView view = new ModelAndView("time/conf/app_url_list");

		view.addObject("appInfo", appInfo);

		return view;
	}

	@RequestMapping(params = "method=list")
	public void list(HttpServletResponse response, int appId)
			throws IOException, JSONException {

		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		List<AppUrlRelation> aulist = AppInfoAo.get().findAllAppUrlRelation(
				appInfo.getAppName());
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (AppUrlRelation po : aulist) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("po", po);
			list.add(map);
		}
		JSONArray ja = JSONArray.fromObject(list);
		String jsonStr = ja.toString();
		writeJSONToResponse(response, jsonStr);

	}

	@RequestMapping(params = "method=edit")
	public ModelAndView edit(int appId, HttpServletRequest request) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);

		Object id = request.getParameter("id");
		ModelAndView view = new ModelAndView("time/conf/app_url_edit");
		// 修改
		Map model = new HashMap();
		model.put("appInfo", appInfo);
		AppUrlRelation po = null;
		
		// 新增
		if (id == null){
			po =  new AppUrlRelation();
			po.setAppName(appInfo.getAppName());
			model.put("po", po);
			view.addAllObjects(model);
			return view;
		}
		po = AppInfoAo.get().get().getAppUrlRelationById(
				Integer.parseInt((String) id));

		model.put("po", po);
		view.addAllObjects(model);
		return view;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class,
				new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(params = "method=add_or_update")
	public void addOrUpdate(AppUrlRelation po, HttpServletRequest request,
			HttpServletResponse response)
			throws IOException {
		
		po.setModifyDate(new Date());
		
		boolean res = false;
		String update = request.getParameter("update");
		boolean updateBool = "true".equals(update) ? true : false;
		if (updateBool) {
			res = AppInfoAo.get().updateAppUrlRelation(po);
		} else {
			res = AppInfoAo.get().addAppUrlRelation(po);
		}

		Map map = new HashMap();
		// 1成功 0失败
		map.put("result", res ? 1 : 0);
		map.put("operate", updateBool ? "update" : "save");
		JSONObject jsonObj = new JSONObject();

		writeJSONToResponseJSONObject(response, map);

	}

	@RequestMapping(params = "method=delete")
	public void delete(int id, HttpServletResponse response) throws IOException {
		AppInfoAo.get().deleteAppUrlRelation(id);
		writeToResponse(response, "");
	}

	



	private static final Logger log = Logger
			.getLogger(ApacheInfoController.class);
}
