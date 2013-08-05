package com.taobao.sentinel.permission;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.taobao.arkclient.ArkDomain;
import com.taobao.sentinel.dao.UserPermissionDao;

public class UserPermissionCheck {
	
	
	private static final Logger logger = Logger.getLogger(UserPermissionCheck.class);

	private UserPermissionDao userPermissionDao;

	public UserPermissionDao getUserPermissionDao() {
		return userPermissionDao;
	}

	public void setUserPermissionDao(UserPermissionDao userPermissionDao) {
		this.userPermissionDao = userPermissionDao;
	}

	public boolean hasPermission(HttpServletRequest request, String appName) {
		boolean hasPermission = false;
		try {
			String email = ArkDomain.getArkUserEmail(request);
			if (email == null) {
				logger.info("sentinel permission: mail is null");
			} else {
				logger.info("sentinel permission:" + email+":" + appName);
				Set<String> apps = userPermissionDao.getAppsByUser(email);
				
				boolean isAdministrator = userPermissionDao.isAdministrator(email);
				if (apps != null) {
					hasPermission = apps.contains(appName) || isAdministrator;
				} 
			}
		} catch (Exception e) {
			logger.info("sentinel permission: mail is null: " + e);
		}
		
		return hasPermission;
	}
	
	public boolean isAdministrator(HttpServletRequest request) {
		
		try {
			String email = ArkDomain.getArkUserEmail(request);
			if (email == null) {
				logger.info("sentinel permission: mail is null");
			} else {
				return userPermissionDao.isAdministrator(email);
			}
		} catch (Exception e) {
			logger.info("sentinel permission: mail is null: " + e);
		}
		
		return false;
	}
}
