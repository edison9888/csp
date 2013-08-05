package com.taobao.csp.depend.auto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.HsfServiceCodeMap;
/**
 * ÿ�춨ʱ���£���һЩ���õ����ݷ��뻺������
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
		//TFS ��  DB����Ϣ������common�����ˡ����ﲻ�������ˡ�
		logger.info("��ʼͬ��������Ϣ��" + new Date());
	}

	public static DependCache getDependcache() {
		return dependCache;
	}

	private void syncCenterList() {
		logger.info("��ʼͬ��CenterList��Ϣ��" + new Date());
		List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
		if(appList.size() > 0) {
			centerList.clear();
			for(AppInfoPo po: appList) {
				if(po.getAppType() != null && po.getAppType().equals("center")) {
					centerList.add(po);
				}
			}  
		}
		logger.info("ͬ��CenterList��Ϣ������" + new Date());
	}
	private void syncTddlAppList() {
		logger.info("��ʼͬ��Tddl AppList��Ϣ��" + new Date());
	}	
	public List<AppInfoPo> getCenterList() {
		if(centerList.size() == 0)
			syncCenterList();
		return centerList;
	}

}
