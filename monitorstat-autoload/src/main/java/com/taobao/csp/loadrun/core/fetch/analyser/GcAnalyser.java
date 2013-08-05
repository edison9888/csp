package com.taobao.csp.loadrun.core.fetch.analyser;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.monitor.common.util.Arith;

/**
 * gc信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class GcAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(GcAnalyser.class);
	
	private IFetchTask task;

	public GcAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
	}
	
	@Override
	public void analyse(String g) {
		double gcUseTime = 0;
		double fullUseTime = 0;

		Pattern pattern = Pattern.compile(",\\s*([\\w\\.]+)\\s*secs");
		Pattern pattern1 = Pattern
				.compile("\\[PSYoungGen: (\\w+)K->(\\w+)K\\(\\w+K\\)\\]");
		Pattern pattern2 = Pattern
				.compile("\\[ParNew: (\\w+)K->(\\w+)K\\(\\w+K\\),");

		try {
			if (g.indexOf("[GC") > 0) {
				Matcher matchtime = pattern.matcher(g);
				if (matchtime.find()) {
					String time = matchtime.group(1);
					gcUseTime = Arith.add(gcUseTime, Double.parseDouble(time));

					task.putData(ResultKey.GC_Min_Time, gcUseTime, new Date());

				}
				Matcher matchgc = null;
				if (g.indexOf("PSYoungGen") > 0) {
					matchgc = pattern1.matcher(g);
				} else if (g.indexOf("ParNew") > 0) {
					matchgc = pattern2.matcher(g);
				}

				if (matchgc != null && matchgc.find()) {
					String m1 = matchgc.group(1);
					String m2 = matchgc.group(2);
					long u = Long.parseLong(m1) - Long.parseLong(m2);

					task.putData(ResultKey.GC_Memory, new Double(u), new Date());

				}

				task.putData(ResultKey.GC_Min, 1d, new Date());

			}
			if (g.indexOf("[Full") > 0) {

				Matcher matchgc = null;
				if (g.indexOf("PSYoungGen") > 0) {
					matchgc = pattern1.matcher(g);
				} else if (g.indexOf("ParNew") > 0) {
					matchgc = pattern2.matcher(g);
				}

				if (matchgc != null && matchgc.find()) {
					String m1 = matchgc.group(1);
					String m2 = matchgc.group(2);
					long u = Long.parseLong(m1) - Long.parseLong(m2);

					task.putData(ResultKey.GC_Memory, new Double(u), new Date());

				}

				Matcher match = pattern.matcher(g);
				if (match.find()) {
					String time = match.group(1);
					fullUseTime = Arith.add(fullUseTime, Double.parseDouble(time));

					task.putData(ResultKey.GC_Full_Time, gcUseTime, new Date());
				}

				task.putData(ResultKey.GC_Full, 1d, new Date());

			}
			if (g.indexOf("[CMS-concurrent-mark-start") > 0) {
				task.putData(ResultKey.GC_CMS, 1d, new Date());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
