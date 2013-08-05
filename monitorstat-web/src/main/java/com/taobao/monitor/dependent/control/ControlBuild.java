
package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 ����03:11:02
 */
public class ControlBuild {
	
	private AppLaunchControl launch =null;
	
	private AppRunCheckControl runCheck = null;
	
	private IpTableControl ipTableControl = null;
	
	private PortAppDependentCollect collect = null;
	
	private ShellCommon common =null;
	
	private String ip;
	
	private String userName = "xiaoxie";
	
	private String password = "xiaoxie123";
	
	private String launchpath = null;
	
	private String scriptUrl = null;
	
	
	public ControlBuild(String ip,String lauch,String scriptUrl) throws IOException{
		this.ip = ip;
		this.launchpath = lauch;
		this.scriptUrl = scriptUrl;
		common = new ShellCommon(ip,userName,password);
		collect = new PortAppDependentCollect(common);
		launch = new AppLaunchControl(common,launchpath);
		runCheck = new AppRunCheckControl(this.scriptUrl);
		ipTableControl = new IpTableControl(common);
	}
	
	
	

	
	
	
	public void testDependent(String targetAppname) throws IOException{
		
		
		
		boolean b = false;
		System.out.println("��鵱ǰӦ��״̬");
		boolean g = launch.checkProcess();
		if(g){
			System.out.println("Ӧ�ý�������>>>");
			System.out.println("����ָֹͣ��>>>");
			b = launch.stopApp();			
			g = launch.checkProcess();
			if(g){
				System.out.println("�޷�ֹͣӦ��>>> ����ֹͣ");
				return;
			}
		}
		System.out.println("ִ��iptable ���");
		b = ipTableControl.recoverAll();
		if(!b){
			System.out.println("IP table ���ʧ��>>> ����ֹͣ");
			return;
		}
		System.out.println("��������Ӧ��ָ��>>>");
		b = launch.startApp();
		if(!b){
			System.out.println("����Ӧ��ʧ��>>>����ֹͣ");
			return;
		}
		System.out.println("��鵱ǰӦ��״̬");
		g = launch.checkProcess();
		if(g){
			System.out.println("Ӧ�ý��̼������>>>");
		}else{
			System.out.println("����Ӧ��ʧ��>>>����ֹͣ");
			return;
		}
		System.out.println("���ܼ��");
		b = runCheck.runCheck();
		if(b){
			System.out.println("���ܼ������>>>");
		}else{
			System.out.println("���ܼ��ʧ��>>>����ֹͣ");
			return;
		}
		System.out.println("���˿ڣ���ȡ��Ҫ��ֹ��Ӧ��IP>>>");
		
		List<String> list= collect.getAppDependentIp(targetAppname);
		if(list==null||list.size()==0){
			System.out.println("�޶�Ӧ��IP >>>����ֹͣ");
			return;
		}
		System.out.println(targetAppname+":");
		for(String ip:list){
			System.out.println(ip);
		}
		System.out.println("ֹͣӦ��");
		g = launch.stopApp();
		if(g){			
			g = launch.checkProcess();
			if(g){
				System.out.println("�޷�ֹͣӦ��>>> ����ֹͣ");
				return;
			}
		}
		
		for(String ip:list){
			b = ipTableControl.forbidIp(ip);
			System.out.println("ip table ���� ��"+ip);
		}
		
		launch.startApp();
		System.out.println("����Ӧ��>>>");
		g = launch.checkProcess();
		if(g){
			System.out.println("Ӧ�ý��̼������>>>");
		}else{
			System.out.println("����Ӧ��ʧ��>>>����ֹͣ");
			return;
		}
		b = runCheck.runCheck();
		
		if(b){
			System.out.println("Ӧ�ü��ͨ��������");
		}else{
			System.out.println("Ӧ�ü��ʧ��");
		}
		
		
	}
	

}
