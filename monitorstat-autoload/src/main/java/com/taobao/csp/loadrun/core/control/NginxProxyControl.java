package com.taobao.csp.loadrun.core.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ch.ethz.ssh2.SCPClient;


public class NginxProxyControl extends BaseControl {
	
	public NginxProxyControl(String targetip, String userName, String password, String appUser) throws Exception {
		super(targetip, userName, password, appUser);
	}

	/***
	 * 从压测机器拉proxy配置文件，进行修改，生成临时配置文件
	 * @throws Exception
	 */
	@Override
	protected void createTempConfig() throws Exception {
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		String localPath = getAttribute(ControlAtrribute.LOCAL_PATH);
		String loadIp = getAttribute(ControlAtrribute.LOAD_IP);
		String tempFileName = getAttribute(ControlAtrribute.TEMP_FILE_NAME);
		
		File file = new File(localPath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		SCPClient scp = this.getTargetSSHConn().createSCPClient();
		scp.get(configPath, localPath);
		String fileName = configPath.split("/")[configPath.split("/").length - 1];
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			FileInputStream fr = new FileInputStream(localPath + fileName);  
			InputStreamReader ir = new InputStreamReader(fr, "UTF-8");
	        br = new BufferedReader(ir);  
	          
	        String s;  
	        StringBuilder sb = new StringBuilder();  
	        while((s = br.readLine())!=null){ 
	        	if (s.indexOf("proxy_pass") > -1 && !s.trim().startsWith("#")) {
	        		s = "proxy_pass    http://" + loadIp + ";";
	        	}
	        	
	            sb.append(s);  
	            sb.append("\n");  
	        }       
	        FileOutputStream fw = new FileOutputStream(localPath + tempFileName);
	        OutputStreamWriter ow = new OutputStreamWriter(fw, "UTF-8");
	        bw = new BufferedWriter(ow);  
	        bw.write(sb.toString());  
	        bw.flush(); 
		} finally {
        	if (br != null) br.close();
        	if (bw != null) bw.close();
        }
	}
	
	@Override
	protected String launchCommand() {
		String binPath = getAttribute(ControlAtrribute.BIN_PATH);
		
		if(!this.getAppUser().equals(this.getTargetUserName())){
			return "sudo " + this.getLauchUser() + binPath + " -s reload";
		} else {
			return binPath + " -s reload";
		}
	}
}
