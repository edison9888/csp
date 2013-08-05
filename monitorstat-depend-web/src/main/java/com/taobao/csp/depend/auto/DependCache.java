package com.taobao.csp.depend.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.HsfServiceCodeMap;
/**
 * 每天定时更新，把一些有用的数据放入缓存中来
 * @author zhongting.zy
 *
 */
public class DependCache {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args){
	}

	private static Logger logger = Logger.getLogger(DependCache.class);
	private static final DependCache dependCache = new DependCache();
	
	private List<AppInfoPo> centerList = new ArrayList<AppInfoPo>();

	private DependCache(){
		syncDependCacheInfo();
	}
	public void syncDependCacheInfo() {
		//TFS 和  DB的信息都房子common里面了。这里不做处理了。
		logger.info("开始同步缓存信息：" + new Date());
	}

	public static DependCache getDependcache() {
		return dependCache;
	}

	private void syncCenterList() {
		logger.info("开始同步CenterList信息：" + new Date());
		List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
		if(appList.size() > 0) {
			centerList.clear();
			for(AppInfoPo po: appList) {
				if(po.getAppType() != null && po.getAppType().equals("center")) {
					centerList.add(po);
				}
			}  
		}
		logger.info("同步CenterList信息结束：" + new Date());
	}
	private void syncTddlAppList() {
		logger.info("开始同步Tddl AppList信息：" + new Date());
	}	
	public List<AppInfoPo> getCenterList() {
		if(centerList.size() == 0)
			syncCenterList();
		return centerList;
	}

}
