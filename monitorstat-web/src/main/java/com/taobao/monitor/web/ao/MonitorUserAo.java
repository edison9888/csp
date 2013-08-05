
package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.user.UserDefine;
import com.taobao.monitor.alarm.n.user.UserService;
import com.taobao.monitor.alarm.po.ExtraUserAppKeyDefine;
import com.taobao.monitor.web.core.dao.impl.MonitorUserDao;
import com.taobao.monitor.web.vo.LoginUserPo;

/**
 * 
 * @author xiaodu
 * @version 2010-10-20 ����04:53:47
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
	 * ��ȡӦ�ö���key�Ľ��յĶ�������
	 * @param userId
	 * @return
	 */
	public List<ExtraUserAppKeyDefine> getExtraUserAppKeyDefine(int userId){
		return dao.getExtraUserAppKeyDefine(userId);
	}
	
	
	/**
	 * ���ظ����û������˺��list
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
	 * ����keyName����
	 * ���ع��˺��list
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
	 * �����û�����ȡ�˺�
	 * @param name
	 * @return
	 */
	public LoginUserPo getLoginUserPo(String name){
		
		return dao.getLoginUserPo(name);
	}
	
	/**
	 * ����id ��ȡ�û�
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
	 * �����û���
	 * @param po
	 * @return
	 */
	public boolean updateLoginUserPo(LoginUserPo po){
		boolean result = dao.updateLoginUserPo(po);
		if(result){
			
			UserService.get().register(UserService.get().createUserDefine(po));
			
		}
		return result;
	}
	
	/**
	 * ɾ��һ���û�
	 * @param id
	 * @return
	 */
	public boolean deleteLoginUserPo(Integer id){
		boolean result = dao.deleteLoginUserPo(id);
		if(result){
			UserDefine define = new UserDefine();
			define.setUserId(id);
			UserService.get().register(define);
		}
		
		
		return result;
	}
	

	/**
	 * ��Ӽ�ؽ�����
	 * 
	 * @param po
	 */
	public boolean addLoginUserPo(LoginUserPo po) {
		boolean result =  dao.addLoginUserPo(po);
		if(result){
			UserService.get().register(UserService.get().createUserDefine(po));
		}
		return result;
	}
	
	/**
	 * ͨ���ʼ���ַ��ȡ��Ϣ
	 * @param mail
	 * @return
	 */
	public LoginUserPo getUserByMail(String mail){
		return dao.getUserByMail(mail);
	}
	
	

}
