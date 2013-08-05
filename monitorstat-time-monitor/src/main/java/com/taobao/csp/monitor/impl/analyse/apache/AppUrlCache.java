
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.apache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.common.ZKClient;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppUrlRelation;

/**
 * 
 * 这里记录了，应用对应的url信息
 * 
 * 
 * 
 * @author xiaodu
 *
 * 上午11:26:42
 */
public class AppUrlCache implements Watcher{
	
	private static AppUrlCache instance = new AppUrlCache();
	/**
	 * 这里记录了所有固定的URL，分割了后面的所有参数
	 * 
	 * 如http://detail.tmall.com/item.htm
	 * http://detail.taobao.com/item.htm
	 * 等
	 */
	private  Set<String> fixedUrlSet = Collections.synchronizedSet(new HashSet<String>());
	/**
	 * 这里记录了所有动态的URL，所以这里需要是一个正则表达式
	 * 如：http://dingjun.tmall.com/shop/viewShop.htm
	 * 
	 * http://shop68587650.taobao.com/
	 * 
	 * 等等，
	 */
	private  Set<String> dynamicUrlSet = Collections.synchronizedSet(new HashSet<String>());
	
	private AppUrlCache(){
		initNode();
		doResetData();
	}
	
	public static AppUrlCache get(){
		return instance;
	}
	
	private void initNode(){
		ZKClient.get().mkdirPersistent("/csp/url_cache");
	}
	
	/**
	 * 更新URl信息
	 */
	public void doResetData(){
		
		fixedUrlSet.clear();
		dynamicUrlSet.clear();
		
		Map<String,List<AppUrlRelation>> map = AppInfoAo.get().findAllAppUrlRelation();
		for(Map.Entry<String,List<AppUrlRelation>> entry:map.entrySet()){
			List<AppUrlRelation> list = entry.getValue();
			for(AppUrlRelation u:list){
				if(u.isDynamic()){
					dynamicUrlSet.add(u.getAppUrl());
				}else{
					fixedUrlSet.add(u.getAppUrl());
				}
			}
		}
		
		try {
			ZKClient.get().getData("/csp/url_cache", this);
		}catch (Exception e) {
		}
	}
	
	
	/**
	 * 
	 * 转换URL 为一个统一的表达式，
	 * 
	 * 这个方法会截取 ？后面的所有字符，保留前面的
	 * 
	 * 
	 * @param url
	 * @return
	 */
	public  String translateUrl(String url){
		
		try{
			int len = url.indexOf("?");
			if(len >0){
				url = url.substring(0,len);
			}
			
			if(url.charAt(url.length()-1) == '/'){
				url = url.substring(0, url.length()-1);
			}
			
			if(fixedUrlSet.contains(url)){
				return url;
			}
			
			for(String p:dynamicUrlSet){
				Pattern pattern = Pattern.compile(p);
				Matcher m = pattern.matcher(url);
				if(m.find()){
					return p;
				}
			}
		}catch (Exception e) {
		}
		return "未知";
	}
	
	
	public static void  main(String[] arges){
		
		
		Pattern pattern = Pattern.compile("http://\\w*.taobao.com/shop/viewShop.htm");
		Matcher m = pattern.matcher("http://shop706726627.taobao.com/shop/viewShop.htm");
		if(m.matches()){
			System.out.println("dd");
		}
	}

	@Override
	public void process(WatchedEvent event) {
		
		switch(event.getType()){
		case NodeDataChanged:
			doResetData();
		;break;
	}
		
	}

}
