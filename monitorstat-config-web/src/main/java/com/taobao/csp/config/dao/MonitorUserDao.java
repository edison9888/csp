
package com.taobao.csp.config.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.config.po.LoginUserPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 
 * @author xiaodu
 * @version 2010-9-26 下午05:27:26
 */
public class MonitorUserDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(MonitorUserDao.class);
	
	
	
	/**
	 * 根据id 获取用户
	 * @param id
	 * @return
	 */
	public LoginUserPo getLoginUserPo(Integer id){
		String sql = "select * from ms_monitor_user where id=?";
		
		final LoginUserPo po = new LoginUserPo();
		
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
	public LoginUserPo getLoginUserPo(String name,String pwd){
		String sql = "select * from ms_monitor_user where user_name=? and user_password=password(?)";
		
		final LoginUserPo po = new LoginUserPo();
		
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
	public LoginUserPo getLoginUserPo(String name){
		String sql = "select * from ms_monitor_user where user_name=?";
		
		final LoginUserPo po = new LoginUserPo();
		
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
	public List<LoginUserPo> findAllUser(){
		String sql = "select * from ms_monitor_user";
		
		final List<LoginUserPo> list = new ArrayList<LoginUserPo>();
		
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					LoginUserPo po = new LoginUserPo();
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
	public boolean updateLoginUserPo(LoginUserPo po){
		
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
	 * 删除一个用户
	 * @param id
	 * @return
	 */
	public boolean deleteLoginUserPo(Integer id){
		String sql = "delete from ms_monitor_user where id=?";
		try {
			this.execute(sql, new Object[]{id});
			return true;
		} catch (SQLException e) {
			logger.error("", e);
		}		
		return false;
	}
	
	
	public boolean addLoginUserPo(LoginUserPo po){
		

		//判断表单提交的有没有问题
		if(po == null || po.getName().equals("") || po.getWangwang().equals("") || po.getMail().equals("") || po.getPhone().equals("")) {
			
			return false;
		}
		List<LoginUserPo> userList = this.findAllUser();
		for(LoginUserPo userPo : userList) {
			
			if(userPo.getName()!=null&&userPo.getName().equals(po.getName())){
				return false;
			}
			if(userPo.getWangwang()!=null&&userPo.getWangwang().equals(po.getWangwang())){
				return false;
			}
			if(userPo.getPhone()!=null&&userPo.getPhone().equals(po.getPhone())){
				return false;
			}
			if(userPo.getMail()!=null&&userPo.getMail().equals(po.getMail())){
				return false;
			}
			
		}
		 
			String sql = "insert into ms_monitor_user" +
					"(user_name,phone,wangwang,group_id,wangwang_feature,phone_feature,mail,permission_desc,user_password,report_desc) " +
					"values(?,?,?,?,?,?,?,?,password(?),?)";
			try {
				this.execute(sql, new Object[] { po.getName(), po.getPhone(), po.getWangwang(), po.getGroup(),
						po.getSendWwFeature(), po.getSendPhoneFeature(), po.getMail() ,po.getPermissionDesc(),po.getPassword(),po.getReportDesc()});
			} catch (SQLException e) {
				logger.error("", e);
				return false;
			}
		return true;
		
	}
	
	
	/**
	 * 取得所有监控接收者
	 * 
	 * @return
	 */
//	public List<AlarmUserPo> findAllAlarmUser() {
//		String sql = "select * from ms_monitor_user";
//
//		final List<AlarmUserPo> userList = new ArrayList<AlarmUserPo>();
//
//		try {
//			this.query(sql, new SqlCallBack() {
//				
//				public void readerRows(ResultSet rs) throws Exception {
//					AlarmUserPo po = new AlarmUserPo();
//					po.setId(rs.getInt("id"));
//					po.setName(rs.getString("user_name"));
//					po.setPhone(rs.getString("phone"));
//					po.setWangwang(rs.getString("wangwang"));
//					po.setGroup(rs.getString("group_id"));
//					po.setSendPhoneFeature(rs.getString("phone_feature"));
//					po.setSendWwFeature(rs.getString("wangwang_feature"));
//					po.setMail(rs.getString("mail"));
//					userList.add(po);
//				}
//			});
//		} catch (Exception e) {
//			logger.error("", e);
//		}
//		return userList;
//	}
	
	
	/**
	 * 通过邮件地址获取信息
	 * @param mail
	 * @return
	 */
	public LoginUserPo getUserByMail(String mail){
		String sql = "select * from ms_monitor_user where mail=?";
		
		final LoginUserPo po = new LoginUserPo();
		
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
	
	
	
	private void setValue(LoginUserPo po,ResultSet rs) throws SQLException{
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
