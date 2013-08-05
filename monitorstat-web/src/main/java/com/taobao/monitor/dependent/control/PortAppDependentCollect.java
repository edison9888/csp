
package com.taobao.monitor.dependent.control;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.taobao.monitor.dependent.job.NetstatInfo;

/**
 * 
 * @author xiaodu
 * @version 2011-5-5 下午01:32:52
 */
public class PortAppDependentCollect {
	
	
	private ShellCommon shell = null;
	
	public PortAppDependentCollect(ShellCommon shell){
		this.shell = shell;
	}
	
	
	
	public  List<String> getAppDependentIp(String targetAppname) throws IOException{
		
		
		List<String> appIpList = new ArrayList<String>();
		
		Set<NetstatInfo> set = getDependentIp();
		
		for(NetstatInfo info:set){
			String appname = getAppName(info.getForeignIp());
			System.out.println(appname+":"+info.getForeignIp()+":"+info.getForeignPort());
			if(appname != null){
				if(targetAppname.equals(appname)){
					appIpList.add(info.getForeignIp());
				}
			}
		}
		return appIpList;
	}
	
	/**
	 * ip:10.232.12.130;hostname:v012130.sqa.cm4;appName:alimall;apacheVersion:;jbossVersion:;hsfVersion:;jdkVersion:;ownerName:红巾;useInfo:alimall日常测试环境;mainType:日常;groupSign:标准 
	 * @param ip
	 * @return
	 */
	public  String getAppName(String ip){
		String path = "http://scm.taobao.net/getInfoByIp.htm?ip="+ip;
		try{
			URL url = new URL(path);
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(1000000);
			urlCon.connect();
			BufferedInputStream input = new BufferedInputStream(urlCon.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			StringBuffer sb = new StringBuffer();
			String str = null;
			while((str=reader.readLine())!=null){
				sb.append(str);
			}
			
			String[] k = sb.toString().split(";");
			if(k.length >3){
				String app = k[2];
				String[] name = app.split(":");
				if(name.length ==2){
					return name[1];
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
	}
	
	
	
	public Set<NetstatInfo> getDependentIp() throws IOException{
		String result = shell.doCommon("netstat -an");
		Map<String,Set<NetstatInfo>> map = getAppNetstat(result);
		return map.get("meDepend");
	}
	
		
	/**
	 * 获取这个应用随机一台机器的 netstat 连接情况
	 * 
	 * @param opsName
	 * @return Map<String,Set<NetstatInfo>> map 的key 为：dependMe和meDepend 
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public  Map<String,Set<NetstatInfo>> getAppNetstat(String result) throws NumberFormatException, IOException{
			final Set<Integer> portListen = new HashSet<Integer>();//此应用开放的LISTEN端口
			final Set<NetstatInfo> netstatList = new HashSet<NetstatInfo>();//netstat 出来的所有localip 与foreign ip 关系
			final Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d+)");
			
			
			BufferedReader reader= new BufferedReader(new StringReader(result) );
			
			String line = null;
			
			while((line = reader.readLine())!=null){			
				String[] l = StringUtils.split(line," ");
				if (l.length == 6){			
					if("LISTEN".equals(l[5].toUpperCase())){//如果 State 为LISTEN 表示应用自己开放的端口，
						Matcher m = p.matcher(l[3]);
						if(m.find()){
							String port = m.group(2);
							portListen.add(Integer.parseInt(port));
						}
						
					}else{
						NetstatInfo info = new NetstatInfo();
						Matcher portMatcher = p.matcher(l[3]);
						if(portMatcher.find()){
							String localIp = portMatcher.group(1);
							String localPort = portMatcher.group(2);
							info.setLocalIp(localIp);
							info.setLocalPort(Integer.parseInt(localPort));
						}
						
						Matcher m = p.matcher(l[4]);
						if(m.find()){
							String foreignIp = m.group(1);
							String foreignPort = m.group(2);
							info.setForeignIp(foreignIp);
							info.setForeignPort(Integer.parseInt(foreignPort));
						}
						
						if(info.getForeignIp()!=null&&info.getLocalIp()!=null){
							
							if(info.getLocalPort() != 80&&info.getLocalPort() != 8009&&info.getForeignPort()!=80&&info.getForeignPort()!=8009)//将apache 端口去除									
								netstatList.add(info);
						}
						
					}		
				}
			}
			
			Set<NetstatInfo> dependentMe = new HashSet<NetstatInfo>();//通过Listen 端口获取依赖我的应用IP
			
			Iterator<NetstatInfo> it = netstatList.iterator();
			while(it.hasNext()){
				NetstatInfo netstatInfo = it.next();
				if(portListen.contains(netstatInfo.getLocalPort())){
					dependentMe.add(netstatInfo);
					it.remove();
				}
			}
			
			Set<NetstatInfo> meDependent =netstatList; //剩下的就是我依赖的IP
			
			
			Map<String,Set<NetstatInfo>> map = new HashMap<String, Set<NetstatInfo>>();
			map.put("dependMe", dependentMe);
			map.put("meDepend", meDependent);
			return map;
		
	}
	

}
