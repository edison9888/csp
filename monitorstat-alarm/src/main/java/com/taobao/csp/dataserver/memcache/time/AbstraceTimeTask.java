package com.taobao.csp.dataserver.memcache.time;

import java.util.Set;

public abstract class AbstraceTimeTask implements Runnable{

	protected Timeout timeout;
	private volatile Set mySet;
	
	public Set getMySet() {
		return mySet;
	}

	public void setMySet(Set mySet) {
		this.mySet = mySet;
	}
	
	
	public Timeout getTimeout() {
		return timeout;
	}

	public void setTimeout(Timeout timeout) {
		this.timeout = timeout;
	}
	
}
