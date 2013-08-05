package com.taobao.monitor.common.ao.center;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.common.db.impl.center.CspMapKeyInfoDao;
import com.taobao.monitor.common.po.CspMapDependkeyBlacklistPo;
import com.taobao.monitor.common.po.CspMapKeyInfoPo;
import com.taobao.monitor.common.util.Constants;

public class CspMapKeyInfoAo {
	private static final Logger logger =  Logger.getLogger(CspMapKeyInfoAo.class);
	
	private static CspMapKeyInfoAo ao = new CspMapKeyInfoAo();
	private CspMapKeyInfoAo(){}
	public static CspMapKeyInfoAo get() {
		return ao;
	}
	
	private CspMapKeyInfoDao dao = new CspMapKeyInfoDao();

	public boolean addCspMapKeyInfoPo(CspMapKeyInfoPo po){
		try {
			dao.addCspMapKeyInfoPo(po);
			return true;
		} catch (SQLException e) {
			logger.error("", e);
		}
		return false;
	}

	public boolean addCspMapKeyInfoPo(String keyname,String appname,int key_level,int control_type,String updateby,Date update_time,int is_black,int key_status) {
		if(update_time == null)
			update_time = new Date();
		if(StringUtil.isBlank(updateby))
			updateby = Constants.CSP_DEPEND_MAP_DEFAULT_CREATOR;
		
		CspMapKeyInfoPo po = new CspMapKeyInfoPo();
		po.setKeyname(keyname);
		po.setAppname(appname);
		po.setKeyLevel(key_level);
		po.setControlType(control_type);
		po.setUpdateBy(updateby);
		po.setUpdateTime(update_time);
		po.setIsBlack(is_black);
		po.setKeyStatus(key_status);
		return addCspMapKeyInfoPo(po);
	}
	
	public boolean updateCspMapKeyInfoPo(CspMapKeyInfoPo po){
		try {
			dao.updateCspMapKeyInfoPo(po);
			return true;
		} catch (SQLException e) {
			logger.error("", e);
		}
		return false;
	}
	
	public List<CspMapKeyInfoPo> getNormalKeyList(String appname) {
		List<CspMapKeyInfoPo> list = dao.getKeyList(Constants.CSP_DEPEND_MAP_NOT_BLACK, appname);
		Collections.sort(list,new Comparator<CspMapKeyInfoPo>(){	//按照key的级别
			@Override
			public int compare(CspMapKeyInfoPo o1, CspMapKeyInfoPo o2) {
				return o1.getKeyLevel() - o2.getKeyLevel();
			}
        });
		return list;
	}
	
	public CspMapKeyInfoPo getMapKeyPoById(String id) throws Exception {
		if(!StringUtil.isNumeric(id))
			throw new Exception("id is not a number");
		return dao.getMapKeyPoById(id);
	}

	public CspMapKeyInfoPo getMapKeyPoByKey(String appName, String keyName) throws Exception {
		if(StringUtil.isBlank(appName) || StringUtil.isBlank(keyName) )
			throw new Exception("appName,keyName can't be null");
		return dao.getMapKeyPoByKey(appName, keyName);
	}
	
	public List<CspMapKeyInfoPo> getBlackKeyList(String appname) {
		List<CspMapKeyInfoPo> list = dao.getKeyList(Constants.CSP_DEPEND_MAP_BLACK, appname);
		Collections.sort(list,new Comparator<CspMapKeyInfoPo>(){	//黑名单按更新时间
			@Override
			public int compare(CspMapKeyInfoPo o1, CspMapKeyInfoPo o2) {
				return o1.getUpdateTime().compareTo(o2.getUpdateTime());
			}
        });
		return list;
	}
	
	public List<CspMapDependkeyBlacklistPo> searchDependBlackList(String ownAppName) {
		List<CspMapDependkeyBlacklistPo> list = null;
		try {
			list = dao.searchByOwnApp(ownAppName);
		} catch (Exception e) {
			logger.error("",e);
			list = new ArrayList<CspMapDependkeyBlacklistPo>();
		}
		return list;
	}
	
	public boolean addBlackPo(CspMapDependkeyBlacklistPo po) {
		try {
			dao.addBlackPo(po);
		} catch (Exception e) {
			logger.error("",e);
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
//		CspMapDependkeyBlacklistPo po = new CspMapDependkeyBlacklistPo();
//		po.setAppName("itemcenter");
//		po.setKeyName("some interface");
//		po.setOwnApp("detail");
//		po.setUpdateBy("zhongting.zy");
//		po.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//		boolean flag = CspMapKeyInfoAo.get().addBlackPo(po);
//		List<CspMapDependkeyBlacklistPo> list = CspMapKeyInfoAo.get().searchDependBlackList("detail");
//		for(CspMapDependkeyBlacklistPo tmp : list) {
//			System.out.println(tmp.toString());
//		}
	}
}
