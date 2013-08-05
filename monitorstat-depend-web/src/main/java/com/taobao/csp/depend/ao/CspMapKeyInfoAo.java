package com.taobao.csp.depend.ao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.csp.depend.dao.CspMapKeyInfoDao;
import com.taobao.csp.depend.po.AjaxResult;
import com.taobao.csp.depend.util.ConstantParameters;
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
			updateby = ConstantParameters.CSP_DEPEND_MAP_DEFAULT_CREATOR;
		
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
		List<CspMapKeyInfoPo> list = dao.getKeyList(ConstantParameters.CSP_DEPEND_MAP_NOT_BLACK, appname);
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
			throw new Exception("id is not a number,id=" + id);
		return dao.getMapKeyPoById(id);
	}

	public CspMapKeyInfoPo getMapKeyPoByKey(String appName, String keyName) throws Exception {
		if(StringUtil.isBlank(appName) || StringUtil.isBlank(keyName) )
			throw new Exception("appName,keyName can't be null");
		return dao.getMapKeyPoByKey(appName, keyName);
	}
	
	public List<CspMapKeyInfoPo> getBlackKeyList(String appname) {
		List<CspMapKeyInfoPo> list = dao.getKeyList(ConstantParameters.CSP_DEPEND_MAP_BLACK, appname);
		Collections.sort(list,new Comparator<CspMapKeyInfoPo>(){	//黑名单按更新时间
			@Override
			public int compare(CspMapKeyInfoPo o1, CspMapKeyInfoPo o2) {
				return o1.getUpdateTime().compareTo(o2.getUpdateTime());
			}
        });
		return list;
	}
	
	public void deleteAutoKeyByAppName(String appname) {
		try {
			dao.deleteKeyByAppNameAndStatus(appname, Constants.CSP_DEPEND_MAP_STATUS_AUTO);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	public void deleteKeyByIds(Set<Long> idSet) {
		if(idSet == null || idSet.size() == 0)
			return;
		
		try {
			dao.deleteKeyByIds(idSet);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	public AjaxResult changeKeyStatus(String id, int status, String userName) {
		AjaxResult result = new AjaxResult();
		try {
			if(StringUtil.isBlank(userName))
				userName = "";

			CspMapKeyInfoPo po = CspMapKeyInfoAo.get().getMapKeyPoById(id);
			po.setIsBlack(status);
			po.setKeyStatus(Constants.CSP_DEPEND_MAP_STATUS_CONFIG);
			po.setUpdateBy(userName);
			po.setUpdateTime(new Date());
			boolean flag = CspMapKeyInfoAo.get().updateCspMapKeyInfoPo(po);
			result.setSuccess(flag);
		} catch (Exception e) {
			logger.error("");
			result.setMsg(e.toString());
		}
		return result;
	}
}
