
package com.taobao.monitor.web.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.taobao.monitor.web.vo.LoginUserPo;

/**
 * 
 * @author xiaodu
 * @version 2010-11-4 ÉÏÎç10:33:03
 */
public class SessionUtil {
	public static String USER_KEY = "_USER_";
	
	public static LoginUserPo getUserSession(HttpServletRequest request){

		HttpSession session = request.getSession();
		if(session == null){
			return null;
		}
		LoginUserPo po = (LoginUserPo)request.getSession().getAttribute(USER_KEY);
		if(po == null){
			return null;				
		}else{
			return po;
		}
	}
	
	
	
	public static void logout(HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		if(session == null){
			return ;
		}
		
		request.getSession().removeAttribute(USER_KEY);
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie c:cookies){
				c.setValue("");
				Cookie kill = new Cookie(c.getName(),"");
				kill.setMaxAge(0);
				kill.setPath("/");
				response.addCookie(kill);
			}
		}		
	}
	

}
