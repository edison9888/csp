package com.taobao.csp.loadrun.core.fetch.fetcher;

import java.io.IOException;

public interface IFetcher {
	
	/***  ���� ***/
	public void start();
	
	/*** fetch���� ***/
	public void fetch() throws IOException;
	
	/*** �ر� ***/
	public void end();
	
	/*** �ɼ����Ƿ��Ѿ�ֹͣ  ***/
	public boolean isRun();

}
