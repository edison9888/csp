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
//FIXME 测试用，待删除
public class LoginTestFilter implements  Filter {



	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//打印本机ip
		String ip =InetAddress.getLocalHost().getHostAddress().toString();
		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
		String remoteIp = request.getRemoteAddr();
		System.out.println(sdf.format(new Date())+"\t ip: "+ ip+"\t remote ip: "+ remoteIp+"\t remote host: "+ request.getRemoteHost());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}



}

