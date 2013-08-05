package com.taobao.csp.alarm.po.api;



import java.util.ArrayList;
import java.util.List;

// {"list":[{"appname":"cart","callnum":123321},{"appname":"mercy","callnum":13434}],"time":1355108832843,"totalnum":10232323}
public class HsfProviderReferPo {
	private class AppCall {
		private AppCall(String appname, long callnum) {
			this.appname = appname;
			this.callnum = callnum;
		}
		private String appname;
		private long callnum;
		public String getAppname() {
			return appname;
		}
		public void setAppname(String appname) {
			this.appname = appname;
		}
		public long getCallnum() {
			return callnum;
		}
		public void setCallnum(long callnum) {
			this.callnum = callnum;
		}
	}
	
	private List<AppCall> list = new ArrayList<AppCall>();
	private long time;
	private long totalnum;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public List<AppCall> getList() {
		return list;
	}
	public void addToList(String appname, long callnum) {
		AppCall call = new AppCall(appname, callnum);
		this.list.add(call);
	}
	public long getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(long totalnum) {
		this.totalnum = totalnum;
	}
}
