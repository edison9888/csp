package com.taobao.www.arkclient.csp;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.UserInfoDao;
import com.taobao.www.arkclient.ArkDomain;

public class ManualCurUser {

	private static final Logger logger = Logger.getLogger(ManualCurUser.class);

	private UserInfoDao userInfoDao;

	public UserInfoDao getUserInfoDao() {
		return userInfoDao;
	}

	public void setUserInfoDao(UserInfoDao userInfoDao) {
		this.userInfoDao = userInfoDao;
	}

	public static String getLoginUserName(HttpServletRequest request) {

		String userName = (String) request.getAttribute("manualCurUser");
		if (userName == null || userName.length() == 0) {
			String email = ArkDomain.getArkUserEmail(request);
			if (email != null && email.length() > 0) {
				request.getSession().setAttribute("manualCurUserEmail", email);
				userName = email.split("@")[0];
				request.setAttribute("manualCurUser", userName);
				logger.info("curUserName is :" + userName);
			}
		}
		return userName;
	}
}
