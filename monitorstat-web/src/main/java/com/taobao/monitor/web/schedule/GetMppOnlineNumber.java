package com.taobao.monitor.web.schedule;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.web.ao.MonitorWebWWAo;
import com.taobao.monitor.web.vo.WebWWData;

/**
 * MPP����������
 * User: Yang Wenting
 * Date: 2011-06-14
 * Time: 10:57:05
 */
public class GetMppOnlineNumber extends TimerTask {
    public static final long APP_ID = 14L;
//    public static final long KEY_ID = 24197L; //���Կ�KEY_IDֵ
    public static final long KEY_ID = 45589L; //���Ͽ�KEY_IDֵ
    public static final long SITE_ID = 42L;
    volatile private int dirtyNumber = 0;
    private static final Logger logger = Logger.getLogger(GetMppOnlineNumber.class);
    private LinkedList<WebWWData> oneMinuteList;    //���һ������������
    private WebWWData now;                //Ŀǰ��������
    private WebWWData yestodayNow;        //����ͬһʱ����������
    private WebWWData lastweekNow;        //����ͬһʱ����������
    private WebWWData historyNow;        //��ʷͬһʱ����������
    private WebWWData todayMax;            //�����������
    private WebWWData yestodayMax;        //�����������
    private WebWWData lastweekDayMax;    //����ͬһ���������
    private WebWWData historyMax;        //��ʷ�������
    private WebWWData historyWeekMax;    //��ʷͬһ���������
    private LinkedList<WebWWData> twoWeeksList;        //��������������
    private String url = "http://110.75.13.100/online.jsp";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat gmtcreateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static GetMppOnlineNumber onlineNumber = new GetMppOnlineNumber();
    private static final String Histroy_Time = "100";    //ÿ5�����������
    private static final String Max_Day = "101";    //ÿ���������
    private static final String Max = "102";    //��ʷ�������
    private static final String History_Week_Max = "103";        //��ʷͬһ���������
    private String[] dayofweeks = {"(����)", "(��һ)", "(�ܶ�)", "(����)", "(����)", "(����)", "(����)"};

    private GetMppOnlineNumber() {
    }

    public void run() {
        try {
            long onlineNumber = getOnlieNumber() - dirtyNumber;
        	 
            if (onlineNumber > 0) {
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                date.setSeconds((date.getSeconds() / 5) * 5);
                // ��������������5����һ��
                MonitorTimeAo.get().addMonitorData(format.format(date), APP_ID, SITE_ID, KEY_ID, onlineNumber, gmtcreateFormat.format(date)); //yyyy-MM-dd HH:mm
                now = new WebWWData(onlineNumber, gmtcreateFormat.format(date));
                //�������һ������������
                oneMinuteList.addFirst(now);
                if (oneMinuteList.size() >= 12)
                    oneMinuteList.remove(oneMinuteList.size() - 1);

                //��ȡ��ʷͬһʱ�������������
                historyNow = MonitorWebWWAo.get().getWebWWOnlineHistoryNumberByTime(timeFormat.format(date), Histroy_Time);
                if (onlineNumber > historyNow.getNumber()) {
                    if (historyNow.getNumber() == -1) {
                        //������ʷͬһʱ�������������
                        MonitorWebWWAo.get().insertWebWWOnlineHistoryNumberByTime(timeFormat.format(date), Histroy_Time, onlineNumber, gmtcreateFormat.format(date));
                    } else { 
                        //������ʷͬһʱ�������������
                        MonitorWebWWAo.get().updateWebWWOnlineHistoryNumberByTime(timeFormat.format(date), Histroy_Time, onlineNumber, gmtcreateFormat.format(date));
                    }
                    historyNow = now;
                }
                //��ȡ���������������
                todayMax = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay(Max_Day, dayFormat.format(date));
                if (onlineNumber > todayMax.getNumber()) {
                    if (todayMax.getNumber() == -1) {
                        //������������������
                        MonitorWebWWAo.get().insertWebWWOnlineMaxNumberByDay(Max_Day, timeFormat.format(date), dayFormat.format(date), onlineNumber, gmtcreateFormat.format(date), getDayOfWeek(), "MPPÿ���������");
                    } else {
                        //���½��������������
                        MonitorWebWWAo.get().updateWebWWOnlineMaxNumberByDay(Max_Day, timeFormat.format(date), dayFormat.format(date), onlineNumber, gmtcreateFormat.format(date));
                    }
                    todayMax = now;
                }

                //��ȡ��ʷ�����������
                historyMax = MonitorWebWWAo.get().getWebWWOnlineHistoryMaxNumber(Max);
                if (onlineNumber > historyMax.getNumber()) {
                    if (historyMax.getNumber() == -1) {
                        //������ʷ�����������
                        MonitorWebWWAo.get().insertWebWWOnlineHistoryMaxNumber(Max, timeFormat.format(date), gmtcreateFormat.format(date), dayFormat.format(date), onlineNumber, getDayOfWeek(), "MPP��ʷ�������");
                    } else {
                        //������ʷ�����������
                        MonitorWebWWAo.get().updateWebWWOnlineHistoryMaxNumber(Max, timeFormat.format(date), gmtcreateFormat.format(date), dayFormat.format(date), onlineNumber, getDayOfWeek());
                    }
                    historyMax.setNumber(onlineNumber);
                    historyMax.setDate(gmtcreateFormat.format(date) + getDayOfWeek());
                }

                //��ȡ��ʷͬһ���������
                historyWeekMax = MonitorWebWWAo.get().getWebWWOnlineHistoryWeekMaxNumber(History_Week_Max, getDayOfWeek());
                if (onlineNumber > historyWeekMax.getNumber()) {
                    if (historyWeekMax.getNumber() == -1) {
                        //������ʷͬһ�������������
                        MonitorWebWWAo.get().insertWebWWOnlineHistoryWeekMaxNumber(History_Week_Max, timeFormat.format(date), gmtcreateFormat.format(date),
                                dayFormat.format(date), onlineNumber, getDayOfWeek(), "MPP��ʷͬһ�������������");
                    } else {
                        //������ʷͬһ�������������
                        MonitorWebWWAo.get().updateWebWWOnlineHistoryWeekMaxNumber(History_Week_Max, timeFormat.format(date), gmtcreateFormat.format(date),
                                dayFormat.format(date), onlineNumber, getDayOfWeek());
                    }
                    historyWeekMax = now;
                }

                //����
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date yes = calendar.getTime();
                yes.setSeconds((date.getSeconds() / 5) * 5);
                //��ȡ����ͬһʱ����������
                yestodayNow = MonitorWebWWAo.get().getNumberByTimeAndIds(APP_ID, KEY_ID,SITE_ID,gmtcreateFormat.format(yes));
//                yestodayNow = MonitorWebWWAo.get().getWebWWOnlineNumberByTime(gmtcreateFormat.format(yes));
                //��ȡ���������������
                yestodayMax = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay(Max_Day, dayFormat.format(yes));

                //����
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                Date lastweek = calendar.getTime();
                lastweek.setSeconds((date.getSeconds() / 5) * 5);
                //��ȡ����ͬһʱ����������
                lastweekNow = MonitorWebWWAo.get().getNumberByTimeAndIds(APP_ID, KEY_ID,SITE_ID, gmtcreateFormat.format(lastweek));
                //��ȡ����ͬһ�������������
                lastweekDayMax = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay(Max_Day, dayFormat.format(lastweek));
                //��ȡ������������������
                twoWeeksList = MonitorWebWWAo.get().findWebWangWangMaxOnlineNumberOfTwoWeeks(Max_Day);

//                print();
            }
        } catch (Exception e) {
            //logger.error("���г���", e);
        }
    }

    private String getDayOfWeek() {
        int dayofweek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        return dayofweeks[dayofweek - 1];
    }

    public long getOnlieNumber() {
        long onlineNumber = 0;
        try {
            URL url = new URL(this.url);

            URLConnection httpConnection = url.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setConnectTimeout(3000);
            httpConnection.connect();
            InputStream is = httpConnection.getInputStream();

            StringBuffer contents = new StringBuffer();
            byte[] buf = new byte[128];
            int size = 0;
            while ((size = is.read(buf)) > 0) {
                if (buf[size - 1] == '\n') {
                    size = size - 1;
                }
                contents.append(new String(buf, 0, size));
            }
            onlineNumber = Long.parseLong(contents.toString(), 10);
        } catch (Exception e) {
           // logger.error("ȡԶ�̵����ݳ���", e);
        }
        return onlineNumber;
    }
    
    /*private void print(){
       if(historyNow !=null){
    	   System.out.println("��ʷͬһʱ���������");
    	   System.out.print(historyNow.getDate()+"  ");
    	   System.out.println(historyNow.getNumber());
       }
       if(todayMax != null){
    	   System.out.println("�����������");
    	   System.out.print(todayMax.getDate()+"  ");
    	   System.out.println(todayMax.getNumber());   	   
       }
       if(yestodayMax != null){
    	   System.out.println("�����������");
    	   System.out.print(yestodayMax.getDate()+"  ");
    	   System.out.println(yestodayMax.getNumber());     	   
       }
       if(lastweekDayMax != null){
    	   System.out.println("����ͬһ���������");
    	   System.out.print(lastweekDayMax.getDate()+"  ");
    	   System.out.println(lastweekDayMax.getNumber());      	   
       }
       if(historyMax != null){
    	   System.out.println("��ʷ�������");
    	   System.out.print(historyMax.getDate()+"  ");
    	   System.out.println(historyMax.getNumber());    	   
       }
       if(historyWeekMax != null){
    	   System.out.println("��ʷͬһ���������");
    	   System.out.print(historyWeekMax.getDate()+"  ");
    	   System.out.println(historyWeekMax.getNumber());    	   
       }
       
       if(yestodayNow != null){
    	   System.out.println("����ͬһʱ������");
    	   System.out.print(yestodayNow.getDate()+"  ");
    	   System.out.println(yestodayNow.getNumber());    	   
       }
       
       System.out.println("��������������");
       for(WebWWData data:twoWeeksList){
    	   System.out.print(data.getDate()+"  ");
    	   System.out.println(data.getNumber());     	   
       }
       
       System.out.println("���һ������������");
       for(WebWWData data:oneMinuteList){
    	   System.out.print(data.getDate()+"  ");
    	   System.out.println(data.getNumber());     	   
       }
       
       System.out.println("");
       System.out.println("----------------------  �ָ���  ----------------------");
       System.out.println("");
       System.out.println("");
    }*/


    public static GetMppOnlineNumber getInstance() {
        synchronized (GetMppOnlineNumber.class) {
            //��ȡ���һ������������
            if (onlineNumber.oneMinuteList == null) {
                onlineNumber.oneMinuteList = MonitorWebWWAo.get().findWebWangWangOnlineNumber(APP_ID, KEY_ID, new Date());
            }
        }
        return onlineNumber;
    }

    public int getDirtyNumber() {
        return dirtyNumber;
    }

    public void setDirtyNumber(int dirtyNumber) {
        this.dirtyNumber = dirtyNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LinkedList<WebWWData> getOneMinuteList() {
        return oneMinuteList;
    }

    public WebWWData getNow() {
        return now;
    }

    public WebWWData getYestodayNow() {
        return yestodayNow;
    }

    public WebWWData getLastweekNow() {
        return lastweekNow;
    }

    public WebWWData getHistoryNow() {
        return historyNow;
    }

    public WebWWData getTodayMax() {
        return todayMax;
    }

    public WebWWData getYestodayMax() {
        return yestodayMax;
    }

    public WebWWData getLastweekDayMax() {
        return lastweekDayMax;
    }

    public WebWWData getHistoryMax() {
        return historyMax;
    }

    public WebWWData getHistoryWeekMax() {
        return historyWeekMax;
    }

    public LinkedList<WebWWData> getTwoWeeksList() {
        return twoWeeksList;
    }

    public static void main(String[] args) {
//        GetMppOnlineNumber.getInstance().run();
//    	new Timer().scheduleAtFixedRate(GetMppOnlineNumber.getInstance(), 0, 1000 * 5);
    	
    }
}
