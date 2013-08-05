package com.taobao.csp.capacity.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.csp.capacity.po.GroupAppPo;

/***
 * 处理应用分组的工具类
 * @author youji.zj
 *
 */
public class AppGroupUtil {
	
	public static List<GroupAppPo> getAppGroupIds(CapacityAppPo po) {
		List<GroupAppPo> list = new ArrayList<GroupAppPo>();
		if (po == null) {
			return null;
		}
		
		String groupNames = po.getGroupNames();
		if(StringUtils.isNotBlank(groupNames)) {
			String[] groups = groupNames.split(";");
			for (String group : groups) {
				String[] infos = group.split(":");
				
				if (infos.length == 3) {
					String groupName = infos[0];
					String name = infos[1];
					String groupId = infos[2];
					
					GroupAppPo groupAppPo = new GroupAppPo();
					groupAppPo.setAppId(Integer.parseInt(groupId));
					groupAppPo.setAppName(name);
					groupAppPo.setGroupName(groupName);
					groupAppPo.setSourceAppId(po.getAppId());
					list.add(groupAppPo);
				}
			}
		}
		
		return list;
	}
	
	/***
	 * 暂时写死
	 * @param appId
	 * @return
	 */
	public static int getSourceAppId(int appId) {
		int souceAppId = appId;
		
		if (appId == 375 || appId == 376 || appId == 377 || appId == 378) {
			souceAppId = 8;
		}
		
		if (appId == 383 || appId == 384 || appId == 385) {
			souceAppId = 322;
		}
		
		if (appId == 431 || appId == 432 || appId == 456) {
			souceAppId = 338;
		}
		
		if (appId == 700 || appId == 48 || 239 == 240) {
			souceAppId = 47;
		}
		
		return souceAppId;
	}
	
	public static List<Integer> getGroupIds(int sourceId) {
		List<Integer> ids = new ArrayList<Integer>();
		
		if (sourceId == 8) {
			ids.add(375);
			ids.add(376);
			ids.add(377);
			ids.add(378);
		} else if (sourceId == 322) {
			ids.add(383);
			ids.add(384);
			ids.add(385);
		} else if (sourceId == 322) {
			ids.add(431);
			ids.add(432);
			ids.add(456);
		} else if (sourceId == 47) {
			ids.add(700);
			ids.add(48);
			ids.add(239);
			ids.add(240);
		} else {
			ids.add(sourceId);
		}
		
		return ids;
	}

}
