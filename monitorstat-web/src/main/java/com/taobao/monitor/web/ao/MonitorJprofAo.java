
package com.taobao.monitor.web.ao;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.web.core.dao.impl.MonitorJprofDao;
import com.taobao.monitor.web.core.po.JprofClassMethod;
import com.taobao.monitor.web.core.po.JprofClassMethodStack;
import com.taobao.monitor.web.core.po.JprofHost;

/**
 * 
 * @author xiaodu
 * @version 2010-8-11 ����11:52:26
 */
public class MonitorJprofAo {
	
	private static MonitorJprofAo jprofAo = new MonitorJprofAo();
	
	private MonitorJprofDao jprofDao = new MonitorJprofDao();
	
	private MonitorJprofAo(){
		
	}
	
	public static  MonitorJprofAo get(){
		return jprofAo;
	}
	
	
	/**
	 * ��ѯ�����вɼ�jprof ����
	 * @param host
	 */
	public List<JprofHost> findAllJprofHosts(){		
		
		return jprofDao.findAllJprofHosts();
	}
	 
	/**
	 * ɾ��һ���ɼ�jprof ����
	 * @param host
	 */
	public void deleteJprofHosts(JprofHost host){		
		jprofDao.deleteJprofHosts(host);		
	}
	/**
	 * ���һ���ɼ�jprof ����
	 * @param host
	 */
	public boolean addJprofHost(JprofHost host){
		return jprofDao.addJprofHost(host);
	}
	
	/**
	 * 
	 * @param classMethod
	 * @return
	 */
	public boolean addClassMethod(JprofClassMethod classMethod){
		return jprofDao.addClassMethod(classMethod);
	}
	
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public List<JprofClassMethod> findJprofClassMethod(String appName,String collectDay){
		return jprofDao.findJprofClassMethod(appName, collectDay);
	}
	
	
	public boolean addClassMethodStack(JprofClassMethodStack classMethodStack){
		return jprofDao.addClassMethodStack(classMethodStack);
	}
	
	/**
	 * 
	 * @param appName
	 * @param className
	 * @param methodName
	 * @param collectDay
	 * @return
	 */
	public Map<String,List<JprofClassMethodStack>> findJprofClassMethodStack(String appName,String className,String methodName,String collectDay){
		return jprofDao.findJprofClassMethodStack(appName, className, methodName, collectDay);
	}
	
	/**
	 * 
	 * @param appName
	 * @param className
	 * @param methodName
	 * @param collectDay
	 * @return
	 */
	public Map<String,String> findJprofClassMethodStackRootParent(String appName,String className,String methodName,String collectDay){
		return jprofDao.findJprofClassMethodStackRootParent(appName, className, methodName,collectDay);
	}
	

	/**
	 * 
	 * @param appName
	 * @param className
	 * @param methodName
	 * @param collectDay
	 * @return
	 */
	public List<JprofClassMethodStack> findJprofClassMethodStack(String appName,String md5,String collectDay){
		return jprofDao.findJprofClassMethodStack(appName, md5, collectDay);
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public JprofHost getJprofHosts(int id){
		return jprofDao.getJprofHosts(id);
	}
	
	/**
	 * �޸�һ���ɼ�jprof ����
	 * @param host
	 */
	public boolean updateJprofHost(JprofHost host){
		return jprofDao.updateJprofHost(host);
	}
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public boolean deleteClassMethodByDay(String appName,String collectDay){
		return jprofDao.deleteClassMethodByDay(appName, collectDay);
	}
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public boolean deleteJprofClassMethodStack(String appName,String collectDay){
		return jprofDao.deleteJprofClassMethodStack(appName, collectDay);
	}
	
}
