
package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 下午03:11:02
 */
public class ControlBuild_tmp {
	
	private AppLaunchControl launch =null;
	
	private AppRunCheckControl runCheck = null;
	
	private IpTableControl ipTableControl = null;
	
	private ShellCommon common =null;
	
	private String targetip;
	
	private String userName = "xiaoxie";
	
	private String password = "xiaoxie123";
	
	private String ips;
	
	
	private String scriptid = null;
	
	
	public ControlBuild_tmp(String targetip,String appName,String userName,String password,String scriptid,String ips) throws IOException{
		this.targetip = targetip;
		this.scriptid = scriptid;
		this.userName = userName;
		this.password = password;
		this.ips = ips;
		common = new ShellCommon(this.targetip,this.userName,this.password);
		launch = new AppLaunchControl(common,"/home/admin/"+appName+"/bin/jbossctl ");
		runCheck = new AppRunCheckControl(this.scriptid);
		ipTableControl = new IpTableControl(common);
	}
	
	
	
	public String testDependent() throws IOException{
		
		if(!ipTableControl.recoverAll()){
			System.out.println("初始化 -- 清空iptable 设置失败");
			return "初始化 -- 清空iptable 设置失败";
		}
		
		if(!launch.restartApp()){
			System.out.println("初始化 -- 启动应用失败!");
			return "初始化 -- 启动应用失败!";
		}
		if(!launch.checkProcess()){
			System.out.println("初始化 -- 无法检查到应用进程!");
			return "初始化 -- 无法检查到应用进程!";
		}
		if(!runCheck.runCheck()){
			System.out.println("初始化 -- 功能脚本检查失败!");
			return "初始化 -- 功能脚本检查失败!"+runCheck.getResult();
		}
		if(!ipTableControl.forbidIp(this.ips.split(","))){
			System.out.println("运行检查 -- 禁止IP列表"+this.ips+"失败!");
			return "运行检查 -- 禁止IP列表"+this.ips+"失败!";
		}
		if(!launch.restartApp()){
			System.out.println("运行检查 -- 启动应用失败!");
			return "运行检查 -- 启动应用失败!";
		}
		if(!launch.checkProcess()){
			System.out.println("运行检查-- 无法检查到应用进程!");
			return "运行检查-- 无法检查到应用进程!";
		}
		if(!runCheck.runCheck()){
			System.out.println("运行检查-- 功能脚本检查失败!");
			return "运行检查-- 功能脚本检查失败!--"+runCheck.getResult();
		}
		return "检查-成功";
	}
	

}
