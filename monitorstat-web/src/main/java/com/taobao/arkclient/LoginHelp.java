
package com.taobao.arkclient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.taobao.arkclient.Service.ConfigManager;
import com.taobao.monitor.web.ao.MonitorUserAo;
import com.taobao.monitor.web.util.SessionUtil;
import com.taobao.monitor.web.vo.LoginUserPo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-10 ÏÂÎç02:17:54
 */
public class LoginHelp {
	
	private static ConfigManager configManager = ConfigManager.getInstance();
	
	public static void doDomain(HttpServletRequest request,HttpServletResponse response ) throws RedirectException{
		String s = request.getRequestURI().substring(request.getContextPath().length());
		if (!(configManager.isCommonUnProtect(s) || configManager.isUserUnProtect(s))) {
			try {
				new ArkAuthenRequest(request, response);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		doSession(request);
	}
	
	private static void doSession(HttpServletRequest request){
		try{
			String email = ArkDomain.getArkUserEmail(request);
			if(email==null){
			}else{
				LoginUserPo po = (LoginUserPo)request.getSession(true).getAttribute(SessionUtil.USER_KEY);
				if(po == null){
					po= MonitorUserAo.get().getUserByMail(email);
					if(po == null){
					}else{
						request.getSession(true).setAttribute(SessionUtil.USER_KEY, po);
					}					
				}else{
				}
				
			}
		}catch (Exception e) {
			System.out.println("LoginHelp.doSession");
		}
	}

}
