
package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 下午03:11:02
 */
public class ControlBuild {
	
	private AppLaunchControl launch =null;
	
	private AppRunCheckControl runCheck = null;
	
	private IpTableControl ipTableControl = null;
	
	private PortAppDependentCollect collect = null;
	
	private ShellCommon common =null;
	
	private String ip;
	
	private String userName = "xiaoxie";
	
	private String password = "xiaoxie123";
	
	private String launchpath = null;
	
	private String scriptUrl = null;
	
	
	public ControlBuild(String ip,String lauch,String scriptUrl) throws IOException{
		this.ip = ip;
		this.launchpath = lauch;
		this.scriptUrl = scriptUrl;
		common = new ShellCommon(ip,userName,password);
		collect = new PortAppDependentCollect(common);
		launch = new AppLaunchControl(common,launchpath);
		runCheck = new AppRunCheckControl(this.scriptUrl);
		ipTableControl = new IpTableControl(common);
	}
	
	
	

	
	
	
	public void testDependent(String targetAppname) throws IOException{
		
		
		
		boolean b = false;
		System.out.println("检查当前应用状态");
		boolean g = launch.checkProcess();
		if(g){
			System.out.println("应用进程正常>>>");
			System.out.println("发送停止指令>>>");
			b = launch.stopApp();			
			g = launch.checkProcess();
			if(g){
				System.out.println("无法停止应用>>> 流程停止");
				return;
			}
		}
		System.out.println("执行iptable 清空");
		b = ipTableControl.recoverAll();
		if(!b){
			System.out.println("IP table 清空失败>>> 流程停止");
			return;
		}
		System.out.println("发送启动应用指令>>>");
		b = launch.startApp();
		if(!b){
			System.out.println("启动应用失败>>>流程停止");
			return;
		}
		System.out.println("检查当前应用状态");
		g = launch.checkProcess();
		if(g){
			System.out.println("应用进程检查正常>>>");
		}else{
			System.out.println("启动应用失败>>>流程停止");
			return;
		}
		System.out.println("功能检查");
		b = runCheck.runCheck();
		if(b){
			System.out.println("功能检查正常>>>");
		}else{
			System.out.println("功能检查失败>>>流程停止");
			return;
		}
		System.out.println("检查端口，获取需要禁止的应用IP>>>");
		
		List<String> list= collect.getAppDependentIp(targetAppname);
		if(list==null||list.size()==0){
			System.out.println("无对应的IP >>>流程停止");
			return;
		}
		System.out.println(targetAppname+":");
		for(String ip:list){
			System.out.println(ip);
		}
		System.out.println("停止应用");
		g = launch.stopApp();
		if(g){			
			g = launch.checkProcess();
			if(g){
				System.out.println("无法停止应用>>> 流程停止");
				return;
			}
		}
		
		for(String ip:list){
			b = ipTableControl.forbidIp(ip);
			System.out.println("ip table 屏蔽 ："+ip);
		}
		
		launch.startApp();
		System.out.println("启动应用>>>");
		g = launch.checkProcess();
		if(g){
			System.out.println("应用进程检查正常>>>");
		}else{
			System.out.println("启动应用失败>>>流程停止");
			return;
		}
		b = runCheck.runCheck();
		
		if(b){
			System.out.println("应用检查通过。。。");
		}else{
			System.out.println("应用检查失败");
		}
		
		
	}
	

}
