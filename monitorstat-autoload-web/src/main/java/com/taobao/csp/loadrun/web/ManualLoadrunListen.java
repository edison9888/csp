
package com.taobao.csp.loadrun.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.LoadrunResultDetail;
import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;

/**
 * 
 * @author xiaodu
 * @version 2011-7-12 下午04:44:51
 */
public class ManualLoadrunListen implements LoadrunListen{
	
	
	private static final Logger logger = Logger.getLogger(ManualLoadrunListen.class);
	
	private CspLoadRunBo cspLoadRunBo;
	

	public CspLoadRunBo getCspLoadRunBo() {
		return cspLoadRunBo;
	}

	public void setCspLoadRunBo(CspLoadRunBo cspLoadRunBo) {
		this.cspLoadRunBo = cspLoadRunBo;
	}

	@Override
	public void complete(String loadrunId,LoadrunTarget target) {
		logger.info("完成"+target.getAppName()+":"+target.getLoadrunType());
		
		String wws = target.getWangwangs();
		if(wws == null){
			MessageSendFactory.create(MessageSendType.WangWang).send("小赌", "CSP-自动压测", "完成"+target.getAppName()+":"+target.getLoadrunType());
		}else{
			String[] ww = wws.split(";");
			for(String w:ww){
				if(StringUtils.isNotBlank(w))
					MessageSendFactory.create(MessageSendType.WangWang).send(w, "CSP-自动压测", "完成"+target.getAppName()+":"+target.getLoadrunType());
			}
		}
		
		
		
	}

	@Override
	public void error(String loadrunId,LoadrunTarget target, Object e) {
		logger.info("错误"+target.getAppName()+":"+target.getLoadrunType());
		
		String wws = target.getWangwangs();
		if(wws == null){
			MessageSendFactory.create(MessageSendType.WangWang).send("小赌", "CSP-自动压测", "错误 "+target.getAppName()+":"+target.getLoadrunType()+" 异常:"+e);
		}else{
			String[] ww = wws.split(";");
			for(String w:ww){
				if(StringUtils.isNotBlank(w))
					MessageSendFactory.create(MessageSendType.WangWang).send(w, "CSP-自动压测", "错误 "+target.getAppName()+":"+target.getLoadrunType()+" 异常:"+e);
			}
		}
		
	}

	@Override
	public void reportLoad(String loadrunId,LoadrunTarget target, Map<ResultKey, Double> result) {
		for(Map.Entry<ResultKey, Double> entry:result.entrySet()){
			LoadrunResult r = new LoadrunResult();
			r.setAppId(target.getAppId());
			r.setLoadrunType(target.getLoadrunType());
			r.setControlFeature(target.getCurControlFeature());
			r.setKey(entry.getKey());
			r.setTargetIp(target.getTargetIp());
			r.setValue(entry.getValue());
			r.setLoadrunOrder(target.getLoadrunOrder());
			r.setLoadId(loadrunId);
			cspLoadRunBo.addLoadRunResult(r);
		}
	}
	
	@Override
	public void reportLoadDetail(String loadrunId, LoadrunTarget target, List<ResultDetailCell> result) {
		for (ResultDetailCell cell : result) {
			if (cell.getsKey() == null || cell.getsKey().length() > 250) continue;
			LoadrunResultDetail detail = new LoadrunResultDetail();
			detail.setLoadId(loadrunId);
			detail.setLoadrunOrder(target.getLoadrunOrder());
			detail.setMainKey(cell.getmKey());
			detail.setSecendaryKey(cell.getsKey());
			detail.setCount(cell.getCount());
			detail.setTimes(cell.getTime());
			
			Date date = new Date(cell.getCollectTime());
			detail.setCollectTime(date);
			
			cspLoadRunBo.addLoadRunResultDetail(detail);
		}
	}
	
	@Override
	public void reportLoadThreshold(String loadrunId, LoadrunTarget target) {
		cspLoadRunBo.addLoadRunThreshold(loadrunId, target.getTargetIp() + " " + target.getLimitFeature());	
	}

	@Override
	public void start(String loadrunId,LoadrunTarget target) {
		logger.info("开始"+target.getAppName()+":"+target.getLoadrunType());
		
		String wws = target.getWangwangs();
		if(wws == null){
			MessageSendFactory.create(MessageSendType.WangWang).send("小赌", "CSP-自动压测", "开始 "+target.getAppName()+":"+target.getLoadrunType());
		}else{
			String[] ww = wws.split(";");
			for(String w:ww){
				if(StringUtils.isNotBlank(w))
					MessageSendFactory.create(MessageSendType.WangWang).send(w, "CSP-自动压测", "开始 "+target.getAppName()+":"+target.getLoadrunType());
			}
		}
	}
}
