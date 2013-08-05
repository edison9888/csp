package com.taobao.monitor.web.backup;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 数据备份中数据的过滤的抽象类
 * @author wuhaiqian.pt
 *
 */
public abstract class Filter {

	Filter next = null;		//指向下一个过滤器，责任链的思想

	boolean accept = false;	//过滤器的开关

	public Map<String, KeyValuePo> doFilter(Map<String, KeyValuePo> map) {
		
		if (next == null) {
			
			return map;
		}
		else {
			
			return next.doFilter(map);
		}
		
	}
	
	public Filter getNext() {

		return next;

	}

	public void setNext(Filter next) {

		this.next = next;

	}


	public boolean isAccept() {
		return accept;
	}


	public void setAccept(boolean accept) {
		this.accept = accept;
	}
}