package com.taobao.csp.loadrun.core.fetch.fetcher;

import java.io.IOException;

public interface IFetcher {
	
	/***  启动 ***/
	public void start();
	
	/*** fetch数据 ***/
	public void fetch() throws IOException;
	
	/*** 关闭 ***/
	public void end();
	
	/*** 采集器是否已经停止  ***/
	public boolean isRun();

}
