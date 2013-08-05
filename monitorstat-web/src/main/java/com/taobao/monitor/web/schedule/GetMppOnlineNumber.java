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
 * MPP在想人数。
 * User: Yang Wenting
 * Date: 2011-06-14
 * Time: 10:57:05
 */
public class GetMppOnlineNumber extends TimerTask {
    public static final long APP_ID = 14L;
//    public static final long KEY_ID = 24197L; //测试库KEY_ID值
    public static final long KEY_ID = 45589L; //线上库KEY_ID值
    public static final long SITE_ID = 42L;
    volatile private int dirtyNumber = 0;
    private static final Logger logger = Logger.getLogger(GetMppOnlineNumber.class);
    private LinkedList<WebWWData> oneMinuteList;    //最近一分钟在线人数
    private WebWWData now;                //目前在线人数
    private WebWWData yestodayNow;        //昨天同一时刻在线人数
    private WebWWData lastweekNow;        //上周同一时刻在线人数
    private WebWWData historyNow;        //历史同一时刻在线人数
    private WebWWData todayMax;            //今天最高在线
    private WebWWData yestodayMax;        //昨天最高在线
    private WebWWData lastweekDayMax;    //上周同一天最高在线
    private WebWWData historyMax;        //历史最高在线
    private WebWWData historyWeekMax;    //历史同一天最高在线
    private LinkedList<WebWWData> twoWeeksList;        //最近两周最高在线
    private String url = "http://110.75.13.100/online.jsp";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat gmtcreateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static GetMppOnlineNumber onlineNumber = new GetMppOnlineNumber();
    private static final String Histroy_Time = "100";    //每5秒钟最高在线
    private static final String Max_Day = "101";    //每天最高在线
    private static final String Max = "102";    //历史最高在线
    private static final String History_Week_Max = "103";        //历史同一天最高在线
    private String[] dayofweeks = {"(周日)", "(周一)", "(周二)", "(周三)", "(周四)", "(周五)", "(周六)"};

    private GetMppOnlineNumber() {
    }

    public void run() {
        try {
            long onlineNumber = getOnlieNumber() - dirtyNumber;
        	 
            if (onlineNumber > 0) {
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                date.setSeconds((date.getSeconds() / 5) * 5);
                // 加入在线人数，5秒钟一次
                MonitorTimeAo.get().addMonitorData(format.format(date), APP_ID, SITE_ID, KEY_ID, onlineNumber, gmtcreateFormat.format(date)); //yyyy-MM-dd HH:mm
                now = new WebWWData(onlineNumber, gmtcreateFormat.format(date));
                //更新最近一分钟在线人数
                oneMinuteList.addFirst(now);
                if (oneMinuteList.size() >= 12)
                    oneMinuteList.remove(oneMinuteList.size() - 1);

                //获取历史同一时刻最高在线人数
                historyNow = MonitorWebWWAo.get().getWebWWOnlineHistoryNumberByTime(timeFormat.format(date), Histroy_Time);
                if (onlineNumber > historyNow.getNumber()) {
                    if (historyNow.getNumber() == -1) {
                        //插入历史同一时刻最高在线人数
                        MonitorWebWWAo.get().insertWebWWOnlineHistoryNumberByTime(timeFormat.format(date), Histroy_Time, onlineNumber, gmtcreateFormat.format(date));
                    } else { 
                        //更新历史同一时刻最高在线人数
                        MonitorWebWWAo.get().updateWebWWOnlineHistoryNumberByTime(timeFormat.format(date), Histroy_Time, onlineNumber, gmtcreateFormat.format(date));
                    }
                    historyNow = now;
                }
                //获取今天最高在线人数
                todayMax = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay(Max_Day, dayFormat.format(date));
                if (onlineNumber > todayMax.getNumber()) {
                    if (todayMax.getNumber() == -1) {
                        //插入今天最高在线人数
                        MonitorWebWWAo.get().insertWebWWOnlineMaxNumberByDay(Max_Day, timeFormat.format(date), dayFormat.format(date), onlineNumber, gmtcreateFormat.format(date), getDayOfWeek(), "MPP每天最高在线");
                    } else {
                        //更新今天最高在线人数
                        MonitorWebWWAo.get().updateWebWWOnlineMaxNumberByDay(Max_Day, timeFormat.format(date), dayFormat.format(date), onlineNumber, gmtcreateFormat.format(date));
                    }
                    todayMax = now;
                }

                //获取历史最高在线人数
                historyMax = MonitorWebWWAo.get().getWebWWOnlineHistoryMaxNumber(Max);
                if (onlineNumber > historyMax.getNumber()) {
                    if (historyMax.getNumber() == -1) {
                        //插入历史最高在线人数
                        MonitorWebWWAo.get().insertWebWWOnlineHistoryMaxNumber(Max, timeFormat.format(date), gmtcreateFormat.format(date), dayFormat.format(date), onlineNumber, getDayOfWeek(), "MPP历史最高在线");
                    } else {
                        //更新历史最高在线人数
                        MonitorWebWWAo.get().updateWebWWOnlineHistoryMaxNumber(Max, timeFormat.format(date), gmtcreateFormat.format(date), dayFormat.format(date), onlineNumber, getDayOfWeek());
                    }
                    historyMax.setNumber(onlineNumber);
                    historyMax.setDate(gmtcreateFormat.format(date) + getDayOfWeek());
                }

                //获取历史同一天最高在线
                historyWeekMax = MonitorWebWWAo.get().getWebWWOnlineHistoryWeekMaxNumber(History_Week_Max, getDayOfWeek());
                if (onlineNumber > historyWeekMax.getNumber()) {
                    if (historyWeekMax.getNumber() == -1) {
                        //插入历史同一天最高在线人数
                        MonitorWebWWAo.get().insertWebWWOnlineHistoryWeekMaxNumber(History_Week_Max, timeFormat.format(date), gmtcreateFormat.format(date),
                                dayFormat.format(date), onlineNumber, getDayOfWeek(), "MPP历史同一天最高在线人数");
                    } else {
                        //更新历史同一天最高在线人数
                        MonitorWebWWAo.get().updateWebWWOnlineHistoryWeekMaxNumber(History_Week_Max, timeFormat.format(date), gmtcreateFormat.format(date),
                                dayFormat.format(date), onlineNumber, getDayOfWeek());
                    }
                    historyWeekMax = now;
                }

                //昨天
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date yes = calendar.getTime();
                yes.setSeconds((date.getSeconds() / 5) * 5);
                //获取昨天同一时刻在线人数
                yestodayNow = MonitorWebWWAo.get().getNumberByTimeAndIds(APP_ID, KEY_ID,SITE_ID,gmtcreateFormat.format(yes));
//                yestodayNow = MonitorWebWWAo.get().getWebWWOnlineNumberByTime(gmtcreateFormat.format(yes));
                //获取昨天最高在线人数
                yestodayMax = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay(Max_Day, dayFormat.format(yes));

                //上周
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                Date lastweek = calendar.getTime();
                lastweek.setSeconds((date.getSeconds() / 5) * 5);
                //获取上周同一时刻在线人数
                lastweekNow = MonitorWebWWAo.get().getNumberByTimeAndIds(APP_ID, KEY_ID,SITE_ID, gmtcreateFormat.format(lastweek));
                //获取上周同一天最高在线人数
                lastweekDayMax = MonitorWebWWAo.get().getWebWWOnlineMaxNumberByDay(Max_Day, dayFormat.format(lastweek));
                //获取最近两周最高在线人数
                twoWeeksList = MonitorWebWWAo.get().findWebWangWangMaxOnlineNumberOfTwoWeeks(Max_Day);

//                print();
            }
        } catch (Exception e) {
            //logger.error("运行出错：", e);
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
           // logger.error("取远程的数据出错。", e);
        }
        return onlineNumber;
    }
    
    /*private void print(){
       if(historyNow !=null){
    	   System.out.println("历史同一时刻最高数据");
    	   System.out.print(historyNow.getDate()+"  ");
    	   System.out.println(historyNow.getNumber());
       }
       if(todayMax != null){
    	   System.out.println("今天最高数据");
    	   System.out.print(todayMax.getDate()+"  ");
    	   System.out.println(todayMax.getNumber());   	   
       }
       if(yestodayMax != null){
    	   System.out.println("昨天最高数据");
    	   System.out.print(yestodayMax.getDate()+"  ");
    	   System.out.println(yestodayMax.getNumber());     	   
       }
       if(lastweekDayMax != null){
    	   System.out.println("上周同一天最高数据");
    	   System.out.print(lastweekDayMax.getDate()+"  ");
    	   System.out.println(lastweekDayMax.getNumber());      	   
       }
       if(historyMax != null){
    	   System.out.println("历史最高数据");
    	   System.out.print(historyMax.getDate()+"  ");
    	   System.out.println(historyMax.getNumber());    	   
       }
       if(historyWeekMax != null){
    	   System.out.println("历史同一天最高数据");
    	   System.out.print(historyWeekMax.getDate()+"  ");
    	   System.out.println(historyWeekMax.getNumber());    	   
       }
       
       if(yestodayNow != null){
    	   System.out.println("昨天同一时刻数据");
    	   System.out.print(yestodayNow.getDate()+"  ");
    	   System.out.println(yestodayNow.getNumber());    	   
       }
       
       System.out.println("最近两周最高数据");
       for(WebWWData data:twoWeeksList){
    	   System.out.print(data.getDate()+"  ");
    	   System.out.println(data.getNumber());     	   
       }
       
       System.out.println("最近一分钟在线人数");
       for(WebWWData data:oneMinuteList){
    	   System.out.print(data.getDate()+"  ");
    	   System.out.println(data.getNumber());     	   
       }
       
       System.out.println("");
       System.out.println("----------------------  分割线  ----------------------");
       System.out.println("");
       System.out.println("");
    }*/


    public static GetMppOnlineNumber getInstance() {
        synchronized (GetMppOnlineNumber.class) {
            //获取最近一分钟在线人数
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
