package com.taobao.monitor.web.backup;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.po.KeyValuePo;

/**
 * ���ݱ��������ݵĹ��˵ĳ�����
 * @author wuhaiqian.pt
 *
 */
public abstract class Filter {

	Filter next = null;		//ָ����һ������������������˼��

	boolean accept = false;	//�������Ŀ���

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