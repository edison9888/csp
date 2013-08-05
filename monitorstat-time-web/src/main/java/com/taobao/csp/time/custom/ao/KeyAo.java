package com.taobao.csp.time.custom.ao;

import java.util.ArrayList;
import java.util.List;

import com.taobao.csp.time.custom.dao.KeyDao;
import com.taobao.csp.time.web.po.UserCustomNaviKeyPo;

public class KeyAo {
	private static KeyAo keyAo = new KeyAo();
	private static KeyDao keyDao = new KeyDao();
	private KeyAo(){
	}
	public static KeyAo get(){
		return keyAo;
	}
	public boolean insertInfoByNaviId(List<UserCustomNaviKeyPo> list){
		return keyDao.insertInfoByNaviId(list);
	}
	public boolean findInfoByNaviId(final List<UserCustomNaviKeyPo> list,Integer navId){
		return keyDao.findInfoByNaviId(list, navId);
	}
	public boolean deleteInfoByNaviId(Integer naviId){
		return keyDao.deleteInfoByNaviId(naviId);
	}
	public boolean deleteInfoByNaviId_AppName_KeyName(List<UserCustomNaviKeyPo> list){
		return keyDao.deleteInfoByNaviId_AppName_KeyName(list);
	}
	public boolean deleteInfoByKeyId(List<Integer> list){
		return keyDao.deleteInfoByKeyId(list);
	}
}
