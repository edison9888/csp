package com.taobao.csp.depend.job.eagleeye;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;

public abstract class EagleeyeAnalyse {
	public static final Logger logger = Logger.getLogger("cronTaskLog");
	
	public abstract CspEagleeyeApiRequestDay changePartToDay(String jsonContent, List<CspEagleeyeApiRequestPart> partList);
	
	protected abstract void addPartToDayPo(Object dayObj, Object partPoObj) throws Exception ;
	
	/*
	 * 把API Day表的数据插入到 Eagleeye自动表中
	 */
	public abstract void addApiDayToTopo(CspEagleeyeApiRequestDay dayObj);
	
	public abstract void addApiDayToEagleeyeAuto(CspEagleeyeApiRequestDay dayObj);
	
}
