package com.taobao.csp.loadrun.core.fetch.analyser;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.fetch.JvmFetchTaskImpl;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * load信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class LoadAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(JvmFetchTaskImpl.class);
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public LoadAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		
		Pattern pattern1 = Pattern.compile("top\\s?-\\s?(\\d\\d:\\d\\d:\\d\\d)\\s?up.*load average:\\s?([\\d\\.]+),\\s?([\\d\\.]+),\\s?([\\d\\.]+)");
		Matcher matcher1 = pattern1.matcher(line);
		if(matcher1.find()){
			Date collectTime = new Date();
			String load = matcher1.group(2);
			task.putData(ResultKey.Load, Double.parseDouble(load), collectTime);
			
			ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.PERFORMANCE_INDEX, "LOAD", transferDate(collectTime, target));
			task.putDetailData(resultDetailKey, Double.parseDouble(load));
		}
	}

}
