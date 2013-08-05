package com.taobao.sentinel.constant;

public enum ConfigType {
	
	BLACK_LIST_INTERFACE("black_list_interface"),
	
	WHITE_LIST_INTERFACE("white_list_interface"),
	
	BLACK_LIST_APP("black_list_app"),
	
	BLACK_LIST_CUSTOMER("black_list_customer"),
	
	WHITE_LIST_CUSTOMER("white_list_customer"),
	
	FLOW_CONTROL_INTERFACE("flow_control_interface"),
	
	FLOW_CONTROL_APP("flow_control_app"),
	
	FLOW_CONTROL_DEPENDENCY("flow_control_dependency"),
	
	FLOW_CONTROL_PARAM("flow_control_param"),
	
	QPS_INTERFACE("qps_interface"),
	
	QPS_APP("qps_app"),
	
	QPS_PERIOD("qpsPeriod"),
	
	/*** group info ***/
	GROUPS("groups"),
	
	/*** ip info ***/
	IPS("ips");
	
	private String key;
	
	private ConfigType(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
