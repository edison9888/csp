
package com.taobao.monitor.web.jprof;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import com.taobao.monitor.web.core.po.JprofHost;





import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;


/**
 * 
 * @author xiaodu
 * @version 2010-4-26 上午10:10:13
 */
public class ScpAccept {
	
	private static final Logger logger =  Logger.getLogger(ScpAccept.class);
	
	private String sshUserName;
	
	private String sshPassword;
	
	private String hostIP;
	private Connection sshConn;  
	
	public ScpAccept(JprofHost info) throws Exception{
		this.hostIP = info.getHostIp();
		this.sshPassword = info.getHostPasswd();
		this.sshUserName = info.getHostUser();
		initSshConnect();
	}
	
	
	private void initSshConnect() throws Exception{
		if(this.sshConn!=null){
			this.sshConn.close();
		}
		if(this.hostIP==null){
			throw new Exception("无远程机器IP");			
		}
		if(this.sshPassword==null||this.sshUserName==null){
			throw new Exception(this.hostIP+":无登陆名或密码");			
		}
		
		this.sshConn = new Connection(this.hostIP);
		
		this.sshConn.connect();
		boolean isAuthenticated = this.sshConn.authenticateWithPassword(this.sshUserName,this.sshPassword);
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		logger.info("创建ssh Connection :"+this.hostIP);
	}
	
	
	 public void getRemoteFile(String remoteFile,String localFile) throws IOException{
		 try{
			 createLocalDir(localFile);
			 
			 SCPClient client = this.sshConn.createSCPClient();		 	 
			 OutputStream out = new FileOutputStream(localFile);
			 client.get(remoteFile, out);
		 }finally{
			 close();
		 }
		 
	 }
	 
	 
	 private void close(){
		 
		 if(sshConn!=null){
			 sshConn.close();
		 }
		 
	 }
	 
	 /**
	  * 创建本地 目录
	  * @param localFile
	  */
	 private void createLocalDir(String localFile){
		 File file = new File(localFile);
		 if(file.exists()){
			 file.delete();
		 }else{
			 String path = file.getParent();
			 File filepath = new File(path);
			 if(!filepath.exists()){
				 filepath.mkdirs();
			 }else{				 
				 if(!filepath.isDirectory()){
					 file.delete();
					 filepath.mkdirs();
				 }
				 
			 }			 
		 }		 
	 }


	
	
	
	
	

}
