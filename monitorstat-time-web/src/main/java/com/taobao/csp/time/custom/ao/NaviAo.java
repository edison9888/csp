package com.taobao.csp.time.custom.ao;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.time.custom.dao.NaviDao;
import com.taobao.csp.time.web.po.UserCustomNaviKeyPo;
import com.taobao.csp.time.web.po.UserCustomNaviMainPo;
import com.taobao.csp.time.web.po.UserCustomNaviViewPo;

public class NaviAo {
	private static NaviAo mainAo =new NaviAo();
	private static NaviDao mainDao = new NaviDao();
	private NaviAo(){
	}
	public static NaviAo get(){
		return mainAo;
	}
	public boolean insertNavisByUserName(String userName,List<String> naviNames){
		return mainDao.insertNavisByUserName(userName, naviNames);
	}
	public boolean insertNavisByAppName(String appName,List<String> naviNames){
		return mainDao.insertNavisByAppName(appName, naviNames);
	}
	public boolean findNavisByUserName(String userName,final List<UserCustomNaviMainPo> navis){
		return mainDao.findNavisByUserName(userName, navis);
	}
	public boolean findNavisByAppName(String appName,final List<UserCustomNaviMainPo> navis){
		return mainDao.findNavisByAppName(appName, navis);
	}
	public boolean updateNaviName(Integer id,String naviName){
		return mainDao.updateNaviName(id, naviName);
	}
	public boolean deleteNavisByUserName(String userName){
		return mainDao.deleteNavisByUserName(userName);
	}
	public boolean deleteNavisByUserName(String userName,List<Integer> naviIds){
		return mainDao.deleteNavisByUserName(userName, naviIds);
	}
	public boolean deleteNavisByAppName(String appName,List<Integer> naviIds){
		return mainDao.deleteNavisByAppName(appName, naviIds);
	}
	public boolean findNaviInfoByUserNameAndNaviName(String userName,String naviName,final UserCustomNaviMainPo po){
		return mainDao.findNaviInfoByUserNameAndNaviName(userName, naviName, po);
	}
}
