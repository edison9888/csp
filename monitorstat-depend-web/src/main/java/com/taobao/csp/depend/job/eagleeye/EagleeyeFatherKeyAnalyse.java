package com.taobao.csp.depend.job.eagleeye;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;
import com.taobao.monitor.common.po.EagleeyeChildKeyListPo;

public class EagleeyeFatherKeyAnalyse extends EagleeyeAnalyse{
	private static EagleeyeFatherKeyAnalyse po = new EagleeyeFatherKeyAnalyse();
	
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
		
		EagleeyeChildKeyListPo dayPo = JSONObject.parseObject(jsonContent, EagleeyeChildKeyListPo.class);
		
		CspEagleeyeApiRequestPart part = partList.get(0);
		
		if(partList.size()>1) {
			for(int i=1; i<partList.size(); i++) {
				try {
					EagleeyeChildKeyListPo partApiPo = JSONObject.parseObject(
							partList.get(i).getResponseContent(),
							EagleeyeChildKeyListPo.class);
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
	}

	@Override
	public void addApiDayToEagleeyeAuto(CspEagleeyeApiRequestDay dayObj) {
	}
}
