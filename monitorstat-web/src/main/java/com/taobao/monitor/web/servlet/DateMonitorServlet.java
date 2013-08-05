
package com.taobao.monitor.web.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.monitor.web.ao.MonitorBaseLineAo;
import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.cache.CacheTimeData;
import com.taobao.monitor.web.util.AmLineFlash;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-19 上午10:51:42
 */
public class DateMonitorServlet extends HttpServlet {

	

	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doGet(req, resp);
	}
	
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		
		try {
			String result = getLoadResult(req,resp);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(result);
				resp.flushBuffer();
			} catch (IOException e) {
			}	
		} catch (ParseException e) {
			
		}
		
		
		
		
	}
	
	
	private String getRestResult(HttpServletRequest request, HttpServletResponse resp)throws ParseException{
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String collectTime1 = request.getParameter("collectTime1");
		Date current = new Date();		
		if(collectTime1 != null){
			current = sdf.parse(collectTime1);
		}else{
			collectTime1 = sdf.format(current);
		}
		
		String startDate = sdf.format(current)+" 00:00:00";
		String endDate = sdf.format(current)+" 23:59:59";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.add(Calendar.DAY_OF_MONTH,-7);
		
		
		
		String appName = request.getParameter("appName");
		int keyId = Integer.parseInt(request.getParameter("keyId"));
		String keyName = request.getParameter("keyName");
		int appId = AppCache.get().getKey(appName).getAppId();
		
		
		Map<String, List<KeyValuePo>> resTFlashMap = new HashMap<String, List<KeyValuePo>>();
		
		//当前数据
		List<KeyValuePo> resCurrentTPv = MonitorTimeAo.get().findKeyValueByRangeDate(appId,176,parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
		
		List<KeyValuePo> rtCacheList = new ArrayList<KeyValuePo>();
		Map<String,KeyValuePo> rtmap = CacheTimeData.get().getAppKeyData(appName, "PV_REST_AVERAGEUSERTIMES");
		if(rtmap != null)
			rtCacheList.addAll(rtmap.values());
		//基线
		List<KeyValuePo> baseListrest = MonitorBaseLineAo.get().findKeyBaseValue(appId,176);	
		
		for(KeyValuePo po:baseListrest){
			try{
				double ll = (Double.parseDouble(po.getValueStr())/1000);
				po.setValueStr((int)ll+"");			
			}catch(Exception e){
				
			}
		}
		
		//处理
		List<KeyValuePo> lastRest= new ArrayList<KeyValuePo>();	
		
		for(int i=2;i<resCurrentTPv.size()&&i<7;i++){
			lastRest.add(resCurrentTPv.get(i));
		}
		
		//
		for(KeyValuePo po:resCurrentTPv){
			try{
				double ll = (Double.parseDouble(po.getValueStr())/1000);
				po.setValueStr((int)ll+"");			
			}catch(Exception e){
				
			}
		}
		List<KeyValuePo> rtCacheListtmp = new ArrayList<KeyValuePo>();
		
		for(KeyValuePo po:rtCacheList){
			try{
				KeyValuePo potmp = new KeyValuePo();
				double ll = (Double.parseDouble(po.getValueStr())/1000);
				potmp.setValueStr((int)ll+"");	
				potmp.setCollectTime(po.getCollectTime());
				potmp.setCollectTimeStr(po.getCollectTimeStr());
				rtCacheListtmp.add(potmp);
			}catch(Exception e){
				
			}
		}
		
		resTFlashMap.put("ResT["+sdf.format(current)+"]",resCurrentTPv);
		resTFlashMap.put("ResT["+sdf.format(cal.getTime())+"]",rtCacheListtmp);	
		resTFlashMap.put("基线",baseListrest);
	   	
		String Restreslut = AmLineFlash.createCharXml(resTFlashMap);
		
		return Restreslut;
	}
	
	
	private String getPvResult(HttpServletRequest request, HttpServletResponse resp)throws ParseException{
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String collectTime1 = request.getParameter("collectTime1");
		Date current = new Date();		
		if(collectTime1 != null){
			current = sdf.parse(collectTime1);
		}else{
			collectTime1 = sdf.format(current);
		}
		
		String startDate = sdf.format(current)+" 00:00:00";
		String endDate = sdf.format(current)+" 23:59:59";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.add(Calendar.DAY_OF_MONTH,-7);
		
		String appName = request.getParameter("appName");
		int keyId = Integer.parseInt(request.getParameter("keyId"));
		String keyName = request.getParameter("keyName");
		int appId = AppCache.get().getKey(appName).getAppId();
		
		
		Map<String, List<KeyValuePo>> pvFlashMap = new HashMap<String, List<KeyValuePo>>();	
		
		//当前数据
		List<KeyValuePo> listCurrentPv = MonitorTimeAo.get().findKeyValueByRangeDate(appId,175,parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
		
		List<KeyValuePo> pvCacheList = new ArrayList<KeyValuePo>();
		List<KeyValuePo> rtCacheList = new ArrayList<KeyValuePo>();
		Map<String,KeyValuePo> pvmap = CacheTimeData.get().getAppKeyData(appName, "PV_VISIT_COUNTTIMES");
		if(pvmap != null)
			pvCacheList.addAll(pvmap.values());
		List<KeyValuePo> baseListpv = MonitorBaseLineAo.get().findKeyBaseValue(appId,175);
		
		
		//处理
		List<KeyValuePo> lastPv = new ArrayList<KeyValuePo>();
		
		
		Collections.sort(listCurrentPv);		
		for(int i=2;i<listCurrentPv.size()&&i<7;i++){
			lastPv.add(listCurrentPv.get(i));
		}
		
		pvFlashMap.put("流量["+sdf.format(current)+"]",listCurrentPv);
		pvFlashMap.put("流量["+sdf.format(cal.getTime())+"]",pvCacheList);
		pvFlashMap.put("基线",baseListpv);
		
		List<KeyValuePo> rtCacheListtmp = new ArrayList<KeyValuePo>();
		
		for(KeyValuePo po:rtCacheList){
			try{
				KeyValuePo potmp = new KeyValuePo();
				double ll = (Double.parseDouble(po.getValueStr())/1000);
				potmp.setValueStr((int)ll+"");	
				potmp.setCollectTime(po.getCollectTime());
				potmp.setCollectTimeStr(po.getCollectTimeStr());
				rtCacheListtmp.add(potmp);
			}catch(Exception e){
				
			}
		}
		
		
		String pvreslut = AmLineFlash.createCharXml(pvFlashMap);
		
		return pvreslut;
	}
	
	private String getLoadResult(HttpServletRequest request, HttpServletResponse resp) throws ParseException{
		String appName = request.getParameter("appName");
		int keyId = Integer.parseInt(request.getParameter("keyId"));
		String keyName = request.getParameter("keyName");
		String collectTime1 = request.getParameter("collectTime1");
		String desc = request.getParameter("desc");
		int appId = AppCache.get().getKey(appName).getAppId();
		
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date current = new Date();		
		if(collectTime1 != null){
			current = sdf.parse(collectTime1);
		}else{
			collectTime1 = sdf.format(current);
		}
		
		String startDate = sdf.format(current)+" 00:00:00";
		String endDate = sdf.format(current)+" 23:59:59";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.add(Calendar.DAY_OF_MONTH,-7);
		
		//当前
		List<KeyValuePo> currentList = MonitorTimeAo.get().findKeyValueByRangeDate(appId,keyId,parseLogFormatDate.parse(startDate),parseLogFormatDate.parse(endDate));
		//以前
		List<KeyValuePo> beforeList = new ArrayList<KeyValuePo>();
		Map<String,KeyValuePo> beforeMap = CacheTimeData.get().getAppKeyData(appName,keyName);
		if(beforeMap!=null)
			beforeList.addAll(beforeMap.values());
		
		Map<String, List<KeyValuePo>> mapLoad = new HashMap<String, List<KeyValuePo>>();
		List<KeyValuePo> baseListload = MonitorBaseLineAo.get().findKeyBaseValue(appId,keyId);
		mapLoad.put(desc+"基线",baseListload);
		
		if(currentList!=null){
			mapLoad.put(desc+"["+sdf.format(current)+"]",currentList);
		}
		if(beforeList!=null){
			mapLoad.put(desc+"["+sdf.format(cal.getTime())+"]",beforeList);
		}
		String loadReslut = AmLineFlash.createCharXml(mapLoad);		
		return loadReslut;
	}

}
