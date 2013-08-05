
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xiaodu
 *
 * ����9:41:00
 */
public class MonitorServlet  extends HttpServlet{

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
	
		String writeTime =  getInitParameter("writeTime");//����writeTime Ĭ����60s
		if(writeTime!= null){
			try{
				MonitorLog.setWaitTime(Long.parseLong(writeTime));
			}catch (Exception e) {
			}
		}
		String compressKeys = getInitParameter("compressKeys");//�����Ƿ�ѹ�� key
		if(compressKeys!= null){
			try{
				MonitorLog.setCompress(Boolean.valueOf(compressKeys));
			}catch (Exception e) {
			}
		}
		String maxKeySize = getInitParameter("maxKeySize");//�����Ƿ�ѹ�� key
		if(maxKeySize!= null){
			try{
				MonitorLog.setMaxKeySize(Integer.valueOf(maxKeySize));
			}catch (Exception e) {
			}
		}
		String maxCompressKeySize = getInitParameter("maxCompressKeySize");//�����Ƿ�ѹ�� key
		if(maxCompressKeySize!= null){
			try{
				MonitorLog.setMaxCompressKeySize(Integer.valueOf(maxCompressKeySize));
			}catch (Exception e) {
			}
		}
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String writeTime = req.getParameter("writeTime");
		if(writeTime!= null){
			try{
				MonitorLog.setWaitTime(Long.parseLong(writeTime));
			}catch (Exception e) {
			}
		}
		
	}

	
	
	
	

}
