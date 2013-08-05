package com.taobao.csp.config.util;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther shigangxing
 * 
 * */

public class DecodeFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		// System.out.println("sysout getClass(): " + getClass() + "\t方法名："+
		// Thread.currentThread().getStackTrace()[1].getMethodName());
		logger.debug("getClass(): " + getClass() + "\t方法名："
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;

		boolean isAjax = judgeAjaxOrNot(httpReq);

		String reqEnc = null;
		if (isAjax)
			reqEnc = ajaxRequestCharacterEncoding;
		else
			reqEnc = requestCharacterEncoding;

		//System.out.println("------isAjax: " + isAjax + "reqEnc: " + reqEnc);
		if (handlePost) {
			//System.out.println("handlePost-------------------------");
			httpReq.setCharacterEncoding(reqEnc);
		}
		if ("get".equalsIgnoreCase(httpReq.getMethod()) && handleGet) {
			logger.debug("[GET请求: ]");
			// 关键代码
			req = new GetRequestWrapper(httpReq, reqEnc, uriEncoding);
		}

		if (handleResponse) {
			httpResp.setCharacterEncoding(responseCharacterEncoding);
		}
		chain.doFilter(req, resp);

	}

	/**
	 * @author xing 2012-1-12 上午10:44:47
	 * @param httpReq
	 * @return
	 */
	private boolean judgeAjaxOrNot(HttpServletRequest httpReq) {

		// 可能存在多种大小写形式，所以在这里处理一下
		// X-Requested-With: XMLHttpRequest
		// x-requested-with
		Enumeration<String> headNames = httpReq.getHeaderNames();
		String destHeadName = null;
		while (headNames.hasMoreElements()) {
			String headName = headNames.nextElement();
			if ("x-requested-with".equalsIgnoreCase(headName)) {
				destHeadName = headName;
			}
		}
		if (destHeadName == null)
			return false;
		String destHeadV = httpReq.getHeader(destHeadName);
		if ("XMLHttpRequest".equals(destHeadV))
			return true;
		return false;
	}

	public void init(FilterConfig config) throws ServletException {
		String reqEnc = config.getInitParameter("requestCharacterEncoding");
		String ajaxReqEnc = config
				.getInitParameter("ajaxRequestCharacterEncoding");
		String respEnc = config.getInitParameter("responseCharacterEncoding");

		String uriEnc = config.getInitParameter("URIEncoding");

		String hg = config.getInitParameter("handleGet");
		String hp = config.getInitParameter("handlePost");
		String hr = config.getInitParameter("handleResponse");

		if (reqEnc != null)
			requestCharacterEncoding = reqEnc;
		if (ajaxReqEnc != null)
			ajaxRequestCharacterEncoding = ajaxReqEnc;
		if (respEnc != null)
			responseCharacterEncoding = respEnc;

		// tomcat URIEncoding 配置
		if (uriEnc != null)
			uriEncoding = uriEnc;

		if (hg != null)
			handleGet = Boolean.parseBoolean(hg);

		if (hp != null)
			handlePost = Boolean.parseBoolean(hp);

		if (hr != null)
			handleResponse = Boolean.parseBoolean(hr);

	}

	private boolean handleResponse = true;

	private boolean handlePost = true;
	private boolean handleGet = true;
	// tomcat Connector URIEncoding配置
	private String uriEncoding = "ISO-8859-1";
	private String requestCharacterEncoding = "UTF-8";
	private String responseCharacterEncoding = "UTF-8";

	private String ajaxRequestCharacterEncoding = "UTF-8";

	private static final Logger logger = LoggerFactory
			.getLogger(DecodeFilter.class);
}