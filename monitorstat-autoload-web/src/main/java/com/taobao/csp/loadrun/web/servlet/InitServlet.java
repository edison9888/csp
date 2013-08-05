package com.taobao.csp.loadrun.web.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		try {
			Class.forName("com.taobao.monitor.common.util.TBProductCache");
		} catch (ClassNotFoundException e) {
		}
	}
}
