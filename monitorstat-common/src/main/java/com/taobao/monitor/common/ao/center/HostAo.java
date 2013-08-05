package com.taobao.monitor.common.ao.center;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.center.HostDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;

/**
 * 主机的DAO
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
	 * 添加addHostData
	 * @param hostPo
	 */
	public boolean addHostData(HostPo hostPo) {
	
		return dao.addHostData(hostPo);
	}
	
	/**
	 * 删除HostPo
	 * @param appId
	 */
	public boolean deleteHostData(int appId) {
	
		return dao.deleteHostData(appId);
	}
	
	/**
	 * 删除HostPo
	 * @param hostId
	 */
	public boolean deleteHostbyHostId(int hostId) {
		
		return dao.deleteHostbyHostId(hostId);
	}

	/**
	 * 删除HostPo
	 * @param hostPo
	 */
	public boolean deleteHostData(HostPo hostPo) {

		return dao.deleteHostData(hostPo);
	}
	
	/**
	 * 根据hostid返回hostPo
	 * @param hostId
	 * @return
	 */
	public HostPo findHostByHostId(int hostId) {
		
		return dao.findHostByHostId(hostId);
	}
	/**
	 * 获取全部HostPo
	 * 
	 * @return
	 */
	public List<HostPo> findAllHost() {
		
		return dao.findAllHost();
	}
	
	/**
	 * 根据HostPo更新
	 * @param HostPo
	 * @return
	 */
	public boolean updateHostInfo(HostPo hostPo){
		
		return dao.updateHostInfo(hostPo);
	}
	/**
	 * 查询出这个应用下所有需要监控的机器列表
	 * @param appId
	 * @return
	 */
	public List<HostPo> findTimeAppHost(int appId){
		return dao.findAppAllHost(appId);
	}
	
	/**
	 * 根据appId返回包含有对应包含HostList的AppInfoPo
	 * 
	 * @return
	 */
	public AppInfoPo findAppWithHostListByAppId(int appId) {
		
		return dao.findAppWithHostListByAppId(appId);
	}
	
	
	/**
	 * 添加hostList
	 * @param hostList
	 */
	public boolean addHostList(List<HostPo> hostList) {
		
		
		return dao.addHostList(hostList);
	}
	/**
	 *@author wb-lixing
	 *2012-3-8 下午07:34:21 
	 */
	public List<HostPo> findAllSyncHostInfos(String opsName) {
		return dao.findAllSyncHostInfos(opsName);
	}
	/**
	 *@author wb-lixing
	 *2012-3-8 下午07:34:21 
	 */
	public boolean deleteHostListSync(String[] ipList) {
		return dao.deleteHostListSync(ipList);
	}
	
	/**
	 *@author wb-lixing
	 *2012-3-8 下午07:34:21 
	 */
	public boolean deleteHostListSyncByOpsName(String opsName) {
		return dao.deleteHostListSyncByOpsName(opsName);
	}

	/**
	 *@author wb-lixing
	 *2012-3-8 下午07:34:21 
	 */
	public boolean addHostListSync(List<HostPo> hostList){
		return dao.addHostListSync(hostList);
	}
	/**
	 * 删除HostPo
	 * @param hostIdList
	 */
	public boolean deleteHostList(String[] hostIdList) {
		
		return dao.deleteHostList(hostIdList);
	}
	
	
	
	/**
	 * 根据hostIp和AppId查询host是否存在
	 * 
	 * @return
	 */
	public boolean isExistHostByHostIpAndAppId(int appId, String hostIp) {
		
		return dao.isExistHostByHostIpAndAppId(appId, hostIp);
	}
	
	/**
	 * 返回持久表的个数
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
