
package com.taobao.jprof;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author xiaodu
 * @version 2010-6-23 ÏÂÎç01:46:52
 */
public class JprofData {
	
	private JprofData head;
	
	private JprofData next;
	
	private Queue<JprofData> nextLine = new LinkedList<JprofData>();
	
	private JprofData previous;
	
	private int  methodId;
	
	private long starttime ;
	private long endtime ;
	private int stacknum ;
	
	public JprofData(){
		this.head = this;
	}
		
	public JprofData addNext(int id){		
		JprofData next = new JprofData();
		next.setStarttime(System.currentTimeMillis());
		next.setMethodId(id);	
		next.setPrevious(this);
		next.setHead(this.head);
		this.next = next;
		nextLine.add(next);	
		return next;
	}
	
	
	public int getMethodId() {
		return methodId;
	}
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	public long getStarttime() {
		return starttime;
	}
	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}
	public long getEndtime() {
		return endtime;
	}
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	public int getStacknum() {
		return stacknum;
	}
	public void setStacknum(int stacknum) {
		this.stacknum = stacknum;
	}


	public JprofData getHead() {
		return head;
	}


	public void setHead(JprofData head) {
		this.head = head;
	}


	public JprofData getNext() {
		return next;
	}


	public void setNext(JprofData next) {
		this.next = next;
	}


	public JprofData getPrevious() {
		return previous;
	}


	public void setPrevious(JprofData previous) {
		this.previous = previous;
	}

	public Queue<JprofData> getNextLine() {
		return nextLine;
	}

	public void setNextLine(Queue<JprofData> nextLine) {
		this.nextLine = nextLine;
	}
	
	
	
	
	

}
