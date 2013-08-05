package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * hsf信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class HsfAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(HsfAnalyser.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public HsfAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
		if (logResult.length < 4) return;
		String titleKey = logResult[0];
		
		if (titleKey.startsWith("HSF-ProviderDetail")) {
			if (line.indexOf("JVM") > -1) return;
				
			// HSF-ProviderDetail
			// com.taobao.item.service.ItemService:1.0.0-L1
			// sellerUploadPropertyImage 172.24.22.52 1 10 2010-04-10
			// 00:01:13 v015180.cm4.tbsite.net
			// String className = logResult[1];
			// String methodName = logResult[2];
			String count = logResult[4];
			String useTime = logResult[5];
			String time = logResult[6];
			String timeSub = time.substring(0, 16);
			
			String key = logResult[1] + ":" + logResult[2];
			
			try {
				Date date = sdf.parse(timeSub);

				task.putData(ResultKey.Hsf_pv, Double.parseDouble(count), date);
				task.putData(ResultKey.Hsf_Rest, Double.parseDouble(useTime), date);
				
				long collectTime = transferDate(date, target);
				ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.HSF_PROVIDER, key, collectTime);
				task.putDetailData(resultDetailKey, Long.parseLong(count), Long.parseLong(useTime)); // 微妙转换成毫秒
			} catch (ParseException e) {
				logger.error("分析hsf日志出错", e);
			}
		}
		
		if (titleKey.trim().equals("HSF-Consumer")) {
			if (line.indexOf("JVM") > -1) return;
			String count = logResult[4];
			String useTime = logResult[5];
			String time = logResult[6];
			String timeSub = time.substring(0, 16);
			
			String key = logResult[1] + ":" + logResult[2];
			
			try {
				Date date = sdf.parse(timeSub);
				long collectTime = transferDate(date, target);
				ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.HSF_CONSUMER, key, collectTime);
				task.putDetailData(resultDetailKey, Long.parseLong(count), Long.parseLong(useTime)); // 微妙转换成毫秒
			} catch (ParseException e) {
				logger.error("分析hsf日志出错", e);
			}
		}
	}
}
