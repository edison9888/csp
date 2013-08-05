package com.taobao.csp.loadrun.core.fetch.analyser;

import java.util.Date;

import com.taobao.csp.loadrun.core.LoadrunTarget;

/**
 * ����ķ�����
 * @author youji.zj
 * @version 2012-07-05
 */
public abstract class AbstractAnalyser {
	
	public abstract void analyse(String line);
	
	
	/***
	 * �Զ�ѹ��10��һ���㣬�ֶ�ѹ��30��
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
