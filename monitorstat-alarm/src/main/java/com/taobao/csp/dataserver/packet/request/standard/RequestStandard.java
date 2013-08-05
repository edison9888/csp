package com.taobao.csp.dataserver.packet.request.standard;

import com.taobao.csp.dataserver.item.KeyObject;
import com.taobao.csp.dataserver.item.ValueObjectBox;
import com.taobao.csp.dataserver.packet.RequestPacket;

/**
 * 统一传输信息的报文类，暂时可去掉
 * @author zhongting.zy
 *
 */
public class RequestStandard extends RequestPacket {

	private static final long serialVersionUID = 7440976759808005960L;

	protected long collecttime;
	
	protected String appName;
	protected String ip;
	protected KeyObject item;
	protected ValueObjectBox valueBox;
	
	public RequestStandard(){
		super();
	}


	public RequestStandard(String appName,String ip, long collecttime, KeyObject item, ValueObjectBox valueBox) {
		super();
		this.collecttime = collecttime;
		this.appName = appName;
		this.ip = ip;
		this.item = item;
		this.valueBox = valueBox;
	}


	public long getCollecttime() {
		return collecttime;
	}


	public void setCollecttime(long collecttime) {
		this.collecttime = collecttime;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public KeyObject getItem() {
		return item;
	}


	public void setItem(KeyObject item) {
		this.item = item;
	}


	public ValueObjectBox getValueBox() {
		return valueBox;
	}


	public void setValueBox(ValueObjectBox valueBox) {
		this.valueBox = valueBox;
	}

	

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getAppName() {
		return appName;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
