
package com.taobao.arkclient.csp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.taobao.arkclient.ArkAuthenRequest;
import com.taobao.arkclient.RedirectException;
import com.taobao.arkclient.Service.ConfigManager;

/**
 * 
 * @author xiaodu
 * @version 2010-10-26 下午02:39:40
 */
public class SSOAuthFilter implements Filter {
	
	
	public void destroy() {

	}

	
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// this must be added, otherwise exception(session can not be opened after response was commited
		request.getSession();
		
		String s = request.getRequestURI().substring(request.getContextPath().length());
		if (!(ConfigManager.getInstance().isCommonUnProtect(s) || ConfigManager.getInstance().isUserUnProtect(s))) {
			try {
				// data.do为外部数据接口，不加验证
				if (request.getRequestURL().toString().indexOf("data.do") == -1) {
					new ArkAuthenRequest(request, response);
				}
			} catch (JSONException e) {
			} catch (RedirectException e) {
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
	
	public void init(FilterConfig arg0) throws ServletException {
		
	}


}
