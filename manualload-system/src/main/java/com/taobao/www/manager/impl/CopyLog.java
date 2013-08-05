package com.taobao.www.manager.impl;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @author shutong.dy
 * 
 */
public class CopyLog {

	
	private static final Logger logger = Logger
	.getLogger(CopyLog.class);
	
	private String hostIp;
	
	private boolean isAuthenticated;

	private Connection sshConn;

	public CopyLog(String hostIp, String userName, String password) throws IOException {
		initSshConnect(hostIp,userName,password);
	}


	public boolean initSshConnect(String hostIp, String userName, String password) throws IOException {
		if (sshConn != null) {
			sshConn.close();
		}
		this.sshConn = new Connection(hostIp);
		
		try {
			this.sshConn.connect();
			isAuthenticated = sshConn.authenticateWithPassword(userName, password);
			logger.info("connect success: %s ip:==="+hostIp+"== uname: =="+userName+" === pass:==="+password);
		} catch (IOException e) {
			logger.error("Authentication failed: %s"+hostIp, e);
		} finally {
			if (!isAuthenticated) {
				close();
			}
		}
		return isAuthenticated;
	}

	public void getRemoteFile(String remoteFile, String localFile) throws Exception {
		if (isAuthenticated) {
			OutputStream outStream = null;
			try {
				SCPClient client = sshConn.createSCPClient();
				outStream = new FileOutputStream(localFile);
				client.get(remoteFile, outStream);
			} catch (Exception e) {
				logger.error("copy log failed: %s"+hostIp,e);
			} finally {
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				close();
			}
		}
	}
	
	public void executeCommands(String  command)   {
		if (isAuthenticated) {
			Session session = null;
		    try {
				session = sshConn.openSession();
				session.execCommand(command);
			} catch (IOException e) {
				logger.error("execute command failed: %s"+command,e);
			}finally{
				closeSession(session);
				close();
			}
		}
	}
	
	public boolean getExecuteMachineName(String commod) {
        boolean result = false;		 
		if (isAuthenticated) {
			Session session = null;
		    try {
		    	session = sshConn.openSession();
				session.execCommand(commod);
				InputStream stdout = new StreamGobbler(session.getStdout());
				BufferedReader br = new BufferedReader(
						new InputStreamReader(stdout));
				while (true) {
					String tempString = br.readLine();
					if (tempString == null || tempString.equals("")){
						result = true;
						break;
					}
				}
			} catch (IOException e) {
				logger.error("execute command get loadavg  failed: %s"+commod,e);
			}finally{
				closeSession(session);
				close();
			}
		}
		return result;
	}

	
	public double getLoadRateInfo(String commod) {
		double result = 0.0;		 
        Pattern pattern = Pattern
		.compile("load average:\\s?([\\d\\.]+),\\s?([\\d\\.]+),\\s?([\\d\\.]+)");
		if (isAuthenticated) {
			Session session = null;
		    try {
		    	session = sshConn.openSession();
				session.execCommand(commod);
				InputStream stdout = new StreamGobbler(session.getStdout());
				BufferedReader br = new BufferedReader(
						new InputStreamReader(stdout));
				while (true) {
					String tempString = br.readLine();
					if (tempString != null && !tempString.equals("")){
						Matcher match = pattern.matcher(tempString);
						if (match.find()) {
							result = Double.parseDouble(match.group(1));
						}
					}
				}
			} catch (IOException e) {
				logger.error("execute command get loadavg  failed: %s"+commod,e);
			}finally{
				closeSession(session);
				close();
			}
		}
		return result;
	}
	
	
	
	

	public void getBeforeRemoteFile(String remoteFile, String localFile) throws Exception {
		if (isAuthenticated) {
			OutputStream outStream = null;
			try {
				SCPClient client = sshConn.createSCPClient();
				outStream = new FileOutputStream(localFile);
				client.get(remoteFile, outStream);
				logger.info("copy  log end: %s" + hostIp + "; file name"+ remoteFile);
			} catch (Exception e) {
				logger.error("copy  log failed: %s" + hostIp + "; file name"+ remoteFile,e);
			} finally {
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				close();
			}
		}
	}

	public void sendRemoteFile(String localFile, String remoteFile) throws Exception {
		if (isAuthenticated) {
			OutputStream outStream = null;
			try {
				SCPClient client = sshConn.createSCPClient();
				client.put(localFile, remoteFile);
			} catch (Exception e) {
				logger.error("copy log failed: %s " + hostIp, e);
			} finally {
				if (outStream != null) {
					try {
						outStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				close();
			}
		}
	}

	private void close() {
		if (sshConn != null) {
			sshConn.close();
		}
	}
	
	private void closeSession(Session session) {
		if (session  != null) {
			session.close();
		}
	}
}
