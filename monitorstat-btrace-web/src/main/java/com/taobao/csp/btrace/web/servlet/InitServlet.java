
package com.taobao.csp.btrace.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.taobao.csp.btrace.core.server.BtraceServer;

/**
 * 
 * @author xiaodu
 * @version 2011-8-26 обнГ04:24:06
 */
public class InitServlet extends HttpServlet{

	@Override
	public void init() throws ServletException {
		
		BtraceServer.get().startServer();
		
		System.out.println("BtraceServer.get().startServer()");
	}

}
