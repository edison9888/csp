
package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.UserInfoPo;

/**
 * 
 * @author xiaodu
 * @version 2010-9-26 下午05:27:26
 */
public class UserInfoDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(UserInfoDao.class);
	
	
	/**
	 * 根据id 获取用户
	 * @param id
	 * @return
	 */
	public UserInfoPo getLoginUserPo(Integer id){
		String sql = "select * from ms_monitor_user where id=?";
		
		final UserInfoPo po = new UserInfoPo();
		
		try {
			this.query(sql, new Object[]{id}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					setValue(po,rs);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getName()==null?null:po;
	}
	
	/**
	 * 根据用户名和密码获取账号
	 * @param name
	 * @param pwd
	 * @return
	 */
	public UserInfoPo getLoginUserPo(String name,String pwd){
		String sql = "select * from ms_monitor_user where user_name=? and user_password=password(?)";
		
		final UserInfoPo po = new UserInfoPo();
		
		try {
			this.query(sql, new Object[]{name,pwd}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					setValue(po,rs);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getName()==null?null:po;
		
	}
	
	/**
	 * 根据用户名获取账号
	 * @param name
	 * @return
	 */
	public UserInfoPo getLoginUserPo(String name){
		String sql = "select * from ms_monitor_user where user_name=?";
		
		final UserInfoPo po = new UserInfoPo();
		
		try {
			this.query(sql, new Object[]{name}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					setValue(po,rs);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getName()==null?null:po;
		
	}
	
	/**
	 * 获取全部用户账号
	 * @return
	 */
	public List<UserInfoPo> findAllUser(){
		String sql = "select * from ms_monitor_user";
		
		final List<UserInfoPo> list = new ArrayList<UserInfoPo>();
		
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					UserInfoPo po = new UserInfoPo();
					setValue(po,rs);
					list.add(po);					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 更新用户表
	 * @param po
	 * @return
	 */
	public boolean updateLoginUserPo(UserInfoPo po){
		
		//判断表单提交的有没有问题
		if(po == null || po.getName().equals("") || po.getWangwang().equals("") || po.getMail().equals("") || po.getPhone().equals("")) {
			
			return false;
		}
		String sql = "update ms_monitor_user set phone=?,wangwang=?,group_id=?,wangwang_feature=?,phone_feature=?,mail=?,report_desc=?,permission_desc=? where id=? ";
		try {
			this.execute(sql, new Object[]{po.getPhone(), po.getWangwang(), po.getGroup(), po.getSendWwFeature(),
					po.getSendPhoneFeature(), po.getMail(),po.getReportDesc(),po.getPermissionDesc(),po.getId()});
			return true;
		} catch (SQLException e) {
			logger.error("updateLoginUserPo", e);
		}
		
		return false;
	}
	
	
	/**
	 * 通过邮件地址获取信息
	 * @param mail
	 * @return
	 */
	public UserInfoPo getUserByMail(String mail){
		String sql = "select * from ms_monitor_user where mail=?";
		
		final UserInfoPo po = new UserInfoPo();
		
		try {
			this.query(sql, new Object[]{mail},new SqlCallBack(){

				
				public void readerRows(ResultSet rs) throws Exception {
					setValue(po,rs);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getMail()==null?null:po;
	}
//	
	
	
	private void setValue(UserInfoPo po,ResultSet rs) throws SQLException{
		po.setId(rs.getInt("id"));
		po.setName(rs.getString("user_name"));
		po.setPassword(rs.getString("user_password"));
		po.setPermissionDesc(rs.getString("permission_desc"));					
		po.setPhone(rs.getString("phone"));
		po.setWangwang(rs.getString("wangwang"));
		po.setGroup(rs.getString("group_id"));
		po.setSendPhoneFeature(rs.getString("phone_feature"));
		po.setSendWwFeature(rs.getString("wangwang_feature"));
		po.setMail(rs.getString("mail"));
		po.setReportDesc(rs.getString("report_desc"));
	}
	

}
