
package com.taobao.csp.depend.auto;

import java.util.ArrayList;
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

import com.taobao.csp.depend.dao.BeiDouDao;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.po.AppDepApp;
import com.taobao.csp.depend.po.AppDependentRelationPo;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.RemoteCommonUtil;
import com.taobao.monitor.common.util.RemoteCommonUtil.CallBack;

public class FetchAppDepRelation {
	private static final Logger logger = Logger.getLogger(FetchAppDepRelation.class);

	private static final String DEPEND_ME = "dependMe";
	private static final String ME_DEPEND = "meDepend";

	private Map<String,Map<String,Set<Integer>>> dependMap = new HashMap<String, Map<String,Set<Integer>>>();

	private Map<String,HostPo> ipMap = new HashMap<String, HostPo>();

	private Set<Integer> filterPort = new HashSet<Integer>();

	private Map<Integer,String> portUseAppType = new HashMap<Integer, String>();

	private BeiDouDao beiDouDao;

	private CspDependentDao cspDependentDao;

	public BeiDouDao getBeiDouDao() {
		return beiDouDao;
	}
	public void setBeiDouDao(BeiDouDao beiDouDao) {
		this.beiDouDao = beiDouDao;
	}
	public CspDependentDao getCspDependentDao() {
		return cspDependentDao;
	}
	public void setCspDependentDao(CspDependentDao cspDependentDao) {
		this.cspDependentDao = cspDependentDao;
	}

	public Map<Integer, String> getPortUseAppType() {
		return portUseAppType;
	}
	public void setPortUseAppType(Map<Integer, String> portUseAppType) {
		this.portUseAppType = portUseAppType;
	}

	public Set<Integer> getFilterPort() {
		return filterPort;
	}
	public void setFilterPort(Set<Integer> filterPort) {
		this.filterPort = filterPort;
	}


	private final Set<NetstatInfo> netstatList = new HashSet<NetstatInfo>();//netstat 出来的所有localip 与foreign ip 关系
	private final Set<Integer> portListen = new HashSet<Integer>();//此应用开放的LISTEN端口
	private final Pattern p = Pattern.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d+)");

	private CallBack lineCallBack = new CallBack(){
		public void doLine(String line){
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
		}
	};

	/**
	 * 通过IP 获取目标机器的netstat 的信息
	 * @param ip
	 */
	public Map<String,Set<NetstatInfo>> findHostNetstatInfos(String ip,String userName,String pwd, String company) throws Throwable{
		netstatList.clear();
		portListen.clear(); 
		try {     
			if(!company.equals("b2b")) {
				RemoteCommonUtil.excute(ip,userName,pwd,"netstat -an", lineCallBack);	
			} else {
				B2BRemoteUtil.excute(ip, lineCallBack);
			}
		} catch (Throwable e) {
			logger.error("", e);
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
		map.put(ME_DEPEND, meDependent);
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
		for(NetstatInfo net:meDepend){
			try{
				String foreignIp = net.getForeignIp();

				if("127.0.0.1".equals(foreignIp)){
					continue;
				}

				HostPo foreignhost = ipMap.get(foreignIp);
				if(foreignhost == null){
					foreignhost = CspCacheTBHostInfos.get().getHostInfoByIp(foreignIp);

					//          if(net.getForeignPort() == 1521){
					//            if(foreignhost != null){
					//              if(oracleIpMap.get(foreignIp)!=null){
					//                foreignhost.setOpsName(oracleIpMap.get(foreignIp));
					//              }
					//            }
					//          }

					if(foreignhost == null){
						foreignhost = new HostPo();
						foreignhost.setHostIp(foreignIp);
						foreignhost.setHostSite("未知");
						foreignhost.setOpsName("未知");

						if(VipCache.isVip(foreignIp)){
							foreignhost.setOpsName(VipCache.getVipInfo(foreignIp));
						}
						//            if(oracleIpMap.get(foreignIp)!=null){
						//              foreignhost.setOpsName(oracleIpMap.get(foreignIp));
						//            }
						if("未知".equals(foreignhost.getOpsName())){
							logger.error("can not find "+net.toString());
							cspDependentDao.addUnknownDepIp(selfOpsName,"medep", net);
						}
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

				//cspDependentDao.addMeDependent(po);


			}catch (Exception e) {
				logger.error("", e);
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

		for(NetstatInfo net:dependMe){
			try{
				String foreign = net.getForeignIp();        
				if("127.0.0.1".equals(foreign)){
					continue;
				}

				HostPo host = ipMap.get(foreign);
				if(host == null){
					host =  CspCacheTBHostInfos.get().getHostInfoByIp(foreign);     
					if(host == null){
						host = new HostPo();
						host.setHostIp(foreign);
						host.setHostSite("未知");
						host.setOpsName("未知");

						if(VipCache.isVip(foreign)){
							host.setOpsName(VipCache.getVipInfo(foreign));
						}
						//            if(oracleIpMap.get(foreign)!=null){
						//              host.setOpsName(oracleIpMap.get(foreign));
						//            }
						if("未知".equals(host.getOpsName())){
							logger.error("can not find "+net.toString());
							cspDependentDao.addUnknownDepIp(depOpsName,"depme", net);
						}
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

				//cspDependentDao.addDependentMe(po);

			}catch (Exception e) {
				logger.error("", e);
			}
		}

	}



	public void checkupDepend() {
		String time = MethodUtil.getStringOfDate(new Date());
		logger.info("定时任务检查依赖关系开始->" + time);
		checkupDepend(null, time);
		logger.info("定时依赖检测结束");
	}


	/**
	 *  检查应用的依赖情况
	 */
	public void checkupDepend(String name, String time) {

		logger.info("开始checkupDepend name:" + name);

		Date date = null;
		try {
			date = MethodUtil.getDate(time);
		} catch (Exception e) {
			logger.error("输入的时间错误->" + time, e);
		}
		long start = System.currentTimeMillis();
		VipCache.getAllVip();
		logger.info("getAllVip 结束,耗时：" + (System.currentTimeMillis() - start));
		//      oracleIpMap = beiDouDao.findOracleInfo();
		if (name == null) {
			cspDependentDao.deleteAppDepApp(date);
		} else {
			cspDependentDao.deleteAppDepAppByName(date, name); 
		}
		logger.info("获取所有 当前有效的应用");
		start = System.currentTimeMillis();
		List<AppInfoPo> opsNameSet = AppInfoAo.get().findAllDayApp();
		logger.info("findAllDayApp结束,耗时：" + (System.currentTimeMillis() - start) + ",共" + opsNameSet.size() + "个。");

		for(AppInfoPo opsname:opsNameSet){
			if (name != null && !name.equals(opsname.getOpsName())) {
				continue;
			}
			logger.info("开始--- checkup depend opsName:"+opsname.getOpsName());
			//获取这个应用的机器
			Map<String, List<HostPo>> hostMap = CspCacheTBHostInfos.get().getHostMapByRoom(opsname.getOpsName());
			if(hostMap == null||hostMap.size()==0){
				logger.error("can not find "+opsname.getOpsField()+"--"+opsname.getOpsName()+" message");
				continue;
			}
			for(Map.Entry<String, List<HostPo>> entry:hostMap.entrySet()){
				String siteName = entry.getKey();
				List<HostPo> list = entry.getValue();

				if(list.size() == 0){
					logger.error("can not find "+opsname.getOpsField()+"--"+opsname.getOpsName()+" 机房:"+siteName+" 机器数量");
					continue;
				}

				int iCounter = 0;
				for(HostPo po:list){
					if (iCounter <= 50) {
						String company = opsname.getCompanyName();
						if(company == null)
							company = "";
						else
							company = company.trim().toLowerCase();
						Map<String, Set<NetstatInfo>> mapDep;
						try {
							mapDep = findHostNetstatInfos(po.getHostIp(),
									opsname.getLoginName(), opsname.getLoginPassword(), company);
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
						logger.info(opsname.getOpsField() + "--" + opsname.getOpsName()
								+ " 机房:" + siteName + " is over.");
						break;
					}								
				}
			}
			logger.info("结束  checkup depend opsName:"+opsname.getOpsName());
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

				if(portSet !=null){
					for(Integer port:portSet){
						String type = portUseAppType.get(port);
						app.setPortInfo(port);  //记录端口信息
						if(type !=null){
							app.setDependAppType(type);
							dependTypeMap.put(depOpsName, type);              
							break;
						}
					}
				}
				list.add(app);
			}
		}

		for(AppDepApp app:list){
			app.setSelfAppType(dependTypeMap.get(app.getOpsName()));
			cspDependentDao.addAppDepApp(app);
		}
		//oracleIpMap.clear();
		dependMap.clear();
		VipCache.clear();
	}
}