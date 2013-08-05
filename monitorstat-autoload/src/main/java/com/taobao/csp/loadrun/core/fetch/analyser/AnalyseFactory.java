package com.taobao.csp.loadrun.core.fetch.analyser;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.fetch.ApacheFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.CpuFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.EagleeyeFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.GcFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.HsfFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.fetch.IoFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.JvmFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.LoadFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.TairFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.TddlFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.ThreadCountFetchTaskImpl;
import com.taobao.csp.loadrun.core.fetch.TomcatFetchTaskImpl;

/***
 * analyser µÄ¹¤³§
 * @author youji.zj
 * @version 2012-07-05
 *
 */
public class AnalyseFactory {
	
	public static AbstractAnalyser createAnalyser(IFetchTask task, LoadrunTarget target) {
		if (task instanceof ApacheFetchTaskImpl) {
			return new ApacheAnalyse(task, target);
		}
		
		if (task instanceof CpuFetchTaskImpl) {
			return new CpuAnalyser(task, target);
		}
		
		if (task instanceof GcFetchTaskImpl) {
			return new GcAnalyser(task, target);
		}
		
		if (task instanceof HsfFetchTaskImpl) {
			return new HsfAnalyser(task, target);
		}
		
		if (task instanceof JvmFetchTaskImpl) {
			return new JvmAnalyser(task, target);
		}
		
		if (task instanceof LoadFetchTaskImpl) {
			return new LoadAnalyser(task, target);
		}
		
		if (task instanceof TairFetchTaskImpl) {
			return new TairAnalyser(task, target);
		}
		
		if (task instanceof TddlFetchTaskImpl) {
			return new TddlAnalyser(task, target);
		}
		
		if (task instanceof TomcatFetchTaskImpl) {
			return new TomcatAnalyser(task, target);
		}
		
		if (task instanceof EagleeyeFetchTaskImpl) {
			return new EagleeyeAnalyser(task, target);
		}
		
		if (task instanceof ThreadCountFetchTaskImpl) {
			return new ThreadCountAnalyser(task, target);
		}
		
		if (task instanceof IoFetchTaskImpl) {
			return new IoAnalyser(task, target);
		}
		
		return null;
	}

}
