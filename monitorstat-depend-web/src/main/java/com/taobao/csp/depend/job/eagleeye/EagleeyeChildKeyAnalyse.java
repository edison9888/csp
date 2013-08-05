package com.taobao.csp.depend.job.eagleeye;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.EagleeyeDataAo;
import com.taobao.monitor.common.po.CspCallsRelationship;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;
import com.taobao.monitor.common.po.EagleeyeChildKeyListPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;

public class EagleeyeChildKeyAnalyse extends EagleeyeAnalyse{
	private static EagleeyeChildKeyAnalyse po = new EagleeyeChildKeyAnalyse();
	
	public static EagleeyeAnalyse get() {
		return po;
	}
	
	private static Set<String> keySet = new HashSet<String>();
	
	@Override
	public CspEagleeyeApiRequestDay changePartToDay(String jsonContent,
			List<CspEagleeyeApiRequestPart> partList) {
//		if(partList.size() == 0 || partList == null) {
//			logger.error("partList= null or partList.size() = 0");
//			return null;
//		}
		
		EagleeyeChildKeyListPo dayPo = JSONObject.parseObject(jsonContent, EagleeyeChildKeyListPo.class);
		
		CspEagleeyeApiRequestPart part = partList.get(0);
		System.out.println(part.getResponseContent());
		if(partList.size()>1) {
			for(int i=1; i<partList.size(); i++) {
				try {
					EagleeyeChildKeyListPo partApiPo = JSONObject.parseObject(
							partList.get(i).getResponseContent(),
							EagleeyeChildKeyListPo.class);
					System.out.println("i=" + i + "\t" + partList.get(i).getResponseContent());
					addPartToDayPo(dayPo, partApiPo);
				} catch (Exception e) {
					logger.error("", e);
					e.printStackTrace();
				}
			}
		}
		System.out.println("over");
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
		  
		  if(!(dayObj instanceof EagleeyeChildKeyListPo) || !(partPoObj instanceof EagleeyeChildKeyListPo)) {
			  throw new Exception("参数类型不符合预期");
		  }
		  
		  EagleeyeChildKeyListPo day = (EagleeyeChildKeyListPo)dayObj;
		  EagleeyeChildKeyListPo partPo = (EagleeyeChildKeyListPo)partPoObj;
		  
		  if(day == null || partPo == null || !(day.getKeyName().equals(partPo.getKeyName())))
			  return;
		  
		  day.setFaliRt(partPo.getFaliRt() + day.getFaliRt());
		  day.setFailCallNum(partPo.getFailCallNum() + day.getFailCallNum());
		  day.setTotalCallNum(partPo.getTotalCallNum() + day.getTotalCallNum());
		  day.setRt(partPo.getRt() + day.getRt());
		  
		  List<EagleeyeChildKeyListPo> dayNextStepList = day.getTopo();
		  List<EagleeyeChildKeyListPo> partNextStepList = partPo.getTopo();
		  
		  for(EagleeyeChildKeyListPo tmpPartPo : partNextStepList) {
			  boolean isPartInDay = false;
			  for(EagleeyeChildKeyListPo tmpDayPo: dayNextStepList) {
				  if(tmpPartPo.getKeyName().equals(tmpDayPo.getKeyName())) {
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
		EagleeyeChildKeyListPo dayApiPo = JSONObject.parseObject(
				dayObj.getResponseContent(),EagleeyeChildKeyListPo.class);
		addNextStepRelation(dayApiPo, dayApiPo);
	}
	
	public void addNextStepRelation(Object dayApiPoObj, Object dayApiPoObjRoot) {
		if(dayApiPoObj instanceof EagleeyeChildKeyListPo && dayApiPoObjRoot instanceof EagleeyeChildKeyListPo) {
			EagleeyeChildKeyListPo dayApiPo = (EagleeyeChildKeyListPo)dayApiPoObj;
			if(dayApiPoObj.equals(dayApiPoObjRoot)) {	
				//插入topo的根节点
				CspCallsRelationship relation = new CspCallsRelationship();
				relation.setSourceUrl(dayApiPo.getKeyName());
				relation.setSourceApp(dayApiPo.getAppName());
				relation.setOrigin("browser");
				relation.setOriginApp("sourcestart");
				relation.setRate(1);
				relation.setTarget(dayApiPo.getKeyName());
				relation.setTargetApp(dayApiPo.getAppName());
				EagleeyeDataAo.get().addCallsRelationship(relation);
			}
			EagleeyeChildKeyListPo dayApiPoRoot = (EagleeyeChildKeyListPo)dayApiPoObjRoot;
			
			List<EagleeyeChildKeyListPo> list = dayApiPo.getTopo();
			for(EagleeyeChildKeyListPo po : list) {
				addNextStepRelation(po, dayApiPoObjRoot);	//先插入子节点
				CspCallsRelationship relation = new CspCallsRelationship();
				relation.setSourceUrl(dayApiPoRoot.getKeyName());
				relation.setSourceApp(dayApiPoRoot.getAppName());
				relation.setOrigin(dayApiPo.getKeyName());
				relation.setOriginApp(dayApiPo.getAppName());
				relation.setRate((float)Arith.div(po.getTotalCallNum(), dayApiPoRoot.getTotalCallNum(), 4));
				relation.setTarget(po.getKeyName());
				relation.setTargetApp(po.getAppName());
				EagleeyeDataAo.get().addCallsRelationship(relation);
			}
		} else {
			logger.error("dayApiPoObj类型不对，dayApiPoObj.getClass()=" + dayApiPoObj.getClass());
		}
	}

	@Override
	public void addApiDayToEagleeyeAuto(CspEagleeyeApiRequestDay dayObj) {
		if(!dayObj.getApiType().equals(Constants.API_CHILD_KEY)) {
			return;
		}
		EagleeyeChildKeyListPo jsonPo = JSONObject.parseObject(
				dayObj.getResponseContent(),EagleeyeChildKeyListPo.class);
		
		addApiDayToEagleeyeAuto(jsonPo, dayObj.getSourcekey());
	}
	
	private void addApiDayToEagleeyeAuto(EagleeyeChildKeyListPo jsonPo, final String sourceUrl) {
		for(EagleeyeChildKeyListPo po : jsonPo.getTopo()) {
			List<EagleeyeChildKeyListPo> topoList = po.getTopo();
			for(EagleeyeChildKeyListPo topoPo : topoList) {
				addApiDayToEagleeyeAuto(topoPo, sourceUrl);
			}
			String key = combinueString(po.getKeyName(), po.getAppName(), jsonPo.getKeyName(), jsonPo.getAppName(), jsonPo.getKeyName(), sourceUrl);
			if(keySet.contains(key))
				continue;
			else {
				if(keySet.size() > 50000)
					keySet.clear();
				keySet.add(key);				
			}
			CspDependInfoAo.get().insertKeyRelToDependInfo(po.getKeyName(), po.getAppName(), jsonPo.getKeyName(), jsonPo.getAppName(), sourceUrl);
		}
	}
	
	public String combinueString(String ...args) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<args.length;i++){
			sb.append(args[i]);
		}
		return sb.toString();
	}
}
