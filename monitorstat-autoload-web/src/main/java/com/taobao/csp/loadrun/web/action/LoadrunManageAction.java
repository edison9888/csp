package com.taobao.csp.loadrun.web.action;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;

/***
 * 
 * @author youji.zj
 * 2011-10-26
 *
 */
@Controller
@RequestMapping("/loadrun/manage.do")
public class LoadrunManageAction {
	
	private static final Logger logger = Logger.getLogger(LoadrunManageAction.class);
	
	@Resource(name = "cspLoadRunBo")
	private CspLoadRunBo cspLoadRunBo;
	
	@RequestMapping(params = "method=deleteResult")
	public ModelAndView deleteResult(HttpServletRequest rquest,
			HttpServletResponse response, String resultId, String appId, String collectTime) {
		if (StringUtils.isNotBlank(resultId))
		{
			cspLoadRunBo.deleteLoadrunResultById(resultId);
		}
		
		return new ModelAndView(new RedirectView("/autoload/loadrun/show.do?method=show&appId=" + appId + "&collectTime=" + collectTime));
	}

}
