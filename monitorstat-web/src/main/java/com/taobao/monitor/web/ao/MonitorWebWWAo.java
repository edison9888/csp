
package com.taobao.monitor.web.ao;

import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.MonitorWebWwDao;
import com.taobao.monitor.web.vo.WebWWData;

/**
 *
 * @author xiaodu
 * @version 2010-4-19 ����11:36:00
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
     * ��ȡĳ��ĳʱ����������
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineNumberByTime(final String time){
    	return dao.getWebWWOnlineNumberByTime(time);
    }

    /**
     * ��ȡ���һ������������
     * @param appId
     * @param keyId
     * @param now
     * @return
     */
    public LinkedList<WebWWData> findWebWangWangOnlineNumber(long appId, long keyId, Date now) {
        try {
            return dao.findWebWangWangOnlineNumber(appId, keyId, now);
            
        } catch (Exception e) {
            logger.error("��ѯ���ݳ���", e);
            return new LinkedList<WebWWData>();
        }
    }
    
    /**
     * ��ȡ��ʷͬһʱ�������������
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineHistoryNumberByTime(final String time,final String type){
    	return dao.getWebWWOnlineHistoryNumberByTime(time,type);
    }
    
    /**
     * ������ʷͬһʱ�������������
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineHistoryNumberByTime(final String time,final String type,final long number,final String date){
    	return dao.updateWebWWOnlineHistoryNumberByTime(time,type,number,date);
    }
    
    /**
     * ������ʷͬһʱ�������������
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineHistoryNumberByTime(final String time,final String type,final long number,final String date){
    	return dao.insertWebWWOnlineHistoryNumberByTime(time,type,number,date);
    }
    
    /**
     * ��ȡĳ�������������
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineMaxNumberByDay(final String type,final String day){
    	return dao.getWebWWOnlineMaxNumberByDay(type,day);
    }
    
    /**
     * ����ĳ�������������
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineMaxNumberByDay(final String type,final String time,final String day,final long number,final String date){
    	return dao.updateWebWWOnlineMaxNumberByDay(type,time,day,number,date);
    }
    
    /**
     * ����ĳ�������������
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineMaxNumberByDay(final String type,final String time,final String day,
    		final long number,final String date,final String des,final String commet){
    	return dao.insertWebWWOnlineMaxNumberByDay(type,time,day,number,date,des,commet);
    }
    
    /**
     * ��ȡ��ʷ�����������
     * @param time
     * @return
     */
    public WebWWData getWebWWOnlineHistoryMaxNumber(final String type){
    	return dao.getWebWWOnlineHistoryMaxNumber(type);
    }
    
    /**
     * ������ʷ�����������
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineHistoryMaxNumber(final String type,final String time,final String date,final String day,final long number,final String des){
    	return dao.updateWebWWOnlineHistoryMaxNumber(type,time,date,day,number,des);
    }
    
    /**
     * ������ʷ�����������
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineHistoryMaxNumber(final String type,final String time,final String date,final String day,final long number,final String des,final String commet){
    	return dao.insertWebWWOnlineHistoryMaxNumber(type,time,date,day,number,des,commet);
    }
    
    /**
     * ��ȡ��ʷͬһ���������
     * @return
     */
    public WebWWData getWebWWOnlineHistoryWeekMaxNumber(final String type,final String des){
    	return dao.getWebWWOnlineHistoryWeekMaxNumber(type,des);
    }
    
    /**
     * ������ʷͬһ�������������
     * @param time
     * @return
     */
    public boolean updateWebWWOnlineHistoryWeekMaxNumber(final String type,final String time,final String date,final String day,final long number,final String des){
    	return dao.updateWebWWOnlineHistoryWeekMaxNumber(type, time, date, day, number, des);
    }
    
    /**
     * ������ʷͬһ�������������
     * @param time
     * @return
     */
    public boolean insertWebWWOnlineHistoryWeekMaxNumber(final String type,final String time,final String date,
    		final String day,final long number,final String des,final String commet){
    	return dao.insertWebWWOnlineHistoryWeekMaxNumber(type, time, date, day, number, des, commet);
    }
    
    /**
     * ��ȡ������������������
     * @param appId
     * @param keyId
     * @param now
     * @return
     */
    public LinkedList<WebWWData> findWebWangWangMaxOnlineNumberOfTwoWeeks(String type) {
        try {
            return dao.findWebWangWangMaxOnlineNumberOfTwoWeeks(type);
            
        } catch (Exception e) {
            logger.error("��ѯ���ݳ���", e);
            return null;
        }
    }

    /**
     * ��ȡĳһʱ��ĳӦ�õ���������
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
