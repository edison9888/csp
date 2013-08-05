
package com.taobao.monitor.web.ao;

import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.MonitorWebWwDao;
import com.taobao.monitor.web.vo.WebWWData;

/**
 *
 * @author xiaodu
 * @version 2010-4-19 上午11:36:00
 */
public class MonitorWebWWAo {

	private static final Logger logger =  Logger.getLogger(MonitorWebWWAo.class);

	private static MonitorWebWWAo  ao = new MonitorWebWWAo();
	private MonitorWebWwDao dao = new MonitorWebWwDao();

	public static  MonitorWebWWAo get(){
		return ao;
	}

    public WebWWData getWebWWOnlineNumber(int type, Date date) throws Exception {
        return dao.getWebWWOnlineNumber(type, date);
    }

    public void setWebWWOnlineNumber(int type, long number, Date date) throws Exception {
        dao.setWebWWOnlineNumber(type, number, date);
    }
    
    /**
     * 获取某天某时刻在线人数
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineNumberByTime(final String time){
    	return dao.getWebWWOnlineNumberByTime(time);
    }

    /**
     * 获取最近一分钟在线人数
     * @param appId
     * @param keyId
     * @param now
     * @return
     */
    public LinkedList<WebWWData> findWebWangWangOnlineNumber(long appId, long keyId, Date now) {
        try {
            return dao.findWebWangWangOnlineNumber(appId, keyId, now);
            
        } catch (Exception e) {
            logger.error("查询数据出错", e);
            return new LinkedList<WebWWData>();
        }
    }
    
    /**
     * 获取历史同一时刻最高在线人数
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineHistoryNumberByTime(final String time,final String type){
    	return dao.getWebWWOnlineHistoryNumberByTime(time,type);
    }
    
    /**
     * 更新历史同一时刻最高在线人数
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineHistoryNumberByTime(final String time,final String type,final long number,final String date){
    	return dao.updateWebWWOnlineHistoryNumberByTime(time,type,number,date);
    }
    
    /**
     * 插入历史同一时刻最高在线人数
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineHistoryNumberByTime(final String time,final String type,final long number,final String date){
    	return dao.insertWebWWOnlineHistoryNumberByTime(time,type,number,date);
    }
    
    /**
     * 获取某天最高在线人数
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineMaxNumberByDay(final String type,final String day){
    	return dao.getWebWWOnlineMaxNumberByDay(type,day);
    }
    
    /**
     * 更新某天最高在线人数
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineMaxNumberByDay(final String type,final String time,final String day,final long number,final String date){
    	return dao.updateWebWWOnlineMaxNumberByDay(type,time,day,number,date);
    }
    
    /**
     * 插入某天最高在线人数
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineMaxNumberByDay(final String type,final String time,final String day,
    		final long number,final String date,final String des,final String commet){
    	return dao.insertWebWWOnlineMaxNumberByDay(type,time,day,number,date,des,commet);
    }
    
    /**
     * 获取历史最高在线人数
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineHistoryMaxNumber(final String type){
    	return dao.getWebWWOnlineHistoryMaxNumber(type);
    }
    
    /**
     * 更新历史最高在线人数
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineHistoryMaxNumber(final String type,final String time,final String date,final String day,final long number,final String des){
    	return dao.updateWebWWOnlineHistoryMaxNumber(type,time,date,day,number,des);
    }
    
    /**
     * 插入历史最高在线人数
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineHistoryMaxNumber(final String type,final String time,final String date,final String day,final long number,final String des,final String commet){
    	return dao.insertWebWWOnlineHistoryMaxNumber(type,time,date,day,number,des,commet);
    }
    
    /**
     * 获取历史同一天最高在线
     * @return
     */
    public WebWWData getWebWWOnlineHistoryWeekMaxNumber(final String type,final String des){
    	return dao.getWebWWOnlineHistoryWeekMaxNumber(type,des);
    }
    
    /**
     * 更新历史同一天最高在线人数
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineHistoryWeekMaxNumber(final String type,final String time,final String date,final String day,final long number,final String des){
    	return dao.updateWebWWOnlineHistoryWeekMaxNumber(type, time, date, day, number, des);
    }
    
    /**
     * 插入历史同一天最高在线人数
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineHistoryWeekMaxNumber(final String type,final String time,final String date,
    		final String day,final long number,final String des,final String commet){
    	return dao.insertWebWWOnlineHistoryWeekMaxNumber(type, time, date, day, number, des, commet);
    }
    
    /**
     * 获取最近两周最高在线人数
     * @param appId
     * @param keyId
     * @param now
     * @return
     */
    public LinkedList<WebWWData> findWebWangWangMaxOnlineNumberOfTwoWeeks(String type) {
        try {
            return dao.findWebWangWangMaxOnlineNumberOfTwoWeeks(type);
            
        } catch (Exception e) {
            logger.error("查询数据出错", e);
            return null;
        }
    }

    /**
     * 获取某一时刻某应用的在线人数
     * @param appId
     * @param keyId
     * @param siteId
     * @param time
     * @return
     */
	public WebWWData getNumberByTimeAndIds(long appId, long keyId, long siteId,
			String time) {
		return dao.getNumberByTimeAndIds(appId, keyId, siteId, time);
	}

	
	
}
