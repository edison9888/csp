package com.taobao.csp.depend.web.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.depend.ao.AlarmConfigAo;
import com.taobao.csp.depend.po.alarm.AlarmConfigPo;
import com.taobao.csp.depend.util.ConstantParameters;

@Controller
@RequestMapping("/alarmconfig.do")
public class AlarmConfigAction extends BaseAction {
	@RequestMapping(params = "method=saveAlarmConfig")
	public void saveAlarmConfig (HttpServletResponse response,
			AlarmConfigPo po) {
		
		if(po.getId() == null) {
			po.setId(-1);
		}
		if(po.getDaysPre() == null)
			po.setDaysPre(-1);
		
		if(po.getAlarmMode() == null) {
			po.setAlarmMode(ConstantParameters.HSF_PROVIDE_ALARM);
		}
		
		if(po.getLastSendTime() == null) {
			po.setLastSendTime(-1l);
		}
		po.setCollectDate(new Date());
		po.setGMTCreate(new Date());
		boolean result = AlarmConfigAo.get().insertOrUpdate(po);
		
		this.writeResponse(String.valueOf(result), response);
	}
	
	/**
	 * 查询结果
	 * @param response
	 * @param appName
	 * @param result
	 * @return
	 */
	@RequestMapping(params = "method=searchAlarmConfig")
	public ModelAndView  searchAlarmConfig (HttpServletResponse response, String appName, String result) {
		ModelAndView view = new ModelAndView("/depend/alarm/alarmconfig");
		List<AlarmConfigPo> list = AlarmConfigAo.get().getAlarmConfig(ConstantParameters.HSF_PROVIDE_ALARM);
		for(AlarmConfigPo po: list) {	//现在量比较小，遍历一下也比较快
			if(po.getAppName().equalsIgnoreCase(appName)) {
				view.addObject("alarmConfigPo", po);
				break;
			}
		}
		view.addObject("result", result);
		return view;
	}
}
