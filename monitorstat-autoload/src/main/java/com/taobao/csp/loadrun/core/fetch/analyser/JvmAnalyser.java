package com.taobao.csp.loadrun.core.fetch.analyser;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.fetch.JvmFetchTaskImpl;
import com.taobao.monitor.common.util.Arith;

/**
 * jvm信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class JvmAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(JvmFetchTaskImpl.class);
	
	private IFetchTask task;

	public JvmAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
	}
	
	@Override
	public void analyse(String line) {
		Date time = new Date();
		if (line.indexOf("Memory:") > 0) {
			Pattern pattern = Pattern
			.compile("\\[HeapMemoryUsage:\\s+Used=(\\d+)K\\s+Committed=(\\d+)K\\]\\[NonHeapMemoryUsage:\\s+Used=(\\d+)K\\s+Committed=(\\d+)K\\]");
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				String heap = matcher.group(1);
				String heapAll = matcher.group(2);
				String noHeap = matcher.group(3);
				String noHeapAll = matcher.group(4);
	
				int heapInt = Integer.parseInt(heap);
				int heapAllInt = Integer.parseInt(heapAll);
				int noheapInt = Integer.parseInt(noHeap);
				int noheapAllInt = Integer.parseInt(noHeapAll);
	
				double jvm_memeory = Arith.mul(Arith.div(heapInt + noheapInt, heapAllInt + noheapAllInt, 2), 100);
				task.putData(ResultKey.Jvm_Memeory, jvm_memeory,time);
			}
		} 
		
		// 感觉没什么意义，去掉
//		else if (line.indexOf("Thread-dump:") > 0) {
//			Pattern pattern = Pattern.compile("ajp\\[(\\d+),(\\d+),(\\d+),(\\d+),(\\d+),(\\d+)\\]");
//			Matcher matcher = pattern.matcher(line);
//			while (matcher.find()) {
//				String sBlocked = matcher.group(2);
//				String sRunnable = matcher.group(3);
//				String sWaiting = matcher.group(4);
//				
//				task.putData(ResultKey.AJP_BLOCKED, Double.valueOf(sBlocked), time);
//				task.putData(ResultKey.AJP_RUNNABLE, Double.valueOf(sRunnable), time);
//				task.putData(ResultKey.AJP_WAITING, Double.valueOf(sWaiting), time);
//			}
//		}
	}

}
