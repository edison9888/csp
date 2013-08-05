
package com.taobao.monitor.common.vo;
/**
 * 
 * @author xiaodu
 * @version 2010-4-10 ÏÂÎç03:16:29
 */
public class GcPo {
	
	private String gcname;
	
	private Integer keyId;
	
	private String keyValue;
	
	
	private Integer aveMachinekeyId;
	private Integer aveuserTimeKeyId;
	
	
	public Integer getAveMachinekeyId() {
		return aveMachinekeyId;
	}

	public void setAveMachinekeyId(Integer aveMachinekeyId) {
		this.aveMachinekeyId = aveMachinekeyId;
	}

	public Integer getAveuserTimeKeyId() {
		return aveuserTimeKeyId;
	}

	public void setAveuserTimeKeyId(Integer aveuserTimeKeyId) {
		this.aveuserTimeKeyId = aveuserTimeKeyId;
	}

	private String gcCount;
	
	private String gcAverage;

	public String getGcname() {
		return gcname;
	}

	public void setGcname(String gcname) {
		this.gcname = gcname;
	}

	public String getGcCount() {
		return gcCount;
	}

	public void setGcCount(String gcCount) {
		this.gcCount = gcCount;
	}

	public String getGcAverage() {
		return gcAverage;
	}

	public void setGcAverage(String gcAverage) {
		this.gcAverage = gcAverage;
	}

	public Integer getKeyId() {
		return keyId;
	}

	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

}
