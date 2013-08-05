package com.taobao.sentinel.push;


public interface IPusher {
	public boolean pushConfig(String key, String configInfo);
}
