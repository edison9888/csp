package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.alarm.ao.AppLeftMenuAo;
import com.taobao.csp.time.cache.AppLeftMenuCache;
import com.taobao.csp.time.web.po.LeftMenuPo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;

@Controller
@RequestMapping("/app/config.do")
public class AppConfigController extends BaseController{
	private static Logger logger = Logger.getLogger(AppConfigController.class);
	@RequestMapping(params = "method=editAppNavi")
	public ModelAndView editAppNavi(Integer appId){
		ModelAndView view = new ModelAndView("/time/conf/app_navi_config");
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		List<LeftMenuPo> list = AppLeftMenuAo.get().find(appInfo.getAppName());
		LeftMenuPo po = new LeftMenuPo();
		if(list.size()>0)po = list.get(0);
		view.addObject("appInfo", appInfo);
		view.addObject("leftmenuPo",po);
		return view;
	}
	@RequestMapping(params = "method=updateAppNavi")
	public void updateAppNavi(HttpServletRequest request,Integer appId,HttpServletResponse response){
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(appId);
		String[] values = request.getParameterValues("box");
		LeftMenuPo leftmenu = new LeftMenuPo();
		leftmenu.setAppName(appInfo.getAppName());
		if(values!=null&&values.length>0){
		for(String value : values){
			try{
				Class clazz = leftmenu.getClass();
				Field f = clazz.getDeclaredField(value);
				f.setAccessible(true);
				f.setBoolean(leftmenu, true);
			}catch(Exception e){
				logger.info(e);
			}
		}
		}
		AppLeftMenuAo.get().update(leftmenu);
		AppLeftMenuCache.get().insert(appInfo.getAppName(), leftmenu);
		try {
			response.sendRedirect(request.getContextPath()+"/app/detail/show.do?method=showIndex&appId="+appInfo.getAppId());
		} catch (IOException e) {
		}
	} 
}
