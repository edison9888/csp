package com.taobao.csp.time.web.action;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.other.artoo.Artoo;
import com.taobao.csp.other.artoo.ArtooInfo;
import com.taobao.csp.other.beidou.BeiDouAlarmRecordCache;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.cache.KeyCache;
import com.taobao.csp.time.cache.PropertyToViewMap;
import com.taobao.csp.time.cache.ShieldIpCache;
import com.taobao.csp.time.util.AmlineFlash;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

@Controller
@RequestMapping("/app/detail/alarm/show.do")
public class AppAlarmController extends BaseController{
	@RequestMapping(params = "method=showIndex")
	public ModelAndView showIndex(HttpServletRequest request,int appId,String keyName,String property,long time){
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		Date datetime = new Date(time);
		String ips="";
		String title="";
		boolean hasAppScope = false;
		boolean hasHostScope = false;
		title=appInfo.getAppName()+" "+keyName+" "+PropertyToViewMap.get().get(property);
		List<CspTimeKeyAlarmRecordPo> alarmPos = CspTimeKeyAlarmRecordAo.get().findRecentlyAlarmInfo(appInfo.getAppName(), keyName,property, 5,datetime);
		if(alarmPos.size()>3)alarmPos = alarmPos.subList(0, 3);
		ModelAndView view = new ModelAndView("/time/alarm/alarm_detail");

		for(CspTimeKeyAlarmRecordPo po : alarmPos){
			if(po.getKey_scope().equals("HOST")){
				po.setKey_scope(po.getIp());
				hasHostScope = true;
				ips+=po.getIp()+",";
			}else if(po.getKey_scope().equals("APP")){
				po.setKey_scope("È«Íø");
				hasAppScope = true;
			}
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			String ftime = format.format(po.getAlarm_time());
			po.setFtime(ftime);
		}
		List<BeiDouAlarmRecordPo> beidouList = BeiDouAlarmRecordCache.get().get(appInfo.getAppName());
		Integer keyId;
		try {
			keyId = KeyCache.getCache().getKeyInfo(keyName).getKeyId();
		} catch (Exception e) {
			keyId=-1;
		}
		view.addObject("keyId",keyId);
		filterSame(alarmPos);
		List<Artoo> artooList = ArtooInfo.get().getRecentlyArtoo(appInfo.getAppName());
		List<Artoo> appArtooList = new ArrayList<Artoo>();
		for(Artoo artoo : artooList){
			if(artoo.getAppName().equals(appInfo.getAppName())){
				appArtooList.add(artoo);
			}
		}
		view.addObject("artooList",appArtooList);
		view.addObject("beidouList",beidouList);
		view.addObject("appId",appInfo.getAppId());
		view.addObject("appName", appInfo.getAppName());
		view.addObject("property",property);
		view.addObject("appInfo", appInfo);
		view.addObject("alarmPos",alarmPos);
		view.addObject("keyName",keyName);
		view.addObject("title", title);
		view.addObject("ips", ips);
		view.addObject("hasAppScope", hasAppScope);
		view.addObject("hasHostScope",hasHostScope);
		return view;
	}
	private void filterSame(List<CspTimeKeyAlarmRecordPo> alarmPos){
		for(int i=0;i<alarmPos.size();i++){
			for(int j=i+1;j<alarmPos.size();j++){
				CspTimeKeyAlarmRecordPo po1 = alarmPos.get(i);
				CspTimeKeyAlarmRecordPo po2 = alarmPos.get(j);
				if(po1.getAlarm_time().equals(po2.getAlarm_time())&&po1.getApp_name().equals(po2.getApp_name())&&po1.getKey_name().equals(po2.getKey_name())&&po1.getKey_scope().equals(po2.getKey_scope())&&po1.getProperty_name().equals(po2.getProperty_name())){
					alarmPos.remove(i);
					i--;
				}
			}
		}
	}
	@RequestMapping(params = "method=chartData")
	public void chartData(HttpServletResponse response,String appName,String property,String ips,Integer keyId){
		String keyName = KeyCache.getCache().getKeyInfo(keyId).getKeyName();
		Calendar time = Calendar.getInstance();
		Date cur = time.getTime();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		time.add(Calendar.DAY_OF_MONTH, -7);
		Date lastweek = time.getTime();
		AmlineFlash am = new AmlineFlash();
		String[] ipList = ips.split(",");
		for(String ip : ipList){
			if(ip.equals(""))continue;
			Map<Date, String> mapHost = QueryHistoryUtil.querySingleHost(appName, keyName,ip,property,cur);
			Map<Date, String> mapHostLastWeek = QueryHistoryUtil.querySingleHost(appName, keyName,ip,property,lastweek);
			Map<String, Double> mapBaselineHost = BaseLineCache.get().getBaseLine(appName, keyName, property,ip);
			for(Map.Entry<String, Double> entry : mapBaselineHost.entrySet()){
				if(entry.getValue()==null)continue;
				SimpleDateFormat format = new SimpleDateFormat("HH:mm");
				try {
					long tmp = format.parse(entry.getKey()).getTime();
					am.addValue("baseline",tmp,entry.getValue());
				} catch (ParseException e) {
				}
			}
			for(Map.Entry<Date, String> entry : mapHost.entrySet()){
				if(entry.getValue()==null)continue;
				am.addValue(ip+"-"+format2.format(cur), entry.getKey().getTime(), DataUtil.transformDouble(entry.getValue()));
			}
			for(Map.Entry<Date, String> entry : mapHostLastWeek.entrySet()){
				if(entry.getValue()==null)continue;
				am.addValue(ip+"-"+format2.format(lastweek), entry.getKey().getTime(), DataUtil.transformDouble(entry.getValue()));
			}
		}
		try {
			writeJSONToResponse(response, am.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(params = "method=chartData2")
	public void charData2(HttpServletResponse response,String appName,String property,Integer keyId){
		String keyName = KeyCache.getCache().getKeyInfo(keyId).getKeyName();
		Calendar time = Calendar.getInstance();
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
		Date cur = time.getTime();
		time.add(Calendar.DAY_OF_MONTH, -7);
		Date lastweek = time.getTime();
		AmlineFlash am = new AmlineFlash();
		Map<Date, String> map = QueryHistoryUtil.querySingle(appName, keyName, property, cur);
		Map<Date, String> mapLastWeek = QueryHistoryUtil.querySingle(appName, keyName, property, lastweek);
		Map<String, Double> mapBaseline = BaseLineCache.get().getBaseLine(appName, keyName, property);
		for(Map.Entry<String, Double> entry : mapBaseline.entrySet()){
			if(entry.getValue()==null)continue;
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			try {
				long tmp = format.parse(entry.getKey()).getTime();
				am.addValue("baseline",tmp , entry.getValue());
			} catch (ParseException e) {
			}
		}
		for(Map.Entry<Date, String> entry : map.entrySet()){
			if(entry.getValue()==null)continue;
			am.addValue(format2.format(cur), entry.getKey().getTime(), DataUtil.transformDouble(entry.getValue()));
		}
		for(Map.Entry<Date, String> entry : mapLastWeek.entrySet()){
			if(entry.getValue()==null)continue;
			am.addValue(format2.format(lastweek), entry.getKey().getTime(), DataUtil.transformDouble(entry.getValue()));
		}
		try {
			writeJSONToResponse(response, am.getAmline());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping(params = "method=IpShield")
	public ModelAndView IpShield(HttpServletResponse response ,HttpServletRequest request,String ip,Integer appId) throws IOException{
		ModelAndView view = new ModelAndView("time/alarm/ip_shield");
		List<String> ips = ShieldIpCache.get().getShieldIps();
		AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
		Set<String> set = new HashSet<String>(CspCacheTBHostInfos.get().getIpsListByOpsName(appInfo.getAppName()));
		Iterator<String> iter = ips.iterator();
		while(iter.hasNext()){
			if(!set.contains(iter.next())){
				iter.remove();
			}
		}
		view.addObject("ips", ips);
		view.addObject("appInfo",appInfo);
		return view;
	}
	@RequestMapping(params = "method=addIpShield")
	public void addIpShield(HttpServletResponse response,HttpServletRequest request,Integer appId){
		try {
			String ipName = request.getParameter("ipName");
			ShieldIpCache.get().addShieldIp(ipName);
			response.sendRedirect(request.getContextPath()+"/app/detail/alarm/show.do?method=IpShield&appId="+appId);
		} catch (Exception e) {
		}
	}
	@RequestMapping(params = "method=deleteIpShield")
	public void deleteIpShield(HttpServletResponse response,HttpServletRequest request,Integer appId){
		try {
			String[] ips = request.getParameterValues("ipId");
			if(ips!=null){
				for(String ip : ips){
					ShieldIpCache.get().deleteShieldIp(ip);
				}
			}
			response.sendRedirect(request.getContextPath()+"/app/detail/alarm/show.do?method=IpShield&appId="+appId);
		} catch (Exception e) {
		}
	}
}
