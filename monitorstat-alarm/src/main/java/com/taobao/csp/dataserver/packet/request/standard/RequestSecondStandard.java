package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.item.KeyObject;
import com.taobao.csp.dataserver.item.ValueObjectBox;


/**
 * 秒级别的数据传输
 */
public class RequestSecondStandard extends RequestStandard {

	private static final long serialVersionUID = 7250492654137028618L;
	
	public RequestSecondStandard(){
		super();
	}
	
	public RequestSecondStandard(String appName,String ip, long collecttime, KeyObject item, ValueObjectBox valueBox) {
		super();
		this.collecttime = collecttime;
		this.appName = appName;
		this.ip = ip;
		this.item = item;
		this.valueBox = valueBox;
	}
}
