package com.taobao.csp.loadrun.core.fetch.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.fetch.HsfFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;

public class ScriptFetcher implements IFetcher {

public static Logger logger = Logger.getLogger(SshFetcher.class);
	
	/*** url***/
	private String command;
	
	/*** »Øµ÷analyse ***/
	private IFetchTask task;
	
	private volatile boolean isRun;
	
	public ScriptFetcher(IFetchTask task, String command) {
		this.task = task;
		this.command = command;
	}

	@Override
	public void fetch() {
		char splitLine = getSplitLine();
			
		
		
		while (isRun) {
			
			BufferedReader2 br = null;
			try {
				URL url = new URL(command);
				InputStream inputStream = url.openStream();
				br = new BufferedReader2(new InputStreamReader(inputStream), splitLine);
					
				String line = null;
				while ((line = br.readLine()) != null) {
					task.analyse(line);
				}
			} catch (Exception e) {
				logger.error(e);
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
			
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				logger.info("", e);
			}
		}	
	}
	
	private char getSplitLine() {
		char split = '\n';
		
		if (task instanceof HsfFetchTaskImpl) {
			split = '\02';
		}
		
		return split;
	}

	@Override
	public void start() {
		isRun = true;
	}

	@Override
	public void end() {
		isRun = false;
	}
	
	@Override
	public boolean isRun() {
		return isRun;
	}
}
