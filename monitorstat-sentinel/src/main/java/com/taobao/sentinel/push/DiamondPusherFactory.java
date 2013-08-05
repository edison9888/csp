package com.taobao.sentinel.push;

public class DiamondPusherFactory extends PusherFactory {

	@Override
	public IPusher createPush() {
		return new DiamondPusher();
	}

}
