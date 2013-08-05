package com.taobao.csp.depend.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.taobao.csp.depend.ao.CspMapKeyInfoAo;
import com.taobao.csp.depend.ao.HsfProviderAo;
import com.taobao.csp.depend.ao.SentinelAo;
import com.taobao.csp.depend.ao.UrlAo;
import com.taobao.csp.depend.job.eagleeye.EagleeyeAnalyse;
import com.taobao.csp.depend.job.eagleeye.EagleeyeApiFactory;
import com.taobao.csp.depend.util.MethodUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspAppDepAppPo;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;
import com.taobao.monitor.common.po.CspMapKeyInfoPo;
import com.taobao.monitor.common.po.EagleeyeChildKeyListPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.Utlitites;

public class BuildDependRelationJob {
	private static final Logger logger = Logger.getLogger("cronTaskLog");

	/**
	 * ��ʱ��API part������ݵ��뵽Day��
	 */
	public void changePartToDayAuto() {
		changePartToDayAuto(null);
	}

	public void changePartToDayAuto(Date date) {
		//��Ҫ����Ĺ���,������ڲ��Լ�����Ҫ��URL
		Calendar cal = Calendar.getInstance();
		if(date == null)
			cal.add(Calendar.DAY_OF_YEAR, -1);
		else 
			cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date collect_timeStart = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date collect_timeEnd = cal.getTime();

		
		logger.info("ɾ��csp_key_depend_info����App������");
		CspDependInfoAo.get().clearApp_CspKeyDependInfo();

		cal.add(Calendar.DAY_OF_YEAR, -14);	
		Date time15Pre = cal.getTime();

		logger.info("ɾ��csp_eagleeye_api_request_part����15��ǰ�����ݡ�ɾ�����ڣ�time15Pre=" + time15Pre.toString());
		EagleeyeDataAo.get().deleteEagleeyePartBeforeDate(time15Pre);

		//�������ϼ���APP��������ϵ
		logger.info("�Ѷ˿ڼ���App������ϵ���뵽->csp_key_depend_info");
		savePortCheckAppRelationToCspKeyDepend();

		//		String[] checkType = new String[]{Constants.API_CHILD_APP,Constants.API_FATHER_APP,
		//				Constants.API_CHILD_KEY,Constants.API_FATHER_KEY};
		String[] checkType = new String[]{Constants.API_CHILD_KEY, Constants.API_FATHER_KEY};

		for(String api_type: checkType) {
			//Ҳ���Ը���APP����
			Set<String> sourceKeySet = EagleeyeDataAo.get().getDistinctSourceKey(api_type, collect_timeStart, collect_timeEnd);
			logger.info("��ѯ���ݣ�collect_timeStart=" + collect_timeStart + ";collect_timeEnd=" + collect_timeEnd + ";sourceKeySet.size=" + sourceKeySet.size());
			for(String sourceKey : sourceKeySet) {
				changePartToDay(api_type,collect_timeStart,collect_timeEnd,sourceKey);
			}
		}

		String collect_time = MethodUtil.getStringOfDate(MethodUtil.getYestoday());

		//��Ӧ�þۺ���Ϣ
		combineDayDataTogether(collect_time);
	}

	private void changePartToDay(String api_type, Date collect_timeStart, Date collect_timeEnd, String sourcekey) {
		logger.info("�������:api_type=" + api_type + "\tcollect_timeStart=" + collect_timeStart + "\tcollect_timeEnd=" + collect_timeEnd + "\tsourceKey=" + sourcekey);
		final long start = System.currentTimeMillis();		
		List<CspEagleeyeApiRequestPart> partList = EagleeyeDataAo.get().searchEagleeyeApiRequestPart(sourcekey, api_type, collect_timeStart, collect_timeEnd);
		if(partList.size() == 0) {
			logger.error("api_type=" + api_type + "\tcollect_timeStart=" + collect_timeStart + "\tcollect_timeEnd=" + collect_timeEnd + "\tsourceKey=" + sourcekey + "\tû�����ݣ�");
			return;
		}

		try {
			EagleeyeAnalyse analyseBase = EagleeyeApiFactory.getEagleeyeAnalyseByType(api_type);
			CspEagleeyeApiRequestDay day = analyseBase.changePartToDay(partList.get(0).getResponseContent(), partList);

			if(day.getApiType().equals(Constants.API_CHILD_APP) || day.getApiType().equals(Constants.API_CHILD_KEY)) {
				boolean flag = EagleeyeDataAo.get().addCspEagleeyeApiRequestDay(day);
				if(flag) {
					/*csp_calls_relationship*/
					/* �ϵ���CallRelationship���ű�û�о�����ô�*/
					//EagleeyeDataAo.get().deleteCallsRelationship(day.getSourcekey());
					//analyseBase.addApiDayToTopo(day);	//����������ϵ¼�뵽Eagleeye���Զ�����

					/*csp_time_app_depend_info*/
					analyseBase.addApiDayToEagleeyeAuto(day);
				}
				logger.info("������:flag=" + flag);				
			} else if(day.getApiType().equals(Constants.API_FATHER_APP) || day.getApiType().equals(Constants.API_FATHER_KEY)) {
				boolean flag = EagleeyeDataAo.get().addCspEagleeyeApiRequestDay(day);
				logger.info("������:flag=" + flag);				
			} else {
				logger.info("���������͵�Json���ݣ������뵽csp_calls_relationship��csp_time_app_depend_info");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		logger.info("�������,��ʱ->" + (System.currentTimeMillis() - start));
	}

	private void savePortCheckAppRelationToCspKeyDepend() {
		try {
			String yesterday = Utlitites.getDateBefore(new Date(), 1, null);
			List<CspAppDepAppPo> list = CspDependInfoAo.get().getAppDepAppList(yesterday);

			long ctime = System.currentTimeMillis();
			logger.info("��ʼ����csp_app_depend_app�����ݣ�����ʱ��:" + yesterday);
			logger.info("�˴δ����Ӧ�ü�¼����Ϊ��" + list.size());

			for (CspAppDepAppPo po : list) {
				if (po.getDepAppType() == null) {
					po.setDepAppType("δ֪");
				}

				if (po.getDepOpsName().equals(po.getOpsName()) || po.getOpsName().equals("δ֪") || po.getDepOpsName().equals("δ֪")) {
					continue;
				}
				CspDependInfoAo.get().insertAppRelToDependInfo(po);
			}

			long ctime2 = System.currentTimeMillis();
			logger.info("����Ӧ�����ݽ�������ʱ" + (ctime2 - ctime));

		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/*
	 * ���ձ���ͬһӦ�õ����ݺϲ�,ͬʱ��Ӧ��key�����ݲ��뵽csp_map_key_info��
	 */
	public void combineDayDataTogether(String collect_time) {
		long start = System.currentTimeMillis();
		logger.info("��ʼ�ϲ�api day����Ϣ��������Ϣ");
		List<String> appList = CallEagleeyeApiJob.getApplist();
		String[] checkType = new String[]{Constants.API_CHILD_KEY, Constants.API_FATHER_KEY};
		for(String api_type: checkType) {
			for(String appName : appList) {
				final EagleeyeChildKeyListPo eagleTotalPo = new EagleeyeChildKeyListPo();
				eagleTotalPo.setAppName(appName);
				eagleTotalPo.setKeyName(Constants.DEFAULT_URL);

				List<CspEagleeyeApiRequestDay> list = EagleeyeDataAo.get().searchCspEagleeyeApiRequestDayList(api_type, appName, collect_time);
				if(list.size() == 0)
					continue;

				for(CspEagleeyeApiRequestDay targetDayPo : list) {
					try {
						EagleeyeChildKeyListPo targetEagleeyeDayPo = JSONObject
								.parseObject(targetDayPo.getResponseContent(),
										EagleeyeChildKeyListPo.class);
						addDayToTotal(eagleTotalPo, targetEagleeyeDayPo);
					} catch (Exception e) {
						logger.error("", e);
					}					
				}

				final CspEagleeyeApiRequestDay totalDayPo = new CspEagleeyeApiRequestDay();
				totalDayPo.setApiType(api_type);
				totalDayPo.setAppName(appName);
				totalDayPo.setCollectTime(MethodUtil.getDate(collect_time));
				totalDayPo.setSourcekey(Constants.DEFAULT_URL);
				totalDayPo.setVersion("1.0");
				totalDayPo.setResponseContent(JSONObject.toJSONString(eagleTotalPo));
				boolean result = EagleeyeDataAo.get().addCspEagleeyeApiRequestDay(totalDayPo);
				if(result == true) {
					long total = eagleTotalPo.getTotalCallNum();
					List<EagleeyeChildKeyListPo> childList = eagleTotalPo.getTopo();
					if(childList.size() > 0) {
						CspMapKeyInfoAo.get().deleteAutoKeyByAppName(appName);
						logger.info("ɾ��appName=" + appName + "���Զ�key��Ϣ1������" + childList.size() + "�����ݡ�");
					}
					for(EagleeyeChildKeyListPo child: childList) {
						double rate = getRate(child.getTotalCallNum(), total);
						int level = getKeyLevelByNumber(rate);
						CspMapKeyInfoPo info = new CspMapKeyInfoPo();
						info.setAppname(child.getAppName());
						info.setControlType(Constants.CSP_DEPEND_MAP_NO_CONTROL);
						info.setIsBlack(Constants.CSP_DEPEND_MAP_NOT_BLACK);
						info.setKeyLevel(level);
						info.setKeyname(child.getKeyName());
						info.setKeyStatus(Constants.CSP_DEPEND_MAP_STATUS_AUTO);
						info.setUpdateBy(Constants.CSP_DEPEND_MAP_DEFAULT_CREATOR);
						info.setUpdateTime(new Date());
						info.setRate(rate);
						CspMapKeyInfoAo.get().addCspMapKeyInfoPo(info);
					}
				}
				logger.info("result=" + result);
			}
		}
		logger.info("�����ϲ�api day����Ϣ,��ʱ->" + (System.currentTimeMillis()-start));
	}

	/*
	 * ������ʵ���������趨key�ļ���
	 */
	public void changeKeyRateByOnlineRealData(String collect_time) {
		List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

		final Set<String> sentinelSet = SentinelAo.get().getKeyListFromSentinel();

		for(AppInfoPo appInfo : appList) {
			String appName = appInfo.getOpsName();
			List<CspMapKeyInfoPo> existList = CspMapKeyInfoAo.get().getNormalKeyList(appName);
			List<CspMapKeyInfoPo> updateList = new ArrayList<CspMapKeyInfoPo>();
			Map<String, Long> keyMap = null;
			if("pv".equals(appInfo.getAppType())) {
				keyMap = UrlAo.get().getRequestUrlCall(appName, collect_time);
			} else if("center".equals(appInfo.getAppType())) {
				keyMap = HsfProviderAo.get().getHsfProvideMethods(appName, collect_time);				
			} else {
				logger.error(appName + "---appType����ȷ��type=" + appInfo.getAppType());
				continue;
			}
			long total = 0;	//�ܵ�����
			for(Long num : keyMap.values()) {
				total += num;
			}
			Iterator<CspMapKeyInfoPo> iter = existList.iterator();
			while(iter.hasNext()) {
				CspMapKeyInfoPo po = iter.next();
				String keyName = po.getKeyname();

				if(keyMap.containsKey(keyName)) {
					final long value = keyMap.get(keyName);
					final double rate = getRate(value, total);
					po.setRate(rate);
					if(sentinelSet.contains(keyName)) {
						po.setKeyLevel(0);
					} else {
						po.setKeyLevel(getKeyLevelByNumber(rate));						
					}
					po.setUpdateTime(new Date());
					updateList.add(po);
					keyMap.remove(keyName);
					iter.remove();
				}

				if(po.getKeyStatus() == Constants.CSP_DEPEND_MAP_STATUS_CONFIG) {
					iter.remove();
				}
			}

			StringBuilder sb = new StringBuilder();
			//delete
			Set<Long> set = new HashSet<Long>();
			for(CspMapKeyInfoPo po : existList) {
				set.add(po.getId());
				sb.append(po.getKeyname()).append(",");
			}

			//FIXME ����Eagleeye���У�ʵ����־û�е����ݣ���ʱ�Ȳ�ɾ��
			//CspMapKeyInfoAo.get().deleteKeyByIds(set);

			//update
			for(CspMapKeyInfoPo po : updateList) {
				CspMapKeyInfoAo.get().updateCspMapKeyInfoPo(po);				
			}

			//insert
			for(Entry<String, Long> entry :keyMap.entrySet()) {
				final long value = keyMap.get(entry.getKey());
				final double rate = getRate(value, total);
				CspMapKeyInfoPo info = new CspMapKeyInfoPo();
				info.setAppname(appName);
				info.setControlType(Constants.CSP_DEPEND_MAP_NO_CONTROL);
				info.setIsBlack(Constants.CSP_DEPEND_MAP_NOT_BLACK);
				if(sentinelSet.contains(entry.getKey())) {
					info.setKeyLevel(0);
				} else {
					info.setKeyLevel(getKeyLevelByNumber(rate));						
				}
				info.setKeyname(entry.getKey());
				info.setKeyStatus(Constants.CSP_DEPEND_MAP_STATUS_AUTO);
				info.setUpdateBy(Constants.CSP_DEPEND_MAP_DEFAULT_CREATOR);
				info.setUpdateTime(new Date());
				info.setRate(rate);
				CspMapKeyInfoAo.get().addCspMapKeyInfoPo(info);
			}
		}
		System.out.println("over");
	}

	private void addDayToTotal(EagleeyeChildKeyListPo totalDayPo, EagleeyeChildKeyListPo targetDayPo) throws Exception {
		totalDayPo.setTotalCallNum(totalDayPo.getTotalCallNum() + targetDayPo.getTotalCallNum());
		totalDayPo.setRt(totalDayPo.getRt() + targetDayPo.getRt());
		totalDayPo.setFailCallNum(totalDayPo.getFailCallNum() + targetDayPo.getFailCallNum());
		totalDayPo.setFaliRt(totalDayPo.getFaliRt() + targetDayPo.getFaliRt());

		if(isSameLevelNode(totalDayPo,targetDayPo)) {
			for(EagleeyeChildKeyListPo targetChildKeyPo : targetDayPo.getTopo()) {
				addDayToTotal(totalDayPo, targetChildKeyPo);	//�����ӽڵ�
			}
		} else {
			boolean isSameLevel = false;
			for(EagleeyeChildKeyListPo childKeyPo : totalDayPo.getTopo()) {
				if(isSameLevelNode(childKeyPo, targetDayPo)) {
					addDayToTotal(childKeyPo, targetDayPo);
					isSameLevel = true;
					break;
				}
			}
			if(!isSameLevel) {
				totalDayPo.getTopo().add(targetDayPo);
			}
		}
	}

	private boolean isSameLevelNode(EagleeyeChildKeyListPo p1, EagleeyeChildKeyListPo p2) throws Exception {
		if(p1 == null || p2 == null)
			throw new Exception("some node is null");
		if(p1.getAppName().equals(p2.getAppName()) 
				&& p1.getKeyName().equals(p2.getKeyName())) 
			return true;
		return false;
	}

	public int getKeyLevelByNumber(double rate) {
		if(rate >= 0.15)
			return 0;
		else if(rate >= 0.1 && rate < 0.15)
			return 1;
		else if(rate >= 0.05 && rate <= 0.1)
			return 2;
		else //if(rate >= 0.01 && rate < 0.05)
			return 3;		
	}

	public double getRate(long num1, long num2) {
		return Arith.div(num1, num2, 4);	
	}

	public static void main(String[] args) {
		BuildDependRelationJob job = new BuildDependRelationJob();
		Date date = MethodUtil.getYestoday();
		
		Calendar cal = Calendar.getInstance();
		if(date == null)
			cal.add(Calendar.DAY_OF_YEAR, -1);
		else 
			cal.setTime(date);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date collect_timeStart = cal.getTime();

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date collect_timeEnd = cal.getTime();
		String[] checkType = new String[]{Constants.API_CHILD_KEY};//, Constants.API_FATHER_KEY

		for(String api_type: checkType) {
			//Ҳ���Ը���APP����
			Set<String> sourceKeySet = EagleeyeDataAo.get().getDistinctSourceKey(api_type, collect_timeStart, collect_timeEnd);
			logger.info("��ѯ���ݣ�collect_timeStart=" + collect_timeStart + ";collect_timeEnd=" + collect_timeEnd + ";sourceKeySet.size=" + sourceKeySet.size());
			for(String sourceKey : sourceKeySet) {
				job.changePartToDay(api_type,collect_timeStart,collect_timeEnd,sourceKey);
			}
		}		

		//		new BuildDependRelationJob().changeKeyRateByOnlineRealData("2012-12-24");
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DAY_OF_YEAR, -1);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		Date collect_timeStart = cal.getTime();
//
//		cal.set(Calendar.HOUR_OF_DAY, 23);
//		cal.set(Calendar.MINUTE, 59);
//		cal.set(Calendar.SECOND, 59);
//		Date collect_timeEnd = cal.getTime();
//
//		cal.add(Calendar.DAY_OF_YEAR, -14);	
//		Date time15Pre = cal.getTime();
//
//		System.out.println(collect_timeStart);
//		System.out.println(collect_timeEnd);
//		System.out.println(time15Pre);
	}

}
