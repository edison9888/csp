package com.taobao.monitor.common.ao.center;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.center.HostDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;

/**
 * ������DAO
 * @author wuhaiqian.pt
 *
 */
public class HostAo  {
	private static final Logger logger = Logger.getLogger(HostAo.class);

	private static HostAo ao = new HostAo();
	private HostDao dao = new HostDao();
	
	public HostAo() {
			
	}
	
	public static HostAo get() {
		
		return ao;
	}
	
	/**
	 * ���addHostData
	 * @param hostPo
	 */
	public boolean addHostData(HostPo hostPo) {
	
		return dao.addHostData(hostPo);
	}
	
	/**
	 * ɾ��HostPo
	 * @param appId
	 */
	public boolean deleteHostData(int appId) {
	
		return dao.deleteHostData(appId);
	}
	
	/**
	 * ɾ��HostPo
	 * @param hostId
	 */
	public boolean deleteHostbyHostId(int hostId) {
		
		return dao.deleteHostbyHostId(hostId);
	}

	/**
	 * ɾ��HostPo
	 * @param hostPo
	 */
	public boolean deleteHostData(HostPo hostPo) {

		return dao.deleteHostData(hostPo);
	}
	
	/**
	 * ����hostid����hostPo
	 * @param hostId
	 * @return
	 */
	public HostPo findHostByHostId(int hostId) {
		
		return dao.findHostByHostId(hostId);
	}
	/**
	 * ��ȡȫ��HostPo
	 * 
	 * @return
	 */
	public List<HostPo> findAllHost() {
		
		return dao.findAllHost();
	}
	
	/**
	 * ����HostPo����
	 * @param HostPo
	 * @return
	 */
	public boolean updateHostInfo(HostPo hostPo){
		
		return dao.updateHostInfo(hostPo);
	}
	/**
	 * ��ѯ�����Ӧ����������Ҫ��صĻ����б�
	 * @param appId
	 * @return
	 */
	public List<HostPo> findTimeAppHost(int appId){
		return dao.findAppAllHost(appId);
	}
	
	/**
	 * ����appId���ذ����ж�Ӧ����HostList��AppInfoPo
	 * 
	 * @return
	 */
	public AppInfoPo findAppWithHostListByAppId(int appId) {
		
		return dao.findAppWithHostListByAppId(appId);
	}
	
	
	/**
	 * ���hostList
	 * @param hostList
	 */
	public boolean addHostList(List<HostPo> hostList) {
		
		
		return dao.addHostList(hostList);
	}
	/**
	 *@author wb-lixing
	 *2012-3-8 ����07:34:21 
	 */
	public List<HostPo> findAllSyncHostInfos(String opsName) {
		return dao.findAllSyncHostInfos(opsName);
	}
	/**
	 *@author wb-lixing
	 *2012-3-8 ����07:34:21 
	 */
	public boolean deleteHostListSync(String[] ipList) {
		return dao.deleteHostListSync(ipList);
	}
	
	/**
	 *@author wb-lixing
	 *2012-3-8 ����07:34:21 
	 */
	public boolean deleteHostListSyncByOpsName(String opsName) {
		return dao.deleteHostListSyncByOpsName(opsName);
	}

	/**
	 *@author wb-lixing
	 *2012-3-8 ����07:34:21 
	 */
	public boolean addHostListSync(List<HostPo> hostList){
		return dao.addHostListSync(hostList);
	}
	/**
	 * ɾ��HostPo
	 * @param hostIdList
	 */
	public boolean deleteHostList(String[] hostIdList) {
		
		return dao.deleteHostList(hostIdList);
	}
	
	
	
	/**
	 * ����hostIp��AppId��ѯhost�Ƿ����
	 * 
	 * @return
	 */
	public boolean isExistHostByHostIpAndAppId(int appId, String hostIp) {
		
		return dao.isExistHostByHostIpAndAppId(appId, hostIp);
	}
	
	/**
	 * ���س־ñ�ĸ���
	 * 
	 * @return
	 */
	public int sumOfSaveData(int appId) {
		
		return dao.sumOfSaveData(appId);
	}
	
	
	
	/*****************************************************/
	
	public boolean addSyncOpsHostInfo(HostPo hostPo,int version){
		return dao.addSyncPeSystemHostInfo(hostPo,version);
	}
	
	
	public boolean deleteSyncOpsHostInfo(String appName,int version){
		return dao.deleteSyncPeSystemHostInfo(appName,version);
	}
	
	public List<HostPo> findAllSyncHostInfos(){
		
		int version = dao.getSyncVersion();
		
		return dao.findAllSyncHostInfos(version);
	}
	
	public List<HostPo> findSyncHostInfos(String opsName,int version){
		return dao.findAllSyncHostInfos(opsName,version);
	}
	
//	public boolean addSyncOpsFlag(){
//		HostPo hostPo = new HostPo();
//		hostPo.setOpsName("CSP-SYNC-OPS-FLAG");
//		return dao.addSyncPeSystemHostInfo(hostPo);
//	}
	public boolean deleteSyncOldVersion(int version){
		return dao.deleteSyncOldVersion(version);
	}
	
	public int isSync(){
		return dao.isSync();
	}
	public boolean addSync(int flag){
		return dao.addSync(flag);
	}
	
	
	public int getSyncOpsVersion(){
		
		return dao.getSyncVersion();
	}
	
	
	public boolean updateSyncVersion(int version){
		return dao.updateSyncVersion(version);
	}
	
	
	
}
