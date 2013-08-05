package com.taobao.csp.time.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.MonitorAppUtil;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppUrlRelation;
import com.taobao.monitor.common.po.CspAppRtCount;

/**
 * 分析应用的响应时间和相关的URl的响应时间
 * @author zhongting.zy
 *
 */
public class ResponseTimeJob {
	private CommonServiceInterface commonService = null;
	private static final Logger logger = Logger.getLogger("cronTaskLog");

	public void calculateRt() {
		String capacity = System.getenv("RT_CALCULATE");
		if (capacity == null || !capacity.equals("true")) {
			logger.info("不是计算RT的主机，定时任务忽略");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
		Date date = cal.getTime();

		logger.info("开始执行RT分析程序->" + new Date());
		logger.info("删除" + new Date() + "的数据!");
		AppInfoAo.get().deleteAppRtCountByTime(date);

		long begin = System.currentTimeMillis();

		WebApplicationContext webAppContext = ContextLoader.getCurrentWebApplicationContext();
		commonService = (CommonServiceInterface)webAppContext.getBean("commonService");	

		List<String> applist = MonitorAppUtil.getMonitorApps();
		for(String app:applist) {
			List<TimeDataInfo> list = commonService.queryKeyDataHistory(app, KeyConstants.PV, PropConstants.E_TIMES, date);
			if(list.size()==0){
				continue;
			}

			long pv_count = 0;//这个是总pv
			long rt_error_count = 0;//大于1000ms的请求
			long rt_100_count = 0;//0-100ms的请求
			long rt_500_count = 0;//100-500ms的请求
			long rt_1000_count = 0;//500-1000ms的请求

			for(TimeDataInfo td:list) {
				Object pv = td.getOriginalPropertyMap().get(PropConstants.E_TIMES);
				pv_count+=DataUtil.transformInt(pv);
				Object rt_error = td.getOriginalPropertyMap().get("rt_error");
				rt_error_count+=DataUtil.transformInt(rt_error);

				Object rt_100 = td.getOriginalPropertyMap().get("rt_100");
				if(rt_100 != null){
					rt_100_count+=DataUtil.transformInt(rt_100);
				}
				Object rt_500 = td.getOriginalPropertyMap().get("rt_500");

				if(rt_500 != null){
					rt_500_count+=DataUtil.transformInt(rt_500);
				}

				Object rt_1000 = td.getOriginalPropertyMap().get("rt_1000");
				if(rt_1000 != null){
					rt_1000_count+=DataUtil.transformInt(rt_1000);
				}
			}

			CspAppRtCount apprt = new CspAppRtCount();
			apprt.setAppName(app);
			apprt.setCollectTime(date);
			apprt.setPvRt100(rt_100_count);
			apprt.setPvRt1000(rt_1000_count);
			apprt.setPvRt500(rt_500_count);
			apprt.setPvRtCount(pv_count);
			apprt.setPvRtError(rt_error_count);
			apprt.setPvRtType("app");

			AppInfoAo.get().addCspAppRtCount(apprt);
			List<AppUrlRelation> urlList = AppInfoAo.get().findAllAppUrlRelation(app);

			//统计这个应用下面的所有URL信息
			for(AppUrlRelation url:urlList) {
				long pv_url_count = 0;
				long rt_url_count = 0;
				long rt_url_100_count = 0;
				long rt_url_500_count = 0;
				long rt_url_1000_count = 0;

				long rt_max = 0;
				List<TimeDataInfo> urlDataList = commonService.queryKeyDataHistory(app, KeyConstants.PV + Constants.S_SEPERATOR + url.getAppUrl(), PropConstants.E_TIMES, date);

				for(TimeDataInfo td:urlDataList){
					Object pv = td.getOriginalPropertyMap().get(PropConstants.E_TIMES);
					pv_url_count+=DataUtil.transformInt(pv);
					Object rt_error = td.getOriginalPropertyMap().get("rt_error");
					int error = DataUtil.transformInt(rt_error);
					if(error >rt_max){
						rt_max = error;
					}

					rt_url_count+=error;

					Object rt_100 = td.getOriginalPropertyMap().get("rt_100");
					if(rt_100 != null){
						rt_url_100_count+=DataUtil.transformInt(rt_100);
					}
					Object rt_500 = td.getOriginalPropertyMap().get("rt_500");

					if(rt_500 != null){
						rt_url_500_count+=DataUtil.transformInt(rt_500);
					}

					Object rt_1000 = td.getOriginalPropertyMap().get("rt_1000");
					if(rt_1000 != null){
						rt_url_1000_count+=DataUtil.transformInt(rt_1000);
					}
				}

				if(rt_url_count ==0){
					continue;
				}

				CspAppRtCount apprtUrl = new CspAppRtCount();
				apprtUrl.setAppName(app);
				apprtUrl.setUrl(url.getAppUrl());
				apprtUrl.setCollectTime(date);
				apprtUrl.setPvRt100(rt_url_100_count);
				apprtUrl.setPvRt1000(rt_url_1000_count);
				apprtUrl.setPvRt500(rt_url_500_count);
				apprtUrl.setPvRtCount(pv_url_count);
				apprtUrl.setPvRtError(rt_url_count);
				apprtUrl.setPvRtType("url");
				AppInfoAo.get().addCspAppRtCount(apprtUrl);
			}
		}

		logger.info("RT分析程序结束->" + new Date() + ";耗时:" + (System.currentTimeMillis() - begin));
	}

	public void calculateNowByTime(Date date) {
		if(date == null)
			return;
		logger.info("开始执行RT分析程序->" + date);
		logger.info("删除" + new Date() + "的数据!");
		AppInfoAo.get().deleteAppRtCountByTime(date);

		long begin = System.currentTimeMillis();

		WebApplicationContext webAppContext = ContextLoader.getCurrentWebApplicationContext();
		commonService = (CommonServiceInterface)webAppContext.getBean("commonService");	

		List<String> applist = MonitorAppUtil.getMonitorApps();
		for(String app:applist) {
			List<TimeDataInfo> list = commonService.queryKeyDataHistory(app, KeyConstants.PV, PropConstants.E_TIMES, date);
			if(list.size()==0){
				continue;
			}

			long pv_count = 0;//这个是总pv
			long rt_error_count = 0;//大于1000ms的请求
			long rt_100_count = 0;//0-100ms的请求
			long rt_500_count = 0;//100-500ms的请求
			long rt_1000_count = 0;//500-1000ms的请求

			for(TimeDataInfo td:list) {
				Object pv = td.getOriginalPropertyMap().get(PropConstants.E_TIMES);
				pv_count+=DataUtil.transformInt(pv);
				Object rt_error = td.getOriginalPropertyMap().get("rt_error");
				rt_error_count+=DataUtil.transformInt(rt_error);

				Object rt_100 = td.getOriginalPropertyMap().get("rt_100");
				if(rt_100 != null){
					rt_100_count+=DataUtil.transformInt(rt_100);
				}
				Object rt_500 = td.getOriginalPropertyMap().get("rt_500");

				if(rt_500 != null){
					rt_500_count+=DataUtil.transformInt(rt_500);
				}

				Object rt_1000 = td.getOriginalPropertyMap().get("rt_1000");
				if(rt_1000 != null){
					rt_1000_count+=DataUtil.transformInt(rt_1000);
				}
			}

			CspAppRtCount apprt = new CspAppRtCount();
			apprt.setAppName(app);
			apprt.setCollectTime(date);
			apprt.setPvRt100(rt_100_count);
			apprt.setPvRt1000(rt_1000_count);
			apprt.setPvRt500(rt_500_count);
			apprt.setPvRtCount(pv_count);
			apprt.setPvRtError(rt_error_count);
			apprt.setPvRtType("app");

			AppInfoAo.get().addCspAppRtCount(apprt);
			List<AppUrlRelation> urlList = AppInfoAo.get().findAllAppUrlRelation(app);

			//统计这个应用下面的所有URL信息
			for(AppUrlRelation url:urlList) {
				long pv_url_count = 0;
				long rt_url_count = 0;
				long rt_url_100_count = 0;
				long rt_url_500_count = 0;
				long rt_url_1000_count = 0;

				long rt_max = 0;
				List<TimeDataInfo> urlDataList = commonService.queryKeyDataHistory(app, KeyConstants.PV + Constants.S_SEPERATOR + url.getAppUrl(), PropConstants.E_TIMES, date);

				for(TimeDataInfo td:urlDataList){
					Object pv = td.getOriginalPropertyMap().get(PropConstants.E_TIMES);
					pv_url_count+=DataUtil.transformInt(pv);
					Object rt_error = td.getOriginalPropertyMap().get("rt_error");
					int error = DataUtil.transformInt(rt_error);
					if(error >rt_max){
						rt_max = error;
					}

					rt_url_count+=error;

					Object rt_100 = td.getOriginalPropertyMap().get("rt_100");
					if(rt_100 != null){
						rt_url_100_count+=DataUtil.transformInt(rt_100);
					}
					Object rt_500 = td.getOriginalPropertyMap().get("rt_500");

					if(rt_500 != null){
						rt_url_500_count+=DataUtil.transformInt(rt_500);
					}

					Object rt_1000 = td.getOriginalPropertyMap().get("rt_1000");
					if(rt_1000 != null){
						rt_url_1000_count+=DataUtil.transformInt(rt_1000);
					}
				}

				if(rt_url_count ==0){
					continue;
				}

				CspAppRtCount apprtUrl = new CspAppRtCount();
				apprtUrl.setAppName(app);
				apprtUrl.setUrl(url.getAppUrl());
				apprtUrl.setCollectTime(date);
				apprtUrl.setPvRt100(rt_url_100_count);
				apprtUrl.setPvRt1000(rt_url_1000_count);
				apprtUrl.setPvRt500(rt_url_500_count);
				apprtUrl.setPvRtCount(pv_url_count);
				apprtUrl.setPvRtError(rt_url_count);
				apprtUrl.setPvRtType("url");
				AppInfoAo.get().addCspAppRtCount(apprtUrl);
			}
		}

		logger.info("RT分析程序结束->" + new Date() + ";耗时:" + (System.currentTimeMillis() - begin));

	}

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
		Date date = cal.getTime();
	}
}
