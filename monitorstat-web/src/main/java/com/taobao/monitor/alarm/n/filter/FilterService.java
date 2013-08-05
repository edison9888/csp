
package com.taobao.monitor.alarm.n.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.Service;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 ÏÂÎç03:34:12
 */
public class FilterService implements Service<Filter>{
	
	private static final Logger logger =  Logger.getLogger(FilterService.class);
	
	private static FilterService otherService = new FilterService();

	private FilterService(){
		
	}
	
	public static FilterService get(){
		return otherService;
	}
	
	
	private List<Filter> filterList = new ArrayList<Filter>();
	
	public void init(){
		
	}
	
	public void register(Filter filter){
		synchronized (filterList) {
			if(filter!=null){
				filterList.add(filter);
				logger.info("register Filter:"+filter.getClass().getName());
			}
		}
		
		
	}
	public void unregister(Filter filter){
		synchronized (filterList) {
			if(filter!=null){
				filterList.remove(filter);
				logger.info("unregister Filter:"+filter.getClass().getName());
			}
		}
	}
	
	public boolean lookup(AlarmContext context){
		synchronized (filterList) {
			Iterator<Filter> it = filterList.iterator();
			while(it.hasNext()){
				Filter f= it.next();
				if(f.timeExpiry()){
					if(!f.valid(context)){
						return false;
					}
				}else{
					it.remove();
					logger.info("unregister Filter:"+f.getClass().getName());
				}
			}
		}
		return true;
	}

}
