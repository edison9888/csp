
package com.taobao.monitor.dependent.job;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.RemoteCommonUtil;
import com.taobao.monitor.common.util.RemoteCommonUtil.CallBack;
import com.taobao.monitor.dependent.VipCache;
import com.taobao.monitor.dependent.ao.CspDependentAo;
import com.taobao.monitor.dependent.po.AppDependentRelationPo;

/**
 * 
 * @author xiaodu
 * @version 2011-4-21 下午04:10:53
 */
public class AppPortCheckupJob implements Job{
	
	
	private static final Logger logger = Logger.getLogger(AppPortCheckupJob.class);
	
	/**
	 * 获取这个应用随机一台机器的 netstat 连接情况
	 * 
	 * @param opsName
	 * @return Map<String,Set<NetstatInfo>> map 的key 为：dependMe和meDepend 
	 */
	private Map<String,Set<NetstatInfo>> getAppNetstat(AppInfoPo app,String ip){
			final Set<Integer> portListen = new HashSet<Integer>();//此应用开放的LISTEN端口
			final Set<NetstatInfo> netstatList = new HashSet<NetstatInfo>();//netstat 出来的所有localip 与foreign ip 关系
			final Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d+)");
			try {
				RemoteCommonUtil.excute(ip,app.getLoginName(),app.getLoginPassword(),"netstat -an",new CallBack(){
					public void doLine(String line){
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
				});
			} catch (Exception e) {
				e.printStackTrace();
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
	
	
	private Map<String, String> oracleIpMap = new HashMap<String, String>();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		VipCache.getAllVip();
		oracleIpMap = CspDependentAo.get().findOracleInfo();
		
		List<AppInfoPo> opsNameSet = AppInfoAo.get().findAllEffectiveAppInfo();
		for(AppInfoPo opsname:opsNameSet){
			System.out.println("execute :"+opsname);
			Map<String,String> ipMap = new HashMap<String, String>();
 			List<HostPo> hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(opsname.getOpsName());
 			if(hostList == null){
 				logger.error("can not find "+opsname.getOpsField()+"--"+opsname.getOpsName()+" message");
				continue;
 			}
			if(hostList != null&& hostList.size()>0){
				for(HostPo po:hostList){
					String ip = ipMap.get(po.getHostSite());
					if(ip == null){
						ipMap.put(po.getHostSite(), po.getHostIp());
					}else{
						continue;
					}
				}
				
				for(Map.Entry<String, String> entry:ipMap.entrySet()){
					String cm = entry.getKey();
					String ip = entry.getValue();
					Map<String,Set<NetstatInfo>> mapDep = getAppNetstat(opsname,ip);
					if(mapDep != null){
						Set<NetstatInfo> dependMe = mapDep.get("dependMe");
						parseDependMe(dependMe,opsname.getOpsName(),cm,ip);
						Set<NetstatInfo> meDepend = mapDep.get("meDepend");
						parseMeDepent(meDepend,opsname.getOpsName(),cm,ip);
						
					}
					
				}
			
			}
			System.out.println("end execute :"+opsname);
		}
		
	}
	
	
	private void parseMeDepent(Set<NetstatInfo> meDepend,String selfOpsName,String site,String ip){
		for(NetstatInfo net:meDepend){
			try{
				String foreignIp = net.getForeignIp();
				
				if("127.0.0.1".equals(foreignIp)){
					continue;
				}
				
				
				HostPo foreignhost = CspCacheTBHostInfos.get().getHostInfoByIp(foreignIp);
				
				if(foreignhost == null){
					logger.error("can not find "+foreignIp+" message");
					continue;
				}
				
				String opsName = foreignhost.getOpsName();
				AppDependentRelationPo po = new AppDependentRelationPo();
				
				if(VipCache.isVip(foreignIp)){
					opsName = VipCache.getVipInfo(foreignIp);
				}
				if(oracleIpMap.get(foreignIp)!=null){
					opsName = oracleIpMap.get(foreignIp);
				}
				
				po.setSelfSite(site);
				po.setSelfIp(ip);
				po.setCollectTime(new Date());
				po.setDependentIp(net.getForeignIp());
				po.setDependentPort(net.getForeignPort());
				po.setDependentSite(foreignhost.getHostSite());
				po.setDependentOpsName(opsName);
				po.setSelfOpsName(selfOpsName);
				if(!"未知".equals(opsName))
					CspDependentAo.get().addMeDependent(po);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	
	
	private void parseDependMe(Set<NetstatInfo> dependMe,String selfOpsName,String site,String ip){
		
		for(NetstatInfo net:dependMe){
			try{
				String foreign = net.getForeignIp();
				
				if("127.0.0.1".equals(foreign)){
					continue;
				}
				
				HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(foreign);
				
				if(host == null){
					logger.error("can not find "+foreign+" message");
					continue;
				}
				
				String opsName = host.getOpsName();;
				
				if(VipCache.isVip(foreign)){
					opsName = VipCache.getVipInfo(foreign);
				}
				if(oracleIpMap.get(foreign)!=null){
					opsName = oracleIpMap.get(foreign);
				}
				
				AppDependentRelationPo po = new AppDependentRelationPo();
				po.setSelfSite(site);
				po.setSelfIp(ip);
				po.setCollectTime(new Date());
				po.setDependentIp(net.getForeignIp());
				po.setDependentPort(net.getLocalPort());
				po.setDependentSite(host.getHostSite());
				po.setDependentOpsName(opsName);
				po.setSelfOpsName(selfOpsName);
				if(!"未知".equals(opsName))
					CspDependentAo.get().addDependentMe(po);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

	
	public static void main(String[] args){
		AppPortCheckupJob job = new AppPortCheckupJob();
		try {
			job.execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
