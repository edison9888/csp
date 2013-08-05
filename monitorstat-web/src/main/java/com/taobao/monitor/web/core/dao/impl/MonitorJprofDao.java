
package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.core.po.JprofClassMethod;
import com.taobao.monitor.web.core.po.JprofClassMethodStack;
import com.taobao.monitor.web.core.po.JprofHost;

/**
 * 
 * @author xiaodu
 * @version 2010-8-11 上午11:53:55
 */
public class MonitorJprofDao extends MysqlRouteBase{
	
	public MonitorJprofDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Jprof"));
	}
	
	private static final Logger logger =  Logger.getLogger(MonitorJprofDao.class);

	
	/**
	 * 查询出所有采集jprof 机器
	 * @param host
	 */
	public List<JprofHost> findAllJprofHosts(){
		
		final List<JprofHost> jprofHostList = new ArrayList<JprofHost>();
		
		String sql = "select * from MS_JPROF_HOST ";
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					JprofHost host = new JprofHost();
					host.setAppName(rs.getString("APP_NAME"));
					host.setHostIp(rs.getString("HOST_IP"));
					host.setHostUser(rs.getString("HOST_USER"));
					host.setHostPasswd(rs.getString("HOST_PASSWD"));
					host.setFilePath(rs.getString("FILE_PATH"));
					host.setId(rs.getInt("ID"));
					host.setRunType(rs.getInt("RUN_TYPE"));
					jprofHostList.add(host);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return jprofHostList;
	}
	 
	/**
	 * 删除一个采集jprof 机器
	 * @param host
	 */
	public void deleteJprofHosts(JprofHost host){
		
		String sql = "delete from MS_JPROF_HOST where id=?";
		try {
			this.execute(sql, new Object[]{host.getId()});
		} catch (SQLException e) {
			logger.error("", e);
		}
		
	}
	/**
	 * 添加一个采集jprof 机器
	 * @param host
	 */
	public boolean addJprofHost(JprofHost host){
		
		String sql = "insert into MS_JPROF_HOST(APP_NAME,HOST_IP,HOST_USER,HOST_PASSWD,FILE_PATH,RUN_TYPE)" +
				" values(?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{host.getAppName(),host.getHostIp(),host.getHostUser(),host.getHostPasswd(),host.getFilePath(),host.getRunType()});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 修改一个采集jprof 机器
	 * @param host
	 */
	public boolean updateJprofHost(JprofHost host){
		
		String sql = "update MS_JPROF_HOST set APP_NAME=?,HOST_IP=?,HOST_USER=?,HOST_PASSWD=?,FILE_PATH=?,RUN_TYPE=?" +
				" where id=?";
		try {
			this.execute(sql, new Object[]{host.getAppName(),host.getHostIp(),host.getHostUser(),host.getHostPasswd(),host.getFilePath(),host.getRunType(),host.getId()});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param id
	 * @return
	 */
	public JprofHost getJprofHosts(int id){
		
		final JprofHost host = new JprofHost();
		
		String sql = "select * from MS_JPROF_HOST where id=? ";
		try {
			this.query(sql, new Object[]{id}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
					host.setAppName(rs.getString("APP_NAME"));
					host.setHostIp(rs.getString("HOST_IP"));
					host.setHostUser(rs.getString("HOST_USER"));
					host.setHostPasswd(rs.getString("HOST_PASSWD"));
					host.setFilePath(rs.getString("FILE_PATH"));
					host.setId(rs.getInt("ID"));
					host.setRunType(rs.getInt("RUN_TYPE"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return host.getAppName()==null?null:host;
	}
	
	
	public boolean addClassMethod(JprofClassMethod classMethod){		
		String sql = "insert into MS_JPROF_CLASS_METHOD(APP_NAME,CLASS_NAME,METHOD_NAME,LINE_NUM,EXCUTE_NUM,USE_TIME,COLLECT_DAY)" +
				" values(?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{classMethod.getAppName(),classMethod.getClassName(),classMethod.getMethodName(),classMethod.getLineNum(),classMethod.getExcuteNum(),classMethod.getUseTime(),classMethod.getCollectDay()});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public boolean deleteClassMethodByDay(String appName,String collectDay){
		String sql = "delete from MS_JPROF_CLASS_METHOD where APP_NAME=? and COLLECT_DAY=?";
		try {
			this.execute(sql, new Object[]{appName,collectDay});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public boolean deleteJprofClassMethodStack(String appName,String collectDay){
		
		String sql = "delete from MS_JPROF_CLASS_METHOD_STACK where APP_NAME=? and COLLECT_DAY=?";
		try {
			this.execute(sql, new Object[]{appName,collectDay});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
		
	}
	
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public List<JprofClassMethod> findJprofClassMethod(String appName,String collectDay){
		String sql = "select * from MS_JPROF_CLASS_METHOD where APP_NAME=? and COLLECT_DAY=? AND use_time > 0 ORDER BY use_time DESC LIMIT 20";
		
		final List<JprofClassMethod> list = new ArrayList<JprofClassMethod>();
		
		try {
			this.query(sql, new Object[]{appName,collectDay}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					JprofClassMethod c = new JprofClassMethod();
					c.setAppName(rs.getString("APP_NAME"));
					c.setClassName(rs.getString("CLASS_NAME"));
					c.setMethodName(rs.getString("METHOD_NAME"));
					c.setExcuteNum(rs.getLong("EXCUTE_NUM"));
					c.setUseTime(rs.getDouble("USE_TIME"));
					c.setLineNum(rs.getInt("LINE_NUM"));
					c.setId(rs.getInt("ID"));
					c.setCollectDay(rs.getString("COLLECT_DAY"));
					list.add(c);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;		
	}
	
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public List<JprofClassMethod> findJprofTopDaoMethod(String appName,String collectDay){
		String sql = "SELECT * FROM MS_JPROF_CLASS_METHOD WHERE APP_NAME=? AND COLLECT_DAY=? AND class_name LIKE '%dao%' AND use_time > 0 ORDER BY use_time DESC LIMIT 10";
		
		final List<JprofClassMethod> list = new ArrayList<JprofClassMethod>();
		
		try {
			this.query(sql, new Object[]{appName,collectDay}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					JprofClassMethod c = new JprofClassMethod();
					c.setAppName(rs.getString("APP_NAME"));
					c.setClassName(rs.getString("CLASS_NAME"));
					c.setMethodName(rs.getString("METHOD_NAME"));
					c.setExcuteNum(rs.getLong("EXCUTE_NUM"));
					c.setUseTime(rs.getDouble("USE_TIME"));
					c.setLineNum(rs.getInt("LINE_NUM"));
					c.setId(rs.getInt("ID"));
					c.setCollectDay(rs.getString("COLLECT_DAY"));
					list.add(c);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;		
	}	
	
	/**
	 * 
	 * @param appName
	 * @param collectDay
	 * @return
	 */
	public List<JprofClassMethod> findJprofTopExternalMethod(String appName,String collectDay){
		String sql = "SELECT * FROM MS_JPROF_CLASS_METHOD WHERE APP_NAME=? AND COLLECT_DAY=? AND (class_name LIKE '%hsf%' OR class_name LIKE '%tair%' OR class_name LIKE '%notify%' OR class_name LIKE '%tfs%') AND use_time > 0 ORDER BY use_time DESC LIMIT 10";
		
		final List<JprofClassMethod> list = new ArrayList<JprofClassMethod>();
		
		try {
			this.query(sql, new Object[]{appName,collectDay}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					JprofClassMethod c = new JprofClassMethod();
					c.setAppName(rs.getString("APP_NAME"));
					c.setClassName(rs.getString("CLASS_NAME"));
					c.setMethodName(rs.getString("METHOD_NAME"));
					c.setExcuteNum(rs.getLong("EXCUTE_NUM"));
					c.setUseTime(rs.getDouble("USE_TIME"));
					c.setLineNum(rs.getInt("LINE_NUM"));
					c.setId(rs.getInt("ID"));
					c.setCollectDay(rs.getString("COLLECT_DAY"));
					list.add(c);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;		
	}
	
	/**
	 * 
	 * @param appName
	 * @param className
	 * @param methodName
	 * @return
	 */
	public Map<String,String> findJprofClassMethodStackRootParent(String appName,String className,String methodName,String collectDay){
		
		String sql = "select distinct ROOT_PARENT_CLASS,MD5_STACK from  MS_JPROF_CLASS_METHOD_STACK where APP_NAME=? and CLASS_NAME=? and METHOD_NAME=? and COLLECT_DAY=?";
		
		final Map<String,String> map = new HashMap<String, String>();
		
		try {
			this.query(sql, new Object[]{appName,className,methodName,collectDay}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					map.put(rs.getString("ROOT_PARENT_CLASS"),rs.getString("MD5_STACK"));					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
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
		
		String sql = "select * from MS_JPROF_CLASS_METHOD_STACK where APP_NAME=? and COLLECT_DAY=? and ROOT_PARENT_CLASS in (" +
				"select distinct ROOT_PARENT_CLASS from  MS_JPROF_CLASS_METHOD_STACK where APP_NAME=? and CLASS_NAME=? and METHOD_NAME=? and COLLECT_DAY=?" +
				")";
		
		final Map<String,List<JprofClassMethodStack>> mapJprofClassMethodStack = new HashMap<String, List<JprofClassMethodStack>>();
		
		try {
			this.query(sql, new Object[]{appName,collectDay,appName,className,methodName,collectDay}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					String rootParentClass = rs.getString("ROOT_PARENT_CLASS");
					List<JprofClassMethodStack> list = mapJprofClassMethodStack.get(rootParentClass);
					if(list == null){
						list = new ArrayList<JprofClassMethodStack>();
						mapJprofClassMethodStack.put(rootParentClass,list);
					}
					
					JprofClassMethodStack stack = new JprofClassMethodStack();
					stack.setAppName(rs.getString("APP_NAME"));
					stack.setClassName(rs.getString("CLASS_NAME"));
					stack.setMethodName(rs.getString("METHOD_NAME"));
					stack.setLineNum(rs.getInt("LINE_NUM"));
					stack.setParentClass(rs.getString("PARENT_CLASS"));
					stack.setRootParentClass(rs.getString("ROOT_PARENT_CLASS"));
					list.add(stack);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return mapJprofClassMethodStack;
		
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
		
		String sql = "select s.*,m.EXCUTE_NUM,m.USE_TIME from MS_JPROF_CLASS_METHOD_STACK s left join MS_JPROF_CLASS_METHOD m on " +
				" s.APP_NAME=m.APP_NAME and s.COLLECT_DAY=m.COLLECT_DAY and s.CLASS_NAME=m.CLASS_NAME and s.LINE_NUM=m.LINE_NUM" +
				" where s.APP_NAME=? and s.COLLECT_DAY=? and s.MD5_STACK =? " +
				" ";
		
		final List<JprofClassMethodStack> mapJprofClassMethodStack = new ArrayList<JprofClassMethodStack>();
		
		try {
			this.query(sql, new Object[]{appName,collectDay,md5}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {					
					JprofClassMethodStack stack = new JprofClassMethodStack();
					stack.setAppName(rs.getString("APP_NAME"));
					stack.setClassName(rs.getString("CLASS_NAME"));
					stack.setMethodName(rs.getString("METHOD_NAME"));
					stack.setLineNum(rs.getInt("LINE_NUM"));
					stack.setStackNum(rs.getInt("STACK_NUM"));
					stack.setParentClass(rs.getString("PARENT_CLASS"));
					stack.setRootParentClass(rs.getString("ROOT_PARENT_CLASS"));
					stack.setExcuteNum(rs.getLong("EXCUTE_NUM"));
					stack.setUseTime(rs.getDouble("USE_TIME"));
					mapJprofClassMethodStack.add(stack);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return mapJprofClassMethodStack;
		
	}
	
	
	
	/**
	 * 
	 * @param classMethodStack
	 * @return
	 */
	public boolean addClassMethodStack(JprofClassMethodStack classMethodStack){		
		String sql = "insert into MS_JPROF_CLASS_METHOD_STACK(APP_NAME,CLASS_NAME,METHOD_NAME,LINE_NUM,STACK_NUM,ROOT_PARENT_CLASS,PARENT_CLASS,COLLECT_DAY,MD5_STACK)" +
				" values(?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{classMethodStack.getAppName(),classMethodStack.getClassName(),classMethodStack.getMethodName(),classMethodStack.getLineNum(),classMethodStack.getStackNum(),classMethodStack.getRootParentClass(),classMethodStack.getParentClass(),classMethodStack.getCollectDay(),classMethodStack.getMd5()});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	

	
	
	

}
