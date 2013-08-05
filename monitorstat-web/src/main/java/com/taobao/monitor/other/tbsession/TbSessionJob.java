
package com.taobao.monitor.other.tbsession;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.web.util.RequestByUrl;

/**
 * 
 * @author xiaodu
 * @version 2011-5-13 ÏÂÎç02:50:16
 */
public class TbSessionJob implements Job{
	

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
//		String url = "http://127.0.0.1:8080//monitorstat/report/report_tbsession.jsp";		
//		String msg =  RequestByUrl.getMessageByJsp(url);
		
		//impl.sendEmail("CSP-tbSession-±¨±í", msg, "xiaodu@taobao.com;xiaoxie@taobao.com;fenghao@taobao.com");
		
	}

}
