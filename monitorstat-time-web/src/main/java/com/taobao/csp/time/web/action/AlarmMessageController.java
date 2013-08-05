
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.other.beidou.BeiDouAlarmRecordCache;
import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.util.page.Pagination;

/**
 * @author xiaodu
 *
 * 上午11:17:35
 */
@Controller
@RequestMapping("/app/alarm/show.do")
public class AlarmMessageController extends BaseController{
	
	/**
	 * 
	 * 根据keyName、时间范围查询告警记录
	 *@author wb-lixing
	 *2012-4-25 下午03:38:29 
	 * @throws ParseException 
	 */
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(HttpServletRequest request,
			HttpServletResponse response, int appId) throws IOException, JSONException, ParseException {
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		String pageNoParam = request.getParameter("pageNo");
		ModelAndView view = new ModelAndView("time/alarm/alarm_message_list");
		view.addObject("appInfo", appInfo);
		//第一次进入查询页面
		if(pageNoParam==null){
			return view;
		}
			
		//form hidden传一定有值
		int pageNo = Integer.parseInt(pageNoParam);
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date from = sdf.parse(request.getParameter("from"));
		Date to = sdf.parse(request.getParameter("to"));
		String keyNamePart = request.getParameter("keyNamePart");
	
		Pagination<CspTimeKeyAlarmRecordPo> page = CspTimeKeyAlarmRecordAo.get().findAlarmMsgList(appInfo.getAppName(), keyNamePart, from, to, pageNo, pageSize);
		view.addObject("keyNamePart",keyNamePart);
		view.addObject("from", sdf.format(from));
		view.addObject("to", sdf.format(to));
		view.addObject("pagination", page);
		return view;
	}
	
	
	@RequestMapping(params = "method=recentlyAlarmList")
	public ModelAndView gotoHostDetail(String appName) {
		 List<CspTimeKeyAlarmRecordPo> list = CspTimeKeyAlarmRecordAo.get().findRecentlyAlarmInfo(appName, 5);
		ModelAndView view = new ModelAndView("/time/alarm/recently_alarm_list");
		view.addObject("alarmList", list);
		return view;
	}
	@RequestMapping(params = "method=recentlyDBAlarmList")
	public ModelAndView gotoDetail(String appName) {
		List<BeiDouAlarmRecordPo> list = BeiDouAlarmRecordCache.get().get(appName);
		ModelAndView view = new ModelAndView("/time/alarm/recently_db_alarm_list");
		view.addObject("alarmList", list);
		return view;
	}
	
	@RequestMapping(params = "method=recentlyKeyAlarmList")
	public ModelAndView gotoHostDetail(String appName, String keyName) {
	  List<CspTimeKeyAlarmRecordPo> list = CspTimeKeyAlarmRecordAo.get().findRecentlyAlarmInfo(appName,keyName, 5);
	  ModelAndView view = new ModelAndView("/time/alarm/recently_key_alarm_list");
	  view.addObject("alarmList", list);
	  return view;
	}
	
	private static final Logger logger = Logger
			.getLogger(AlarmMessageController.class);

}
