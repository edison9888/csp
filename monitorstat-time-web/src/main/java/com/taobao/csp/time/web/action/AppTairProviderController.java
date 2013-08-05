package com.taobao.csp.time.web.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

@Controller
@RequestMapping("/app/detail/tairProvider/show.do")
public class AppTairProviderController extends BaseController{
	private static Logger logger = Logger.getLogger(AppTairProviderController.class);
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	@RequestMapping(params = "method=queryHost")
	public void appHostConsumer(HttpServletResponse response, Integer appId) {
		try {
			AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
			List<SortEntry<TimeDataInfo>> list = commonService.querykeyDataForHostBySort(appInfo.getAppName(),KeyConstants.TAIR_PROVIDER , PropConstants.GETCOUNT);
			writeJSONToResponseJSONArray(response, list);
		} catch (Exception e) {
			logger.info(e);
		}
	}
	@RequestMapping(params = "method=gotoHost")
	public ModelAndView gotoHost(int appId) {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		ModelAndView view = new ModelAndView(
				"/time/tair/tair_provider_host");
		view.addObject("appInfo", appInfo);
		return view;
	}
}
