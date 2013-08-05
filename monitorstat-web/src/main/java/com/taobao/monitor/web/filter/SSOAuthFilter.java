
package com.taobao.monitor.web.filter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.arkclient.LoginHelp;
import com.taobao.arkclient.RedirectException;
import com.taobao.monitor.web.util.SessionUtil;

/**
 * 
 * @author xiaodu
 * @version 2010-10-26 ÏÂÎç02:39:40
 */
public class SSOAuthFilter implements Filter {
	
	private Set<String> permissionJspSet = new HashSet<String>();
	
	
	public void destroy() {

	}

	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		
		
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		request.setCharacterEncoding("gbk");
		String ip = request.getRemoteHost();
		if(!"127.0.0.1".equals(ip)&&!"localhost".equals(ip)){
			String s = request.getRequestURI().substring(request.getContextPath().length());
			
			if(s.indexOf("index.jsp")>0){
			
				if(s.replaceAll("\\/", "").equals("load")){
					response.sendRedirect("http://cm.taobao.net:9999/monitorstat/load/load_capacity.jsp");
					return ;
				}
				if(s.replaceAll("\\/", "").equals("jprof")){
					response.sendRedirect("http://cm.taobao.net:9999/monitorstat/jprof/manage_jprof_host.jsp");
					return ;
				}
				
				if(s.indexOf("logout.jsp")>0){
					SessionUtil.logout(request, response);
				}
				
				
				try {
					LoginHelp.doDomain(request, response);
				} catch (RedirectException e) {
					return ;
				}
			}
		}		
		filterChain.doFilter(servletRequest, servletResponse);
	}

	
	public void init(FilterConfig arg0) throws ServletException {
		permissionJspSet.add("/alarm/add_alarm_key.jsp");
		permissionJspSet.add("/alarm/alarm_filter.jsp");
		permissionJspSet.add("/alarm/manage_alarm_key.jsp");
		permissionJspSet.add("/alarm/manage_key.jsp");
		permissionJspSet.add("/alarm/manage_user.jsp");
		permissionJspSet.add("/alarm/update_alarm_key.jsp");
		permissionJspSet.add("/jprof/add_jprof_host.jsp");
		permissionJspSet.add("/jprof/update_jprof_host.jsp");
		permissionJspSet.add("/jprof/upload_file.jsp");
		permissionJspSet.add("/load/loadrun_splitflow.jsp");
		permissionJspSet.add("/load/loadrun_manual.jsp");
		permissionJspSet.add("/load/loadrun_manual_new.jsp");
		permissionJspSet.add("/user/manage_user.jsp");
		permissionJspSet.add("/user/user_info_add.jsp");
		permissionJspSet.add("/user/user_info_update.jsp");
		permissionJspSet.add("/user/user_info.jsp");
		permissionJspSet.add("/comm/keyInfo_update.jsp");
		permissionJspSet.add("/health/update_rating_app.jsp");
		permissionJspSet.add("/health/add_rating_app.jsp");
	}


}
