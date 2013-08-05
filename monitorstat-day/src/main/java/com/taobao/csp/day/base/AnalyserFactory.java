package com.taobao.csp.day.base;

import com.taobao.csp.day.apache.ApacheSpecialLogAnalyser;
import com.taobao.csp.day.gc.GcLogAnalyser;
import com.taobao.csp.day.pinkie.PinkieAccessLogAnalyser;
import com.taobao.csp.day.sph.SphLogAnalyser;
import com.taobao.csp.day.tddl.TddlLogAnalyser;
import com.taobao.csp.day.tdod.TdodLogAnalyser;

public class AnalyserFactory {
	
	public static AbstractAnalyser getInstance(String appName, HostInfo hostInfo, DataType type) {
		AbstractAnalyser analyser = null;
		
		switch (type) {
		case TDDL:
			char lineSplitTddl = '\n';
			analyser = new TddlLogAnalyser(appName, hostInfo, lineSplitTddl);
			break;
			
		case SPH: 
			char lineSplitSph = '\n';
			analyser = new SphLogAnalyser(appName, hostInfo, lineSplitSph);
			break;
			
		case APACHE_SPECIAL: 
			char lineSplitApacheSpecial = '\n';
			analyser = new ApacheSpecialLogAnalyser(appName, hostInfo, lineSplitApacheSpecial);
			break;
			
		case TDOD: 
			char lineSplitTdod = '\n';
			analyser = new TdodLogAnalyser(appName, hostInfo, lineSplitTdod);
			break;
			
		case PINKIE_ACCESS: 
			char lineSplitPinkieAccess = '\n';
			analyser = new PinkieAccessLogAnalyser(appName, hostInfo, lineSplitPinkieAccess);
			break;
		
		case GC:
			char lineSplitGc = '\n';
			analyser = new GcLogAnalyser(appName, hostInfo, lineSplitGc);

		default:
			break;
		}
		
		return analyser;
	}

}
