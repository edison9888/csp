package com.taobao.sentinel.pull;

public abstract class PullerFactory {
	
	public abstract IQueryPuller createQueryPuller(String appName, String ip);
	
	public abstract IResultPuller createResultPuller(String appName, String ip);

}
