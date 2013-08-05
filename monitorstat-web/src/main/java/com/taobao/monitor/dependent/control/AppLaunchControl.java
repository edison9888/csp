
package com.taobao.monitor.dependent.control;

import java.io.IOException;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 ионГ09:33:28
 */
public class AppLaunchControl {
	
	private String binPath;
	
	
	private ShellCommon shell = null;
	
	public AppLaunchControl(ShellCommon shell,String bin){
		this.binPath = bin;
		this.shell = shell;
	}
	
	
	
	public boolean checkProcess() throws IOException{

		String ps = "ps -e -o lstart,cmd |grep -w java|grep -v grep|grep 'jboss.server.home.dir\\=/home/admin/\\w*/.default'";
		String str = shell.doCommon(ps);
		
		if(str ==null){
			return false;
		}
		
		
		if(str.indexOf("/opt/taobao/java/bin/java")<0){
			return false;
		}
		
		
		return true;
		
	}
	
	
	
	public boolean restartApp(){
		try {
			shell.doCommon("sudo -u admin "+binPath+" restart");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean stopApp(){
		try {
			shell.doCommon("sudo -u admin "+binPath+" stop");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean startApp(){
		try {
			shell.doCommon(""+binPath+" restart");
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
