
package com.taobao.csp.capacity.model;


/**
 * 
 * @author xiaodu
 * @version 2011-4-6 上午10:48:46
 */
public class Coordinate implements Comparable<Coordinate>{
	
	public Coordinate(){
		
	}
	
	public Coordinate(long x,double y){
		this.x = x;
		this.y = y;
	}
	
	
	private long x; 
	
	private double y;
	
	private long tmpValue; //用于存放一些临时值
	


	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}


	@Override
	public int compareTo(Coordinate o) {
		
		if(o.x == this.x){
			return 0;
		}else if(o.x <this.x){
			return 1;
		}else{
			return -1;
		}
	}

	public long getTmpValue() {
		return tmpValue;
	}

	public void setTmpValue(long tmpValue) {
		this.tmpValue = tmpValue;
	}
	
	

}
