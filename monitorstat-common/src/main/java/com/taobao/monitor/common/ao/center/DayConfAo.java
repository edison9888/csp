package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.DayConfDao;
import com.taobao.monitor.common.db.impl.center.DayConfTmpDao;
import com.taobao.monitor.common.po.DayConfPo;
import com.taobao.monitor.common.po.DayConfTmpPo;

public class DayConfAo {

	private static final Logger logger =  Logger.getLogger(DayConfAo.class);

	private static DayConfAo  ao = new DayConfAo();
	private DayConfDao dao = new DayConfDao();
	
	private DayConfTmpDao tmpDao = new DayConfTmpDao();
	
	private DayConfAo(){
		
	}

	public static  DayConfAo get(){
		return ao;
	}
	
	public boolean addDayConfData(DayConfPo dayConf) {
		
		return dao.addDayConfData(dayConf);
	}
	
	/**
	 * 删除DayConfPo
	 * @param appId
	 */
	public void deleteDayConfByAppId(int appId) {
		
		dao.deleteDayConfByAppId(appId);
	}
	

	/**
	 * 删除DayConfPo
	 * @param confId
	 */
	public void deleteDayConfByConfId(int confId) {
		
		dao.deleteDayConfByConfId(confId);
	}
	
	
	public void deleteDayConfData(DayConfPo dayConf) {
		
		dao.deleteDayConfData(dayConf);
	}
	
	public List<DayConfPo> findAllAppDayConf() {
		
		return dao.findAllAppDayConf();
	}
	
	public boolean updateDayConf(DayConfPo dayConf){
		
		return dao.updateDayConf(dayConf);
	}
	
	
	/**
	 * 根据confId获取dayConf
	 * @return
	 */
	public DayConfPo findAppDayConfByConfId(int confId) {
		
		return dao.findAppDayConfByConfId(confId);
	}
	/**
	 * 根据实时应用appid 查询出所有dayConf
	 * @return
	 */
	public List<DayConfPo> findAllAppDayConfByAppId(int appId) {
		
		return dao.findAllAppDayConfById(appId);
	}
	
	/**
	 * 添加addDayConfTmp
	 * @param DayConfTmpPo
	 */
	public boolean addDayConfTmp(DayConfTmpPo tmpPo) {
		
		return tmpDao.addDayConfTmp(tmpPo);
	}
	
	
	/**
	 * 删除deleteDayConfTmp
	 * @param tmpId
	 */
	public boolean deleteDayConfTmp(int tmpId) {
		
		return tmpDao.deleteDayConfTmp(tmpId);
	}
	
	/**
	 * 删除deleteDayConfTmp
	 * @param dayConf
	 */
	public boolean deleteDayConfTmp(DayConfTmpPo tmpPo) {
		
		return tmpDao.deleteDayConfTmp(tmpPo);
	}
	
	/**
	 * 获取全部DayConfTmpPo
	 * @return
	 */
	public List<DayConfTmpPo> findAllDayConfTmp() {
		
		return tmpDao.findAllDayConfTmp();
	}
	
	/**
	 * 根据DayConfTmpPo更新
	 * @param tmpPo
	 * @return
	 */
	public boolean updateDayConfTmp(DayConfTmpPo tmpPo){
		
		return tmpDao.updateDayConfTmp(tmpPo);
	}
	
	/**
	 * 根据tmpId获取DayConfTmpPo
	 * @return
	 */
	public DayConfTmpPo findDayConfTmpById(int tmpId) {
		
		return tmpDao.findDayConfTmpById(tmpId);
	}
	
	
}
