
package com.taobao.monitor.web.vo;
/**
 * 
 * @author xiaodu
 * @version 2010-4-15 ÉÏÎç10:12:56
 */
public class ThreadPo {
	
	private String newThread ="0";
	private String blocked = "0";
	private String runnable= "0";
	private String waiting= "0";
	private String terminated= "0";
	private String timedWaiting= "0";
	public String getNewThread() {
		return newThread;
	}
	public void setNewThread(String newThread) {
		this.newThread = newThread;
	}
	public String getBlocked() {
		return blocked;
	}
	public void setBlocked(String blocked) {
		this.blocked = blocked;
	}
	public String getRunnable() {
		return runnable;
	}
	public void setRunnable(String runnable) {
		this.runnable = runnable;
	}
	public String getWaiting() {
		return waiting;
	}
	public void setWaiting(String waiting) {
		this.waiting = waiting;
	}
	public String getTerminated() {
		return terminated;
	}
	public void setTerminated(String terminated) {
		this.terminated = terminated;
	}
	public String getTimedWaiting() {
		return timedWaiting;
	}
	public void setTimedWaiting(String timedWaiting) {
		this.timedWaiting = timedWaiting;
	}
	
	

}
