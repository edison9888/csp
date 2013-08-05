
package com.taobao.wwnotify.biz;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.wwnotify.web.StringUtil;

/**
 * 
 * @author xiaodu
 * @version 2010-12-1 上午10:05:48
 */
public class TextConvertHtml {
	
	private static final String HTTP = "HTTP://";
	private static final String HTTPS = "HTTPS://";
	private static final String HTML_FORMAT = "<a target='_blank' href='{value}'>{value}</a>";
	
	public static String urlConvertHtml(String url){		
		if(url==null){
			return "";
		}
		
		if(url.toUpperCase().indexOf("HTTP")<0){
			return LFtoBr(url);
		}
		
		//检查这个url 是否已经在<a>标签中
		if(check(url)){
			return LFtoBr(url);
		}
		
		StringBuilder sb  = new StringBuilder();
		int len = url.length();
		int index=0;
		for(;index<len;){
			char c = url.charAt(index);
			if(c == 'h'||c == 'H'){
				if(index+HTTPS.length() < len){
					String http = url.substring(index, index+HTTP.length());
					String https = url.substring(index, index+HTTPS.length());
					if(http.toUpperCase().equals(HTTP)||https.toUpperCase().equals(HTTPS)){				
						String u = cutUrl(index,url);
						if(u != null){
							index+=u.length();
							sb.append(HTML_FORMAT.replace("{value}", u));
							continue;
						}
					}
				}		
			}
			index++;
			sb.append(c);
		}
		String html =  sb.toString();
		return LFtoBr(html);
	}
	
	
	private static String LFtoBr(String html){
		StringBuilder tmpStr = new StringBuilder();
		for(int i=0;i<html.length();i++){
			char c = html.charAt(i);
			if(c == '\n'){
				tmpStr.append("<br/>");
			}else if(c == '\r'){
				
			}else{
				tmpStr.append(c);
			}
			
		}
		return tmpStr.toString();
	}
	
	private static boolean check(String url){
		Pattern pattern = Pattern.compile("<a(.*?)>(.*?)<\\/a>");
		Matcher matcher = pattern.matcher(url);
		return matcher.find();
	}
	
	
	
	//如果存在空格符h或非字符表示 url 结束
	private static String cutUrl(int index,String url){
		int i = index;
		for(;i<url.length();){
			char c = url.charAt(i);
			if(c == '\t' ||c == '\n'||c == '\f'||c == '\r'|| c == ' ' || c > 128){
				break;
			}else{
				i++;
			}
		}
		return url.substring(index,i);
	}

}
