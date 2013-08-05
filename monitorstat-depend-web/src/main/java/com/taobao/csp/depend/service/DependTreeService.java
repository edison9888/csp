package com.taobao.csp.depend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.taobao.csp.depend.ao.CspMapKeyInfoAo;
import com.taobao.csp.depend.po.EagleeyeTreeGridData;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspMapKeyInfoPo;
import com.taobao.monitor.common.po.EagleeyeChildKeyListPo;
import com.taobao.monitor.common.util.Arith;

public class DependTreeService {
	private DependTreeService(){}
	private static DependTreeService service = new DependTreeService();
	public static DependTreeService get() {
		return service;
	}

	public void changeToEagleeyeTreeData(EagleeyeChildKeyListPo dayPo ,EagleeyeTreeGridData father) {
		List<EagleeyeChildKeyListPo> list = dayPo.getTopo();
		int i=1;
		for(EagleeyeChildKeyListPo po: list) {
			if(isFilter(po.getAppName(), po.getKeyName()))
				continue;
			EagleeyeTreeGridData data = new EagleeyeTreeGridData();
			data.setId(10*father.getId() + i++);
			data.setKeyName(po.getKeyName());
			data.setCallnum(po.getTotalCallNum());
			data.setAppName(po.getAppName());
			father.getChildren().add(data);
			changeToEagleeyeTreeData(po, data);
		}
	}

	public EagleeyeTreeGridData getDependTreeAll(CspEagleeyeApiRequestDay day) {
		EagleeyeChildKeyListPo dayPo = JSONObject.parseObject(day.getResponseContent(), EagleeyeChildKeyListPo.class);
		EagleeyeTreeGridData root = new EagleeyeTreeGridData();
		root.setId(1);
		root.setKeyName(dayPo.getKeyName());
		root.setCallnum(dayPo.getTotalCallNum());
		root.setAppName(dayPo.getAppName());
		root.setRate(1);
		changeToEagleeyeTreeData(dayPo, root);
		return root;
	}

	public EagleeyeTreeGridData getDependTreeBlack(CspEagleeyeApiRequestDay day, boolean isContainBlack) {
		EagleeyeChildKeyListPo dayPo = JSONObject.parseObject(day.getResponseContent(), EagleeyeChildKeyListPo.class);
		EagleeyeTreeGridData root = new EagleeyeTreeGridData();
		root.setId(1);
		root.setKeyName(dayPo.getKeyName());
		root.setCallnum(dayPo.getTotalCallNum());
		root.setAppName(dayPo.getAppName());
		root.setBlack(false);
		root.setRate(1);
		
		Set<String> blackSet = new HashSet<String>();
		List<CspMapKeyInfoPo> blackKeyList = CspMapKeyInfoAo.get().getBlackKeyList(day.getAppName());
		for(CspMapKeyInfoPo po : blackKeyList) {
			blackSet.add(conbineAppAndKey(po.getAppname(),po.getKeyname()));
		}
		
		changeToEagleeyeTreeData(dayPo, root, blackSet, isContainBlack, root.getCallnum());
		return root;
	}

	public void changeToEagleeyeTreeData(EagleeyeChildKeyListPo dayPo ,EagleeyeTreeGridData father, 
			Set<String> blackSet, final boolean isContainBlack, final long total) {
		List<EagleeyeChildKeyListPo> list = dayPo.getTopo();
		int i=1;
		for(EagleeyeChildKeyListPo po: list) {
			if(isFilter(po.getAppName(), po.getKeyName()))
				continue;
			EagleeyeTreeGridData data = new EagleeyeTreeGridData();
			data.setId(10*father.getId() + i++);
			data.setKeyName(po.getKeyName());
			data.setCallnum(po.getTotalCallNum());
			data.setAppName(po.getAppName());
			father.getChildren().add(data);
			data.setRate(Arith.div(po.getTotalCallNum(), total, 4));
			
			if(father.isBlack() || blackSet.contains(
					conbineAppAndKey(data.getAppName(),data.getKeyName()))) {
				data.setBlack(true);
				continue;
			} else {
				data.setBlack(false);
			}
			
			//不显示黑名单，且节点本身就是黑名单时，不显示下级节点
			if(!isContainBlack && data.isBlack())
				continue;
			
			changeToEagleeyeTreeData(po, data, blackSet, isContainBlack, total);
		}
	}

	public String conbineAppAndKey(String appName, String keyName) {
		return appName + "-" + keyName;
	}

	/**
	 * 依赖树节点的过滤规则
	 * @param appName
	 * @param keyName
	 * @return
	 */
	public boolean isFilter(String appName, String keyName) {
		if(StringUtil.isBlank(appName) || StringUtil.isBlank(keyName)
				||appName.startsWith("rpc:") || keyName.startsWith("rpc:"))
			// keyName.equalsIgnoreCase("Notify_recv") notify的过滤暂时去掉
			return true;
		return false;
	}
}
