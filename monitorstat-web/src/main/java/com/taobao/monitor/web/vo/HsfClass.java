
package com.taobao.monitor.web.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2010-4-12 обнГ02:43:17
 */
public class HsfClass implements Comparable<HsfClass>{
	private long count;
	
	private List<HsfPo> hsfPo=new ArrayList<HsfPo>();

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<HsfPo> getHsfPo() {
		return hsfPo;
	}

	public void setHsfPo(List<HsfPo> hsfPo) {
		this.hsfPo = hsfPo;
	}

	public int compareTo(HsfClass o) {
		
		if(o.count==this.count){
			return 0;
		}else if(o.count>this.count){
			return 1;
		}else if(o.count<this.count){
			return -1;
		}
		return 0;
	}
	
}
