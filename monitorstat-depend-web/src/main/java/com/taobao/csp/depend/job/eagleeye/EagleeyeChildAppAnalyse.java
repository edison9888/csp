package com.taobao.csp.depend.job.eagleeye;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
import com.taobao.monitor.common.po.CspAppDepAppPo;
import com.taobao.monitor.common.po.CspCallsRelationship;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;
import com.taobao.monitor.common.po.EagleeyeChildAppListPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;

public class EagleeyeChildAppAnalyse extends EagleeyeAnalyse{
	private static EagleeyeChildAppAnalyse po = new EagleeyeChildAppAnalyse();
	
	public static EagleeyeAnalyse get() {
		return po;
	}
	
	@Override
	public CspEagleeyeApiRequestDay changePartToDay(String jsonContent,
			List<CspEagleeyeApiRequestPart> partList) {
//		if(partList.size() == 0 || partList == null) {
//			logger.error("partList= null or partList.size() = 0");
//			return null;
//		}
		
		EagleeyeChildAppListPo dayPo = JSONObject.parseObject(jsonContent, EagleeyeChildAppListPo.class);
		
		CspEagleeyeApiRequestPart part = partList.get(0);
		
		if(partList.size()>1) {
			for(int i=1; i<partList.size(); i++) {
				try {
					EagleeyeChildAppListPo partApiPo = JSONObject.parseObject(
							partList.get(i).getResponseContent(),
							EagleeyeChildAppListPo.class);
					addPartToDayPo(dayPo, partApiPo);
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		
		CspEagleeyeApiRequestDay day = new CspEagleeyeApiRequestDay();
		day.setAppName(part.getAppName());
		day.setApiType(part.getApiType());
		day.setCollectTime(part.getCollectTime());
		day.setSourcekey(part.getSourcekey());
		day.setVersion(part.getVersion());
		day.setResponseContent(JSONObject.toJSONString(dayPo));
		
		return day;
	}
	
	@Override
	protected void addPartToDayPo(Object dayObj, Object partPoObj) throws Exception {
		  
		  if(!(dayObj instanceof EagleeyeChildAppListPo) || !(partPoObj instanceof EagleeyeChildAppListPo)) {
			  throw new Exception("参数类型不符合预期");
		  }
		  
		  EagleeyeChildAppListPo day = (EagleeyeChildAppListPo)dayObj;
		  EagleeyeChildAppListPo partPo = (EagleeyeChildAppListPo)partPoObj;
		  
		  if(day == null || partPo == null || !(day.getAppName().equals(partPo.getAppName())))
			  return;
		  
		  day.setFaliRt(partPo.getFaliRt() + day.getFaliRt());
		  day.setFailCallNum(partPo.getFailCallNum() + day.getFailCallNum());
		  day.setSuccessCallNum(partPo.getSuccessCallNum() + day.getSuccessCallNum());
		  day.setSuccessRt(partPo.getSuccessRt() + day.getSuccessRt());
		  
		  List<EagleeyeChildAppListPo> dayNextStepList = day.getChildList();
		  List<EagleeyeChildAppListPo> partNextStepList = partPo.getChildList();
		  
		  for(EagleeyeChildAppListPo tmpPartPo : partNextStepList) {
			  boolean isPartInDay = false;
			  for(EagleeyeChildAppListPo tmpDayPo: dayNextStepList) {
				  if(tmpPartPo.getAppName().equals(tmpDayPo.getAppName())) {
					  isPartInDay = true;
					  addPartToDayPo(tmpDayPo, tmpPartPo);
					  break;
				  }
			  }
			  if(!isPartInDay) {
				  dayNextStepList.add(tmpPartPo);
			  }
		  }
		
	}

	@Override
	public void addApiDayToTopo(CspEagleeyeApiRequestDay dayObj) {
		EagleeyeChildAppListPo dayApiPo = JSONObject.parseObject(
				dayObj.getResponseContent(),EagleeyeChildAppListPo.class);
		addNextStepRelation(dayApiPo, dayApiPo);
	}
	
	public void addNextStepRelation(Object dayApiPoObj, Object dayApiPoObjRoot) {
		if(dayApiPoObj instanceof EagleeyeChildAppListPo && dayApiPoObjRoot instanceof EagleeyeChildAppListPo ) {
			EagleeyeChildAppListPo dayApiPo = (EagleeyeChildAppListPo)dayApiPoObj;
			if(dayApiPoObj.equals(dayApiPoObjRoot)) {	
				//插入topo的根节点
				CspCallsRelationship relation = new CspCallsRelationship();
				relation.setSourceUrl(dayApiPo.getAppName());
				relation.setSourceApp(dayApiPo.getAppName());
				relation.setOrigin("browser");
				relation.setOriginApp("sourcestart");
				relation.setRate(1);
				relation.setTarget(dayApiPo.getAppName());
				relation.setTargetApp(dayApiPo.getAppName());
				EagleeyeDataAo.get().addCallsRelationship(relation);
			}
			EagleeyeChildAppListPo dayApiPoRoot = (EagleeyeChildAppListPo)dayApiPoObjRoot;
			List<EagleeyeChildAppListPo> list = dayApiPo.getChildList();
			for(EagleeyeChildAppListPo po : list) {
				addNextStepRelation(po, dayApiPoObjRoot);	//先插入子节点
				CspCallsRelationship relation = new CspCallsRelationship();
				relation.setSourceUrl(dayApiPo.getAppName());
				relation.setSourceApp(dayApiPo.getAppName());
				relation.setOrigin(dayApiPo.getAppName());
				relation.setOriginApp(dayApiPo.getAppName());
				relation.setRate((float)Arith.div(po.getSuccessCallNum(), dayApiPoRoot.getSuccessCallNum(), 4));
				relation.setTarget(po.getAppName());
				relation.setTargetApp(po.getAppName());
				EagleeyeDataAo.get().addCallsRelationship(relation);
			}
		} else {
			logger.error("dayApiPoObj类型不对，dayApiPoObj.getClass()=" + dayApiPoObj.getClass());
		}
	}

	@Override
	public void addApiDayToEagleeyeAuto(CspEagleeyeApiRequestDay dayObj) {
		if(!dayObj.getApiType().equals(Constants.API_CHILD_APP)) {
			return;
		}
		
		EagleeyeChildAppListPo jsonPo = JSONObject.parseObject(
				dayObj.getResponseContent(),EagleeyeChildAppListPo.class);
		addApiDayToEagleeyeAuto(jsonPo);
	}
	
	private void addApiDayToEagleeyeAuto(EagleeyeChildAppListPo jsonPo) {
		for(EagleeyeChildAppListPo po : jsonPo.getChildList()) {
			CspAppDepAppPo appDepApp = new CspAppDepAppPo();
			appDepApp.setOpsName(jsonPo.getAppName());
			appDepApp.setDepOpsName(po.getAppName());
			appDepApp.setCollectTime(jsonPo.getTime());
			appDepApp.setDepAppType("");
			CspDependInfoAo.get().insertAppRelToDependInfo(appDepApp);
		}
	}
}
