package com.taobao.csp.loadrun.core.fetch.analyser;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;
import com.taobao.monitor.common.util.Arith;

/**
 * cpu信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class CpuAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(CpuAnalyser.class);
	
	private IFetchTask task;
	
	private LoadrunTarget target;
	
	private int idleIndex = 0;

	public CpuAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}

	@Override
	public void analyse(String line) {
		String[] tmp = StringUtils.splitByWholeSeparator(line.trim(), " ");
		if (tmp.length < 8) {
			return;
		}
		
		for (int i = 0; i < tmp.length && idleIndex == 0; i++) {
			if (tmp[i].trim().equals("%idle")) {
				idleIndex = i;
				logger.info("head idle index:" + idleIndex);
				return;
			}
		}
		
		// String time = tmp[0];
		try {
			if (idleIndex == 0) idleIndex = tmp.length - 1;
			double use;
			use = Arith.sub(100, Double.parseDouble(tmp[idleIndex]));
			
			Date collectTime = new Date();
			task.putData(ResultKey.CPU, use, collectTime);
			
			ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.PERFORMANCE_INDEX, "CPU", transferDate(collectTime, target));
			task.putDetailData(resultDetailKey, use);
		} catch (Exception e) {
		}
	}
	
	public static void main(String [] args) {
//		String test ="Time             user     sys    wait    hirq    sirq    util   ";
		String test1 = "24/09/12-09:46   16.1     1.5     0.0     0.0     0.5    18.0 ";
//		String test = "11:35:09 PM  all    0.00    0.59    0.20    0.00    0.00    0.00    0.20   99.01   2240.59";
		String[] tmp = StringUtils.splitByWholeSeparator(test1.trim(), " ");
		
		System.out.println(tmp.length);
		System.out.println(tmp[tmp.length - 1]);
		
	}

}
