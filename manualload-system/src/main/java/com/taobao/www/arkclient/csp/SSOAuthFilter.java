package com.taobao.www.arkclient.csp;

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

import com.taobao.www.arkclient.ArkAuthenRequest;
import com.taobao.www.arkclient.RedirectException;
import com.taobao.www.arkclient.Service.ConfigManager;

/**
 * 
 * @author xiaodu
 * @version 2010-10-26 下午02:39:40
 */
public class SSOAuthFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String s = request.getRequestURI().substring(request.getContextPath().length());
		request.getSession();
		if (!(ConfigManager.getInstance().isCommonUnProtect(s) || ConfigManager.getInstance().isUserUnProtect(s))) {
			try {
				new ArkAuthenRequest(request, response);
			} catch (JSONException e) {
			} catch (RedirectException e) {
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
