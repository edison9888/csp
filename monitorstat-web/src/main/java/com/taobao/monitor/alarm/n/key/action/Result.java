
package com.taobao.monitor.alarm.n.key.action;

import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;

/**
 * 
 * @author xiaodu
 * @version 2011-3-1 ÉÏÎç10:59:55
 */
public class Result {
	
	public Result(String m,KeyJudgeEnum e){
		this.message = m;
		this.e = e;
	}
	
	private String message;
	
	private KeyJudgeEnum e;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public KeyJudgeEnum getE() {
		return e;
	}

	public void setE(KeyJudgeEnum e) {
		this.e = e;
	}
	
	

}
