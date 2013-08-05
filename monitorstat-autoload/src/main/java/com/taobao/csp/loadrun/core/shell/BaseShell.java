
package com.taobao.csp.loadrun.core.shell;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;

/**
 * 
 * @author xiaodu
 * @version 2011-6-24 上午11:43:27
 */
public abstract class BaseShell {
	
	private static final Logger logger = Logger.getLogger(BaseShell.class);
	
	private String targetIp = null;
	
	private String targetUserName = null;
	
	private String targetPassword = null;
	
	private Connection targetSSHConn = null;
	
	public String getTargetIp() {
		return targetIp;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public String getTargetPassword() {
		return targetPassword;
	}

	public Connection getTargetSSHConn() {
		return targetSSHConn;
	}
	
	protected void close() {
		if(this.targetSSHConn!=null){
			this.targetSSHConn.close();
			logger.info("关闭 ssh2 连接 "+this.targetIp+" "+this.targetUserName+"/"+this.targetPassword);
		}
	}
	
	public  BaseShell(String targetIp,String user,String pwd){
		this.targetIp = targetIp.trim();
		this.targetUserName = user.trim();
		this.targetPassword = pwd.trim();
	}
	
	
	/**
	 * 初始化连接
	 * @throws Exception
	 */
	protected void initSshConnect() throws Exception{		
		if(this.targetSSHConn!=null){
			this.targetSSHConn.close();
		}		
		if(this.targetIp==null){
			throw new Exception("无远程机器IP");			
		}
		if(this.targetPassword==null||this.targetUserName==null){
			throw new Exception(this.targetIp+":无登陆名或密码");			
		}		
		this.targetSSHConn = new Connection(this.targetIp);	
		
		
		
		this.targetSSHConn.connect(null,10000,10000);
		boolean isAuthenticated = this.targetSSHConn.authenticateWithPassword(this.targetUserName,this.targetPassword);
		
		// 文件登录的暂时去掉
//		if(!isAuthenticated){
//			 File keyfile = new File("/home/admin/.ssh/id_rsa");
//			 isAuthenticated = this.targetSSHConn.authenticateWithPublicKey("admin",keyfile,"nopsw");
//		}
		
		
		if (isAuthenticated == false){
			throw new Exception(this.targetUserName + " 无法登陆到 "+this.targetIp);
		}
		
		logger.info("初始化 ssh2 连接 "+this.targetIp+" "+this.targetUserName+"/"+this.targetPassword);
		
	}
}
