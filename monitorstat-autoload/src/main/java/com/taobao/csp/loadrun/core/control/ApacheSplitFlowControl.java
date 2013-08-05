package com.taobao.csp.loadrun.core.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ch.ethz.ssh2.SCPClient;

public class ApacheSplitFlowControl extends BaseControl {
	
	public ApacheSplitFlowControl(String targetip, String userName, String password, String appUser) throws Exception {
		super(targetip, userName, password, appUser);
	}
	
	/***
	 * 从压测机器拉jk配置文件，进行修改，生成临时配置文件
	 * @throws Exception
	 */
	@Override
	protected void createTempConfig() throws Exception {
		String configPath = getAttribute(ControlAtrribute.CONFIG_PATH);
		String localPath = getAttribute(ControlAtrribute.LOCAL_PATH);
		String ratioLocal = getAttribute(ControlAtrribute.RATIO_LOCAL);
		String ratioTarget = getAttribute(ControlAtrribute.RATIO_TARGET);
		String loadIp = getAttribute(ControlAtrribute.LOAD_IP);
		String tempFileName = getAttribute(ControlAtrribute.TEMP_FILE_NAME);
		
		File file = new File(localPath);
		if(!file.exists()){
			file.mkdirs();
		}
		
		SCPClient scp = this.getTargetSSHConn().createSCPClient();
		scp.get(configPath, localPath);
		String fileName = configPath.split("/")[configPath.split("/").length - 1];
		
		boolean isSplitFlow = false;
		
		int appenderFlag = 0;
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			FileInputStream fr = new FileInputStream(localPath + fileName);  
			InputStreamReader ir = new InputStreamReader(fr, "UTF-8");
	        br = new BufferedReader(ir);  
	          
	        String s;  
	        String appender = "";
	        StringBuilder sb = new StringBuilder();  
	        while((s = br.readLine())!=null){ 
	        	if (s.indexOf("worker.list") > -1) {
	        		appenderFlag = 1;
	        		if (s.indexOf("lb") > -1) {
	        			isSplitFlow = true;
	        		} else {
	        			isSplitFlow = false;
	        			s = "JkWorkerProperty worker.list=local,target,lb";
	        		}
	        	}
	        	
	        	// 已经是分流,修改local与target的配置,因为会压多次所以会出现这种情况
	        	if (isSplitFlow && s.indexOf("worker.list") == -1) {
	        		if (s.indexOf("JkWorkerProperty") > -1 && s.indexOf("local") > -1 && s.indexOf("host") > -1) {
	        			s = "JkWorkerProperty worker.local.host=localhost";
	        		} else if (s.indexOf("JkWorkerProperty") > -1 && s.indexOf("target") > -1 && s.indexOf("host") > -1) {
		        		s = "JkWorkerProperty worker.target.host=" + loadIp;
		        	} else if (s.indexOf("JkWorkerProperty") > -1 && s.indexOf("local") > -1 && s.indexOf("lbfactor") > -1) {
		        		s = "JkWorkerProperty worker.local.lbfactor=" + ratioLocal;
		        	} else if (s.indexOf("JkWorkerProperty") > -1 && s.indexOf("target") > -1 && s.indexOf("lbfactor") > -1) {
		        		s = "JkWorkerProperty worker.target.lbfactor=" + ratioTarget;
		        	}
	        	} 
	        	
	        	// 不是分流修改local配置，并构造appender作为target
	        	if (!isSplitFlow && s.indexOf("worker.list") == -1) {
	        		String tmp = s;
	        		if (s.indexOf("JkWorkerProperty") > -1 && s.indexOf("local") > -1 && s.indexOf("host") > -1) {
	        			s = "JkWorkerProperty worker.local.host=localhost";
	        			s += "\n";
		        		s += "JkWorkerProperty worker.local.lbfactor=" + ratioLocal;
		        		
		        		tmp = "JkWorkerProperty worker.target.host=" + loadIp;
		        		tmp += "\n";
		        		tmp += "JkWorkerProperty worker.target.lbfactor=" + ratioTarget;
		        		appender += tmp;
		        		appender += "\n";
	        		}  else if (s.indexOf("JkWorkerProperty") > -1 && s.indexOf("local") > -1 && s.indexOf("host") == -1) {
		        		tmp = tmp.replaceAll("local", "target");
		        		appender += tmp;
		        		appender += "\n";
	        		} else if (s.indexOf("JkMount") > -1) {
	        			s = s.replaceAll("local", "lb");
	        		}
	        	}
	        	
	        	// 不是分流的情况需要加上appender的target部分
	        	if (!isSplitFlow && appenderFlag == 1 && s.trim().length() != 0 && s.indexOf("JkWorkerProperty") == -1) {
	        		appender = appender + "\n" + "JkWorkerProperty worker.lb.type=lb" + "\n";
	        		appender = appender + "JkWorkerProperty worker.lb.balance_workers=local,target";
	        		s = appender + "\n\n" + s;
	        		appenderFlag = 0;
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
			return "sudo " + this.getLauchUser() + binPath + " restart";
		} else {
			return binPath + " restart";
		}
	}

}
