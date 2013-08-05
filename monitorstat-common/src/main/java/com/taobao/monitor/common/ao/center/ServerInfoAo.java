package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.ServerInfoDao;
import com.taobao.monitor.common.po.ServerInfoPo;

/**
 * ��������Ao
 * @author wuhaiqian.pt
 *
 */
public class ServerInfoAo  {
	
	private static final Logger logger = Logger.getLogger(ServerInfoAo.class);

	private static ServerInfoAo ao = new ServerInfoAo();
	private ServerInfoDao dao = new ServerInfoDao();
	
	public ServerInfoAo() {
			
	}
	
	public static ServerInfoAo get() {
		
		return ao;
	}
	
	
	/**
	 * ���addServerInfoData
	 * @param timeConf
	 */
	public boolean addServerInfoData(ServerInfoPo serverInfoPo) {
		
		return dao.addServerInfoData(serverInfoPo);
	}
	
	/**
	 * ɾ��ServerInfoPo
	 * @param appId
	 */
	public boolean deleteHostData(int serverId) {
	
		return dao.deleteHostData(serverId);
	}

	/**
	 * ɾ��ServerInfoPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfData(ServerInfoPo serverInfoPo) {
	
		return dao.deleteTimeConfData(serverInfoPo);
	}
	
	/**
	 * ��ȡȫ��ServerInfoPo
	 * 
	 * @return
	 */
	public List<ServerInfoPo> findAllServerInfo() {
	
		return dao.findAllServerInfo();
	}
	
	/**
	 * ����id��ȡServerInfoPo
	 * 
	 * @return
	 */
	public ServerInfoPo findAllServerInfoById(int id) {
		
		return dao.findAllServerInfoById(id);
	}
	/**
	 * ����ServerInfoPo����
	 * @param HostPo
	 * @return
	 */
	public boolean updateServerInfo(ServerInfoPo serverInfoPo){
		
		return dao.updateServerInfo(serverInfoPo);
	}
	
	

	/**
	 * ����Ӧ��id �����ͻ�ȡ����������
	 * @param appid
	 * @param type
	 * @return
	 */
	public ServerInfoPo findServerInfoByAppIdAndType(int appid,String type) {
		return dao.findServerInfoByAppIdAndType(appid, type);
	}
	
	
}
