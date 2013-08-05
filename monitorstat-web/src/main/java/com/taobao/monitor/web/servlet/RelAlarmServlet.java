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
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.cache.KeyCache;

/**
 * Servlet implementation class TestServet
 */
public class RelAlarmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RelAlarmServlet() {
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
		
		
		//���Ӧ�ö�Ӧ��key����
		if("addRelAppKey".equals(action)){//ȡ��ȫ����Ϣ
			String gs = request.getParameter("keyGroups");		//ֵ�����ӣ�87,91,..
			String[] alarmKeys = gs.split(",");
			List<String> keysList = new ArrayList<String>();
			for(String keyId : alarmKeys) {
				
				keysList.add(keyId);
			}
			JSONArray json = JSONArray.fromObject(keysList);
			response.setContentType("text/html;charset=utf-8"); 
			try {
				response.getWriter().write(json.toString());
				response.flushBuffer();
			} catch (IOException e) {
				
			}
			return ;
		}
		
		//��Ӹ澯��Ӧ�ù���
		if("addRelAlarmApp".equals(action)){//ȡ��ȫ����Ϣ
			String gs = request.getParameter("appGroups");		//ֵ�����ӣ�87,91,..
			String[] alarmApps = gs.split(",");
			Map<String,String> responseMap = new HashMap<String, String>();

			for(String appId : alarmApps) {
				
				responseMap.put(appId, AppCache.get().getKey(Integer.parseInt(appId)).getAppName());
				//alarmAppList.add(AppCache.get().getKey(Integer.parseInt(appId)).getAppName());
			}
			JSONObject json = JSONObject.fromObject(responseMap);

			//JSONArray json = JSONArray.fromObject(alarmAppList);
			response.setContentType("text/html;charset=utf-8"); 
			try {
				response.getWriter().write(json.toString());
				response.flushBuffer();
			} catch (IOException e) {
				
			}
			return ;
		}

		//��� �����Ӧ�õ�id
		if("addRelAppReport".equals(action)){//ȡ��ȫ����Ϣ
			String gs = request.getParameter("appGroups");		//ֵ�����ӣ�87,91,..
			String[] reportApp = gs.split(",");
			List<String> reportAppsList = new ArrayList<String>();
			for(String keyId : reportApp) {
				
				reportAppsList.add(keyId);
			}
			JSONArray json = JSONArray.fromObject(reportAppsList);
			response.setContentType("text/html;charset=utf-8"); 
			try {
				response.getWriter().write(json.toString());
				response.flushBuffer();
			} catch (IOException e) {
				
			}
			return ;
		}
		
		
	}

}
