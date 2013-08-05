package com.taobao.monitor.web.schedule;

import java.util.List;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.time.ScheduleContainer;
import com.taobao.monitor.other.mysql.CreateTableJob;
import com.taobao.monitor.other.review.AppCheckJob;
import com.taobao.monitor.web.core.dao.impl.MonitorReportConfigDao;
import com.taobao.monitor.web.vo.ReportTemplate;


/**
 * @author xiaodu
 * @version 2010-5-26 下午02:33:26
 */
public class ScheduleControl {

    private static final Logger logger = Logger.getLogger(ScheduleControl.class);


    public static void startSchedule() {   	
//    	ScheduleContainer.addJob("analyse-job", "0 0 5 * * ?", BaseLineJob.class);
//    	ScheduleContainer.addJob("insert-baseLine-job", "0 0 0 * * ?", MonitorBaseLineJob.class);
    	ScheduleContainer.addJob("mail-job", "0 10 9 * * ?", MailReportJob.class);
    	//ScheduleContainer.addJob("Ranking-Job", "0 0 9 * * ?", RankingJob.class);
    	//ScheduleContainer.addJob("port-dep-Job", "0 30 3 * * ?", AppPortCheckupJob.class);
    	//ScheduleContainer.addJob("jar-dep-Job", "0 0 2 * * ?", AppJarInfoJob.class);
    	ScheduleContainer.addJob("mysqltable-Job", "0 30 6 * * ?", CreateTableJob.class);
    	ScheduleContainer.addJob("app-check-Job", "0 0 1 * * ?", AppCheckJob.class);
    	ScheduleContainer.addJob("week-report-Job", "0 30 6 * * ?", CollectReportDataJob.class);
    	//ScheduleContainer.addJob("app-pv-Job", "0 0 0 * * ?", UpdateAppPvJob.class);
//    	ScheduleContainer.addJob("limit-check-Job", "0 0/30 9-23 * * ?", CheckLimitJob.class);
//    	ScheduleContainer.addJob("urlmai-job", "0 50 23 * * ?", UrlSendEmailJob.class);
//    	ScheduleContainer.addJob("statistics-job", "0 0/3 * * * ?", UrlStatisticsJob.class);
        try {
            startWebWWOnline();
        } catch (Throwable e) {
            logger.error("WEB旺旺实时统计,", e);
        }
        startReportJob();
    }

    public static void startWebWWOnline() {
    	Timer timer = new Timer();
        timer.scheduleAtFixedRate(GetWebWWOnlineNumber.getInstance(), 0, 1000 * 10);
        timer.scheduleAtFixedRate(GetMppOnlineNumber.getInstance(), 0, 1000 * 10);
        timer.scheduleAtFixedRate(GetAuctionsOnlineNumber.getInstance(), 0, 1000 * 10);
    }
    
    /**
     * 初始化报表job程序
     * @author 斩飞
     * 2011-5-13 - 下午05:48:41
     */
    public static void startReportJob(){
    	logger.info("startReportJob....");
    	MonitorReportConfigDao reportConfigDao = new MonitorReportConfigDao();
		List<ReportTemplate> reports = reportConfigDao.findAllReport();
		for(ReportTemplate report: reports){
			QuartzManager.addJob(report.getReportId()+"", 
					report.getQuartzCron(), ReportJob.class);
		}
		logger.info("startReportJob end ...");
    }
    
    public static void main(String[] args) {
    	ScheduleControl.startReportJob();
	}

}
