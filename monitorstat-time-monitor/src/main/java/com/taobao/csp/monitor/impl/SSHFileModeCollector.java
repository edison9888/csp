
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
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
public class SSHFileModeCollector  implements DataCollector{
	
	private static final Logger logger =  Logger.getLogger(SSHFileModeCollector.class);

	
	private JobInfo jobInfo;
	
	private Connection connection = null;
	
	
	public SSHFileModeCollector(JobInfo jobInfo){
		this.jobInfo = jobInfo;
	}
	
	
	private int callSsh(Connection conn,CallBack call,String command){
		int lines = 0;
		Session session=null; 
		try{
			char separator = this.jobInfo.getLinebreaks();	
			session = conn.openSession();
			session.execCommand(command);		
			InputStream stdout = new StreamGobbler(session.getStdout());		
			BufferedReader2 br = new BufferedReader2(new InputStreamReader(stdout,"gbk"),separator);		
			String line = null;
			while((line = br.readLine()) != null){
				call.readerLine(line);
				lines++;
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
		return lines;
	}
	
	
	
	private String callSsh(Connection conn,String command){
		
		StringBuffer sb = new StringBuffer();
		Session session=null; 
		try{
			session = conn.openSession();
			session.execCommand(command);		
			InputStream stdout = new StreamGobbler(session.getStdout());		
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout,"gbk"));		
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line);
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
		return sb.toString();
	}
	
	
	@Override
	public void collect(CallBack call) {
		
		connection = createSshConnect();
		
		if(connection == null){
			return ;
		}
		
		String filepath = this.jobInfo.getFilepath();
		long currentLine = this.jobInfo.getFileLineNum();
		int lines =0;
		String commadn = null;
		if(currentLine >0){
			commadn = "tail -n +"+currentLine+" "+ filepath;
			lines = callSsh(connection,call,commadn);
			logger.debug(commadn+":"+lines);				
		}
		if(lines <= 0){
			//可能启动重启后，文件被重置了
			//获取当前行数
			commadn = "wc -l "+ filepath;		
			String msg =callSsh(connection,commadn);
			String[] tmp = StringUtils.split(msg);
			try{
				if(tmp.length>1){
					int num = Integer.parseInt(tmp[0]);
					if(num >0){
						this.jobInfo.setFileLineNum(num+1);
					}
					logger.debug(commadn+":"+num);
				}else{
					logger.debug(commadn+":"+msg);
				}
				
			}catch (Exception e) {
				logger.error(commadn+":"+msg,e);
			}
		}else{
			this.jobInfo.setFileLineNum(this.jobInfo.getFileLineNum()+lines);
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


	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataCollector#getName()
	 */
	@Override
	public String getName() {
		return "sshfile";
	}
	
	public static void main(String[] args){
		
		JobInfo job = new JobInfo("");
		job.setIp("login1.cm3.taobao.org");
		job.setSshUserName("xiaodu");
		job.setSshPassword("Hello_123");
		job.setFilepath("/home/xiaodu/test.txt");
		job.setLinebreaks('\n');
		
		SSHFileModeCollector collector = new SSHFileModeCollector(job);
		
		while(true){
			collector.collect(new CallBack() {
				@Override
				public void readerLine(String line) {
					System.out.println(line);
				}
			});
		}
		
	}

}
