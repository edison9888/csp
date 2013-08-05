
package com.taobao.monitor.web.util;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author xiaodu
 * @version 2010-9-17 ÉÏÎç09:41:58
 */
public class UrlCode {
	
	public static String encode(String u){
		StringBuilder sb = new StringBuilder();
		byte[] bs;
		try {
			bs = u.getBytes("GBK");
			for(byte b:bs){
				int v = b;
				sb.append(v+",");
			}
		} catch (UnsupportedEncodingException e) {
		}
		
		return sb.toString();
	}
	
	
	public static String decode(String u){
		String[] vs = u.split(",");
		StringBuilder sb = new StringBuilder();
		byte[] f = null;
		for(int i=0;i<vs.length;i++){
			String v = vs[i];
			Byte b = Byte.parseByte(v);
			if( b>0){
				f = new byte[]{b};
				sb.append(new String(f));
			}else{
				String v1 = vs[i+1];
				Byte b1 = Byte.parseByte(v1);
				f = new byte[]{b,b1};
				try {
					sb.append(new String(f,"GBK"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
			}
			
		}
		return sb.toString();
	}
	
	
	public static void main(String[] s){
		System.out.println(decode("79,84,72,69,82,95,-67,-69,-46,-41,-49,-33,-42,-40,-46,-86,-46,-77,-61,-26,-51,-77,-68,-58,95,-73,-61,-65,-51,-71,-70,-62,-14,45,111,107,95,67,79,85,78,84,84,73,77,69,83,"));;
	}

}
