package com.taobao.csp.btrace.web.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BtraceBaseAction {
	
	Logger logger = Logger.getLogger(BtraceBaseAction.class);
	/**
	 * ajax方式返回字符串
	 * @param str			要返回的字符串
	 * @param response		需要的输出流
	 * @return
	 */
	public boolean writeResponse(String str, HttpServletResponse response){
		boolean ret = true;
		try{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter pw = response.getWriter();
			pw.write(str);
			response.flushBuffer();
			pw.close();
		}catch (Exception e) {
			logger.debug("Ajax返回信息是异常", e);
		}
		return ret;
	}
}
