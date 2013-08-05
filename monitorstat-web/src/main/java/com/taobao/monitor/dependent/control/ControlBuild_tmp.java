
package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 ����03:11:02
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
			System.out.println("��ʼ�� -- ���iptable ����ʧ��");
			return "��ʼ�� -- ���iptable ����ʧ��";
		}
		
		if(!launch.restartApp()){
			System.out.println("��ʼ�� -- ����Ӧ��ʧ��!");
			return "��ʼ�� -- ����Ӧ��ʧ��!";
		}
		if(!launch.checkProcess()){
			System.out.println("��ʼ�� -- �޷���鵽Ӧ�ý���!");
			return "��ʼ�� -- �޷���鵽Ӧ�ý���!";
		}
		if(!runCheck.runCheck()){
			System.out.println("��ʼ�� -- ���ܽű����ʧ��!");
			return "��ʼ�� -- ���ܽű����ʧ��!"+runCheck.getResult();
		}
		if(!ipTableControl.forbidIp(this.ips.split(","))){
			System.out.println("���м�� -- ��ֹIP�б�"+this.ips+"ʧ��!");
			return "���м�� -- ��ֹIP�б�"+this.ips+"ʧ��!";
		}
		if(!launch.restartApp()){
			System.out.println("���м�� -- ����Ӧ��ʧ��!");
			return "���м�� -- ����Ӧ��ʧ��!";
		}
		if(!launch.checkProcess()){
			System.out.println("���м��-- �޷���鵽Ӧ�ý���!");
			return "���м��-- �޷���鵽Ӧ�ý���!";
		}
		if(!runCheck.runCheck()){
			System.out.println("���м��-- ���ܽű����ʧ��!");
			return "���м��-- ���ܽű����ʧ��!--"+runCheck.getResult();
		}
		return "���-�ɹ�";
	}
	

}
