
package com.taobao.monitor.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.web.vo.LoginUserPo;

/**
 * 
 * @author xiaodu
 * @version 2010-10-20 ÏÂÎç05:39:06
 */
public class UserPermissionCheck {
	
	public static boolean check(HttpServletRequest request,String type,String aim){

		
//		LoginUserPo po = SessionUtil.getUserSession(request);
//		if(po != null){
//			return permission(po,type,aim);				
//		}else{
//			return false;
//		}
		return true;
	}
	
	/**
	 * load:ALL;
	 * 
	 * @param po
	 * @param m
	 * @return
	 */
	private static boolean permission(LoginUserPo po,String type,String aim){
		
//		Pattern pattern = Pattern.compile(type+":(ALL|[\\d,]+);");
//		
//		String permission = po.getPermissionDesc();
//		if(permission != null){			
//			Matcher matcher = pattern.matcher(permission);
//			if(matcher.find()){
//				String p = matcher.group(1);
//				if(p.equals("ALL")){
//					return true;
//				}else{
//					for(String miss:p.split(",")){
//						if(miss.equals(aim)){
//							return true;							
//						}
//					}
//				}				
//			}
//		}
//		
//		
//		return false;
		return true;
	}
	
	
	
//	public static void main(String[] args){
//		
//		Pattern pattern = Pattern.compile("load:(ALL|[\\d,]+);");
//		String permission = "user:1,2,3,4;load:1,2,3,4;get:ALL;";
//		if(permission != null){			
//			Matcher matcher = pattern.matcher(permission);
//			if(matcher.find()){
//				String p = matcher.group(1);
//				System.out.println(p);
//				if(p.equals("ALL")){
//					System.out.println(p);
//				}else{
//					for(String miss:p.split(",")){
//						if(miss.equals("4")){
//							System.out.println(miss);						
//						}
//					}
//				}				
//			}
//		}
//	}

}
