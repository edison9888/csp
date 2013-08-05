package com.taobao.csp.loadrun.core.fetch.analyser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/**
 * tddl信息的分析器
 * @author youji.zj
 * @version 2012-07-05
 */
public class TddlAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(TddlAnalyser.class);
	
	private Pattern timePattern = Pattern.compile("(\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private IFetchTask task;
	
	private LoadrunTarget target;

	public TddlAnalyser(IFetchTask task, LoadrunTarget target) {
		this.task = task;
		this.target = target;
	}
	
	@Override
	public void analyse(String line) {
		String[] logResult = StringUtils.splitPreserveAllTokens(line, "#@#");

		if (logResult.length != 8 || !"EXECUTE_A_SQL_SUCCESS".equals(logResult[2]))
			return;
		String sql = cleanSql(logResult[0]);
		if (sql == null) {
			return;
		}

		String count = logResult[3];
		String useTime = logResult[4];
		String time = parseLogLineCollectTime(logResult[7]);
		

		try {
			Date date = sdf.parse(time);
			long collectTime = transferDate(date, target);
			ResultDetailKey resultDetailKey = new ResultDetailKey(ResultDetailType.TDDL, sql, collectTime);
			task.putDetailData(resultDetailKey, Long.parseLong(count.trim()), Long.parseLong(useTime.trim()));
		} catch (ParseException e) {
			logger.error("分析Tddl日志出错", e);
		}

	}
	
	private String cleanSql(String sql) {
		try {
			if (sql.length() > 2048) {
				return null;
			} else {
				return sql.replaceAll("\\(\\?(,\\?)*\\)", "(?)");
			}
		} catch (Exception e) {
			logger.error("清理sql出错", e);
			e.printStackTrace();
			return null;
		}
	}
	
	private String parseLogLineCollectTime(String time) {
		Matcher m = timePattern.matcher(time);		
		if(m.find()){
			return "20"+m.group(1);
		}
		
		return null;
	}

}
