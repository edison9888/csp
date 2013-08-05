
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.servlet;

import com.taobao.csp.alarm.AlarmKeyContainer;
import com.taobao.csp.alarm.tddl.TddlChecker;
import com.taobao.csp.alarm.url.UrlMonitorThread;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * @author xiaodu
 *
 * ÏÂÎç3:41:41
 */
public class InitServlet extends HttpServlet{
	private static final Logger logger = Logger.getLogger(InitServlet.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -4917339947648465603L;

	@Override
	public void init() throws ServletException {
		
		 String prefix = getServletContext().getRealPath("/");  
         String file = getInitParameter("Log4j-init-file");  
         if(file != null) {  
        	 DOMConfigurator.configure(prefix+file);
         }
		AlarmKeyContainer.startup();
		UrlMonitorThread.startup();
        TddlChecker.startup();
		
	}
	
	

}
