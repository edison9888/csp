package com.taobao.monitor.web.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.taobao.monitor.web.vo.HostConfigPo;

public class SshFetchSystemInfo {
	private static final Logger logger = Logger.getLogger(SshFetchSystemInfo.class);

	public static SshFetchSystemInfo instance = new SshFetchSystemInfo();

	private SshFetchSystemInfo() {
	}

	private String hostname = "hostname";
	private String hostnamei = "hostname -i";
	private String cpunumber = "cat /proc/cpuinfo | grep name | cut -f2 -d: |wc -l";
	private String cpu = "cat /proc/cpuinfo | grep name | cut -f2 -d: |uniq |sed 's/[[:space:]]\\{2,\\}/ /g'";
	private String processorPlatform = "uname -i";
	private String memorySize = "cat /proc/meminfo |grep MemTotal |cut -d':' -f2 |sed 's/[[:space:]]\\{2,\\}//g'";
	private String javaversion = "ls -l /opt/taobao/java | sed -e 's/^.*\\/jdk-\\?//'";
	private String jbossVersion = "ls -l /opt/taobao/jboss | sed -e 's/^.*\\/jboss-\\?//'";
	private String apacheVersion = "/opt/taobao/install/httpd/bin/httpd -v |grep version |sed -e 's/^.*\\///' -e 's/ .*$//'";
	private String osVersion = "cat /etc/issue |head -n1";
	private String diskhome = "df -h | grep /home";
	private String uptime = "uptime";
	private String bit = "getconf LONG_BIT";
	private String jvm = "ps -ef|grep '/opt/taobao/java/bin/java -D' |grep -server";
	
	private Connection targetSSHConn = null;

	/**
	 * 初始化连接
	 * 
	 * @throws Exception
	 */
	private void initSshConnect(String ip, String username, String password) throws Exception {
		this.targetSSHConn = new Connection(ip);

		this.targetSSHConn.connect(null, 10000, 10000);
		boolean isAuthenticated = this.targetSSHConn.authenticateWithPassword(username, password);

		if (!isAuthenticated) {
			File keyfile = new File("/home/admin/.ssh/id_rsa");
			isAuthenticated = this.targetSSHConn.authenticateWithPublicKey("admin", keyfile, "nopsw");
		}
		if (isAuthenticated == false) {
			throw new Exception(username + "/" + password + " 无法登陆到 " + ip);
		}
		logger.debug("========初始化 ssh2 连接");
	}

	public void fetch(HostConfigPo configPo, String ip, String username, String password) {
		try {
			initSshConnect(ip, username, password);
			if (targetSSHConn != null) {
				configPo.setDomainIp(executeaCommand(hostname));
				configPo.setDomainIp(configPo.getDomainIp() + "_" + executeaCommand(hostnamei));
				configPo.setCpu(executeaCommand(cpu));
				configPo.setCpu(configPo.getCpu() + " * " + executeaCommand(cpunumber));
				configPo.setPlatform(executeaCommand(processorPlatform));
				configPo.setPlatform(configPo.getPlatform() + " " + executeaCommand(bit) + "bit");
				configPo.setMemory(executeaCommand(memorySize));
				configPo.setJavaVersion(executeaCommand(javaversion));
				configPo.setJbossVersion(executeaCommand(jbossVersion));
				configPo.setApacheVersion(executeaCommand(apacheVersion));
				configPo.setOsVersion(executeaCommand(osVersion));
				configPo.setDiskHome(executeaCommand(diskhome));
				configPo.setUptime(executeaCommand(uptime));
				String jvmParameter = executeaCommand(jvm);
				// FIXME
				logger.info("========从服务器取回的JVM参数：" + jvmParameter);
				if (jvmParameter != null && !jvmParameter.isEmpty()) {
					if (jvmParameter.length() < 100) {
						logger.info("========ip " + ip + " username "
								+ username + " password " + password
								+ " jvmParameter " + jvmParameter);
					}
					int begin = jvmParameter.indexOf("-server");
					configPo.setJvmParameters(jvmParameter.substring(begin, begin + Math.min(jvmParameter.length() - begin, 200)));
					logger.info("========经过截断的JVM参数：" + configPo.getJvmParameters());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnect();
		}
	}

	/**
	 * @param configPo
	 * @throws IOException
	 */
	private String executeaCommand(String parameter){
		Session session = null;
		InputStream stdout = null;
		BufferedReader br = null;
		String line = null;
		String result = null;
		try {
			session = targetSSHConn.openSession();
			session.execCommand(parameter);
			// 机器域名
			stdout = new StreamGobbler(session.getStdout());
			br = new BufferedReader(new InputStreamReader(stdout));
			while ((line = br.readLine()) != null) {
				result = line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
			try {
				if (stdout != null)
					stdout.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private void closeConnect() {
		if (this.targetSSHConn != null) {
			this.targetSSHConn.close();
			logger.debug("========关闭 ssh2 连接 ");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ip = "10.232.14.165";
		String username = "admin";
		String password = "SZuce02o@";
		HostConfigPo po = new HostConfigPo();
		instance.fetch(po, ip, username, password);
		System.out.println(po);
	}

}
