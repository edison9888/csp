package com.taobao.monitor.common.ao.center;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.db.impl.center.AnalyseClassDao;
import com.taobao.monitor.common.po.AnalyseClass;

/**
 * AnalyseClass��DAO
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
	 * ���addAnalyseClassData
	 * @param analyseClass
	 */
	public void addAnalyseClassData(AnalyseClass analyseClass) {
	
		dao.addAnalyseClassData(analyseClass);
	}
	
	/**
	 * ɾ��AnalyseClass
	 * @param appId
	 */
	public void deleteanalyseClass(int classId) {
		
		dao.deleteanalyseClass(classId);
	}

	/**
	 * ɾ��AnalyseClass
	 * @param analyseClass
	 */
	public void deleteTimeConfData(AnalyseClass analyseClass) {

		dao.deleteTimeConfData(analyseClass);
	}
	
	/**
	 * ��ȡȫ��AnalyseClass
	 * 
	 * @return
	 */
	public List<AnalyseClass> findAllAnalyseClass() {
		
		return dao.findAllAnalyseClass();
	}
	
	/**
	 * ����AnalyseClass����
	 * @param analyseClass
	 * @return
	 */
	public boolean updateAnalyseClass(AnalyseClass analyseClass){

		return dao.updateAnalyseClass(analyseClass);
	}
	
}
