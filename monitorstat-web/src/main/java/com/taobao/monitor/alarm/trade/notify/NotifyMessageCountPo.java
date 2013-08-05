package com.taobao.monitor.alarm.trade.notify;


public class NotifyMessageCountPo {
	private String date;
	private String count;
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "NotifyMessageCountPo [date=" + date + ", count=" + count + "]";
	}

}
