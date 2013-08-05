
package com.taobao.monitor.dependent.ao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.dependent.dao.BeiDouDao;
import com.taobao.monitor.dependent.dao.CspDependentDao;
import com.taobao.monitor.dependent.po.AppDependentRelationPo;

/**
 * 
 * @author xiaodu
 * @version 2011-4-22 下午01:58:17
 */
public class CspDependentAo extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(CspDependentAo.class);
	
	private CspDependentDao dao = new CspDependentDao();
	
	private BeiDouDao beiDouDao = new BeiDouDao();
	
	
	private static CspDependentAo ao = new CspDependentAo();
	
	private CspDependentAo(){
	}
	
	public static CspDependentAo get(){
		return ao;
	}
	
	
	public boolean addAppDependentRelation(AppDependentRelationPo po){
		
		return dao.addAppDependentRelation(po);
		
	}
	
	
	public boolean addMeDependent(AppDependentRelationPo po){
		if(po.getDependentIp()!=null)
			return dao.addMeDependent(po);
		return true;
		
	}
	
	public boolean addDependentMe(AppDependentRelationPo po){
		if(po.getDependentIp()!=null)
			return dao.addDependentMe(po);
		return true;
	}
	
	/**
	 * 获取所有我依赖的应用
	 * @param self
	 * @param collectTime
	 * @return
	 */
	public List<AppDependentRelationPo> findMeDependent(String self,Date collectTime){
		return dao.findMeDependent(self, collectTime);
	}
	
	/**
	 * 获取所有依赖我的应用
	 * @param self
	 * @param collectTime
	 * @return
	 */
	public List<AppDependentRelationPo> findDependentMe(String self,Date collectTime){
		return dao.findDependentMe(self, collectTime);
	}
	
	
	public boolean addAppDependentRelation(String self,String dependent){
		AppDependentRelationPo po = new AppDependentRelationPo();
		po.setCollectTime(new Date());
		po.setDependentOpsName(dependent);
		po.setSelfOpsName(self);
		return addAppDependentRelation(po);
		
	}
	
	public Set<String> getAllAppOpsName(Date collectTime){
		return dao.getAllAppOpsName(collectTime);
	}
	
	public Map<String,String> findOracleInfo(){
		return beiDouDao.findOracleInfo();
	}

}
