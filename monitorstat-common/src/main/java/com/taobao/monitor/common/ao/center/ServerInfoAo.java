package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.ServerInfoDao;
import com.taobao.monitor.common.po.ServerInfoPo;

/**
 * 服务器的Ao
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
	 * 添加addServerInfoData
	 * @param timeConf
	 */
	public boolean addServerInfoData(ServerInfoPo serverInfoPo) {
		
		return dao.addServerInfoData(serverInfoPo);
	}
	
	/**
	 * 删除ServerInfoPo
	 * @param appId
	 */
	public boolean deleteHostData(int serverId) {
	
		return dao.deleteHostData(serverId);
	}

	/**
	 * 删除ServerInfoPo
	 * @param timeConf
	 */
	public boolean deleteTimeConfData(ServerInfoPo serverInfoPo) {
	
		return dao.deleteTimeConfData(serverInfoPo);
	}
	
	/**
	 * 获取全部ServerInfoPo
	 * 
	 * @return
	 */
	public List<ServerInfoPo> findAllServerInfo() {
	
		return dao.findAllServerInfo();
	}
	
	/**
	 * 根据id获取ServerInfoPo
	 * 
	 * @return
	 */
	public ServerInfoPo findAllServerInfoById(int id) {
		
		return dao.findAllServerInfoById(id);
	}
	/**
	 * 根据ServerInfoPo更新
	 * @param HostPo
	 * @return
	 */
	public boolean updateServerInfo(ServerInfoPo serverInfoPo){
		
		return dao.updateServerInfo(serverInfoPo);
	}
	
	

	/**
	 * 根据应用id 和类型获取服务器对象
	 * @param appid
	 * @param type
	 * @return
	 */
	public ServerInfoPo findServerInfoByAppIdAndType(int appid,String type) {
		return dao.findServerInfoByAppIdAndType(appid, type);
	}
	
	
}
