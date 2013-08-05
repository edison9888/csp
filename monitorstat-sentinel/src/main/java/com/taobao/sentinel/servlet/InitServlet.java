package com.taobao.sentinel.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.taobao.sentinel.client.SentinelConfig;
import com.taobao.sentinel.sync.SynchroOpsDiamond;

public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(InitServlet.class);
	
	public void init(ServletConfig config) throws ServletException {
		if (SentinelConfig.RUN_SYNC_IP.equals("true")) {
			logger.info("init servlet ....");    
			SynchroOpsDiamond.start();
		}
		
		super.init(config);
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
