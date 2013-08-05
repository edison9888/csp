
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.time.web.po.UrlRtErrorCount;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.CspAppRtCount;

/**
 * @author xiaodu
 *
 *用于一些统计信息与报表的控制类
 *
 * 下午2:58:43
 */
@Controller
@RequestMapping("/app/report.do")
public class AppReportController {

	/**
	 * 展示应用URL响应时间分布情况
	 *@author xiaodu
	 * @param time
	 * @return
	 *TODO
	 */
	@RequestMapping(params = "method=reportAppRTDistribution")
	public ModelAndView reportAppRTDistribution(String time, String compareTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar cal  = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date date = cal.getTime();
		try{
			date = sdf.parse(time);
		}catch (Exception e) {
		}
		time = sdf.format(date);

		
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -7);//默认对比七天之前
		Date dateCompareTime = cal.getTime();
		try{
			dateCompareTime = sdf.parse(compareTime);
		}catch (Exception e) {
		}
		compareTime = sdf.format(dateCompareTime);	

		List<CspAppRtCount> list = AppInfoAo.get().getCspAppRtCountList(time, "app");
		List<CspAppRtCount> listCompare = AppInfoAo.get().getCspAppRtCountList(compareTime, "app");

		Map<String, UrlRtErrorCount> map = new HashMap<String, UrlRtErrorCount>();
		Map<String, UrlRtErrorCount> mapCompare = new HashMap<String, UrlRtErrorCount>();


		for(CspAppRtCount count : list) {
			UrlRtErrorCount url = new UrlRtErrorCount();
			url.setAppErrorPv(count.getPvRtError());
			url.setAppName(count.getAppName());
			url.setAppPv(count.getPvRtCount());
			url.setAppRt1000pv(count.getPvRt1000());
			url.setAppRt100Pv(count.getPvRt100());
			url.setAppRt500pv(count.getPvRt500());
			map.put(count.getAppName(), url);
		}

		for(CspAppRtCount count : listCompare) {
			UrlRtErrorCount url = new UrlRtErrorCount();
			url.setAppErrorPv(count.getPvRtError());
			url.setAppName(count.getAppName());
			url.setAppPv(count.getPvRtCount());
			url.setAppRt1000pv(count.getPvRt1000());
			url.setAppRt100Pv(count.getPvRt100());
			url.setAppRt500pv(count.getPvRt500());
			mapCompare.put(count.getAppName(), url);
		}		

		ModelAndView view = new ModelAndView("/time/report/app_rt_distribution_report");
		view.addObject("listapp", new ArrayList<UrlRtErrorCount>(map.values()));
		view.addObject("mapCompare", mapCompare);
		view.addObject("time", time);
		view.addObject("compareTime", compareTime);
		return view;
	}

	/**
	 * 
	 *@author xiaodu
	 * @param time yyyy-MM-dd
	 *统计应用中响应时间超过1s的数量和具体的URL
	 * @throws ParseException 
	 */
	@RequestMapping(params = "method=reportUrlRTDistribution")
	public ModelAndView reportUrlRTDistribution(String appName,String time, String compareTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		try{
			date = sdf.parse(time);
		}catch (Exception e) {
		}
		time = sdf.format(date);
		List<CspAppRtCount> list = AppInfoAo.get().getCspAppRtCountList(time, "url", appName);
		CspAppRtCount appCount = AppInfoAo.get().getCspAppRtCount(time, "app", appName);
		Map<String, UrlRtErrorCount> map = new HashMap<String, UrlRtErrorCount>();
		for(CspAppRtCount urlCount : list) {
			UrlRtErrorCount count = new UrlRtErrorCount();
			count.setAppName(appName);
			count.setAppPv(appCount.getPvRtCount());
			count.setAppErrorPv(appCount.getPvRtError());
			count.setAppRt1000pv(appCount.getPvRt1000());
			count.setAppRt100Pv(appCount.getPvRt100());
			count.setAppRt500pv(appCount.getPvRt500());
			count.setAverageErrorUrlPv(urlCount.getPvRtError()/1440);
			count.setErrorUrlPv(urlCount.getPvRtError());
			count.setUrlRt1000pv(urlCount.getPvRt1000());
			count.setUrlRt100Pv(urlCount.getPvRt100());
			count.setUrlRt500pv(urlCount.getPvRt500());
			count.setUrlpv(urlCount.getPvRtCount());
			count.setMaxErrorUrlPv(0);	//前台没有使用。如果未来需要的话，在数据库中加一个字段
			count.setUrl(urlCount.getUrl());	
			map.put(urlCount.getUrl(), count);	//去重复
		}

		Date dateCompare = new Date();
		try{
			dateCompare = sdf.parse(compareTime);
		}catch (Exception e) {
		}
		compareTime = sdf.format(dateCompare);

		List<CspAppRtCount> listCompare = AppInfoAo.get().getCspAppRtCountList(compareTime, "url", appName);
		CspAppRtCount appCountCompare = AppInfoAo.get().getCspAppRtCount(compareTime, "app", appName);
		Map<String, UrlRtErrorCount> mapCompare = new HashMap<String, UrlRtErrorCount>();

		for(CspAppRtCount urlCount : listCompare) {
			UrlRtErrorCount count = new UrlRtErrorCount();
			count.setAppName(appName);
			count.setAppPv(appCountCompare.getPvRtCount());
			count.setAppErrorPv(appCountCompare.getPvRtError());
			count.setAppRt1000pv(appCountCompare.getPvRt1000());
			count.setAppRt100Pv(appCountCompare.getPvRt100());
			count.setAppRt500pv(appCountCompare.getPvRt500());
			count.setAverageErrorUrlPv(urlCount.getPvRtError()/1440);
			count.setErrorUrlPv(urlCount.getPvRtError());
			count.setUrlRt1000pv(urlCount.getPvRt1000());
			count.setUrlRt100Pv(urlCount.getPvRt100());
			count.setUrlRt500pv(urlCount.getPvRt500());
			count.setUrlpv(urlCount.getPvRtCount());
			count.setMaxErrorUrlPv(0);	//前台没有使用。如果未来需要的话，在数据库中加一个字段
			count.setUrl(urlCount.getUrl());
			mapCompare.put(urlCount.getUrl(), count);	//不做重复判断，用新值覆盖老值
		}
		ModelAndView view = new ModelAndView("/time/report/url_rt_distribution_report");
		view.addObject("urlrtList", new ArrayList<UrlRtErrorCount>(map.values()));
		view.addObject("mapCompare", mapCompare);
		view.addObject("time", time);
		view.addObject("compareTime", compareTime);
		view.addObject("appName", appName);
		return view;
	}

}
