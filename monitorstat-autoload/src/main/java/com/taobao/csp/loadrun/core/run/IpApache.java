
package com.taobao.csp.loadrun.core.run;

import com.taobao.csp.loadrun.core.control.IControl;

/**
 * 
 * @author xiaodu
 * @version 2011-7-12 обнГ09:13:31
 */
public class IpApache {
	


	private String ip;

	private String configName;
	
	private IControl control;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public IControl getControl() {
		return control;
	}

	public void setControl(IControl control) {
		this.control = control;
	}

}
