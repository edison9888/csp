package com.taobao.monitor.alarm.n.key.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.alarm.compare.MonitorAlarmHelper;
import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.key.KeyDefine;
import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;
import com.taobao.monitor.alarm.n.key.RangeDefine;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/***
 * 
 * @author youji.zj
 *
 */
public class AutoJudge implements Judge {
	
	/***
	 * cache the LongTimeMillis of last alarm
	 */
	private static Map<String,Long> cache = new HashMap<String, Long>();
	
	/***
	 * the exactly check time
	 */
	private long nowTimeMillis = 0;
	
	public long getNowTimeMillis() {
		return nowTimeMillis;
	}

	public void setNowTimeMillis(long nowTimeMillis) {
		this.nowTimeMillis = nowTimeMillis;
	}
	
	@Override
	public Result judge(KeyDefine define, AlarmContext context) {
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		List<RangeDefine> rangeList = define.getRangeList();
		
		// 晚上12点以后，早上9点之前不报警,数据依然要取，要发邮件
		if (hour >= 0 && hour <= 8) {
			return null;
		}
		
		if (rangeList != null && rangeList.size() > 0) {
			RangeDefine miniteRange = rangeList.get(0);
			RangeDefine weekRange = miniteRange;
			if (rangeList.size() > 1) {
				weekRange = rangeList.get(1);
			}

			Date now = new Date(this.getNowTimeMillis());
			
			// 当前数据
			Map<String, BigDecimal> nowStatisticsData = MonitorTimeAo.get().findToalInRangeDate(context.getAppId(), context.getKeyId(), now, now, true);
			double nowStatisticsAverage = MonitorAlarmHelper.getAverageData(nowStatisticsData).doubleValue();
			context.setRecentlyValue(String.valueOf(nowStatisticsAverage));
			
			// 一分钟前数据
			Map<String, BigDecimal> miniteMapData = getCompareData(this.getNowTimeMillis(), context.getAppId(), context.getKeyId(), 0, -1, true);
			double miniteHistoryData = MonitorAlarmHelper.getAverageData(miniteMapData).doubleValue();
			miniteHistoryData = miniteHistoryData == 0 ? 0.01 : miniteHistoryData; 
			double miniteRatio = nowStatisticsAverage / miniteHistoryData * 100;
			
			double weekLess = weekRange.getLessthan();
			double miniteLess = miniteRange.getLessthan();
			double weekGreater = weekRange.getGreaterthan();
			double miniteGreater = miniteRange.getGreaterthan();
			
			boolean judgeMinite = true;
			// 如果前一分钟已经报警，前一分钟的数据将不做参考，只与上个星期作比较，比例范围的上下限分别扩张5(比如原来80:120变成75:125)
			if (isLastMiniteUnNormal(context)) {
				judgeMinite = false;
				weekLess += 5;
				weekGreater -= 5;
			}			
			
			// 一个星期前数据
			Map<String, BigDecimal> weekMapData = getCompareData(this.getNowTimeMillis(), context.getAppId(), context.getKeyId(), -7, 0, false);
			double weekHistoryData = MonitorAlarmHelper.getAverageData(weekMapData).doubleValue();
			weekHistoryData = weekHistoryData == 0 ? 0.01 : weekHistoryData;
			double weekRatio = nowStatisticsAverage /weekHistoryData * 100;				
			
			int nowHostNum = nowStatisticsData.size();
			int historyHostNum = weekMapData.size();
			
			if ((miniteRatio > miniteLess && judgeMinite && weekRatio > weekLess) ||  (!judgeMinite && weekRatio > weekLess)) {
				record(context);
				String message = createMessage(KeyJudgeEnum.greaterTo, miniteRange, weekRange, context.getKeyId(), nowStatisticsAverage, miniteHistoryData, weekHistoryData, nowHostNum, historyHostNum);
				return new Result(message,KeyJudgeEnum.greaterTo);
			}
			if ((miniteRatio < miniteGreater && judgeMinite && weekRatio < weekGreater) || (!judgeMinite && weekRatio < weekGreater)) {
				record(context);
				String message = createMessage(KeyJudgeEnum.lessTo, miniteRange, weekRange, context.getKeyId(), nowStatisticsAverage, miniteHistoryData, weekHistoryData, nowHostNum, historyHostNum);
				return new Result(message,KeyJudgeEnum.lessTo);
			}
		}
		return null;
	}
	
	private Map<String, BigDecimal> getCompareData(long nowInMillis, int appId, int keyId, int dayBefore, int minuteBefore, boolean isLimit) {
		
		Date compareDate = MonitorAlarmHelper.getPreviousDate(nowInMillis, dayBefore, minuteBefore);
		return MonitorTimeAo.get().findToalInRangeDate(appId, keyId, compareDate, compareDate, isLimit);
	}
	
	private String createMessage(KeyJudgeEnum e, RangeDefine miniteDefine, RangeDefine weekDefine, 
			int keyId, double nowStatisticsAverage, double miniteHistoryData,
			double weekHistoryData, int nowHostNum, int historyHostNum) {
		String ratioMessage = "";

		if (miniteDefine == null || weekDefine == null) {
			return null;
		}
		
		if (e == KeyJudgeEnum.greaterTo) {
			ratioMessage = ratioMessage + "超出告警上限比率:" + miniteDefine.getLessthan() + "%" + ("(前一分钟),") + weekDefine.getLessthan() + "%" + "(前一星期)";
		} else if (e == KeyJudgeEnum.lessTo) {
			ratioMessage = ratioMessage + "低于告警下限比率:" + miniteDefine.getGreaterthan() + "%" + ("(前一分钟),") + weekDefine.getGreaterthan() + "%" + "(前一星期)";

		} else {
			return null;
		}

		String message = "<br/>";
		message += "当前统计机器数:";
		message += nowHostNum;
		message += "<br/>";
		message += ratioMessage;
	
		message += "<br/>";
		message += "当前均值:";
		message += nowStatisticsAverage;
		message += "<br/>";
		message += "前一分钟均值:";
		message += miniteHistoryData;
		message += "<br/>";
		message += "前一星期均值:";
		message += weekHistoryData;
		message += "<br/>";

		return message;
	}

	/***
	 * 前一分钟是否报警
	 * @param context
	 * @param range
	 * @return
	 */
	private boolean isLastMiniteUnNormal(AlarmContext context) {
		int appId = context.getAppId();
		int keyId = context.getKeyId();
		String key = String.valueOf(appId) + String.valueOf(keyId);
		Long value = cache.get(key);
		if (value == null || value.longValue() <= 0) return false;
		
		long nowTime = this.getNowTimeMillis();
		// 正常情况不会出现没有被初始化
		if (nowTime == 0) return false;
		
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(nowTime);
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, -1);
		long lastMinite = calendar.getTimeInMillis();
		
		// 前一分钟数据不正常
		if (lastMinite == value) return true;
		return false;
	}
	
	/***
	 * 缓存出异常时间
	 * @param context
	 */
	private void record(AlarmContext context) {
		int appId = context.getAppId();
		int keyId = context.getKeyId();
		String key = String.valueOf(appId) + String.valueOf(keyId);
		
		long nowTime = this.getNowTimeMillis();
		// 正常情况不会出现没有被初始化
		if (nowTime == 0) return;
		
		cache.put(key, nowTime);
	}

}
