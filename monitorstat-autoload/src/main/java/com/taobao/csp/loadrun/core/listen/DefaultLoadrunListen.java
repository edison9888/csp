
package com.taobao.csp.loadrun.core.listen;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;

/**
 * 
 * @author xiaodu
 * @version 2011-6-24 上午10:40:56
 */
public class DefaultLoadrunListen implements LoadrunListen{
	
	
	private static final Logger logger = Logger.getLogger(DefaultLoadrunListen.class);
	

	public void complete(String loadrunId,LoadrunTarget target) {
		logger.info("完成"+target.getAppName()+":"+target.getLoadrunType());
		
	}

	public void error(String loadrunId,LoadrunTarget target, Object e) {
		logger.info("错误"+target.getAppName()+":"+target.getLoadrunType()+" 信息:"+e);
		
	}

	public void start(String loadrunId,LoadrunTarget target) {
		logger.info("开始"+target.getAppName()+":"+target.getLoadrunType());
		
	}

	public void reportLoad(String loadrunId,LoadrunTarget target,Map<ResultKey, Double> result) {
		
	}

	@Override
	public void reportLoadDetail(String loadrunId, LoadrunTarget target, List<ResultDetailCell> result) {
		
	}

	@Override
	public void reportLoadThreshold(String loadrunId, LoadrunTarget target) {
		// TODO Auto-generated method stub
		
	}

}
