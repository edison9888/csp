package com.taobao.csp.time.web.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;


public class ArkUserInfoFilter implements Filter {
	private final static String USERITEM = "Ark:User";
	private final static String USERITEM_COOKIE_NAME = "Ark_User";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if(request.getAttribute(USERITEM)!=null){
			//ÉèÖÃcookie
			String raw = request.getAttribute(USERITEM).toString();
			String encoded = URLEncoder.encode(raw,"utf-8");
			Cookie c = new Cookie(USERITEM_COOKIE_NAME, encoded);
			c.setMaxAge(60*60*100);
			response.addCookie(c);
		}else{
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (Cookie cookie : cookies) {
					if (StringUtils.equals(USERITEM_COOKIE_NAME, cookie.getName())) {
						String encoded = cookie.getValue();
						String decoded = URLDecoder.decode(encoded,"utf-8");
						request.setAttribute(USERITEM,decoded);
						break;
					}
				}
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
