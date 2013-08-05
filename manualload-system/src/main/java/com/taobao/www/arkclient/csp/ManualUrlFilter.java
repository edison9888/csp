package com.taobao.www.arkclient.csp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 
 * 功能：处理错误的url地址信息。
 * 
 * @author wb-tangjinge E-mail:wb-tangjinge@taobao.com
 * @version 1.0
 * @since 2013-1-10 下午4:10:13
 */
public class ManualUrlFilter implements Filter {

	private static final Logger logger = Logger.getLogger(ManualUrlFilter.class);

	private static List<String> Url_Suff = new ArrayList<String>();

	static {
		Url_Suff.add("css");
		Url_Suff.add("js");
		Url_Suff.add("bmp");
		Url_Suff.add("jpg");
		Url_Suff.add("jpeg");
		Url_Suff.add("png");
		Url_Suff.add("gif");
		Url_Suff.add("do");
		Url_Suff.add("action");
		Url_Suff.add("htm");
		Url_Suff.add("jsp");
	}

	public void destroy() {

	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String urls = request.getRequestURI().substring(request.getContextPath().length());
		int dex = urls.lastIndexOf(".");
		int leng = urls.length();
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath();
		String urlSuff = urls.substring(dex + 1, leng).toLowerCase();
		logger.info(" base url address is:" + baseUrl + " ,==== urls :" + request.getRequestURI());
		if (Url_Suff.contains(urlSuff) || urls.equals("/")) {
			filterChain.doFilter(servletRequest, servletResponse);
		} else {
			request.getSession().setAttribute("manualErrorUrl", baseUrl + request.getRequestURI());
			logger.info("manual error url address is :" + request.getRequestURI());
			response.sendRedirect(baseUrl + "/manualError.do");
		}
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
