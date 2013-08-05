//package com.taobao.monitor.web.baseline;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.monitor.common.util.Arith;
//import com.taobao.monitor.common.util.Constants;
//import com.taobao.monitor.report.messageSender.HaBoMessageSenderServiceImpl;
//import com.taobao.monitor.report.messageSender.IHaBoMessageSenderService;
//import com.taobao.monitor.web.ao.MonitorAlarmAo;
//import com.taobao.monitor.common.ao.center.AppInfoAo;
//import com.taobao.monitor.web.vo.AlarmDataPo;
//import com.taobao.monitor.common.po.AppInfoPo;
//
///**
// * 
// * @author xiaodu
// * @version 2010-8-31 下午05:28:44
// */
//public class BaseLineThread extends Thread {
//
//	private static final Logger logger = Logger.getLogger(BaseLineThread.class);
//
//	IHaBoMessageSenderService messageSender = new HaBoMessageSenderServiceImpl();
//
//	public BaseLineThread() {
//		this.setDaemon(true);
//	}
//
//	Map<String, Long> calculateEventMap = new HashMap<String, Long>();
//
//	public void run() {
//		while (true) {
//			try {
//				Thread.sleep(1000 * 5 * 60);
//			} catch (InterruptedException e) {
//			}
//			
//			try{
//				docalculate();
//			}catch (Exception e) {
//				logger.info("",e);
//			}
//		}
//	}
//
//	private void docalculate() {
//		List<AppInfoPo> allApps = AppInfoAo.get().findAllEffectiveAppInfo();
//		for (AppInfoPo po : allApps) {
//			int appId = po.getAppId();
//			List<AlarmDataPo> keyList = MonitorAlarmAo.get().findAllAlarmKeyByAimAndLikeName(appId , null);
//			for (AlarmDataPo key : keyList) {
//				try{
//					if (key.getKeyName().indexOf(Constants.AVERAGE_USERTIMES_FLAG) > 0) {
//						double b = BaseLineManage.get().calculateValueUpper(appId, Integer.parseInt(key.getKeyId()));
//					//	logger.info("开始预测上升:" + po.getAppName() + "__" + key.getKeyName() + ":" + b);
//						if (b > 0.8) {
//							addCalculateEvent(1, po, key, b);
//						}
//	
//					} else if (key.getKeyName().indexOf(Constants.COUNT_TIMES_FLAG) > 0) {
//						double b1 = BaseLineManage.get().calculateValueUpper(appId, Integer.parseInt(key.getKeyId()));
//					//	logger.info("开始预测上升:" + po.getAppName() + "__" + key.getKeyName() + ":" + b1);
//						if (b1 > 0.8) {
//							addCalculateEvent(1, po, key, b1);
//						}
//						double b2 = BaseLineManage.get().calculateValueDown(appId, Integer.parseInt(key.getKeyId()));
//						if (b2 > 0.8) {
//							addCalculateEvent(2, po, key, b2);
//						}
//						//logger.info("开始预测下降:" + po.getAppName() + "__" + key.getKeyName() + ":" + b2);
//	
//					} else {
//						double b = BaseLineManage.get().calculateValueUpper(appId, Integer.parseInt(key.getKeyId()));
//						if (b > 0.8) {
//							addCalculateEvent(1, po, key, b);
//						}
//					//	logger.info("开始预测上升:" + po.getAppName() + "__" + key.getKeyName() + ":" + b);
//					}
//				}catch (Exception e) {
//					logger.info("",e);
//				}
//			}
//		}
//	}
//
//	private void addCalculateEvent(int type, AppInfoPo app, AlarmDataPo keyPo, double v) {
//
//		String key = type + "_" + app.getAppName() + "_" + keyPo.getKeyName();
//		Long l = calculateEventMap.get(key);
//		if (l == null) {
//			calculateEventMap.put(key, System.currentTimeMillis());
//			send(type, app, keyPo, v);
//		} else {
//			if (System.currentTimeMillis() - l > 180 * 60 * 1000) {
//				send(type, app, keyPo, v);
//				calculateEventMap.put(key, System.currentTimeMillis());
//			}
//		}
//
//	}
//
//	private void send(int type, AppInfoPo app, AlarmDataPo key, double v) {
//
//		String url = "http://cm.taobao.net:9999/monitorstat/show.jsp?appId=" + app.getAppId()
//				+ "&keyId=" + key.getKeyId() + "&appName=" + app.getAppName();
//
//		String k = key.getAlarmFeature() == null ? key.getKeyName() : key.getAlarmFeature();
//
//		boolean result = true;
//		try {
//			if (type == 1) {
//				// AlarmUserManage.get().sendAlarm(app.getAppId(),"注意-核心监控","应用:"+app.getAppName()+" 监控点:"+k+" 目前长期运行在基线之上，同时保持着"+Arith.mul(v,
//				// 100)+"%的增长趋势"+url);
//				
//				
////				if(app.getAppId()<10){
////					result = messageSender.sendWangwang("ww", "", "", "", "", "1", "", "应用:" + app.getAppName() + " 监控点:"
////						+ k + " 目前长期运行在基线之上，同时保持着" + Arith.mul(v, 100) + "%的增长趋势" + url, "注意-性能监控告警", "", "小赌;小邪;范禹");
////				}else{
//					result = messageSender.sendWangwang("ww", "", "", "", "", "1", "", "应用:" + app.getAppName() + " 监控点:"
//							+ k + " 目前长期运行在基线之上，同时保持着" + Arith.mul(v, 100) + "%的增长趋势" + url, "注意-性能监控告警", "", "小赌;");
////				}
//			}
//			if (type == 2) {
//				// AlarmUserManage.get().sendAlarm(app.getAppId(),"注意-核心监控","应用:"+app.getAppName()+" 监控点:"+k+" 目前长期运行在基线之下，同时保持着"+Arith.mul(v,
//				// 100)+"%的下降趋势 "+url);
//				
////				if(app.getAppId()<10){
////					result = messageSender.sendWangwang("ww", "", "", "", "", "1", "", "应用:" + app.getAppName() + " 监控点:"
////						+ k + " 目前长期运行在基线之下，同时保持着" + Arith.mul(v, 100) + "%的下降趋势 " + url, "注意-性能监控告警", "", "小赌;小邪;范禹");
////				}else{
//					result = messageSender.sendWangwang("ww", "", "", "", "", "1", "", "应用:" + app.getAppName() + " 监控点:"
//							+ k + " 目前长期运行在基线之下，同时保持着" + Arith.mul(v, 100) + "%的下降趋势 " + url, "注意-性能监控告警", "", "小赌;");
////				}
//			}
//		} catch (Exception e) {
//		}
//		if (!result) {
//			logger.info("小赌;小邪;范禹:应用:" + app.getAppName() + " 监控点:" + k + " 目前长期运行在基线之上，同时保持着" + (v * 100)
//					+ "%的增长趋势 wangwang send fail!");
//		} else {
//			logger.info("小赌;小邪;范禹:应用:" + app.getAppName() + " 监控点:" + k + " 目前长期运行在基线之上，同时保持着" + (v * 100)
//					+ "%的增长趋势 wangwang send success!");
//		}
//
//	}
//
//	public static void main(String[] args) {
//
//		BaseLineThread thread = new BaseLineThread();
//		thread.docalculate();
//
//	}
//
//}
