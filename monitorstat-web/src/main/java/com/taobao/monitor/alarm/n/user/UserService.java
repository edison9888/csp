
package com.taobao.monitor.alarm.n.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.Service;
import com.taobao.monitor.alarm.po.ExtraUserAppKeyDefine;
import com.taobao.monitor.alarm.transfer.PhoneTransfer;
import com.taobao.monitor.alarm.transfer.WangwangTransfer;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.ao.MonitorUserAo;
import com.taobao.monitor.web.vo.LoginUserPo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-27 下午09:26:55
 */
public class UserService implements Service<UserDefine>{
	
	private static UserService userService = new UserService();

	private UserService(){
		init();
	}
	
	public static UserService get(){
		return userService;
	}
	
	private Map<Integer,UserDefine> userMap = new HashMap<Integer,UserDefine>();
	
	
	public void init(){
		
		List<LoginUserPo> userList = MonitorUserAo.get().findAllUser();
		for(LoginUserPo po:userList){
			register(createUserDefine(po));
		}
		
	}
	
	public void register(UserDefine define){
		if(define!=null)
			userMap.put(define.getUserId(), define);
		
		
	}
	
	public void unregister(UserDefine define){
		if(define!=null)
			userMap.remove(define.getUserId() );
		
	}
	
	
	
	public UserDefine createUserDefine(LoginUserPo po){
		
		UserDefine userDefine = new UserDefine();
		userDefine.setEmail(po.getMail());
		userDefine.setPhone(po.getPhone());
		userDefine.setWangwang(po.getWangwang());
		userDefine.setUserId(po.getId());
		userDefine.setPhoneRangeList(parseReportRange(po.getSendPhoneFeature()));
		userDefine.setWangwangRangeList(parseReportRange(po.getSendWwFeature()));
		if(po.getGroup()!=null&&!"".equals(po.getGroup())){
			String appIds = po.getGroup();
			Set<Integer> appSet = new HashSet<Integer>();
			for(String appId:appIds.split(",")){
				try{
					appSet.add(Integer.parseInt(appId));
				}catch (Exception e) {
				}
			}
			
			userDefine.setAcceptAppIdSet(appSet);
			
			List<ExtraUserAppKeyDefine> list = MonitorUserAo.get().getExtraUserAppKeyDefine(po.getId());
			if(list!=null){
				Map<Integer,Set<Integer>> extraAcceptAppKey = new HashMap<Integer, Set<Integer>>();
				
				for(ExtraUserAppKeyDefine define:list){
					
					Set<Integer> set = extraAcceptAppKey.get(define.getAppId());
					if(set == null){
						set = new HashSet<Integer>();
						extraAcceptAppKey.put(define.getAppId(), set);
					}
					set.add(define.getKeyId());
				}
				
				userDefine.setExtraAcceptAppKey(extraAcceptAppKey);
			}
			return userDefine;
			
		}else{
			userDefine.setAcceptAppIdSet(new HashSet<Integer>());
		}
		
		
		return userDefine;
	}
	
	
	public void sendUserExtraMessage(int appId,String title,String message){
		for(Map.Entry<Integer,UserDefine> entry:userMap.entrySet()){
			UserDefine define = entry.getValue();
			Set<Integer> appSet = define.getAcceptAppIdSet();
			if(!appSet.contains(appId)){//判断这个用户是否 接收这个应用告警
				continue;
			}
			WangwangTransfer.getInstance().sendExtraMessage(define.getWangwang(), title, message);
		}
		
	}
	
	
	public boolean lookup(AlarmContext context){
		
		for(Map.Entry<Integer,UserDefine> entry:userMap.entrySet()){
			UserDefine define = entry.getValue();
			Set<Integer> appSet = define.getAcceptAppIdSet();
			if(!appSet.contains(context.getAppId())){//判断这个用户是否 接收这个应用告警
				continue;
			}
			
			//判断对有些用户只是选择某个key的告警
			Map<Integer, Set<Integer>> extraMap = define.getExtraAcceptAppKey();
			if(extraMap!=null&&extraMap.size()>0){
				Set<Integer> keySet = extraMap.get(context.getAppId());
				if(keySet != null){
					if(!keySet.contains(context.getKeyId())){
						continue;
					}
				}
			}
			if(define.getPhone()!=null){
				if(checkIsNeedAlarm(define.getPhoneRangeList(),context)){
					PhoneTransfer.getInstance().doTranser(define.getPhone(), context);
					MonitorAlarmAo.get().addUserAcceptMsg(context.getAppId(), context.getKeyId(), define.getUserId(),context.getRangeMessage(), "phone");
					
				}
			}
			
			if(define.getWangwang() != null){
				if(checkIsNeedAlarm(define.getWangwangRangeList(),context)){
					WangwangTransfer.getInstance().doTranser(define.getWangwang(), context);
					MonitorAlarmAo.get().addUserAcceptMsg(context.getAppId(), context.getKeyId(), define.getUserId(),context.getRangeMessage(), "wangwang");
				}
			}
		}
		return true;
	}
	
	/**
	 * 解析范围
	 * @param desc
	 * @return
	 */
	private List<ReportRange> parseReportRange(String desc){
		
		if(desc==null){
			return new ArrayList<ReportRange>();
		}
		
		List<ReportRange> rangeList = new ArrayList<ReportRange>();
		String[] ranges = desc.split("\\$");		
		for(String range:ranges){			
			String[] datas = range.split("\\#");			
			if(datas.length==4){				
				ReportRange r = new ReportRange();
				String times = datas[0];
				String weekday = datas[1];
				String start = datas[2].replace(":", "");
				String end = datas[3].replace(":", "");
				
				r.setContinueTimes(Integer.parseInt(times));
				r.setWeekDay(Integer.parseInt(weekday));
				r.setStart(Integer.parseInt(start));
				r.setEnd(Integer.parseInt(end));
				rangeList.add(r);
			}
		}
		return rangeList;
	}	
	
	
	/**
	 * 检查告警信息，是否在用户的接收范围内
	 * @param rangeList
	 * @param context
	 * @return
	 */
	private boolean checkIsNeedAlarm(List<ReportRange> rangeList,AlarmContext context) {
		
		Calendar cal = Calendar.getInstance();
		int tmp = cal.get(Calendar.DAY_OF_WEEK);
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int currentTime = Integer.parseInt(sdf.format(cal.getTime())) ;
		for(ReportRange range:rangeList){	
			int week = range.getWeekDay();
			if(week!=tmp){
				continue;
			}
			int continueTimes = range.getContinueTimes();	
			
			if(continueTimes>=0&&context.getContinuousAlarmTimes()<continueTimes){
				continue;
			}				
			int start = range.getStart();
			int end = range.getEnd();
			if(currentTime>=start&&currentTime<=end){
				return true;
			}			
		}		
		return false;
	}


}
