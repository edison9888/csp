
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.taobao.csp.monitor.DataCollector;
import com.taobao.csp.monitor.JobInfo;
import com.taobao.monitor.MonitorLog;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * @author xiaodu
 *
 * 下午6:23:43
 */
public class SSHModeCollector  implements DataCollector{
	
	private static final Logger logger =  Logger.getLogger(SSHModeCollector.class);

	
	private JobInfo jobInfo;
	
	private Connection connection = null;
	
	
	public SSHModeCollector(JobInfo jobInfo){
		this.jobInfo = jobInfo;
	}
	
	@Override
	public void collect(CallBack call) {
		connection = createSshConnect();
		if(connection == null){
			return ;
		}
		Session session=null; 
		String command = jobInfo.getFilepath();
		char separator = this.jobInfo.getLinebreaks();
		try{
			session = connection.openSession();
			session.execCommand(command);		
			InputStream stdout = new StreamGobbler(session.getStdout());		
			BufferedReader2 br = new BufferedReader2(new InputStreamReader(stdout,"gbk"),separator);		
			String line = null;
			while((line = br.readLine()) != null){
				call.readerLine(line);
			}			
		}catch (Exception e) {
			logger.error("执行"+command+"异常",e);
		}finally{
			try{
				if(session != null)
					session.close();
				}catch (Exception e) {
				}
		}
		
		
		
	}

	@Override
	public void release() {
		if(connection != null){
			connection.close();
		}
	}
	
	
	/**
	 * 创建ssh 连接
	 */
	public Connection createSshConnect(){	
		long time = System.currentTimeMillis();
		Connection conn = new Connection(jobInfo.getIp());
		try {
			conn.connect(null,2000,2000);
			boolean isAuthenticated = conn.authenticateWithPassword( jobInfo.getSshUserName(), jobInfo.getSshPassword());
			if (isAuthenticated == false){
				logger.info(jobInfo.getIp()+"login fail:"+jobInfo.getSshUserName()+":"+jobInfo.getSshPassword());
				throw new IOException(jobInfo.getIp()+"login fail:"+jobInfo.getSshUserName()+":"+jobInfo.getSshPassword());
			}
		} catch (IOException e) {
			try{
				conn.close();
			}catch (Exception e1) {
			}
			conn = null;
		}
		MonitorLog.addStat("csp_data_analyse.log", new String[]{"data_analyse","createSsh"}, new Long[]{1l,System.currentTimeMillis()-time});
		return conn;
	}

	
	@Override
	public String getName() {
		return "ssh";
	}
	
	

}
