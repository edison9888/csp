
package com.taobao.monitor.dependent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * 
 * @author xiaodu
 * @version 2011-4-26 ÏÂÎç04:16:20
 */
public class VipCache {
	
	
	private static Set<String> vipSet = new HashSet<String>();
	
	
	public static boolean isVip(String vip){
		return vipSet.contains(vip);
	}
	
	public static String getVipInfo(String vip){
		
		//
		BufferedInputStream input = null;
		try {
			URL url = new URL("http://vipviewer.corp.taobao.com/index.php?ip="+vip);
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(10000);
			urlCon.connect();
			input = new BufferedInputStream(urlCon.getInputStream());
			
			BufferedReader readerf = new BufferedReader(new InputStreamReader(input,"utf-8"));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while((str=readerf.readLine())!=null){
				sb.append(str);
			}
			
			Perl5Compiler compiler = new Perl5Compiler();

			Perl5Matcher matcher = new Perl5Matcher();

			PatternMatcherInput inputContent = new PatternMatcherInput(sb.toString());

			org.apache.oro.text.regex.Pattern p = compiler.compile("VIP\\s+<b>(.*)</b>\\s+is\\s+on\\s+<b>");

			while(matcher.contains(inputContent,p)) {

			       MatchResult result = matcher.getMatch();
			      
			       System.out.println( result.group(1));
			       return result.group(1);
			}
			
		}  catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
	public static void main(String[] args){
		getAllVip();
	}
	
	public static void getAllVip(){
		
		vipSet.clear();
		
		
		BufferedInputStream input = null;
		try {
			URL url = new URL("http://vipviewer.corp.taobao.com/colo/All.html");
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(1000000);
			urlCon.connect();
			input = new BufferedInputStream(urlCon.getInputStream());
			
			BufferedReader readerf = new BufferedReader(new InputStreamReader(input,"utf-8"));
			String str = null;
			StringBuffer sb = new StringBuffer();
			while((str=readerf.readLine())!=null){
				sb.append(str);
			}
			
			Perl5Compiler compiler = new Perl5Compiler();

			Perl5Matcher matcher = new Perl5Matcher();

			PatternMatcherInput inputContent = new PatternMatcherInput(sb.toString());

			org.apache.oro.text.regex.Pattern p = compiler.compile("ip=(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.\\d{1,3})\"");

			while(matcher.contains(inputContent,p)) {

			       MatchResult result = matcher.getMatch();
			       String vip = result.group(1);
			       vipSet.add(vip);
			}
			
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
