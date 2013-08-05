package com.taobao.csp.monitor.impl.analyse.other;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

public class TcMonitorAnalyse extends AbstractDataAnalyse {
	Pattern pattern = Pattern.compile("([\\w-]+)\\s+Total:([\\d]+)\\s+Success:[\\d]+\\s+Failure:[\\d]+\\s+AvgTime:[\\d]+\\s+[\\d\\.]+%\\s+([\\d\\.]+)%");

	private static final Logger logger =  Logger.getLogger(TcMonitorAnalyse.class);
	public TcMonitorAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String line) {
		
	}

	@Override
	public void release() {
		
	}

	@Override
	public void submit() {
		
	}

}
