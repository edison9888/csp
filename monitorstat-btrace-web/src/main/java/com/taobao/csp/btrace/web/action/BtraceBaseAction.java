package com.taobao.csp.btrace.web.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class BtraceBaseAction {
	
	Logger logger = Logger.getLogger(BtraceBaseAction.class);
	/**
	 * ajax��ʽ�����ַ���
	 * @param str			Ҫ���ص��ַ���
	 * @param response		��Ҫ�������
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
			logger.debug("Ajax������Ϣ���쳣", e);
		}
		return ret;
	}
}
