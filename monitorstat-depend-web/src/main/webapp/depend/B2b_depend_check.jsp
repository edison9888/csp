<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.apache.commons.httpclient.methods.PostMethod"%>
<%@page import="org.apache.commons.httpclient.HttpClient"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.AppDepApp"%>
<%@page import="com.taobao.csp.depend.po.AppDependentRelationPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.taobao.csp.depend.util.StartUpParam"%>
<%@page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="com.taobao.csp.depend.auto.NetstatInfo"%>
<%@page import="com.taobao.csp.depend.dao.CspDependentDao"%>
<%@page import="com.taobao.csp.depend.dao.BeiDouDao"%>
<%@page import="java.util.HashSet"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.log4j.Logger"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%!

public String getAppName(String sip,String tip){
	try{
	HttpClient httpClient = new HttpClient();
	String postUrl = "http://"+sip+":8082/scriptexcute";
    PostMethod postMethod =  new PostMethod(postUrl);
    postMethod.addParameter("token", "dcbeb81d186a89a11c1515ced9022bca");
    postMethod.addParameter("script", "getappname.sh");
    postMethod.addParameter("args", tip);
    
    int code = httpClient.executeMethod(postMethod);
    if(code ==200){
    	String name  = postMethod.getResponseBodyAsString();
    	System.out.println("getAppName 执行脚本ip："+sip+"目标："+tip+" name:"+name);
    	return name.trim();
    }}catch(Exception e){
    	
    }
    return "未知";
    
}


/**
 * 
 * @author xiaodu
 * @version 2011-10-8 下午12:02:08
 */
public class FetchAppDepRelationB {
  
  
  private static final String DEPEND_ME = "dependMe";
  private static final String ME_DEPEND = "meDepend";
  
  private Map<String,Map<String,Set<Integer>>> dependMap = new HashMap<String, Map<String,Set<Integer>>>();
  
  private Map<String,HostPo> ipMap = new HashMap<String, HostPo>();
  
  private Set<Integer> filterPort = new HashSet<Integer>();
  
  
  private HttpClient httpClient = new HttpClient();
  
  private CspDependentDao cspDependentDao = new CspDependentDao();
  
  /**
   * 通过IP 获取目标机器的netstat 的信息
   * @param ip
   */
  public Map<String,Set<NetstatInfo>> findHostNetstatInfos(String ip,String userName,String pwd) throws Throwable{
    
    final Set<NetstatInfo> netstatList = new HashSet<NetstatInfo>();//netstat 出来的所有localip 与foreign ip 关系
    final Set<Integer> portListen = new HashSet<Integer>();//此应用开放的LISTEN端口
    try {     
      final Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d+)");
      
      String postUrl = "http://"+ip+":8082/scriptexcute";
      PostMethod postMethod =  new PostMethod(postUrl);
      postMethod.addParameter("token", "dcbeb81d186a89a11c1515ced9022bca");
      postMethod.addParameter("script", "netstatan.sh");
      
      int statusCode = httpClient.executeMethod(postMethod);
      
      BufferedReader br = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"gbk"));
      String line = null;
		while((line = br.readLine()) != null){
	          String[] l = StringUtils.split(line," ");
	          if (l.length == 6){ 
	            //tcp        0      0 127.0.0.1:199               0.0.0.0:*                   LISTEN 
	        	  if("LISTEN".equals(l[5].toUpperCase())){//如果 State 为LISTEN 表示应用自己开放的端口，
	              	String[] tmp =  StringUtils.split(l[3], ":");
	                String port = tmp[tmp.length-1];
	                try{
	                portListen.add(Integer.parseInt(port.trim()));
	                }catch (Exception e) {
					}
	            }else{
	              //tcp        0      0 172.23.181.190:50764        172.23.211.182:12200        ESTABLISHED 
	              //获取本地连接IP与端口
	              NetstatInfo info = new NetstatInfo();
	              Matcher portMatcher = p.matcher(l[3]);
	              if(portMatcher.find()){
	                String localIp = portMatcher.group(1);
	                String localPort = portMatcher.group(2);
	                info.setLocalIp(localIp);
	                info.setLocalPort(Integer.parseInt(localPort));
	              }
	              //获取外部连接IP与端口
	              Matcher m = p.matcher(l[4]);
	              if(m.find()){
	                String foreignIp = m.group(1);
	                String foreignPort = m.group(2);
	                info.setForeignIp(foreignIp);
	                info.setForeignPort(Integer.parseInt(foreignPort));
	              }
	              
	              if(info.getForeignIp()!=null&&info.getLocalIp()!=null){
	                if(info.getForeignPort()>1000&&info.getLocalPort()>1000){
	                  if(!filterPort.contains(info.getForeignPort())&&!filterPort.contains(info.getLocalPort()))//将端口去除                 
	                    netstatList.add(info);
	                }
	              }
	            }   
	          }
	          
	          System.out.println(line);
		}
    } catch (Throwable e) {
      throw e;
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
    map.put(DEPEND_ME, dependentMe);
    System.out.println("DEPEND_ME:"+dependentMe.size());
    map.put(ME_DEPEND, meDependent);
    System.out.println("ME_DEPEND:"+meDependent.size());
    return map;
  }
  
  
  
  /**
   * 分析我依赖的信息数据
   * @param meDepend
   * @param selfOpsName
   * @param site
   * @param ip
   */
  private void parseMeDepent(Set<NetstatInfo> meDepend,String selfOpsName,String site,String ip){
	  
	  //172.23.211.193:48768===mysql_other_notify_mix 172.24.64.15:3306
	  //localIp localPort 表示自己
	  //foreignIp foreignPort 表示我依赖方
	  
    for(NetstatInfo net:meDepend){
      try{
        String foreignIp = net.getForeignIp();
        
        System.out.println("parseDependMe==="+foreignIp);
        
        if("127.0.0.1".equals(foreignIp)){
          continue;
        }
                
        HostPo foreignhost = ipMap.get(foreignIp);
        if(foreignhost == null){
        	String name = getAppName(ip,foreignIp);
        	System.out.println(foreignIp+"==="+name);
        	  foreignhost = new HostPo();
              foreignhost.setHostIp(foreignIp);
              foreignhost.setHostSite("CM");
              foreignhost.setOpsName(name);
              if("未知".equals(foreignhost.getOpsName())){
                cspDependentDao.addUnknownDepIp(selfOpsName,"medep", net);
              }
          ipMap.put(foreignIp, foreignhost);
        }
        
        Map<String,Set<Integer>> map = dependMap.get(selfOpsName);
        if(map == null){
          map = new HashMap<String, Set<Integer>>();
          dependMap.put(selfOpsName, map);
        }
        
        Set<Integer> depPortSet = map.get(foreignhost.getOpsName());
        if(depPortSet == null){
          depPortSet = new HashSet<Integer>();
          map.put(foreignhost.getOpsName(), depPortSet);
        }
        
        depPortSet.add(net.getForeignPort());
        
        
        String opsName = foreignhost.getOpsName();
        AppDependentRelationPo po = new AppDependentRelationPo();
        
        po.setSelfSite(site);
        po.setSelfIp(ip);
        po.setCollectTime(new Date());
        po.setDependentIp(net.getForeignIp());
        po.setDependentPort(net.getForeignPort());
        po.setDependentSite(foreignhost.getHostSite());
        po.setDependentOpsName(opsName);
        po.setSelfOpsName(selfOpsName);
        
        cspDependentDao.addMeDependent(po);
        
        
      }catch (Exception e) {
    	  System.out.println("异常parseMeDepent"+selfOpsName+"==="+site+"==="+ip);
    	  e.printStackTrace();
      }
    }
  }
  
  /**
   * 分析依赖我的数据
   * @param dependMe
   * @param selfOpsName
   * @param site
   * @param ip
   */
  private void parseDependMe(Set<NetstatInfo> dependMe,String depOpsName,String site,String ip){
	 // 172.24.164.116:9777===itemcenter172.23.211.128:46743
	//localIp localPort 表示自己
	  //foreignIp foreignPort 表示依赖方
	  
	  
    for(NetstatInfo net:dependMe){
      try{
        String foreign = net.getForeignIp(); 
        
        System.out.println("parseDependMe==="+foreign);
        
        if("127.0.0.1".equals(foreign)){
          continue;
        }
        
        HostPo host = ipMap.get(foreign);
        if(host == null){
        	String name = getAppName(ip,foreign);
        	System.out.println(foreign+"==="+name);
            host = new HostPo();
            host.setHostIp(foreign);
            host.setHostSite("CM");
            host.setOpsName(name);
            if("未知".equals(host.getOpsName())){
              cspDependentDao.addUnknownDepIp(depOpsName,"depme", net);
            }
          ipMap.put(foreign, host);
        }
        
        String opsName = host.getOpsName();
        
        Map<String,Set<Integer>> map = dependMap.get(opsName);
        if(map == null){
          map = new HashMap<String, Set<Integer>>();
          dependMap.put(opsName, map);
        }
        
        Set<Integer> depPortSet = map.get(depOpsName);
        if(depPortSet == null){
          depPortSet = new HashSet<Integer>();
          map.put(depOpsName, depPortSet);
        }
        depPortSet.add(net.getLocalPort());
        
        AppDependentRelationPo po = new AppDependentRelationPo();
        po.setSelfSite(site);
        po.setSelfIp(ip);
        po.setCollectTime(new Date());
        po.setDependentIp(net.getForeignIp());
        po.setDependentPort(net.getLocalPort());
        po.setDependentSite(host.getHostSite());
        po.setDependentOpsName(opsName);
        po.setSelfOpsName(depOpsName);
        
        cspDependentDao.addDependentMe(po);
        
      }catch (Exception e) {
    	  System.out.println("异常parseDependMe"+depOpsName+"==="+site+"==="+ip);
    	  e.printStackTrace();
      }
    }
    
  }
  
  
  
  public void checkupDepend() {
    checkupDepend(null);
  }
  
  
  /**
   *  检查应用的依赖情况
   */
  public void checkupDepend(String name) {
	  System.out.println("size"+name);
    if(StartUpParamWraper.getPrjType().equals(StartUpParam.PRJTYPE_ONLINE)) {
      if (name == null) {
        cspDependentDao.deleteAppDepApp(new Date());
      } else {
        cspDependentDao.deleteAppDepAppByName(new Date(), name); 
      }
      //获取所有 当前有效的应用
      List<AppInfoPo> opsNameSet = AppInfoAo.get().findAllEffectiveAppInfo();
      for(AppInfoPo opsname:opsNameSet){

        if (name != null && !name.equals(opsname.getOpsName())) {
          continue;
        }
        System.out.println("size"+name);
        //获取这个应用的机器
        Map<String, List<HostPo>> hostMap = CspCacheTBHostInfos.get().getHostMapByRoom(opsname.getOpsName());
        
        System.out.println("size"+hostMap.size());
        if(hostMap == null||hostMap.size()==0){
          continue;
        }

        //
        for(Map.Entry<String, List<HostPo>> entry:hostMap.entrySet()){
          String siteName = entry.getKey();
          List<HostPo> list = entry.getValue();

          if(list.size() == 0){
            continue;
          }

          int iCounter = 0;
          for(HostPo po:list){
            if (iCounter <= 50) {
              Map<String, Set<NetstatInfo>> mapDep;
              try {
                mapDep = findHostNetstatInfos(po.getHostIp(),
                    opsname.getLoginName(), opsname.getLoginPassword());
                
                
                
                if (mapDep != null) {
                  Set<NetstatInfo> dependMe = mapDep.get(DEPEND_ME);
                  parseDependMe(dependMe, opsname.getOpsName(), siteName,
                      po.getHostIp());
                  Set<NetstatInfo> meDepend = mapDep.get(ME_DEPEND);
                  parseMeDepent(meDepend, opsname.getOpsName(), siteName,
                      po.getHostIp());
                }
                iCounter++; // 每一个机房只查看前50台机器

              } catch (Throwable e) {
              }
            } else {
              break;
            }
          }
        }
      }

      Map<String,String> dependTypeMap = new HashMap<String, String>();
      List<AppDepApp> list = new ArrayList<AppDepApp>();

      for(Map.Entry<String,Map<String,Set<Integer>>> entry:dependMap.entrySet()){
        String opsName = entry.getKey();
        Map<String,Set<Integer>> depMap = entry.getValue();
        for(Map.Entry<String,Set<Integer>> dep:depMap.entrySet()){
          String depOpsName = dep.getKey();
          Set<Integer> portSet = dep.getValue();

          AppDepApp app = new AppDepApp();
          app.setOpsName(opsName);
          app.setDependOpsName(depOpsName);
        
          list.add(app);
        }
      }

      for(AppDepApp app:list){
        app.setSelfAppType(dependTypeMap.get(app.getOpsName()));
        cspDependentDao.addAppDepApp(app);
      }

      dependMap.clear();
    } else {
    }
  }
}
%>
<%

String appName = request.getParameter("appName");

FetchAppDepRelationB f = new FetchAppDepRelationB();
f.checkupDepend(appName);

%>
</body>
</html>