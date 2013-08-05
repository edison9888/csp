package com.taobao.csp.depend.web.action;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taobao.csp.depend.alarm.AlarmCenter;
import com.taobao.csp.depend.ao.CspCheckUpDependAo;
import com.taobao.csp.depend.dao.CspDependentDao;
import com.taobao.csp.depend.job.BuildDependRelationJob;
import com.taobao.csp.depend.po.CheckupDependConfig;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.common.util.HsfServiceCodeMap;

@Controller
@RequestMapping("/manualtrigger.do")
public class ManualTriggerAction extends BaseAction{
	private static final Logger logger = Logger.getLogger(ManualTriggerAction.class);

	@Resource(name = "cspDependentDao")
	private CspDependentDao cspDependentDao;

	@RequestMapping(params = "method=bulidAppDepRelation")
	public void bulidAppDepRelation(HttpServletResponse response, String collectTime) {
		logger.info("把Eagleeye的数据从part表导入到day表");
		try{
			Date date = MethodUtil.getDate(collectTime);
			response.getWriter().write("把Eagleeye的数据从part表导入到day表,开始...,date=" + date);
			new BuildDependRelationJob().changePartToDayAuto(date);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("over");
			response.flushBuffer(); 
		}catch (Exception e) {
		}
	}  


	@RequestMapping(params = "method=callHostSync")
	public void callHostSync(HttpServletResponse response) {

		logger.info("调用immediatelySync");
		try{
			CspSyncOpsHostInfos.immediatelySync();
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("调用CspSyncOpsHostInfos.immediatelySync()成功");
			response.flushBuffer(); 
		}catch (Exception e) {
		}
	}

	/**
	 * 把库表provider和consumer中的数据处理掉。
	 * @param response
	 * @param selectDate
	 */
	@RequestMapping(params = "method=changeHsfKey")
	public void changeHsfKey(HttpServletResponse response, String selectDate) {
		String msg = "";
		long startOut = System.currentTimeMillis();
		try {
			Date date = MethodUtil.getDateThrowExceptions(selectDate);

			String[] appNames = new String[]{"classes","cucrm","cuxiao","dpc","dpm",
					"ecrm","itemcenter","itemtools","jtbas","lifemarketweb","marketing","marketingcenter",
					"pamirsmarketing","picturecenter","rxu","selleradmin","sellercenter","sellermanager",
					"sportalapps","tadget","tmallpromotion","tmallrefund","tmallsell","udc","wpc","xu"};
			for(String appName: appNames) {
				try {
					String[] typeArray = new String[] { "provide", "consume" };
					for (String type : typeArray) {
						long start = System.currentTimeMillis();
						Set<String> keySet = cspDependentDao.getDistinctHsfKeyByTime(
								date, type, appName);
						logger.info(appName + "查询distinct 耗时->" + (System.currentTimeMillis() - start));
						//老接口，新接口
						Map<String, String> mapNew = new HashMap<String, String>();
						for (String key : keySet) {
							String simpleKey = MethodUtil.simplifyHsfInterfaceName(key);
							//com.taobao.tc.service.TcTradeService:1.0.0_modifyWapDetailTradeFee
							String[] array = simpleKey.split("_");
							String oldInterface = array[0];
							String method = array[1];
							String[] arrayNew = HsfServiceCodeMap.get()
									.translateServiceMethod(oldInterface, method);
							if(arrayNew[1].equals(array[1]))
								continue;
							String interfaceAndVersion = simpleKey.substring(0,
									simpleKey.lastIndexOf('_'));
							String prefix = "IN_HSF-ProviderDetail_";
							mapNew.put(key, prefix + interfaceAndVersion + "_"
									+ arrayNew[1]);
						}
						for (Entry<String, String> entry : mapNew.entrySet()) {
							cspDependentDao.updateHsfKey(entry.getKey(),
									entry.getValue(), date, type, appName);
						}
						logger.info(type + "更新完毕耗时->" + (System.currentTimeMillis() - start));
					}
				} catch (Exception e) {
					logger.error("", e);
				}			
			}
			logger.info("HSF名称转化任务结束,耗时->" + (System.currentTimeMillis() - startOut));
			msg = "changeHsfKey 调用成功"; 
		} catch (Exception e) {
			msg = e.toString();
			logger.error("",e);
		}
		logger.info("方法总耗时->" + (System.currentTimeMillis() - startOut));
		try{
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(msg);
			response.flushBuffer(); 
		}catch (Exception e) {
		}
	}
	
	@RequestMapping(params = "method=sendAlarmMsg")
	public void sendAlarmMsg(HttpServletResponse response, String appName) {
		new AlarmCenter().startAlarmCheckMannual(appName);
		this.writeResponse("报警已经出发，请查收邮件和旺旺信息", response);
	}
	
	@RequestMapping(params = "method=combineDayDataTogether")
	public void combineDayDataTogether(HttpServletResponse response, String appName, String collect_time) {
		BuildDependRelationJob job = new BuildDependRelationJob();
		job.combineDayDataTogether(collect_time);
		job.changeKeyRateByOnlineRealData(collect_time);
		this.writeResponse("combineDayDataTogether,changeKeyRateByOnlineRealData已经结束", response);
	}
	
	@RequestMapping(params = "method=changeKeyRateByOnlineRealData")
	public void changeKeyRateByOnlineRealData(HttpServletResponse response, String appName, String collect_time) {
		new BuildDependRelationJob().combineDayDataTogether(collect_time);
		this.writeResponse("changeKeyRateByOnlineRealData已经结束", response);
	}
	
	@RequestMapping(params = "method=addCspCheckupDependConfig")
	public void addCspCheckupDependConfig(HttpServletResponse response, CheckupDependConfig config) {
		boolean flag = false;
		StringBuilder sb = new StringBuilder();
		try {
			if(config.getCollect_Time() == null) {
				config.setCollect_Time(new Date());
			}
			CspCheckUpDependAo.getInstance().deleteCheckupDependConfig(config);
			CspCheckUpDependAo.getInstance().addCheckupDependConfig(config);
			flag = true;
		} catch (Exception e) {
			logger.error("", e);
			sb.append(e.toString());
		}
		try {
			if(flag)
				this.writeJSONToResponse(response, "success");
			else
				this.writeJSONToResponse(response, sb.toString());
		} catch (IOException e) {
			logger.error("", e);
		}
	}
}
