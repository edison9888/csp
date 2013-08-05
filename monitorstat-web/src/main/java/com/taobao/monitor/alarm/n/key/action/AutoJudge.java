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
		
		// ����12���Ժ�����9��֮ǰ������,������ȻҪȡ��Ҫ���ʼ�
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
			
			// ��ǰ����
			Map<String, BigDecimal> nowStatisticsData = MonitorTimeAo.get().findToalInRangeDate(context.getAppId(), context.getKeyId(), now, now, true);
			double nowStatisticsAverage = MonitorAlarmHelper.getAverageData(nowStatisticsData).doubleValue();
			context.setRecentlyValue(String.valueOf(nowStatisticsAverage));
			
			// һ����ǰ����
			Map<String, BigDecimal> miniteMapData = getCompareData(this.getNowTimeMillis(), context.getAppId(), context.getKeyId(), 0, -1, true);
			double miniteHistoryData = MonitorAlarmHelper.getAverageData(miniteMapData).doubleValue();
			miniteHistoryData = miniteHistoryData == 0 ? 0.01 : miniteHistoryData; 
			double miniteRatio = nowStatisticsAverage / miniteHistoryData * 100;
			
			double weekLess = weekRange.getLessthan();
			double miniteLess = miniteRange.getLessthan();
			double weekGreater = weekRange.getGreaterthan();
			double miniteGreater = miniteRange.getGreaterthan();
			
			boolean judgeMinite = true;
			// ���ǰһ�����Ѿ�������ǰһ���ӵ����ݽ������ο���ֻ���ϸ��������Ƚϣ�������Χ�������޷ֱ�����5(����ԭ��80:120���75:125)
			if (isLastMiniteUnNormal(context)) {
				judgeMinite = false;
				weekLess += 5;
				weekGreater -= 5;
			}			
			
			// һ������ǰ����
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
			ratioMessage = ratioMessage + "�����澯���ޱ���:" + miniteDefine.getLessthan() + "%" + ("(ǰһ����),") + weekDefine.getLessthan() + "%" + "(ǰһ����)";
		} else if (e == KeyJudgeEnum.lessTo) {
			ratioMessage = ratioMessage + "���ڸ澯���ޱ���:" + miniteDefine.getGreaterthan() + "%" + ("(ǰһ����),") + weekDefine.getGreaterthan() + "%" + "(ǰһ����)";

		} else {
			return null;
		}

		String message = "<br/>";
		message += "��ǰͳ�ƻ�����:";
		message += nowHostNum;
		message += "<br/>";
		message += ratioMessage;
	
		message += "<br/>";
		message += "��ǰ��ֵ:";
		message += nowStatisticsAverage;
		message += "<br/>";
		message += "ǰһ���Ӿ�ֵ:";
		message += miniteHistoryData;
		message += "<br/>";
		message += "ǰһ���ھ�ֵ:";
		message += weekHistoryData;
		message += "<br/>";

		return message;
	}

	/***
	 * ǰһ�����Ƿ񱨾�
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
		// ��������������û�б���ʼ��
		if (nowTime == 0) return false;
		
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(nowTime);
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, -1);
		long lastMinite = calendar.getTimeInMillis();
		
		// ǰһ�������ݲ�����
		if (lastMinite == value) return true;
		return false;
	}
	
	/***
	 * ������쳣ʱ��
	 * @param context
	 */
	private void record(AlarmContext context) {
		int appId = context.getAppId();
		int keyId = context.getKeyId();
		String key = String.valueOf(appId) + String.valueOf(keyId);
		
		long nowTime = this.getNowTimeMillis();
		// ��������������û�б���ʼ��
		if (nowTime == 0) return;
		
		cache.put(key, nowTime);
	}

}
