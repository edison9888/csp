package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * Io信息的分析器
 * @author youji.zj
 * @version 2012-12-17
 */
public class IoAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(IoAnalyser.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public IoAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		/**
		 * Time            bytin  bytout   pktin  pktout 
		 * 17/12/12-16:35   1.5K    8.3K    11.0    16.0
		 */
		if (StringUtils.isBlank(line)) {
			return;
		}
		
		String[] logResult = line.split(" +");
		
		try {
			if (logResult.length != 5 || logResult[0].equals("Time") || logResult[0].equals("tsar")) {
				return;
			}
			
			double bytin = trasferK(logResult[1]);
			double bytout = trasferK(logResult[2]);
			double pktin = trasferK(logResult[3]);
			double pktout = trasferK(logResult[4]);
			
		
			Calendar calendar = Calendar.getInstance();

			long collectTime = transferDate(calendar.getTime(), target);
			ResultDetailKey bytinKey = new ResultDetailKey(ResultDetailType.IO_DATA, "bytin", collectTime);
			ResultDetailKey bytoutKey = new ResultDetailKey(ResultDetailType.IO_DATA, "bytout", collectTime);
			ResultDetailKey pktinKey = new ResultDetailKey(ResultDetailType.IO_DATA, "pktin", collectTime);
			ResultDetailKey pktoutKey = new ResultDetailKey(ResultDetailType.IO_DATA, "pktout", collectTime);
			task.putDetailData(bytinKey, bytin); 
			task.putDetailData(bytoutKey, bytout); 
			task.putDetailData(pktinKey, pktin); 
			task.putDetailData(pktoutKey, pktout); 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private double trasferK(String num) {
		double value = 0l;
		if (StringUtils.isBlank(num)) {
			return 0d;
		}
		
		if (num.endsWith("K")) {
			value = 1000d * (int)(Double.parseDouble(num.substring(0, num.length() - 1)));
		} else if (num.endsWith("M")) {
			value = 1000000d * (int)(Double.parseDouble(num.substring(0, num.length() - 1)));
		} else {
			value = ((int)(Double.parseDouble(num)));
		}
		
		return value;
	}
	
	public static void main(String [] args) {
		String line = "17/12/12-16:35   1.5K    8.3K    11.0    16.0";
		
		System.out.println(line.split(" +").length);
	}
}
