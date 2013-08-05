
package com.taobao.monitor.alarm.n.key.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.key.KeyDefine;
import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;
import com.taobao.monitor.alarm.n.key.RangeDefine;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.web.vo.AlarmDataPo;

/**
 * 
 * @author xiaodu
 * @version 2010-6-30 下午05:10:11
 */
public class WaveJudge implements Judge{
	
	
	
	private Double averagePreValue(int index,int len,List<Date> sortDateList,Map<Date, String> dataMap){
		
		Double first = 0d;
		int firstSize = 0;
		for(int i=index;i<len&&i<sortDateList.size();i++){
			Date tmpDate = sortDateList.get(i);
			Double tmpValue = Double.parseDouble(dataMap.get(tmpDate));
			if(tmpValue!=null){
				first+=tmpValue;
				firstSize++;
			}
		}
		if(firstSize==0){
			return null;
		}
		Double averageFirst = first/firstSize;		
		return averageFirst;
	}
	
	

	@Override
	public Result judge(KeyDefine define, AlarmContext context) {
		
		
		
		Map<Date, String> dataMap = context.getValueMap();
		List<Date> sortDateList = sortDateDesc(dataMap);		
		if(sortDateList==null||sortDateList.size()==0){return null;}
		if(sortDateList.size()>1){			

			try {						
				Double averageFirst = averagePreValue(0,3,sortDateList,dataMap);
				Double averageSecond = averagePreValue(1,3,sortDateList,dataMap);
				
				if(averageFirst<500&&averageSecond<500){
					return null;
				}
				
				List<RangeDefine> rangelist = define.getRangeList();
				
				for(RangeDefine range:rangelist){
				
					double greater = (averageSecond*range.getGreaterthan())/100;
					double less = (averageSecond*range.getLessthan())/100;
					if(less>0){
						if(averageFirst>less&&less!=-1){	
							return new Result(createMessage(KeyJudgeEnum.greaterTo,range,context.getKeyId()),KeyJudgeEnum.greaterTo);
						}
					}
					if(greater>0){
						if(averageFirst<greater&&greater!=-1){
							return new Result(createMessage(KeyJudgeEnum.lessTo,range,context.getKeyId()),KeyJudgeEnum.lessTo);
						}
					}							
				}
			} catch (Exception e) {
			}		
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

}
