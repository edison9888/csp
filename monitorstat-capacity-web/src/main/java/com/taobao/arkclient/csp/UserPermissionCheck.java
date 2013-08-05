package com.taobao.arkclient.csp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.taobao.arkclient.ArkDomain;
import com.taobao.monitor.common.db.impl.center.UserInfoDao;
import com.taobao.monitor.common.po.UserInfoPo;

/***
 * 容量的权限控制类
 * 
 * @author youji.zj 2011-10-20
 * 
 */
public class UserPermissionCheck {
	
	
	private static final Logger logger = Logger.getLogger(UserPermissionCheck.class);

	private UserInfoDao userInfoDao;

	public UserInfoDao getUserInfoDao() {
		return userInfoDao;
	}

	public void setUserInfoDao(UserInfoDao userInfoDao) {
		this.userInfoDao = userInfoDao;
	}

	public boolean check(HttpServletRequest request, String type, String aim) {
		
		try {
			if (System.getProperty("os.name").toLowerCase().indexOf("window") > -1) {
				return true;
			}
			String email = ArkDomain.getArkUserEmail(request);
			if (email == null) {
			} else {
				logger.info("容量操作"+email+":"+type+":"+aim);
				UserInfoPo po = userInfoDao.getUserByMail(email);
				if (po != null) {
					return permission(po, type, aim);
				} else {
					return false;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * load:ALL;
	 * 
	 * @param po
	 * @param m
	 * @return
	 */
	private boolean permission(UserInfoPo po, String type, String aim) {

		Pattern pattern = Pattern.compile(type + ":(ALL|[\\d,]+);");

		String permission = po.getPermissionDesc();
		if (permission != null) {
			Matcher matcher = pattern.matcher(permission);
			if (matcher.find()) {
				String p = matcher.group(1);
				if (p.equals("ALL")) {
					return true;
				} else {
					for (String miss : p.split(",")) {
						if (miss.equals(aim)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public static void main(String[] args) {

		Pattern pattern = Pattern.compile("get:(ALL|[\\d,]+);");
		String permission = "user:1,2,3,4;load:1,2,3,4;get:ALL;";
		if (permission != null) {
			Matcher matcher = pattern.matcher(permission);
			if (matcher.find()) {
				String p = matcher.group(1);
				System.out.println(p);
				if (p.equals("ALL")) {
					System.out.println(p);
				} else {
					for (String miss : p.split(",")) {
						if (miss.equals("4")) {
							System.out.println(miss);
						}
					}
				}
			}
		}
	}

}
