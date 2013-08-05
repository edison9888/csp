
package com.taobao.jprof.sort;

import java.util.Stack;

import com.taobao.jprof.Arith;

/**
 * 
 * @author xiaodu
 * @version 2010-8-11 обнГ05:58:06
 */
public class TimeSortData implements Comparable<TimeSortData>{
	
	
	private long allSum;
	public float max;
	public float min;
	public long size = 0;
	public String name;		
	private int stackIndex = -1;		
	public Stack<Long> valueStack = new Stack<Long>();
	
	public Double getValue(){
		Double tmp=0d;
		for(Long v:valueStack){
			tmp=Arith.add(tmp, v);
		}
		return tmp;
	}
	
	public int compareTo(TimeSortData o) {		
		
		if(this.getValue()>o.getValue()){				
			return -1;
		}else{
			return 1;
		}	
	}	

}
