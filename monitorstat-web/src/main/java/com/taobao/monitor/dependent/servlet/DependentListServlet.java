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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-6 - ����01:16:35
 * @version 1.0
 */
public class DependentListServlet extends HttpServlet {
	
	private static final long serialVersionUID = 195405L;

	private Logger logger = Logger.getLogger(DependentListServlet.class);
	
	private CspDependentDao cspDependentDao = new CspDependentDao();

	/**
	 * @author ն��
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * 2011-5-6 - ����01:18:22
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	/**
	 * @author ն��
	 * @param req
	 * @param resp
	 * @throws ServletException
	 * @throws IOException
	 * 2011-5-6 - ����01:18:22
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
				logger.error("��ȡ�����ҵ�Ӧ��json��Ϣ����"+e.getMessage());
				reStr = "<font style='color:red'>ע�⣺�봫����ȷ�����ڲ���,��ʽΪyyyy-MM-dd!</font>";
				flushDataToBrowser(resp, reStr);
				return;
			}
			
			if("medep".equals(action)){//��������
				reStr = getMeDependentAppInfos(appName, date);
				
			}else if("depme".equals(action)){//�����ҵ�
				reStr = getDependentMeAppInfos(appName, date);
			}
			flushDataToBrowser(resp, reStr);
		}
	}
	
	/**
	 * ��ȡ�����ҵ�Ӧ��json��Ϣ
	 * @author ն��
	 * @return
	 * 2011-5-6 - ����02:20:01
	 */
	private String getDependentMeAppInfos(String self, Date date){
		Map<String, List<AppDependentRelation>> map = cspDependentDao.
		findDependentMeByQuery(self,
				date);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}
	
	/**
	 * ��ȡ��������Ӧ��json��Ϣ
	 * @author ն��
	 * @param self
	 * @param collectTime
	 * @return
	 * 2011-5-6 - ����03:35:33
	 */
	private String getMeDependentAppInfos(String self, Date date){
		Map<String, List<AppDependentRelation>> map = cspDependentDao.
		findMeDependentByQuery(self, date);
		JSONObject json = JSONObject.fromObject(map);
		return json.toString();
	}

	/**
	 * ��֤��ѯ�����Ϸ���
	 * @author ն��
	 * @param action
	 * @param appName
	 * @param collectTime
	 * @param resp
	 * 2011-5-6 - ����04:31:05
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
					"font-weight: bold;'>ע�⣺ȱ�ٲ�ѯ������action<font>(�ο��±�)</td></tr>");
		}else{
			if(!action.equals("medep")&&!action.equals("depme")){
				state = false;
				re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red;" +
				"font-weight: bold;'>ע�⣺��ѯ����action����<font>(�ο��±�)</td></tr>");
			}
		}
		if(collectTime == null || collectTime.equals("")){
			state = false;
			re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red;" +
			"font-weight: bold;'>ע�⣺ȱ�ٲ�ѯ������time<font>(�ο��±�)</td></tr>");
		}else{
			try {
				DateUtil.getDateYMDFormat().parse(collectTime);
			} catch (ParseException e) {
				state = false;
				re.append("<tr style='height:30px'><td colspan='2' align='center'><font style='color:red'>" +
						"ע�⣺�봫����ȷ�����ڲ���,��ʽΪyyyy-MM-dd!</font>(�ο��±�)</td></tr>");
			}
			
		}
		re.append("<tr style='height:30px'><td colspan='2' align='center'>" +
				"<font style='color:blue;font-weight: bold;'>��ѯ����<font>(��ʽ����)</td></tr>" +
				"<tr style='height:30px;font-weight: bold;'><td>����</td><td>ȡֵ</td></tr>"+
				"<tr style='height:60px'><td>action</td><td>medep:��ѯ��������Ӧ��</br>depme����ѯ�����ҵ�Ӧ��" +
				"</td></tr><tr style='height:40px'><td>appName</td><td>��ѯ��Ӧ������,����ÿ��ֻ��ѯ����Ӧ��" +
				"�����ݣ�</td></tr><tr style='height:40px'><td>time</td><td>��ѯ��ʱ�䣺" +
				"<font style='color:red'>ע��->��ȷ�����ڲ���,��ʽΪyyyy-MM-dd!</font></td></tr>"+
				"</table></div></html></head>");
		if(!state){
			flushDataToBrowser(resp, re.toString());
		}
		return state;
	}
	
	/**
	 * ������ݵ������
	 * @author ն��
	 * @param resp
	 * @param reStr
	 * 2011-5-6 - ����04:08:12
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
