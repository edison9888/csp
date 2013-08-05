package com.taobao.monitor.common.po;

/**
 * 行过滤器
 * @author zhongting.zy
 *
 */
public class RowAnalyseConfigWeb implements Comparable<RowAnalyseConfigWeb>{
	private String timeFormat;	//默认 为空，表示毫秒数
	private String logConfigWeb;
	private String regularString;
	private String keyConfig;
	private int keyNumber;
	private String valueConfig;
	private int valueNumber;
	private String keyOrder;
	
	private String rootKeyConfig = "";	//默认为空，表示直接从日志中解析
	
	public String getTimeFormat() {
		return timeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	public String getLogConfigWeb() {
		return logConfigWeb;
	}
	public void setLogConfigWeb(String logConfigWeb) {
		this.logConfigWeb = logConfigWeb;
	}
	public String getRegularString() {
		return regularString;
	}
	public void setRegularString(String regularString) {
		this.regularString = regularString;
	}
	public String getKeyConfig() {
		return keyConfig;
	}
	public void setKeyConfig(String keyConfig) {
		this.keyConfig = keyConfig;
	}
	public int getKeyNumber() {
		return keyNumber;
	}
	public void setKeyNumber(int keyNumber) {
		this.keyNumber = keyNumber;
	}
	public String getValueConfig() {
		return valueConfig;
	}
	public void setValueConfig(String valueConfig) {
		this.valueConfig = valueConfig;
	}
	public int getValueNumber() {
		return valueNumber;
	}
	public void setValueNumber(int valueNumber) {
		this.valueNumber = valueNumber;
	}
	public String getKeyOrder() {
		return keyOrder;
	}
	public void setKeyOrder(String keyOrder) {
		this.keyOrder = keyOrder;
	}
	
	public String getRootKeyConfig() {
		return rootKeyConfig;
	}
	public void setRootKeyConfig(String rootKeyConfig) {
		this.rootKeyConfig = rootKeyConfig;
	}
	@Override
	public int compareTo(RowAnalyseConfigWeb o) {
		int result = o.getKeyNumber() + o.getValueNumber() - (keyNumber + valueNumber);
		if(result > 0)
			return 1;
		else if(result < 0)
			return -1;
		return 0;
	}
}
//timeformat:'long'
//webconfig:{datetime} {key1} {key2}:{key3}:{key4} {key5} {value1} {value2}
//keyname:key1(scope),key2(scope),key3(scope)
//keynumber:3
//value: value1(property, operate),value2(property, operate)
//valuenumber:2
//keyorder:key1,key2,key3;key3,key2,key1