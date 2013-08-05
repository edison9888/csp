
package com.taobao.monitor.alarm.n.key.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.key.KeyDefine;
import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;
import com.taobao.monitor.alarm.n.key.RangeDefine;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.util.DiamondConstant;

/**
 * 
 * @author xiaodu
 * @version 2011-2-27 下午08:40:05
 */
public class ThresholdJudge implements Judge{

	@Override
	public Result judge(KeyDefine define, AlarmContext context) {
		
		
		//取得最近记录
		Map<Date, String> dataMap = context.getValueMap();
		//降序排序时间 
		List<Date> sortDateList = sortDateDesc(dataMap);		
		if(sortDateList==null||sortDateList.size()==0){return null;}

		Date maxDate = sortDateList.get(0);
		Date secondDate = null;
		Date thrDate = null;
		if(sortDateList.size()>2){
			secondDate = sortDateList.get(1);
			thrDate = sortDateList.get(2);
		}
		List<RangeDefine> rangeList = define.getRangeList();
		
		for(RangeDefine range:rangeList){
			
			if(maxDate!=null){
				String maxValue = dataMap.get(maxDate);
				if(maxValue!=null){					
					try {
						int startTime = range.getStartTime();
						int endTime = range.getEndTime();
						int maxTime = parseDateToNumber(maxDate);
						
						if(maxTime>startTime &&maxTime<endTime){
							double greater = range.getGreaterthan();
							double less = range.getLessthan();
							if(Double.parseDouble(maxValue)>less&&less!=-1){
								return new Result(createMessage(KeyJudgeEnum.greaterTo,range,context.getKeyId()) + getSpecialMessage(define),KeyJudgeEnum.greaterTo);
							}
							
							if(greater!=-1&&secondDate!=null&&thrDate!=null){//主要是最近的一个记录可能在收集的时候，不是在一个时间点内全部取完整，所以使用第二个时间点这样能保证是完整的
								Double secondvalue = Double.parseDouble(dataMap.get(secondDate));
								Double thrValue = Double.parseDouble(dataMap.get(thrDate));
								if(thrValue<greater&&secondvalue<greater&&greater!=-1&&Double.parseDouble(maxValue)<greater){
									return new Result(createMessage(KeyJudgeEnum.lessTo,range,context.getKeyId()) + getSpecialMessage(define),KeyJudgeEnum.lessTo);
								}
							}
							
						}
					} catch (Exception e) {
					}
				}				
			}
			
		}
		return null;
	}
	
	private String getSpecialMessage(KeyDefine define){
		if(DiamondConstant.AtomInitialExceptionKeyName.equals(define.getKeyName())||
				DiamondConstant.ConfigExceptionKeyName.equals(define.getKeyName())){
			return ",diamond推送tddl数据源出错";
		}else{
			return "";
		}
		
	}
	private String createMessage(KeyJudgeEnum e,RangeDefine d,int keyId){
		
		if(e == KeyJudgeEnum.greaterTo){
			double l = d.getLessthan();
			if(keyId == 176){
				l = Arith.div(l, 1000,2);
			}
			
			return "超过阀值"+l;
			
		}else if(e == KeyJudgeEnum.lessTo){
			double l = d.getGreaterthan();
			if(keyId == 176){
				l = Arith.div(l, 1000,2);
			}
			
			return "小于阀值"+l;
		}else if(e == KeyJudgeEnum.equalTo){
			
			return null;
		}
		return null;
	}
	
	
	protected List<Date> sortDateDesc(Map<Date, String> dataMap){
		List<Date> dataList = new ArrayList<Date>();
		for(Date date:dataMap.keySet()){
			dataList.add(date);
		}
		Collections.sort(dataList,new Comparator<Date>(){
			
			public int compare(Date o1, Date o2) {
				long thisTime = o1.getTime();
				long anotherTime = o2.getTime();
				return (thisTime<anotherTime ? 1 : (thisTime==anotherTime ? 0 : -1));
			}});
		if(dataList.size()>0){
			return dataList;
		}else{
			return null;
		}		
	}
	/**
	 * 将时间解析成 整型 HHmm
	 * @param date
	 * @return 整型 HHmm
	 */
	protected int parseDateToNumber(Date date){
		if(date==null)return 0;		
		String str = sdf.format(date);		
		return  Integer.parseInt(str);
	}
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HHmm");

}
