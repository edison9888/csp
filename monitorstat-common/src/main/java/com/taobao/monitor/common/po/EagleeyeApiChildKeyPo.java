package com.taobao.monitor.common.po;

import java.util.ArrayList;
import java.util.List;
/**
 * eagleeye key up and downµÄ°ü×°
 * @author zhongting.zy
 */
public class EagleeyeApiChildKeyPo {
	private String appName;
	private String time;	
	private String keyName;
	
	private EagleeyeChildKeyListPo topo = new EagleeyeChildKeyListPo();

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public EagleeyeChildKeyListPo getTopo() {
		return topo;
	}

	public void setTopo(EagleeyeChildKeyListPo topo) {
		this.topo = topo;
	}

}
