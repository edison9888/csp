package com.taobao.csp.loadrun.core.control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ApacheProxyControl extends BaseControl {
	
	private static final String PROXY_CONF_NAME = "mod_proxy.conf";
	
	public ApacheProxyControl(String targetip,String userName, String password, String appUser) throws Exception {
		super(targetip, userName, password, appUser);
	}
	
	/***
	 * 从压测机器拉proxy配置文件，进行修改，生成临时配置文件
	 * @throws Exception
	 */
	@Override
	protected void createTempConfig() throws Exception {
		String localPath = getAttribute(ControlAtrribute.LOCAL_PATH);
		String loadIp = getAttribute(ControlAtrribute.LOAD_IP);
		String tempFileName = getAttribute(ControlAtrribute.TEMP_FILE_NAME);
		
		File file = new File(localPath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		BufferedWriter bw = null;
		try {
	        FileOutputStream fw = new FileOutputStream(localPath + tempFileName);
	        OutputStreamWriter ow = new OutputStreamWriter(fw, "UTF-8");
	        bw = new BufferedWriter(ow);  
	        bw.write("LoadModule proxy_module /opt/taobao/install/httpd/modules/mod_proxy.so \n");  
	        bw.write("LoadModule proxy_http_module /opt/taobao/install/httpd/modules/mod_proxy_http.so \n"); 
	        bw.write("LoadModule authz_host_module /opt/taobao/install/httpd/modules/mod_authz_host.so \n");
	        bw.write("ProxyRequests Off \n"); 
	        bw.write("<Proxy *> \n"); 
	        bw.write("Order deny,allow \n"); 
	        bw.write("Allow from all \n"); 
	        bw.write("</Proxy> \n"); 
	        String proxyTarget = "ProxyPass / http://" + loadIp + "/";
	        String proxyReverse = "ProxyPassReverse / http://" + loadIp + "/";
	        bw.write(proxyTarget + "\n");
	        bw.write(proxyReverse + "\n");
	        
	        bw.flush(); 
		} finally {
        	if (bw != null) bw.close();
        }
	}
	
	@Override
	protected List<String> generateControlCommands() {
		List<String> commands = new ArrayList<String>();
		
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		String tempFileName = getAttribute(ControlAtrribute.TEMP_FILE_NAME);
		
		if(!this.getAppUser().equals(this.getTargetUserName())){
			commands.add("sudo chmod 777 /tmp/" + tempFileName);
			commands.add("sudo -u " + this.getAppUser() + " cp -f /tmp/" + tempFileName + " " + configPath  + PROXY_CONF_NAME);
		} else {
			commands.add("chmod 777 /tmp/" + tempFileName);
			commands.add("cp -f /tmp/" + tempFileName + " " + configPath + PROXY_CONF_NAME);
		}
		
		if (launchCommand() != null) {
			commands.add(launchCommand());
		}
		return commands;
	}
	
	@Override
	protected List<String> generateResetCommands() {
		List<String> commands = new ArrayList<String>();
		
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		if(!this.getAppUser().equals(this.getTargetUserName())){
			commands.add("sudo -u " + this.getAppUser() + " rm " + configPath + PROXY_CONF_NAME);
		} else {
			commands.add("rm  " + configPath + PROXY_CONF_NAME);
		}
		
		if (launchCommand() != null) {
			commands.add(launchCommand());
		}
		return commands;
	}
	
	@Override
	protected String launchCommand() {
		String binPath = getAttribute(ControlAtrribute.BIN_PATH);
		
		if(!this.getAppUser().equals(this.getTargetUserName())){
			return "sudo " + this.getLauchUser() + binPath + " restart";
		} else {
			return binPath + " restart";
		}
	}

}
