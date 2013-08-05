package com.taobao.sentinel.pull;

public class HttpPullerFactory extends PullerFactory {

	@Override
	public IQueryPuller createQueryPuller(String appName, String ip) {
		return new HttpQueryPuller(appName, ip);
	}

	@Override
	public IResultPuller createResultPuller(String appName, String ip) {
		return new HttpResultPuller(appName, ip);
	}

}
