/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.dependent.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.taobao.monitor.dependent.dao.CspDependentDao;
import com.taobao.monitor.dependent.po.AppDependentRelation;
import com.taobao.monitor.web.util.DateUtil;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-6 - 下午01:16:35
 * @version 1.0
 */
public class DependentListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 195405L;

	private Logger logger = Logger.getLogger(DependentListServlet.class);
	
	private CspDependentDao cspDependentDao = new CspDependentDao();

	/**
	 * @author 斩飞
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * 2011-5-6 - 下午01:18:22
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @author 斩飞
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * 2011-5-6 - 下午01:18:22
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		String appName = req.getParameter("appName");
		String collectTime = req.getParameter("time");
		Date date = null;
		String reStr = "";
		boolean state = validateQueryParam(action, appName, collectTime, resp);
		if(state){
			try {
				date = DateUtil.getDateYMDFormat().parse(collectTime);
			} catch (ParseException e) {
				logger.error("获取依赖我的应用json信息出错！"+e.getMessage());
				reStr = "<font style='color:red'>注意：请传入正确的日期参数,格式为yyyy-MM-dd!</font>";
				flushDataToBrowser(resp, reStr);
				return;
			}
			
			if("medep".equals(action)){//我依赖的
				reStr = getMeDependentAppInfos(appName, date);
				
			}else if("depme".equals(action)){//依赖我的
				reStr = getDependentMeAppInfos(appName, date);
			}
			flushDataToBrowser(resp, reStr);
		}
	}
	
	/**
	 * 获取依赖我的应用json信息
	 * @author 斩飞
	 * @return
	 * 2011-5-6 - 下午02:20:01
	 */
	private String getDependentMeAppInfos(String self, Date date){
		Map<String, List<AppDependentRelation>> map = cspDependentDao.
		findDependentMeByQuery(self,
				date);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	
	/**
	 * 获取我依赖的应用json信息
	 * @author 斩飞
	 * @param self
	 * @param collectTime
	 * @return
	 * 2011-5-6 - 下午03:35:33
	 */
	private String getMeDependentAppInfos(String self, Date date){
		Map<String, List<AppDependentRelation>> map = cspDependentDao.
		findMeDependentByQuery(self, date);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

	/**
	 * 验证查询参数合法性
	 * @author 斩飞
	 * @param action
	 * @param appName
	 * @param collectTime
	 * @param resp
	 * 2011-5-6 - 下午04:31:05
	 */
	private boolean validateQueryParam(String action, String appName,
			String collectTime, HttpServletResponse resp){
		boolean state = true;
		StringBuffer re = new StringBuffer("<!DOCTYPE HTML PUBLIC " +
				"'-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3" +
				".org/TR/xhtml1/DTD/xhtml1-transitional.dtd'><html>" +
				"<head><div style='margin:0 auto;width:60%;align:center;" +
				"border:solid 0px #4f81bd;'><table style='font-size:14px;border-collapse:collapse;" +
				"width:100%' border='1' cellspacing='0' bordercolor='#4f81bd' " +
				"cellpadding='0' align='center'>");
		if(action == null || action.equals("") ){
			state = false;
			re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red;" +
					"font-weight: bold;'>注意：缺少查询参数：action<font>(参考下表)</td></tr>");
		}else{
			if(!action.equals("medep")&&!action.equals("depme")){
				state = false;
				re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red;" +
				"font-weight: bold;'>注意：查询参数action错误<font>(参考下表)</td></tr>");
			}
		}
		if(collectTime == null || collectTime.equals("")){
			state = false;
			re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red;" +
			"font-weight: bold;'>注意：缺少查询参数：time<font>(参考下表)</td></tr>");
		}else{
			try {
				DateUtil.getDateYMDFormat().parse(collectTime);
			} catch (ParseException e) {
				state = false;
				re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red'>" +
						"注意：请传入正确的日期参数,格式为yyyy-MM-dd!</font>(参考下表)</td></tr>");
			}
			
		}
		re.append("<tr style='height:30px'><td colspan='2' align='center'>" +
				"<font style='color:blue;font-weight: bold;'>查询参数<font>(格式如下)</td></tr>" +
				"<tr style='height:30px;font-weight: bold;'><td>参数</td><td>取值</td></tr>"+
				"<tr style='height:60px'><td>action</td><td>medep:查询我依赖的应用</br>depme：查询依赖我的应用" +
				"</td></tr><tr style='height:40px'><td>appName</td><td>查询的应用名称,建议每次只查询单个应用" +
				"的数据！</td></tr><tr style='height:40px'><td>time</td><td>查询的时间：" +
				"<font style='color:red'>注意->正确的日期参数,格式为yyyy-MM-dd!</font></td></tr>"+
				"</table></div></html></head>");
		if(!state){
			flushDataToBrowser(resp, re.toString());
		}
		return state;
	}
	
	/**
	 * 输出数据到浏览器
	 * @author 斩飞
	 * @param resp
	 * @param reStr
	 * 2011-5-6 - 下午04:08:12
	 */
	private void flushDataToBrowser(HttpServletResponse resp, String reStr){
		resp.setContentType("text/html;charset=utf-8"); 
		try {
			resp.getWriter().write(reStr);
			resp.flushBuffer();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
	
}
