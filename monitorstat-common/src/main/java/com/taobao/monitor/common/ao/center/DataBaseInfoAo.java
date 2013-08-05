package com.taobao.monitor.common.ao.center;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.DataBaseInfoDao;
import com.taobao.monitor.common.po.AppMysqlInfo;
import com.taobao.monitor.common.po.DataBaseInfoPo;

/**
 * 数据库实体的DAO
 * @author wuhaiqian.pt
 *
 */
public class DataBaseInfoAo  {
	private static final Logger logger = Logger.getLogger(DataBaseInfoAo.class);

	private static DataBaseInfoAo  ao = new DataBaseInfoAo();
	private DataBaseInfoDao dao = new DataBaseInfoDao();
	
	
	private DataBaseInfoAo(){
		
	}

	public static  DataBaseInfoAo get(){
		return ao;
	}
	
	/**
	 * 添加addDataBaseInfoData
	 * @param dataBaseInfoPo
	 */
	public boolean addDataBaseInfoData(DataBaseInfoPo dataBaseInfoPo) {
		
		return dao.addDataBaseInfoData(dataBaseInfoPo);
	}
	
	/**
	 * 删除dataBaseInfoPo
	 * @param database_id
	 */
	public boolean deleteDataBaseInfo(int dbId) {
		
		return dao.deleteDataBaseInfo(dbId);
	}

	/**
	 * 删除dataBaseInfoPo
	 * @param database_id
	 */
	public boolean deleteDataBaseInfo(DataBaseInfoPo dataBaseInfoPo) {
		
		return dao.deleteDataBaseInfo(dataBaseInfoPo);
	}
	
	/**
	 * 获取全部dataBaseInfoPo
	 * 
	 * @return
	 */
	public List<DataBaseInfoPo> findAllDataBaseInfo() {
	
		return dao.findAllDataBaseInfo();
	}
	
	/**
	 * 根据Id获取dataBaseInfoPo
	 * 
	 * @return
	 */
	public DataBaseInfoPo findDataBaseInfoById(int id) {
		
		return dao.findDataBaseInfoById(id);
	}
	/**
	 * 根据dataBaseInfoPo更新
	 * @param HostPo
	 * @return
	 */
	public boolean updateDataBaseInfo(DataBaseInfoPo dataBaseInfoPo){
	
		return dao.updateDataBaseInfo(dataBaseInfoPo);
	}
	
	
	/**
	 * 从历史表中获取 存储信息
	 * @param dataBaseId
	 * @param collectTime yyyyMMdd
	 * @return
	 */
	public Map<Integer,AppMysqlInfo> findAppDatabaseInfo(int dataBaseId,int collectTime){
		return dao.findAppDatabaseInfo(dataBaseId, collectTime);
	}
	/**
	 * 
	 */
	public void saveAppMysqlInfo(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);		
		List<DataBaseInfoPo> list = this.findAllDataBaseInfo();
		for(DataBaseInfoPo database:list){
			if(database.getDbType() == 1){
				Map<Integer,AppMysqlInfo> map = dao.findMysqlDatabaseInfo(database.getDbName(), cal.getTime());
				for(Map.Entry<Integer,AppMysqlInfo> entry:map.entrySet()){
					AppMysqlInfo info = entry.getValue();
					info.setDataBaseId(database.getDbId());
					info.setCollectTime(Integer.parseInt(sdf.format(cal.getTime())));
					dao.addAppMysqlInfo(info);
				}
			}
		}		
	}
	
	
	public long findDataBaseRecordNum(int dataBaseId,int collectTime){
		
		long num = 0;
		Map<Integer,AppMysqlInfo> map = findAppDatabaseInfo(dataBaseId,collectTime);
		
		for(Map.Entry<Integer,AppMysqlInfo> entry:map.entrySet()){
			num+=entry.getValue().getDataNum();
		}
		
		
		return num;
	}
	
}
