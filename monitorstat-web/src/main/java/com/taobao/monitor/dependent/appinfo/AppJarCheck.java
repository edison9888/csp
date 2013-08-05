
package com.taobao.monitor.dependent.appinfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.taobao.monitor.common.util.RemoteCommonUtil;
import com.taobao.monitor.common.util.RemoteCommonUtil.CallBack;

/**
 * 
 * @author xiaodu
 * @version 2011-5-3 ÏÂÎç01:53:59
 */
public class AppJarCheck {
	
	private String targetIp;
	
	final private StringBuffer psJbossString = new StringBuffer();
	
	final private StringBuffer psHttpdString = new StringBuffer();
	
	final private StringBuffer appPath = new StringBuffer();
	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/d",Locale.ENGLISH);
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
	
	public AppJarCheck(String ip){
		this.targetIp = ip;
	}
	
	private String doPsJboss() throws Exception{
		
		if(psJbossString.length() == 0){
			RemoteCommonUtil.excute(this.targetIp, "ps -e -o lstart,cmd |grep -w java|grep -v grep|grep 'jboss.server.home.dir\\=/home/admin/\\w*/.default'", new CallBack(){
				public void doLine(String line) {
					psJbossString.append(line);
					psJbossString.append("\n");
				}
				
			});
			return psJbossString.toString();
		}
		return psJbossString.toString();
	}
	
	private String doPsHttpd()throws Exception{
		if(psHttpdString.length() == 0){
			RemoteCommonUtil.excute(this.targetIp, "ps -e -o lstart,cmd |grep -w 'httpd'|grep -v grep", new CallBack(){
				public void doLine(String line) {
					psJbossString.append(line);
					psJbossString.append("\n");
				}
				
			});
			return psJbossString.toString();
		}
		return psHttpdString.toString();
	}
	
	
	
	public String getAppName() throws Exception{
		String ps = doPsJboss();
		Pattern pattern = Pattern.compile("jboss.server.home.dir=/home/admin/(\\w*)/.default");
		
		Matcher m = pattern.matcher(ps);
		if(m.find()){
			return m.group(1);
		}
		return null;
	}
	
	private String getTime(String p) throws ParseException{
		String[] tmp = StringUtils.split(p," ");
		
		String w = tmp[0];
		String m = tmp[1];
		String d = tmp[2];
		String time = tmp[3];
		String y = tmp[4];
		String cal = y+"/"+m+"/"+d;
		
		return sdf1.format(sdf.parse(cal))+" "+time;	
	}
	
	public String getJbossStartTime() throws Exception{
		String ps = doPsJboss();
		
		return getTime(ps);
	}
	
	
	public String getAppPath() throws Exception{
		if(appPath.length() == 0){
			RemoteCommonUtil.excute(this.targetIp, "find /home/admin/"+getAppName()+"/.default/deploy/ -regex '.*login\\.\\(\\(ear\\)\\|\\(war\\)\\)'", new CallBack(){
				public void doLine(String line) {
					appPath.append(line);
				}
				
			});
			return appPath.toString();
		}
		return appPath.toString();
	}
	
	
	public String getApacheStartTime() throws Exception{
		String ps = doPsHttpd();
		String[] httpds = StringUtils.split(ps,"\n");
		StringBuffer sb = new StringBuffer();
		for(String httpd:httpds){
			
			sb.append(getTime(httpd));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getWebxXml() throws Exception{
		
		final StringBuffer sb = new StringBuffer();
		RemoteCommonUtil.excute(this.targetIp, "cat "+getAppPath()+"/WEB-INF/webx.xml", new CallBack(){
			public void doLine(String line) {
				sb.append(line);
				sb.append("\n");
			}
			
		});
		return sb.toString();
	}
	
	
	public String getWebXml() throws Exception{
		final StringBuffer sb = new StringBuffer();
		RemoteCommonUtil.excute(this.targetIp, "cat "+getAppPath()+"/WEB-INF/web.xml", new CallBack(){
			public void doLine(String line) {
				sb.append(line);
				sb.append("\n");
			}
			
		});
		return sb.toString();
	}
	
	public List<AppJar> getJavaJar() throws Exception{
		
		final StringBuffer sb = new StringBuffer();
		RemoteCommonUtil.excute(this.targetIp, "ls --full-time "+getAppPath()+"/WEB-INF/lib", new CallBack(){
			public void doLine(String line) {
				sb.append(line);
				sb.append("\n");
			}
			
		});
		
		List<AppJar> jarList = new ArrayList<AppJar>();
		
		//-rw-r--r--  1 admin admin   17090 2010-10-28 04:16:04.000000000 +0800 upload-1.0.jar
		String[] jars = StringUtils.split(sb.toString(),"\n");
		for(String jar:jars){
			String[] infos = StringUtils.split(jar);
			if(infos.length >7){
				String size = infos[4];
				String day =infos[5];
				String time = infos[6];
				String name = infos[8];
				
				AppJar appjar = new AppJar();
				appjar.setJarName(name);
				appjar.setJarSize(Integer.parseInt(size));
				String t = day+" "+time.substring(0, 8);
				appjar.setModifyTime(t);
				
				jarList.add(appjar);
			}
		}
		return jarList;
	}
	
	

}
