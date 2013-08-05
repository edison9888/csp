package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * eagleeye信息的分析器
 * @author youji.zj
 * @version 2012-11-18
 */
public class EagleeyeAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(EagleeyeAnalyser.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public EagleeyeAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		/**
		 * ac18b38513533171369633401|1353317136970|2|0.2|172.24.179.133|5|257
		 */
		String[] logResult = StringUtils.splitPreserveAllTokens(line, '|');
		
		try {
			if (logResult.length != 7 || logResult.length <3 || !logResult[2].equals("2")) return;
			String rt = logResult[5];
			String time = logResult[1];
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(time) / 1000 * 1000);
			
			long collectTime = transferDate(calendar.getTime(), target);
			ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.HSF_PV_RT, "count/rt(ms)", collectTime);
			task.putDetailData(resultDetailKey, 1, Double.parseDouble(rt)); 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String [] args) {
		String line = "ac18b38513533171369633401|1353317136970|2|0.2|172.24.179.133|5|257";
		
		System.out.println(StringUtils.splitPreserveAllTokens(line, '|')[1]);
	}
}
