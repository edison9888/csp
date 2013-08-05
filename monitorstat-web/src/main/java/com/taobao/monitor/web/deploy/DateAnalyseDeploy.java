
package com.taobao.monitor.web.deploy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;


import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.common.po.AppInfoPo;



import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;

/**
 * 
 * @author xiaodu
 * @version 2010-5-21 下午02:13:48
 */
public class DateAnalyseDeploy {
	
	private static final Logger logger =  Logger.getLogger(DateAnalyseDeploy.class);
	
	private DeployHost deployHost = null;
	private Connection connection = null;
	
//	private JspWriter jspWriter;
	
	
	private Object lock = new Object();
	
	
	
	public DeployHost getDeployHost() {
		return deployHost;
	}
	public void setDeployHost(DeployHost deployHost) {
		this.deployHost = deployHost;
	}
	public DateAnalyseDeploy(DeployHost deployHost) throws IOException{
		this.deployHost = 	deployHost;	
		init();
	}
	
	private void init() throws IOException{		
		String ip = this.getDeployHost().getIp();
		String userName = this.getDeployHost().getUserName();
		String password = this.getDeployHost().getPassword();
		connection = new Connection(ip);
		connection.connect();
		boolean isAuthenticated = connection.authenticateWithPassword(userName,password);
		if (isAuthenticated == false)
			throw new IOException(ip+" Authentication failed.");
		
		
		
		
	}
	
	
	private boolean checkprocess() throws IOException{
		String command = "ps -ef|grep com.taobao.monitor.stat.Main |grep -v grep";
		CommandResult  result = new CommandResult(connection,command);
		result.doCommon();
		String msg = result.getSuccessMsg();			
		if(msg.indexOf("com.taobao.monitor.stat.Main")>-1){
			printInfo(msg);
			return true;
		}else{
			return false;
		}
		
		
	}
	
	private boolean killprocess() throws IOException{
		String command = ("ps -ef|grep com.taobao.monitor.stat.Main |grep -v grep |awk '{print $2}'");
		CommandResult  result = new CommandResult(connection,command);
		result.doCommon();
		String pid = result.getSuccessMsg();
		int pidNum = 0;
		try{
			pidNum = Integer.parseInt(pid);
		}catch (Exception e) {
		}
		if(pidNum==0){
			printInfo("无法取得进程Pid...");
			return false;
		}else{
			printInfo("取得进程Pid:"+pid);
			printInfo("执行kill -9 "+pid);			
			CommandResult  r = new CommandResult(connection,"kill -9 "+pid);
			r.doCommon();			
			return true;
		}
		
	}
	
	
	public void getHostMsg() throws IOException{
		
		printInfo(this.getDeployHost().getIp()+": 运行进程为=======================");
		{
		String command = "ps -ef|grep com.taobao.monitor.stat.Main |grep -v grep";
		CommandResult  result = new CommandResult(connection,command);
		result.doCommon();
		String msg = result.getSuccessMsg();
		printInfo(msg);
		}
		printInfo(this.getDeployHost().getIp()+": 在/home/admin/logs/ 目录下含有目录为:=========================");
		{
		String command = "ls /home/admin/logs/";
		CommandResult  result = new CommandResult(connection,command);
		result.doCommon();
		String msg = result.getSuccessMsg();
		printInfo(msg);
		}
		
	}
	
	
	
	public void startup() throws IOException{		
		printInfo("检查机器"+deployHost.getIp()+" 是否存在应用进程....");
		for(;checkprocess();){
			if(!killprocess()){
				printInfo("无法kill 进程 ...");
				return ;
			}
			
			try {
				synchronized (lock) {
					this.wait(3000);
				}				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		//find /home/admin/logs/ -name '*' | xargs rm -rf '*'
		printInfo("删除 /home/admin/logs/ 下面的所有文件   rm -rf /home/admin/logs/*");
		{
			CommandResult res = new CommandResult(connection,"rm -rf /home/admin/logs/*");
			res.doCommon();
		}
		List<AppInfoPo> appInfoPoList = deployHost.getAppList();
		for(AppInfoPo po:appInfoPoList){
			printInfo("mkdir /home/admin/logs/"+po.getAppName());
			CommandResult com = new CommandResult(connection,"mkdir /home/admin/logs/"+po.getAppName());
			com.doCommon();			
		}
		
		{
			String jarFileName = scpJar();		
			printInfo("/home/admin/jdk/jdk1.6.0_10/bin/jar -xvf /home/admin/analyse/"+jarFileName);
			CommandResult com = new CommandResult(connection,"/home/admin/jdk/jdk1.6.0_10/bin/jar -xvf /home/admin/analyse/"+jarFileName);
			com.doCommon();
			printInfo(com.getSuccessMsg());
		}
		{
			printInfo("nohup /home/admin/jdk/jdk1.6.0_10/bin/java -cp /home/admin/analyse/lib/*:/home/admin/analyse/ com.taobao.monitor.stat.Main &");
			CommandResult com = new CommandResult(connection,"nohup /home/admin/jdk/jdk1.6.0_10/bin/java -cp /home/admin/analyse/lib/*:/home/admin/analyse/ com.taobao.monitor.stat.Main &");
			com.doCommon();
			printInfo(com.getSuccessMsg());
		}
		
		printInfo("发布结束!");		
		
	}
	
	
	
	
	private String scpJar() throws IOException{
		Session sess = connection.openSession();
		String localJarPath = deployHost.getJarpath();
		try{
			if(sess!=null){				
				SCPClient client = connection.createSCPClient();
				
				
				client.put(localJarPath, "/home/admin/analyse/");
				
				printInfo("scp "+localJarPath+" to /home/admin/analyse/");
				
			}else{
				throw new IOException("ssh session can not create");
			}
		}catch(Exception e){
			printInfo("scp "+localJarPath+" to /home/admin/analyse/",e);
		}
		sess.close();
		
		return Utlitites.getFileName(localJarPath);
		
	}
	
	
	
	
	private class CommandResult{
		private String command;
		final StringBuffer successSb = new StringBuffer();
		final StringBuffer errorSb = new StringBuffer();
		private Connection conn;
		public CommandResult(Connection conn,String command){
			this.command = command;
			this.conn = conn;
		}
		
		
		public void doCommon() throws IOException{			
			final CountDownLatch latch = new CountDownLatch(2);
			Session session = conn.openSession();
			try{
				session.execCommand(command);				
				final InputStream input = session.getStdout();				
				new Thread(){
					
					public void run() {
						try{
							String str = null;
							BufferedReader reader = new BufferedReader(new InputStreamReader(input));
							while((str=reader.readLine())!=null){
								successSb.append(str);
								successSb.append('\n');
							}
						}catch (Exception e) {
							
						}finally{
							latch.countDown();
						}
					}
					
				}.start();
				
				
				final InputStream inputerr = session.getStderr();
				new Thread(){
					
					public void run() {
						try{
							String str = null;
							BufferedReader reader = new BufferedReader(new InputStreamReader(inputerr));
							while((str=reader.readLine())!=null){
								errorSb.append(str);
							}
						}catch (Exception e) {
							
						}finally{
							latch.countDown();
						}
					}
					
				}.start();
				try{
					latch.await();
				}catch (Exception e) {
				}			
				
			}finally{
				session.close();
			}
		}
		
		
		public String getSuccessMsg(){
			return successSb.toString();
		}
		
		public String getErrorMsg(){
			return errorSb.toString();
		}
		
		
	}
	
	
	
	private void printInfo(String msg){
		logger.info(msg);
		
//		if(jspWriter!=null){
//			try {
//				jspWriter.println(msg);
//			} catch (IOException e) {
//			}
//		}
		
	}
	private void printInfo(String msg,Exception e){
		logger.info(msg,e);
//		if(jspWriter!=null){
//			try {
//				jspWriter.println(msg);
//			} catch (IOException e1) {
//			}
//		}
	}
//	public JspWriter getJspWriter() {
//		return jspWriter;
//	}
//	public void setJspWriter(JspWriter jspWriter) {
//		this.jspWriter = jspWriter;
//	}

	

}
