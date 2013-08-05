
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io;


/**
 * @author xiaodu
 *
 * обнГ6:58:47
 */
public class Heartbeat {
	
	private long beat = System.currentTimeMillis();
	
	private String jvmMemory;
	

	public long getBeat() {
		return beat;
	}

	public void setBeat(long beat) {
		this.beat = beat;
	}

	public String getJvmMemory() {
		return jvmMemory;
	}

	public void setJvmMemory(String jvmMemory) {
		this.jvmMemory = jvmMemory;
	}


}
