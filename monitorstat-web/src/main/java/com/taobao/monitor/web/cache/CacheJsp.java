
package com.taobao.monitor.web.cache;

import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.web.util.RequestByUrl;


/**
 * 缓存了日报页面的全部数据
 * @author xiaodu
 * @version 2010-8-12 上午09:14:26
 */
public class CacheJsp {	
	private static CacheJsp cache = new CacheJsp();		
	private CacheJsp(){}
	
	public static CacheJsp get(){
		return cache;
	}
	/**
	 * key :app  keyName time
	 * 
	 * 保存同期的一天的数据
	 */
	
	
	private static Map<String,JspMessage> map = new HashMap<String, JspMessage>();
	
	
	public String getDayHtml(String url,String date,boolean reset){
		
		synchronized (map) {
			JspMessage jsp = map.get(url);	
			if(jsp == null){
				jsp = new JspMessage();
				map.put(url, jsp);
			}
			
			if(jsp.getStartDate()==null||!jsp.getStartDate().equals(date)||reset){
				String dayHtml= RequestByUrl.getMessageByJsp(url);
				jsp.setMsg(dayHtml);
				jsp.setStartDate(date);	
			}
			return jsp.getMsg();
		}
		
		
	}
	
	private class JspMessage{
		private String startDate;
		
		private String msg;

		public String getStartDate() {
			return startDate;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		
		
		
	}
	

}
