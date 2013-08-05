package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.TimeConfDao;
import com.taobao.monitor.common.db.impl.center.TimeConfTmpDao;
import com.taobao.monitor.common.po.TimeConfPo;
import com.taobao.monitor.common.po.TimeConfTmpPo;

/**
 * 实时配置类TimeConf的ao
 * @author wuhaiqian.pt
 *
 */
public class TimeConfAo  {

	private static final Logger logger = Logger.getLogger(TimeConfAo.class);

	private static TimeConfAo  ao = new TimeConfAo();
	private TimeConfDao dao = new TimeConfDao();
	private TimeConfTmpDao tmpDao = new TimeConfTmpDao();
	
	private TimeConfAo(){
		
	}

	public static  TimeConfAo get(){
		return ao;
	}
	
	/**
	 * 添加addTimeConfData
	 * @param timeConf
	 */
	public boolean addTimeConfData(TimeConfPo timeConf) {

		return dao.addTimeConfData(timeConf);
	}
	
	/**
	 * 删除TimeConfPo
	 * @param appId
	 */
	public boolean deleteTimeConfByAppId(int appId) {
	
		return dao.deleteTimeConfByAppId(appId);
	}
	
	/**
	 * 删除TimeConfPo
	 * @param confId
	 */
	public boolean deleteTimeConfByConfId(int confId) {

		return dao.deleteTimeConfByConfId(confId);
	}

	/**
	 * 删除TimeConfPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfData(TimeConfPo timeConf) {

		return dao.deleteTimeConfData(timeConf);
	}
	
	/**
	 * 获取全部timeConf
	 * 
	 * @return
	 */
	public List<TimeConfPo> findAllAppTimeConf() {
		
		return dao.findAllAppTimeConf();
	}
	
	/**
	 * 根据timeConfPo更新
	 * @param timeConf
	 * @return
	 */
	public boolean updateTimeConf(TimeConfPo timeConf){
	
		return dao.updateTimeConf(timeConf);
	}
	
	/**
	 * 查询应用所有的监控配置
	 * @param appId
	 * @return
	 */
	public List<TimeConfPo> findTimeConfByAppId(int appId){
		return dao.findTimeConfByAppId(appId);
	}
	
	/**
	 * 根据confId查询对应的配置
	 * @param confId
	 * @return
	 */
	public TimeConfPo findTimeConfByConfId(int confId){
		
		return dao.findTimeConfByConfId(confId);
	}
	/**
	 * 添加addTimeConfTmp
	 * @param timeConf
	 */
	public boolean addTimeConfTmp(TimeConfTmpPo tmpPo) {
		
		return tmpDao.addTimeConfTmp(tmpPo);
	}
	
	/**
	 * 删除TimeConfPo
	 * @param appId
	 */
	public boolean deleteTimeConfTmp(int tmpId) {
		
		return tmpDao.deleteTimeConfTmp(tmpId);
	}
	
	/**
	 * 删除TimeConfPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfTmp(TimeConfTmpPo tmpPo) {
		
		return tmpDao.deleteTimeConfTmp(tmpPo);
	}
	
	/**
	 * 获取全部timeConf
	 * 
	 * @return
	 */
	public List<TimeConfTmpPo> findAllAppTimeConfTmp() {
		
		return tmpDao.findAllAppTimeConfTmp();
	}
	
	/**
	 * 根据tmpId获得对应的tmpPo
	 * 
	 * @return
	 */
	public TimeConfTmpPo findTimeConfTmpById(int tmpId) {
		
		return tmpDao.findTimeConfTmpById(tmpId);
	}
	
	/**
	 * 根据timeConfPo更新
	 * @param timeConf
	 * @return
	 */
	public boolean updateTimeConfTmp(TimeConfTmpPo tmpPo){
		
		return tmpDao.updateTimeConfTmp(tmpPo);
	}
	
	
}
