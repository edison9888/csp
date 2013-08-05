
package com.taobao.monitor.stat.accept;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.stat.util.Config;

/**
 * 
 * @author xiaodu
 * @version 2010-4-2 下午05:05:14
 */
public class ScpAccept implements Runnable{	
	private static final Logger logger =  Logger.getLogger(ScpAccept.class);
		
	private String hostIp;
	private String userName = "nobody";
	//private String userPasswd = "!qaZXsw2";
	private String userPasswd = "look";
	private List<String> remotePathFileList;
	private String localPath;
	private CountDownLatch latch;
	
	
	
	public ScpAccept(String userName,String userPassword,String hostIP,List<String> remotePathFileList,String localPath,CountDownLatch latch){
		this.localPath = localPath;
		this.remotePathFileList = remotePathFileList;
		this.hostIp = hostIP;
		this.latch = latch;
		this.userName = userName;
		this.userPasswd = userPassword;
	}
	
	
	public void accept() throws Exception{
		
		if(this.hostIp==null){
			logger.info("无IP");
			return ;
		}
		
		logger.info("读取Ip:"+hostIp+" ");
		if(this.remotePathFileList==null){
			logger.info("无日志文件");
			return ;
		}else{
			for(String log:remotePathFileList){
				logger.info("日志:"+log);
			}
		}
		
		Connection conn = new Connection(this.hostIp);
		try{
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(this.userName, this.userPasswd);
			if (isAuthenticated == false){
				throw new IOException();
			}
		}catch (Exception e) {
			conn.close();
			throw new Exception(this.hostIp+" Authentication failed."+this.userName+"/"+this.userPasswd);
		}
			
		for(String logPath:remotePathFileList){
			
			String remoteFileName = Utlitites.getFileName(logPath);
			try{				
				SCPClient client = conn.createSCPClient();				
				if(client!=null){				
					File file = new File(this.localPath);
					if(!file.exists()){
						file.mkdirs();
					}
					if(remoteFileName.indexOf("gc.log")>-1){
						OutputStream out = new FileOutputStream(this.localPath+"/"+remoteFileName+"_"+this.hostIp);
						client.get(logPath, out);
						try{//gc.log 日志很特殊
							Calendar cal = Calendar.getInstance();							
							SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
							{
								String exec = "awk '{print $0}' /home/admin/logs/gc.log."+sdf.format(cal.getTime())+"* >/tmp/gc.log_tmp";
								logger.info(exec);
								Session sess = conn.openSession();
								sess.execCommand(exec);						
								sess.close();
							}
							{
								cal.add(Calendar.DAY_OF_MONTH, -1);
								String exec = "awk '{print $0}' /home/admin/logs/gc.log."+sdf.format(cal.getTime())+"* >>/tmp/gc.log_tmp";
								logger.info(exec);
								Session sess = conn.openSession();
								sess.execCommand(exec);						
								sess.close();
							}
							Connection connSCP = new Connection(this.hostIp);
							connSCP.connect();
							boolean isAuthenticatedSCP = connSCP.authenticateWithPassword(this.userName, this.userPasswd);
							if (isAuthenticatedSCP == false)
								throw new IOException("Authentication failed.");
							OutputStream out_gc = new FileOutputStream(this.localPath+"/gc.log_tmp_"+this.hostIp);
							SCPClient client_gc = connSCP.createSCPClient();
							client_gc.get("/tmp/gc.log_tmp", out_gc);
							connSCP.close();
							
							
						}catch (Exception e) {
							logger.info("执行取得gc重启日志",e);
						}
					}else{
						OutputStream out = new FileOutputStream(this.localPath+"/"+remoteFileName+"_"+this.hostIp);
						client.get(logPath, out);
					}					
					logger.info("scp "+logPath+" to "+this.localPath+"/"+remoteFileName+"_"+this.hostIp);
				}else{
					throw new IOException("ssh session can not create");
				}
				
			}catch(Exception e){
				logger.info("scp "+logPath+" to "+this.localPath+"/"+remoteFileName+"_"+this.hostIp,e);
			}
			
		}
		
		conn.close();
		
	}
	

	public void run() {
		try {
			accept();
			logger.info(this.hostIp+" 结束scp ");
		} catch (Exception e) {
			logger.error("", e);
		}
		latch.countDown();
	}


}
