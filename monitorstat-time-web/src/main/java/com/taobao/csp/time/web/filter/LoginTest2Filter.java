package com.taobao.csp.time.web.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.taobao.csp.time.custom.arkclient.ArkDomain;
//FIXME ≤‚ ‘”√£¨¥˝…æ≥˝
public class LoginTest2Filter implements  Filter {



	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String mail = ArkDomain.getArkUserEmail((HttpServletRequest)request);
		System.out.println("--after ark filter, --from : "+mail);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}



}

