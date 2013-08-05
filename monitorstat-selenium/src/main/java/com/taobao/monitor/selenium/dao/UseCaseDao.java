/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.selenium.dao.model.UseCase;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午04:00:24
 * @version 1.0
 */
@Repository
public class UseCaseDao extends MysqlRouteBase{
	private static final Logger logger = Logger.getLogger(UseCaseDao.class);

	/**
	 * 构造注入数据源
	 */
	public UseCaseDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Monitor_Script"));
	}
	
	/**
	 * 新增测试用例
	 * 
	 * @param useCaseText
	 * 2011-5-30 - 下午01:37:04
	 */
	public int addUseCase(UseCase useCase){
		int useCaseId = addUseCasePlugin(useCase);
		useCase.setUseCaseId(useCaseId);
		addUseCaseText(useCase);
		if(useCase != null && useCase.getRcIdArray() !=null
				&& useCase.getRcIdArray().length > 0){
			for(Integer rcId :useCase.getRcIdArray()){
				addUseCaseSeleniumRc(useCaseId ,rcId);
			}
		}
		return useCaseId;
	}
	
	/**
	 * 新增测试用例
	 * 
	 * @param useCaseText
	 * 2011-5-30 - 下午01:37:04
	 */
	public void updateUseCase(UseCase useCase){
		updateUseCasePlugin(useCase);
		updateUseCaseText(useCase);
		deleteUseCaseSeleniumRc(useCase.getUseCaseId());
		if(useCase != null && useCase.getRcIdArray() !=null
				&& useCase.getRcIdArray().length > 0){
			for(Integer rcId :useCase.getRcIdArray()){
				addUseCaseSeleniumRc(useCase.getUseCaseId()
						,rcId);
			}
		}
	}
	
	/**
	 * 删除测试用例
	 * 
	 * @param useCaseText
	 * 2011-5-30 - 下午01:39:00
	 */
	public void deleteUseCase(int useCaseId){
		deleteUseCasePlugin(useCaseId);
		deleteUseCaseText(useCaseId);
		deleteUseCaseSeleniumRc(useCaseId);
	}
	
	/**
	 * 新增测试用例基本信息
	 * 
	 * @param useCaseText
	 * @return
	 * 2011-5-30 - 下午01:15:12
	 */
	private int addUseCasePlugin(UseCase useCase){
		String add_sql = "INSERT INTO csp_selenium_usecase_plugin(usecase_name, usecase_type, " +
				"usecase_version, param, rc_id, base_url, browser, usecase_alias)VALUES(?,?,?,?" +
				",?,?,?,?);";
		try {
			this.execute(add_sql, new Object[]{
					useCase.getUseCaseName(),
					useCase.getUseCaseType(),
					useCase.getUseCaseVersion(),
					useCase.getParam(),
					useCase.getRcId(),
					useCase.getBaseUrl(),
					useCase.getBrowser(),
					useCase.getUseCaseAlias()
					});
		} catch (SQLException e) {
			logger.error("addUseCasePlugin 出错 "+e);
		}
		return getUseCaseIdByName(useCase.getUseCaseName());
	}
	
	/**
	 * 新增测试用例详细信息
	 * 
	 * @param useCase
	 * 2011-5-30 - 下午01:37:27
	 */
	private void addUseCaseText(UseCase useCase){
		String sql = "INSERT INTO csp_selenium_usecase_text(usecase_id,usecase_source," +
				" param_str)VALUES(?,?,?)";
		try {
			this.execute(sql, new Object[]{
					useCase.getUseCaseId(),
					useCase.getUseCaseSource(),
					useCase.getParamStr()
					});
		} catch (SQLException e) {
			logger.error("addUseCaseText 出错 "+e);
		}
	}
	
	/**
	 * 新增测试用例运行的rc信息
	 * 
	 * @param useCase
	 * 2011-5-30 - 下午01:37:27
	 */
	private void addUseCaseSeleniumRc(int useCaseId, int rcId){
		String sql = "INSERT INTO csp_selenium_usecase_rc(usecase_id, selenium_rc_id) VALUES(?,?);";
		try {
			this.execute(sql, new Object[]{
					useCaseId,
					rcId
					});
		} catch (SQLException e) {
			logger.error("addUseCaseSeleniumRc 出错 "+e);
		}
	}
	
	/**
	 * 新增测试用例基本信息
	 * 
	 * @param useCaseText
	 * @return
	 * 2011-5-30 - 下午01:15:12
	 */
	private void updateUseCasePlugin(UseCase useCase){
		String sql = "UPDATE csp_selenium_usecase_plugin set usecase_name=?, usecase_type=? ," +
				"usecase_version=? ,param=? ,rc_id=?, base_url=?, browser=?, usecase_alias=? " +
				"where id=?";
		try {
			this.execute(sql, new Object[]{
					useCase.getUseCaseName(),
					useCase.getUseCaseType(),
					useCase.getUseCaseVersion(),
					useCase.getParam(),
					useCase.getRcId(),
					useCase.getBaseUrl(),
					useCase.getBrowser(),
					useCase.getUseCaseAlias(),
					useCase.getUseCaseId()
					});
		} catch (SQLException e) {
			logger.error("updateUseCasePlugin 出错 "+e);
		}
	}
	
	/**
	 * 新增测试用例详细信息
	 * 
	 * @param useCase
	 * 2011-5-30 - 下午01:37:27
	 */
	private void updateUseCaseText(UseCase useCase){
		String sql = "UPDATE csp_selenium_usecase_text set usecase_source=?," +
				" param_str=? where usecase_id=?";
		try {
			this.execute(sql, new Object[]{
					useCase.getUseCaseSource(),
					useCase.getParamStr(),
					useCase.getUseCaseId()
					});
		} catch (SQLException e) {
			logger.error("updateUseCaseText 出错 "+e);
		}
	}
	
	/**
	 * 删除用例Selenium rc信息
	 * 
	 * @param useCaseId
	 * 2011-6-9 - 下午07:10:06
	 */
	private void deleteUseCaseSeleniumRc(int useCaseId){
		String sql = "DELETE FROM csp_selenium_usecase_rc WHERE usecase_id=?";
		try {
			this.execute(sql, new Object[]{useCaseId});
		} catch (SQLException e) {
			logger.error("deleteUseCaseSeleniumRc 出错 "+e);
		}
	}
	
	/**
	 * 删除用例详细信息
	 * 
	 * @param useCaseId
	 * 2011-6-9 - 下午07:10:16
	 */
	private void deleteUseCaseText(int useCaseId){
		String sql = "DELETE FROM csp_selenium_usecase_text WHERE usecase_id=?";
		try {
			this.execute(sql, new Object[]{useCaseId});
		} catch (SQLException e) {
			logger.error("deleteUseCaseText 出错 "+e);
		}
	}

	/**
	 * 删除用例基本信息
	 * 
	 * @param useCaseId
	 * 2011-6-9 - 下午07:10:06
	 */
	private void deleteUseCasePlugin(int useCaseId){
		String sql = "DELETE FROM csp_selenium_usecase_plugin WHERE id=?";
		try {
			this.execute(sql, new Object[]{useCaseId});
		} catch (SQLException e) {
			logger.error("deleteUseCasePlugin 出错 "+e);
		}
	}
	
//	/**
//	 * 根据测试用例名字获取运行测试用例selenium rc主键
//	 * 
//	 * @param useCaseName
//	 * @return
//	 * 2011-5-30 - 下午01:27:17
//	 */
//	public int getSeleniumRcIdByName(String useCaseName){
//		int rcId = 0;
//		String sql = "SELECT p.rc_id FROM csp_selenium_usecase_plugin p WHERE p.usecase_name=?";
//		try {
//			rcId = this.getIntValue(sql, new Object[]{useCaseName});
//		} catch (SQLException e) {
//			logger.error("getSeleniumRcIdByName 出错 "+e);
//		}
//		return rcId;
//	}
	
	/**
	 * 查询所有的UC
	 * 
	 * @return
	 * 2011-6-3 - 下午04:11:33
	 */
	public List<UseCase> queryAllUseCase(){
		String sql = "SELECT p.base_url,p.browser,p.id,p.param,p.usecase_name,p.usecase_alias, " +
				"p.usecase_type,p.usecase_version FROM csp_selenium_usecase_plugin p ";
		final List<UseCase> list = new ArrayList<UseCase>();
		try {
			this.query(sql, new SqlCallBack() {		
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UseCase uc = new UseCase();
					uc.setBaseUrl(rs.getString("base_url"));
					uc.setBrowser(rs.getString("browser"));
					uc.setUseCaseId(rs.getInt("id"));
					uc.setParam(rs.getString("param"));
					//uc.setRcId(rs.getInt("rc_id"));
					uc.setUseCaseName(rs.getString("usecase_name"));
					uc.setUseCaseAlias(rs.getString("usecase_alias"));
					uc.setUseCaseType(rs.getString("usecase_type"));
					uc.setUseCaseVersion(rs.getLong("usecase_version"));
					list.add(uc);
				}
			});
		} catch (Exception e) {
			logger.error("queryAllUseCase 出错 "+e);
		}
		return list;
	}
	
	/**
	 * 根据测试用例名字获取测试用例Id
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - 下午01:27:17
	 */
	public int getUseCaseIdByName(String useCaseName){
		int useCaseId = 0;
		String sql = "SELECT id FROM csp_selenium_usecase_plugin p WHERE p.usecase_name = ?";
		try {
			useCaseId = this.getIntValue(sql, new Object[]{useCaseName});
		} catch (SQLException e) {
			logger.error("getUseCaseIdByName 出错 "+e);
		}
		return useCaseId;
	}
	
	/**
	 * 根据测试用例名字获取用例运行selenium rc、运行浏览器信息
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - 下午01:27:17
	 */
	public UseCase getUseCasePlugByName(String useCaseName){

		String sql = "SELECT p.rc_id, p.base_url, p.browser, p.id FROM csp_selenium_usecase_plugin p " +
				" WHERE p.usecase_name=?";
		final UseCase useCase = new UseCase();
		try {
			this.query(sql, new Object[]{useCaseName}, new SqlCallBack() {			
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					useCase.setUseCaseId(rs.getInt("id"));
					useCase.setRcId(rs.getInt("rc_id"));
					useCase.setBaseUrl(rs.getString("base_url"));
					useCase.setBrowser(rs.getString("browser"));
				}
			});
		} catch (Exception e) {
			logger.error("getUseCasePlugByName 出错 "+e);
		}
		return useCase;
	}
	
	/**
	 * 根据测试案例名字，版本获取案例信息
	 * 
	 * @param adapterName
	 * @param adapterTime
	 * @return
	 * 2011-5-30 - 下午01:46:38
	 */
	public UseCase getUseCaseText(String adapterName, long adapterTime){
		String sql = "SELECT p.id, p.usecase_name, p.usecase_version, p.param, p.usecase_type, t.usecase_source ,t.param_str " +
				"FROM csp_selenium_usecase_plugin p, csp_selenium_usecase_text t WHERE p.id = t.usecase_id AND p.usecase_name = ?" +
				" AND p.usecase_version = ?";
		final UseCase useCase = new UseCase();
		try {
			this.query(sql, new Object[]{adapterName, adapterTime}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					useCase.setUseCaseId(rs.getInt("id"));
					useCase.setUseCaseName(rs.getString("usecase_name"));
					useCase.setUseCaseVersion(rs.getLong("usecase_version"));
					useCase.setParam(rs.getString("param"));
					useCase.setUseCaseType(rs.getString("usecase_type"));
					useCase.setUseCaseSource(rs.getString("usecase_source"));
					useCase.setParamStr(rs.getString("param_str"));
				}
			});
		} catch (Exception e) {
			logger.error("getuseCase 出错 "+e);
		}
		return useCase;
	}
	
	/**
	 * 根据selenium server ID获取依赖它的用例
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - 下午03:32:10
	 */
	public List<UseCase> getDependUcByRcId(int rcId){
		String sql = "SELECT p.id,p.usecase_name,p.browser,p.usecase_version " +
				" FROM csp_selenium_usecase_plugin p, csp_selenium_usecase_rc rc " +
				" WHERE rc.usecase_id=p.id AND rc.selenium_rc_id=?";
		final List<UseCase> list = new ArrayList<UseCase>();
		try {
			this.query(sql, new Object[]{rcId}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					UseCase uc = new UseCase();
					uc.setUseCaseId(rs.getInt("id"));
					uc.setUseCaseName(rs.getString("usecase_name"));
					uc.setBrowser(rs.getString("browser"));
					uc.setUseCaseVersion(rs.getLong("usecase_version"));
					list.add(uc);	
				}
			});
		} catch (Exception e) {
			logger.error("getDependUcByRcId 出错 "+e);
		}
		return list;
	}
	
	/**
	 * 获取用例基本信息&&详细信息
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - 下午01:27:17
	 */
	public UseCase getUseCaseByPrimaryKey(int useCaseId){
		String sql = "SELECT t.usecase_source, p.base_url,p.browser,p.id,p.param,p.rc_id,p.usecase_name, p.usecase_type, " +
				" p.usecase_version, p.usecase_alias FROM csp_selenium_usecase_plugin p, csp_selenium_usecase_text t " +
				" WHERE p.id = t.usecase_id AND p.id=?";
		final UseCase uc = new UseCase();
		try {
			this.query(sql, new Object[]{useCaseId}, new SqlCallBack() {		
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					uc.setBaseUrl(rs.getString("base_url"));
					uc.setBrowser(rs.getString("browser"));
					uc.setUseCaseId(rs.getInt("id"));
					uc.setParam(rs.getString("param"));
					uc.setRcId(rs.getInt("rc_id"));
					uc.setUseCaseAlias(rs.getString("usecase_alias"));
					uc.setUseCaseName(rs.getString("usecase_name"));
					uc.setUseCaseType(rs.getString("usecase_type"));
					uc.setUseCaseSource(rs.getString("usecase_source"));
				}
			});
		} catch (Exception e) {
			logger.error("getUseCaseByPrimaryKey 出错 "+e);
		}
		List<Integer> rcList = getDependRcByUcId(useCaseId);
		Integer[] rcIdArray = new Integer[rcList.size()];
		rcList.toArray(rcIdArray);
		uc.setRcIdArray(rcIdArray);
		return uc;
	}
	
	/**
	 * 获取用例运行的selenium rc ID
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - 下午03:32:10
	 */
	public List<Integer> getDependRcByUcId(final int ucId){
		String sql = "SELECT rc.selenium_rc_id FROM csp_selenium_usecase_rc rc " +
				" WHERE rc.usecase_id=?";
		final List<Integer> rcList = new ArrayList<Integer>();
		try {
			this.query(sql, new Object[]{ucId}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					rcList.add(rs.getInt("selenium_rc_id"));	
				}
			});
		} catch (Exception e) {
			logger.error("getDependRcByUcId 出错 "+e);
		}
		return rcList;
	}
}
