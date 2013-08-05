package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * tair信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class TairAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(TairAnalyser.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public TairAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
		if (logResult.length < 4) return;
		String titleKey = logResult[0];
		
		if (titleKey.startsWith("TairClient")) {
			if (line.indexOf("Exception") > -1) return;
				
			//TairClient 2.2.3         put             level-3           855     1057    2010-04-10 00:03:46     item121a.cm4.tbsite.net
			//TairClient 2.2.4       put/len     /172.23.210.39:5191$0     0      1      2011-08-25 00:01:48     item172146.cm3
			//TairClient 2.2.4       Exception           ~~~~              0      1      2011-08-25 00:01:48     item172146.cm3
			//TairClient 2.3.3.1       get   /172.24.143.30:5191$136$group_comm   0  1  2011-11-02 00:00:54  dtstradeface014082.cm4
			//TairClient 2.3.4         get   /172.23.182.23:5191$996$group_1      27 27 2012-05-01 00:01:08  tp169109.cm4
			String count = logResult[4];
			String useTime = logResult[3];
			String time = logResult[5];
			String operator = logResult[1];
			String info = logResult[2];
			String [] infoPart = info.split("\\$");
			if (operator.equals("Exception") || infoPart.length != 3) return;

			String key = operator + "-" + infoPart[2];
			
			try {
				Date date = sdf.parse(time);
				long collectTime = transferDate(date, target);
				ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.TAIR, key, collectTime);
				task.putDetailData(resultDetailKey, Long.parseLong(count.trim()), Long.parseLong(useTime.trim()));
			} catch (ParseException e) {
				logger.error("分析Tair日志出错", e);
			}
		}
	}

}
