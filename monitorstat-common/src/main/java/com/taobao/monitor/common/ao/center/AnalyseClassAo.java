package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.center.AnalyseClassDao;
import com.taobao.monitor.common.po.AnalyseClass;

/**
 * AnalyseClass的DAO
 * @author wuhaiqian.pt
 *
 */
public class AnalyseClassAo  {
	private static final Logger logger = Logger.getLogger(AnalyseClassAo.class);

	private static AnalyseClassAo  ao = new AnalyseClassAo();
	private AnalyseClassDao dao = new AnalyseClassDao();
	
	
	private AnalyseClassAo(){
		
	}

	public static  AnalyseClassAo get(){
		return ao;
	}
	
	public void addAnalyseClassData(int classId, String className, int classType) {
	
		dao.addAnalyseClassData(classId, className, classType);
	}
	
	/**
	 * 添加addAnalyseClassData
	 * @param analyseClass
	 */
	public void addAnalyseClassData(AnalyseClass analyseClass) {
	
		dao.addAnalyseClassData(analyseClass);
	}
	
	/**
	 * 删除AnalyseClass
	 * @param appId
	 */
	public void deleteanalyseClass(int classId) {
		
		dao.deleteanalyseClass(classId);
	}

	/**
	 * 删除AnalyseClass
	 * @param analyseClass
	 */
	public void deleteTimeConfData(AnalyseClass analyseClass) {

		dao.deleteTimeConfData(analyseClass);
	}
	
	/**
	 * 获取全部AnalyseClass
	 * 
	 * @return
	 */
	public List<AnalyseClass> findAllAnalyseClass() {
		
		return dao.findAllAnalyseClass();
	}
	
	/**
	 * 根据AnalyseClass更新
	 * @param analyseClass
	 * @return
	 */
	public boolean updateAnalyseClass(AnalyseClass analyseClass){

		return dao.updateAnalyseClass(analyseClass);
	}
	
}
