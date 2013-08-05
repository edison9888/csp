package com.taobao.monitor.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.ao.center.DayConfAo;
import com.taobao.monitor.common.ao.center.ServerInfoAo;
import com.taobao.monitor.common.ao.center.TimeConfAo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.common.po.DatabaseAppRelPo;
import com.taobao.monitor.common.po.DayConfPo;
import com.taobao.monitor.common.po.ServerAppRelPo;
import com.taobao.monitor.common.po.ServerInfoPo;
import com.taobao.monitor.common.po.TimeConfPo;

/**
 * Servlet implementation class TestServet
 */
public class RelAppServet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RelAppServet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("utf8");
		String action = request.getParameter("action");
		
		if("addDatabase".equals(action)){//取得全部信息
			String appName = request.getParameter("appName");
			String appId = request.getParameter("appId");
			String appType = request.getParameter("appType");
			String databaseId = request.getParameter("dbId");
			
			DatabaseAppRelPo relPo = new DatabaseAppRelPo();
			relPo.setAppId(Integer.parseInt(appId));
			relPo.setAppType(appType);
			relPo.setDatabaseId(Integer.parseInt(databaseId));
			
			boolean addSuccess = false;
			if(!AppInfoAo.get().isExistDatabaseAppRel(relPo)) {
				addSuccess = AppInfoAo.get().addRel(relPo);
				
			}
			
			
			Map<String,String> responseMap = new HashMap<String, String>();
			if(true) {
				
				DataBaseInfoPo po = DataBaseInfoAo.get().findDataBaseInfoById(Integer.parseInt(databaseId));
				//List<AppInfoPo> appInfoPoList = AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId));
			
				responseMap.put("appId", appId);
				responseMap.put("appName", appName);
				responseMap.put("appType", appType);
				responseMap.put("databaseName", po.getDbName());
				responseMap.put("databaseId", databaseId);
				responseMap.put("databaseUrl", po.getDbUrl());
				responseMap.put("isSuccess", addSuccess ? "true" : "false");
				//responseMap.put("relDbSize", appInfo4DbPoList.size() + "");
				
				JSONObject json = JSONObject.fromObject(responseMap);
				
				//JSONArray json3 = JSONArray.fromObject(appInfoPoList);
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
				//	System.out.println(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}

			} 		
			return ;
		}
		if("modifyDatabase".equals(action)){//取得全部信息
			String appName = request.getParameter("appName");
			String appId = request.getParameter("appId");
			String appType = request.getParameter("appType");
			String databaseId = request.getParameter("dbId");
			//设置relPo的值
			DatabaseAppRelPo relPo = new DatabaseAppRelPo();
			relPo.setAppId(Integer.parseInt(appId));
			relPo.setAppType(appType);
			relPo.setDatabaseId(Integer.parseInt(databaseId));
			
			boolean addSuccess = false;	//用来标志dao操作是否成功
			if((AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId)).size() != 0)) {
				
				addSuccess = AppInfoAo.get().updateRel(relPo);				
			}
					
			Map<String,String> responseMap = new HashMap<String, String>();
			if(true) {
				
				DataBaseInfoPo po = DataBaseInfoAo.get().findDataBaseInfoById(Integer.parseInt(databaseId));				
				responseMap.put("appId", appId);
				responseMap.put("appName", appName);
				responseMap.put("appType", appType);
				responseMap.put("databaseName", po.getDbName());
				responseMap.put("databaseId", databaseId);
				responseMap.put("databaseUrl", po.getDbUrl());
				responseMap.put("isSuccess", addSuccess ? "true" : "false");
				//转化为json对象
				JSONObject json = JSONObject.fromObject(responseMap);		
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}
				
			} 		
			return ;			
		}
		
		if("addServer".equals(action)){//取得全部信息
			String appName = request.getParameter("appName");
			String appId = request.getParameter("appId");
			String appType = request.getParameter("appType");
			String serverId = request.getParameter("serverId");
			
			ServerAppRelPo relPo = new ServerAppRelPo();
			relPo.setAppId(Integer.parseInt(appId));
			relPo.setAppType(appType);
			relPo.setServerId(Integer.parseInt(serverId));
			
			boolean addSuccess = false;
			if(!AppInfoAo.get().isExistServerAppRel(relPo)) {
				addSuccess = AppInfoAo.get().addRel(relPo);
				
			}
			
			
			Map<String,String> responseMap = new HashMap<String, String>();
			if(true) {
				
				ServerInfoPo po = ServerInfoAo.get().findAllServerInfoById(Integer.parseInt(serverId));
			
				responseMap.put("appId", appId);
				responseMap.put("appName", appName);
				responseMap.put("appType", appType);
				responseMap.put("serverName", po.getServerName());
				responseMap.put("serverId", serverId);
				responseMap.put("serverIp", po.getServerIp());
				responseMap.put("isSuccess", addSuccess ? "true" : "false");
				
				JSONObject json = JSONObject.fromObject(responseMap);
				
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
				//	System.out.println(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}

			} 		
			return ;			
		}
		
		if("modifyServer".equals(action)){//取得全部信息
			String appName = request.getParameter("appName");
			String appId = request.getParameter("appId");
			String appType = request.getParameter("appType");
			String serverId = request.getParameter("serverId");
			//设置relPo的值
			ServerAppRelPo relPo = new ServerAppRelPo();
			relPo.setAppId(Integer.parseInt(appId));
			relPo.setAppType(appType);
			relPo.setServerId(Integer.parseInt(serverId));
			
			boolean addSuccess = false;	//用来标志dao操作是否成功
			if((AppInfoAo.get().findServerRel(Integer.parseInt(appId)).size() != 0)) {
				
				addSuccess = AppInfoAo.get().updateRel(relPo);				
			}
					
			Map<String,String> responseMap = new HashMap<String, String>();
			if(true) {
				
				ServerInfoPo po = ServerInfoAo.get().findAllServerInfoById(Integer.parseInt(serverId));				
				responseMap.put("appId", appId);
				responseMap.put("appName", appName);
				responseMap.put("appType", appType);
				responseMap.put("serverName", po.getServerName());
				responseMap.put("serverIp", po.getServerIp());
				responseMap.put("serverId", serverId);
				responseMap.put("isSuccess", addSuccess ? "true" : "false");
				//转化为json对象
				JSONObject json = JSONObject.fromObject(responseMap);		
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}
				
			} 		
			return ;			
		}
		if("deleteServer".equals(action)){//取得全部信息
			String appName = request.getParameter("appName");
			String appId = request.getParameter("appId");
			String appType = request.getParameter("appType");
			String ServerName = request.getParameter("ServerName");
			//设置relPo的值
			ServerAppRelPo relPo = new ServerAppRelPo();
			relPo.setAppId(Integer.parseInt(appId));
			relPo.setAppType(appType);
			//relPo.setServerId(Integer.parseInt(serverId));
			
			
			boolean addSuccess = false;	//用来标志dao操作是否成功
			if((AppInfoAo.get().findDatabaseRel(Integer.parseInt(appId)).size() != 0)) {
				
				addSuccess = AppInfoAo.get().deleteRel(relPo);			
			}	
			return ;			
		}
		
		
		if("addDayConf".equals(action)){//取得全部信息
			
			String appId = request.getParameter("appId");
			String aliasLogName = request.getParameter("aliasLogName");
			String className = request.getParameter("className");
			String split = request.getParameter("splitChar");
			String filePath = request.getParameter("filePath");
			String  future = request.getParameter("future");

			DayConfPo po = new DayConfPo();
			po.setAppId(Integer.parseInt(appId));
			po.setAliasLogName(aliasLogName);
			po.setClassName(className);
			po.setSplitChar(split);
			po.setFilePath(filePath);
			//po.setFuture(future);

			boolean isSuccess = false;
			isSuccess = DayConfAo.get().addDayConfData(po);			
			
			Map<String,DayConfPo> responseMap = new HashMap<String, DayConfPo>();
//			responseMap.put("po", po);
			List<DayConfPo> list = new ArrayList<DayConfPo>();
			if(isSuccess) {
				
				list = DayConfAo.get().findAllAppDayConfByAppId(Integer.parseInt(appId));
				
//				JSONObject json = JSONObject.fromObject(responseMap);
				JSONArray json = JSONArray.fromObject(list);
				
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
				//	System.out.println(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}

			} 		
			return ;			
		}
		if("modifyDayConf".equals(action)){//取得全部信息
			
			String appId = request.getParameter("appId");
			String aliasLogName = request.getParameter("aliasLogName");
			String confId = request.getParameter("confId");
			String className = request.getParameter("className");
			String split = request.getParameter("splitChar");
			String filePath = request.getParameter("filePath");
			String  future = request.getParameter("future");
			
			DayConfPo po = new DayConfPo();
			po.setAppId(Integer.parseInt(appId));
			po.setConfId(Integer.parseInt(confId));
			po.setAliasLogName(aliasLogName);
			po.setClassName(className);
			po.setSplitChar(split);
			po.setFilePath(filePath);
			//po.setFuture(future);
			
			boolean isSuccess = false;
			isSuccess = DayConfAo.get().updateDayConf(po);		
			
//			Map<String,DayConfPo> responseMap = new HashMap<String, DayConfPo>();
//			responseMap.put("po", po);
			List<DayConfPo> list = new ArrayList<DayConfPo>();
			if(isSuccess) {
				
				list = DayConfAo.get().findAllAppDayConfByAppId(Integer.parseInt(appId));
				
//				JSONObject json = JSONObject.fromObject(responseMap);
				JSONArray json = JSONArray.fromObject(list);
				
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
			//		System.out.println(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}
				
			} 		
			return ;			
		}
		if("addTimeConf".equals(action)){//取得全部信息
			
			String appId = request.getParameter("appId");
			String aliasLogName = request.getParameter("aliasLogName");
			String filePath =request.getParameter("filePath");
			String future = request.getParameter("future");
			String frequency = request.getParameter("frequency");
			String className = request.getParameter("analyseClass");
			String split = request.getParameter("split");
			String tailType = request.getParameter("tailType");
			String analyseType = request.getParameter("analyseType");
			String analysedesc = request.getParameter("analyseDesc");
			String obtainType = request.getParameter("obtainType");
			
			TimeConfPo po = new TimeConfPo();
			po.setAppId(Integer.parseInt(appId));
			po.setAliasLogName(aliasLogName);
			po.setClassName(className);
			po.setSplitChar(split);
			po.setFilePath(filePath);
			po.setAnalyseFuture(future);
			po.setAnalyseFrequency(Integer.parseInt(frequency));
			po.setTailType(tailType);
			po.setAnalyseType(Integer.parseInt(analyseType));
			
			po.setObtainType(Integer.parseInt(obtainType));
			
			boolean isSuccess = false;
			isSuccess = TimeConfAo.get().addTimeConfData(po);		
			
			List<TimeConfPo> list = new ArrayList<TimeConfPo>();
			if(isSuccess) {
				
				list = TimeConfAo.get().findTimeConfByAppId(Integer.parseInt(appId));
				
				JSONArray json = JSONArray.fromObject(list);
				
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
					//	System.out.println(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}
				
			} 		
			return ;			
		}
		
		if("modifyTimeConf".equals(action)){//取得全部信息
			
			String appId = request.getParameter("appId");
			String split = request.getParameter("split");
			String tmpConfigId = request.getParameter("tmpConfigId");
			String frequency = request.getParameter("frequency");
			String future = request.getParameter("future");
			
			String filePath =request.getParameter("filePath");
			String obtainType = request.getParameter("obtainType");
			String tailType = request.getParameter("tailType");
			String className = request.getParameter("analyseClass");
			String analyseType = request.getParameter("analyseType");
			

			TimeConfPo po = TimeConfAo.get().findTimeConfByConfId(Integer.parseInt(tmpConfigId));
			po.setClassName(className);
			po.setSplitChar(split);
			po.setFilePath(filePath);
			po.setAnalyseFuture(future);
			po.setAnalyseFrequency(Integer.parseInt(frequency));
			po.setTailType(tailType);
			po.setAnalyseType(Integer.parseInt(analyseType));
			po.setObtainType(Integer.parseInt(obtainType));
			
			boolean isSuccess = false;
			isSuccess = TimeConfAo.get().updateTimeConf(po);
			
			List<TimeConfPo> list = new ArrayList<TimeConfPo>();
			if(isSuccess) {
				
				list = TimeConfAo.get().findTimeConfByAppId(Integer.parseInt(appId));
				
				JSONArray json = JSONArray.fromObject(list);
				
				response.setContentType("text/html;charset=utf-8"); 
				try {
					response.getWriter().write(json.toString());
					System.out.println(json.toString());
					response.flushBuffer();
				} catch (IOException e) {
					
				}
				
			} 		
			return ;			
		}
		
	}

}
