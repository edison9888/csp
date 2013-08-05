
package com.taobao.csp.depend.web.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.auto.FetchAppDepRelation;
import com.taobao.csp.depend.auto.NetstatInfo;
import com.taobao.csp.depend.dao.CspCheckupDependDao;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;

@Controller
@RequestMapping("/checkupdepend.do")
public class CheckupDependAction extends BaseAction implements ConstantParameters {

	@Resource(name = "cspDependentDao")
	private CspDependentDao cspDependentDao;

	@Resource(name = "cspCheckupDependDao")
	private CspCheckupDependDao cspCheckupDependDao;

	@Resource(name = "appInfoDao")
	private AppInfoDao appInfoDao;

	@Resource(name = "fetchAppDepRelation")
	private FetchAppDepRelation fetchAppDepRelation;

	@RequestMapping(params = "method=showUnkowDepIp")
	public ModelAndView showUnkowDepIp(String opsName,String unknownType,String collectTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		if(collectTime != null){
			try {
				date = sdf.parse(collectTime);
			} catch (ParseException e) {
			}
		}

		List<NetstatInfo> list = cspDependentDao.findUnknownDepIp(opsName,unknownType, date);

		ModelAndView view = new ModelAndView("/depend/appinfo/unknowndepip");
		view.addObject("unkowdepips", list);
		return view;
	}

	/**
	 * 端口检测
	 */
	@RequestMapping(params = "method=checkupDepend")
	public void checkupDepend(final String opsName, final String time, HttpServletResponse response) {
		Thread thread = new Thread(){
			public void run(){
				fetchAppDepRelation.checkupDepend(opsName,time);
			}
		};
		thread.setName("Thread_CheckDepend");
		thread.setDaemon(false);
		thread.start();
		writeResponse("调用成功,启动线程id=" + thread.getId() + "\t ;name=" + thread.getName(), response);
	}

	@RequestMapping(params = "method=changeParam")
	public ModelAndView changeParam(String parentGroupName, String opsName, String selectDate, String dependAppType,
			String currentAccordion, String showType, String optType) {
		ModelAndView view = new ModelAndView("/depend/mainpage");
		view.addObject("parentGroupName", parentGroupName);
		view.addObject("opsName", opsName);
		view.addObject("selectDate", selectDate);
		view.addObject("dependAppType", dependAppType);
		view.addObject("currentAccordion ", currentAccordion);
		view.addObject("showType", showType);
		view.addObject("optType", optType );
		return view;
	}

	/**
	 * 返回所有有效应用的json数组，初始化页面的级联菜单的
	 * @param response
	 */
	@RequestMapping(params = "method=getAllAppInfo")
	public void getAllAppInfo(HttpServletResponse response) {
		List<AppInfoPo> effectiveApplist = new ArrayList<AppInfoPo>();
		List<AppInfoPo> list = appInfoDao.findAllAppInfo();
		for(AppInfoPo po:list){
			if(po.getAppStatus() == 0){
				effectiveApplist.add(po);
			}
		}
		JSONArray array = JSONArray.fromObject(effectiveApplist);
		this.writeResponse(array.toString(), response);
	}

}
