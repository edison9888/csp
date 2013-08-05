
package com.taobao.monitor.dependent.control;

import java.io.IOException;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 ����09:21:20
 */
public class IpTableControl {
	
	private ShellCommon shell;
	
	public IpTableControl(ShellCommon shell){
		this.shell = shell;
	}
	
	/**
	 * ��ֹĿ������˿�
	 * @param port
	 */
	public boolean forbidPort(int port){
		try {
			shell.doCommon("sudo /sbin/iptables -A INPUT -p tcp --sport "+port+" -j DROP");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * ��ֹ���ʵ�ip�б�
	 * @param ips
	 * @return
	 */
	public boolean forbidIp(String[] ips){
		if(ips !=null){				
			
			String[] comms = new String[ips.length];
			int i=0;
			for(String ip:ips){
				comms[i]="sudo /sbin/iptables -A INPUT -s "+ip+" -j DROP";
				i++;
			}
			try {
				shell.doCommons(comms);
			} catch (IOException e) {
				return false;
			}
			
		}
		
		return true;
	}
	
	public boolean forbidIp(String ip){
		try {
			shell.doCommon("sudo /sbin/iptables -A INPUT -s "+ip+" -j DROP");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * �ָ��˿ڷ���
	 * @param port
	 * @return
	 */
	public boolean recoverPort(int port){
		try {
			shell.doCommon("sudo /sbin/iptables -D INPUT -p tcp --sport "+port+" -j DROP");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * �ָ�IPs ����
	 * @param ips
	 * @return
	 */
	public boolean recoverIps(String[] ips){
		
		if(ips !=null){
			String[] comms = new String[ips.length];
			int i=0;
			for(String ip:ips){
				comms[i]="sudo /sbin/iptables -D INPUT -s "+ip+" -j DROP";
				i++;
			}
			try {
				shell.doCommons(comms);
			} catch (IOException e) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * �������iptable������
	 * @return
	 */
	public boolean recoverAll(){
		String sql = "sudo /sbin/iptables -F";
		try {
			shell.doCommon(sql);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
