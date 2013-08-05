package com.taobao.monitor.alarm.n.key.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.JudgeUtils;
import com.taobao.monitor.alarm.n.key.KeyDefine;
import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;
import com.taobao.monitor.alarm.n.key.RangeDefine;

public class ConstantJudge implements Judge {

	@Override
	public Result judge(KeyDefine define, AlarmContext context) {

		if (define.getKeyJudgeType() == 4 && context.getValueMap().size() > 0) {
			Map<Date, String> dataMap = context.getValueMap();
			List<Date> sortedDateList = JudgeUtils.sortDateDesc(dataMap);
			Date topDate = sortedDateList.get(0);
			for (RangeDefine range : define.getRangeList()) {

				int startTime = range.getStartTime(); 
				int endTime = range.getEndTime();
				int maxTime = JudgeUtils.parseDateToNumber(topDate);

				if (maxTime > startTime && maxTime < endTime
						&& range.getEqualValue() != Long.parseLong(dataMap.get(topDate))) {
					String msg = "不等于阀值  " + range.getEqualValue();
					return new Result(msg, KeyJudgeEnum.equalTo);
				}
			}
		}
		return null;
	}
}
