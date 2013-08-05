
package com.taobao.csp.config.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.config.dao.MonitorUserDao;
import com.taobao.csp.config.po.LoginUserPo;


/**
 * 
 * @author xiaodu
 * @version 2010-10-20 下午04:53:47
 */
public class MonitorUserAo {
	private static final Logger logger =  Logger.getLogger(MonitorUserAo.class);
	
	private static MonitorUserAo ao = new MonitorUserAo();
	
	private MonitorUserDao dao = new MonitorUserDao();
	
	
	private MonitorUserAo(){
	}


	public static  MonitorUserAo get(){
		return ao;
	}
	
	
	/**
	 * 返回根据用户名过滤后的list
	 * @param matcher
	 * @return
	 */
	public List<LoginUserPo> findMatcherUser(List<LoginUserPo> list, String matcherStr) {
		
		List<LoginUserPo> filterList = new ArrayList<LoginUserPo>();
		  Pattern pattern = Pattern.compile(".*"+ matcherStr+ ".*",Pattern.CASE_INSENSITIVE);
		  Matcher matcher = null;
		for(LoginUserPo po : list) {
			if(po.getName() != null) {
				matcher = pattern.matcher(po.getName());
				if(matcher.matches()) {
					filterList.add(po);
				}
			}
		}
		
		return filterList;
		
	}
		
	/**
	 * 根据keyName过滤
	 * 返回过滤后的list
	 * @param matcher
	 * @return
	 */
	public List<LoginUserPo> findMatcherAppName(List<LoginUserPo> list, String matcherappId) {
			
		List<LoginUserPo> filterList = new ArrayList<LoginUserPo>();
		//Pattern pattern = Pattern.compile(".*"+ matcherappId+ ".*",Pattern.CASE_INSENSITIVE);
		for(LoginUserPo userPo : list) {
			if(userPo.getGroup() != null) {
				String[] g =  userPo.getGroup().split(",");
				for(String s:g){
					if(s.equals(matcherappId)){
						filterList.add(userPo);
						break;
					}
				}
			}
		}
		return filterList;
	}
	/**
	 * 
	 * @param name
	 * @param pwd
	 * @return
	 */
	public LoginUserPo getLoginUserPo(String name,String pwd){
		return dao.getLoginUserPo(name, pwd);
	}
	
	/**
	 * 根据用户名获取账号
	 * @param name
	 * @return
	 */
	public LoginUserPo getLoginUserPo(String name){
		
		return dao.getLoginUserPo(name);
	}
	
	/**
	 * 根据id 获取用户
	 * @param id
	 * @return
	 */
	public LoginUserPo getLoginUserPo(Integer id){
		return dao.getLoginUserPo(id);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<LoginUserPo> findAllUser(){		
		return dao.findAllUser();		
	}
	
	/**
	 * 更新用户表
	 * @param po
	 * @return
	 */
	public boolean updateLoginUserPo(LoginUserPo po){
		boolean result = dao.updateLoginUserPo(po);
		return result;
	}
	
	/**
	 * 删除一个用户
	 * @param id
	 * @return
	 */
	public boolean deleteLoginUserPo(Integer id){
		return dao.deleteLoginUserPo(id);
	}
	

	/**
	 * 添加监控接收者
	 * 
	 * @param po
	 */
	public boolean addLoginUserPo(LoginUserPo po) {
		boolean result =  dao.addLoginUserPo(po);
		return result;
	}
	
	/**
	 * 通过邮件地址获取信息
	 * @param mail
	 * @return
	 */
	public LoginUserPo getUserByMail(String mail){
		return dao.getUserByMail(mail);
	}
	
	

}
