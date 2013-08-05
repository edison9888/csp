
package com.taobao.csp.loadrun.core.listen;

import java.util.List;
import java.util.Map;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;

/**
 * 
 * @author xiaodu
 * @version 2011-6-23 ÏÂÎç04:43:25
 */
public interface LoadrunListen {
	
	public void start(String loadrunId,LoadrunTarget target);
	
	public void complete(String loadrunId,LoadrunTarget target);
	
	public void error(String loadrunId,LoadrunTarget target,Object e);
	
	public void reportLoad(String loadrunId,LoadrunTarget target,Map<ResultKey, Double> result);
	
	public void reportLoadDetail(String loadrunId,LoadrunTarget target, List<ResultDetailCell> result);
	
	public void reportLoadThreshold(String loadrunId, LoadrunTarget target);

}
