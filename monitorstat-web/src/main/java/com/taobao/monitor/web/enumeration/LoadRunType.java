
package com.taobao.monitor.web.enumeration;
/**
 * 
 * @author xiaodu
 * @version 2011-2-15 ����11:13:17
 */
public enum LoadRunType {
	
	apache(1,"apache_jk����"),
	tomcat(0,"httpd��־ѹ��"),
	hsf(2,"hsf-config����");
	
	private int type=-1;
	private String name;
	LoadRunType(int type,String name){
		this.type=type;
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getType(){
		return this.type;
	}
	

}
