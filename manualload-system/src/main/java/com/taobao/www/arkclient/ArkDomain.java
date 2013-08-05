
package com.taobao.www.arkclient;

import javax.servlet.http.HttpServletRequest;

 
public class ArkDomain {
	private final static String USERITEM = "Ark:User";
	
	public static String getArkUserEmail(HttpServletRequest request){
		
		
		return indexMsg(1,request);
	}
	public static String getArkUserDomainName(HttpServletRequest request){
		return indexMsg(2,request);
	}
	public static String getArkUserJobNumber(HttpServletRequest request){
		return indexMsg(0,request);
	}
	public static String getArkUserJobName(HttpServletRequest request){
		return indexMsg(3,request);
	}
	
	
	private static String indexMsg(int index,HttpServletRequest request){
		if (request.getAttribute(USERITEM) != null) {			
			String[] useritem = request.getAttribute(USERITEM).toString().split("&");
			if(useritem.length>index)
				return useritem[index];
			else
				return null;
		} else {
			return null;
		}
		
	}

}
