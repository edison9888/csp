package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.TimeConfDao;
import com.taobao.monitor.common.db.impl.center.TimeConfTmpDao;
import com.taobao.monitor.common.po.TimeConfPo;
import com.taobao.monitor.common.po.TimeConfTmpPo;

/**
 * ʵʱ������TimeConf��ao
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
	 * ���addTimeConfData
	 * @param timeConf
	 */
	public boolean addTimeConfData(TimeConfPo timeConf) {

		return dao.addTimeConfData(timeConf);
	}
	
	/**
	 * ɾ��TimeConfPo
	 * @param appId
	 */
	public boolean deleteTimeConfByAppId(int appId) {
	
		return dao.deleteTimeConfByAppId(appId);
	}
	
	/**
	 * ɾ��TimeConfPo
	 * @param confId
	 */
	public boolean deleteTimeConfByConfId(int confId) {

		return dao.deleteTimeConfByConfId(confId);
	}

	/**
	 * ɾ��TimeConfPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfData(TimeConfPo timeConf) {

		return dao.deleteTimeConfData(timeConf);
	}
	
	/**
	 * ��ȡȫ��timeConf
	 * 
	 * @return
	 */
	public List<TimeConfPo> findAllAppTimeConf() {
		
		return dao.findAllAppTimeConf();
	}
	
	/**
	 * ����timeConfPo����
	 * @param timeConf
	 * @return
	 */
	public boolean updateTimeConf(TimeConfPo timeConf){
	
		return dao.updateTimeConf(timeConf);
	}
	
	/**
	 * ��ѯӦ�����еļ������
	 * @param appId
	 * @return
	 */
	public List<TimeConfPo> findTimeConfByAppId(int appId){
		return dao.findTimeConfByAppId(appId);
	}
	
	/**
	 * ����confId��ѯ��Ӧ������
	 * @param confId
	 * @return
	 */
	public TimeConfPo findTimeConfByConfId(int confId){
		
		return dao.findTimeConfByConfId(confId);
	}
	/**
	 * ���addTimeConfTmp
	 * @param timeConf
	 */
	public boolean addTimeConfTmp(TimeConfTmpPo tmpPo) {
		
		return tmpDao.addTimeConfTmp(tmpPo);
	}
	
	/**
	 * ɾ��TimeConfPo
	 * @param appId
	 */
	public boolean deleteTimeConfTmp(int tmpId) {
		
		return tmpDao.deleteTimeConfTmp(tmpId);
	}
	
	/**
	 * ɾ��TimeConfPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfTmp(TimeConfTmpPo tmpPo) {
		
		return tmpDao.deleteTimeConfTmp(tmpPo);
	}
	
	/**
	 * ��ȡȫ��timeConf
	 * 
	 * @return
	 */
	public List<TimeConfTmpPo> findAllAppTimeConfTmp() {
		
		return tmpDao.findAllAppTimeConfTmp();
	}
	
	/**
	 * ����tmpId��ö�Ӧ��tmpPo
	 * 
	 * @return
	 */
	public TimeConfTmpPo findTimeConfTmpById(int tmpId) {
		
		return tmpDao.findTimeConfTmpById(tmpId);
	}
	
	/**
	 * ����timeConfPo����
	 * @param timeConf
	 * @return
	 */
	public boolean updateTimeConfTmp(TimeConfTmpPo tmpPo){
		
		return tmpDao.updateTimeConfTmp(tmpPo);
	}
	
	
}
