package com.taobao.monitor.alarm.trade.realtime;

import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class ReatimeTradeListner implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(ReatimeTradeListner.class);
	//��ʱ��   
    private static Timer timer = null; 
    //��ʱ��  20sһ��
    private static Timer timer2 = null; 
	//��ʱ�� 1s һ��   
    private static Timer wapTimer = null; 
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		timer = new Timer(true); 
		timer2 = new Timer(true);
		wapTimer = new Timer(true);
		logger.warn("��ʱ��������,����ʼִ��");  
        timer.schedule(new RealTimeTradeTask(), 0, 1000);  
        timer2.schedule(new RealTimeTradeThirdTask(), 0, 20000);
        wapTimer.schedule(new RealTimeWapTradeTask(), 0, 1000); 
	}
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		 timer.cancel();
		 timer2.cancel();
		 wapTimer.cancel();
	     //logger.warn("��ʱ��������,����ִ�н���");
	}

}
