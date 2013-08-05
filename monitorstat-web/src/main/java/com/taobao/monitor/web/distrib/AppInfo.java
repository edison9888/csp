
package com.taobao.monitor.web.distrib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 
 * @author xiaodu
 * @version 2010-11-5 ÏÂÎç05:22:54
 */
public class AppInfo {
	private static final Logger logger =  Logger.getLogger(AppInfo.class);
	private String ip;
	
	private String siteName;
	
	private Pattern pattern = Pattern.compile("tcp\\s+\\d+\\s+\\d+\\s+\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)\\s+ESTABLISHED");
	private static Set<String> portType = new HashSet<String>();
	static {
		portType.add("12200");
		portType.add("5191");
		portType.add("2088");
		portType.add("9527");
	}
	
	private Map<String,DepIpInfo> depIpInfoMap = new HashMap<String, DepIpInfo>();
	
	public void putDepIpInfo(String ipPort,DepIpInfo info){
		depIpInfoMap.put(ipPort, info);
	}
	public Map<String, DepIpInfo> getDepIpInfoMap() {
		return depIpInfoMap;
	}






	public Set<String> getDepIpPortSet() throws IOException {
		Set<String> ipportSet = new HashSet<String>();
		try{
			logger.info("connect netstat :"+ip);
			Connection conn = new Connection(ip);
			conn.connect(null,5000,5000);
			boolean success = conn.authenticateWithPassword("nobody","look");
			if(success){
				Session sshSession  = conn.openSession();
				sshSession.execCommand("netstat -an");
				
				InputStream stdout = new StreamGobbler(sshSession.getStdout());			
				BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
				String line = null;
				
				while((line=br.readLine())!=null){
					Matcher matcher =  pattern.matcher(line);
					if(matcher.find()){
						String ipPort = matcher.group(1);
						String[] _ipPort = ipPort.split(":");
						if(_ipPort.length == 2 && portType.contains(_ipPort[1])){
							ipportSet.add(ipPort);
						}
					}
					
				}
				br.close();
				sshSession.close();
			}
			conn.close();
			logger.info("end connect netstat :"+ip);
		}catch (Exception e) {
			logger.error(ip+": ssh error");
		}
		return ipportSet;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	
	

}
