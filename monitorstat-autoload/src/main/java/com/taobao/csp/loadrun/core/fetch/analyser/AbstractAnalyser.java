package com.taobao.csp.loadrun.core.fetch.analyser;

import java.util.Date;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/**
 * 抽象的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public abstract class AbstractAnalyser {
	
	public abstract void analyse(String line);
	
	
	/***
	 * 自动压测10秒一个点，手动压测30秒
	 * @param date
	 * @return
	 */
	protected long transferDate(Date date, LoadrunTarget target) {
		long factor = 10000;
		if (target.getLoadAuto() == 0) {
			factor = 30000;
		}
		
		long millis = date.getTime() / factor * factor;
		
		return millis;
	}
}
