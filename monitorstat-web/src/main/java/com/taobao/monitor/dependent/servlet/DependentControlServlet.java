
package com.taobao.monitor.dependent.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.taobao.monitor.dependent.control.AppLaunchControl;
import com.taobao.monitor.dependent.control.IpTableControl;
import com.taobao.monitor.dependent.control.ShellCommon;

/**
 * 
 * @author xiaodu
 * @version 2011-5-9 上午11:33:36
 */
public class DependentControlServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2191546728730098852L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getParameter("action");
		String appName = req.getParameter("appName");
		String targetIp = req.getParameter("targetIp");
		String forbidIPs = req.getParameter("forbidIps");
		String recoverIps = req.getParameter("recoverIps");
		
		String userName = req.getParameter("userName");
		String password = req.getParameter("password");
		if(StringUtils.isBlank(targetIp)&&
				StringUtils.isBlank(userName)&&
				StringUtils.isBlank(password)&&
				StringUtils.isBlank(appName)&&
				StringUtils.isBlank(action)&&
				StringUtils.isBlank(forbidIPs)
				){
			paramSetExplain(resp);
			return;
		}
		
		
		if("start".equals(action)){
			long t = System.currentTimeMillis();
			if(StringUtils.isBlank(targetIp)||StringUtils.isBlank(userName)||StringUtils.isBlank(password)||StringUtils.isBlank(appName)){
				paramSetExplain(resp);
				return;
			}
			
			
			ShellCommon shell = new ShellCommon(targetIp,userName,password);
			AppLaunchControl appLaunch = new AppLaunchControl(shell,"/home/admin/"+appName+"/bin/jbossctl ");
			if(appLaunch.restartApp()){
				if(appLaunch.checkProcess()){
					flushData(resp,"ok");
				}else{
					flushData(resp,"fail");
				}
			}else{
				flushData(resp,"fail");
			}
			shell.close();
			System.out.println("start:"+(System.currentTimeMillis() - t));
			return ;
		}
		
		if("stop".equals(action)){
			long t = System.currentTimeMillis();
			if(StringUtils.isBlank(targetIp)||StringUtils.isBlank(userName)||StringUtils.isBlank(password)||StringUtils.isBlank(appName)){
				paramSetExplain(resp);
				return;
			}
			
			ShellCommon shell = new ShellCommon(targetIp,userName,password);
			AppLaunchControl appLaunch = new AppLaunchControl(shell,"/home/admin/"+appName+"/bin/jbossctl ");
			if(appLaunch.stopApp()){
				if(!appLaunch.checkProcess()){
					flushData(resp,"ok");
				}else{
					flushData(resp,"fail");
				}
			}else{
				flushData(resp,"fail");
			}
			shell.close();
			System.out.println("stop:"+(System.currentTimeMillis() - t));
			return ;
		}
		
		
		
		if("forbidIps".equals(action)){
			long t = System.currentTimeMillis();
			
			if(StringUtils.isBlank(targetIp)||StringUtils.isBlank(userName)||StringUtils.isBlank(password)||StringUtils.isBlank(forbidIPs)){
				paramSetExplain(resp);
				return;
			}
			
			ShellCommon shell = new ShellCommon(targetIp,userName,password);
			IpTableControl iptable = new IpTableControl(shell);
			if(iptable.recoverAll()){
				if(iptable.forbidIp(forbidIPs.split(","))){
					flushData(resp,"ok");
				}else{
					flushData(resp,"fail");
				}
			}else{
				flushData(resp,"fail");
			}
			shell.close();
			
			System.out.println("forbidIps:"+(System.currentTimeMillis() - t));
			return ;
		}
		
		if("recoverIps".equals(action)){
			long t = System.currentTimeMillis();
			if(StringUtils.isBlank(targetIp)||StringUtils.isBlank(userName)||StringUtils.isBlank(password)){
				paramSetExplain(resp);
				return;
			}
			
			ShellCommon shell = new ShellCommon(targetIp,userName,password);
			IpTableControl iptable = new IpTableControl(shell);
			
			if(recoverIps!=null){
				if(iptable.recoverIps(recoverIps.split(","))){
					flushData(resp,"ok");
				}else{
					flushData(resp,"fail");
				}
			}else{
				if(iptable.recoverAll()){
					flushData(resp,"ok");
				}else{
					flushData(resp,"fail");
				}
			}
			
			shell.close();
			
			System.out.println("recoverIps:"+(System.currentTimeMillis() - t));
			
			return ;
		}
		
		
	}
	
	
	
	private void paramSetExplain(HttpServletResponse resp){
		String msg = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
				"<head>" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />" +
				"<title>无标题文档</title>" +
				"</head>" +
				"<body>" +
				"<table  style='font-size:14px;border-collapse:collapse;width:100%' border='1' cellspacing='0' bordercolor='#4f81bd' cellpadding='0' align='center'>" +
				"	<tr>" +
				"		<td>action</td><td>需要附加参数</td>" +
				"	</tr>" +
				"	<tr>" +
				"		<td>action=start,stop</td><td>targetIp=目标机器IPuserName=目标机器用户名password=目标机器密码appName=应用名称</td>" +
				"	</tr>" +
				"	<tr>" +
				"		<td>action=forbidIps</td><td>targetIp=目标机器IPuserName=目标机器用户名password=目标机器密码forbidIps=ip1,ip2</td>" +
				"	</tr>" +
				"	<tr>" +
				"		<td>action=recoverIps</td><td>targetIp=目标机器IPuserName=目标机器用户名password=目标机器密码recoverIps=ip1,ip2 如果recoverIps为空清楚全部</td>" +
				"	</tr>" +				
				"" +
				"</table>" +
				"</body>" +
				"</html>";
		flushData(resp,msg);
	}
	
	
	
	private void flushData(HttpServletResponse resp,String msg){
		
		
		resp.setCharacterEncoding("GBK");
		try {
			resp.getWriter().print(msg);
			resp.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	

}
