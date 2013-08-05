
package com.taobao.csp.loadrun.core.run;

import com.taobao.csp.loadrun.core.FetchFeature;
import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;

/***
 * 压测任务的task接口
 * @author youji.zj
 * @2012-07-01
 */
public interface ILoadrunTask extends ILoadrun{
	
	/***
	 * 初始化压测task
	 * @param target
	 * @throws Exception
	 */
	public void init(LoadrunTarget target) throws Exception;
	
	/***
	 * 注册task的监听
	 * @param listen
	 */
	public void addLoadrunListen(LoadrunListen listen);
	
	/***
	 * 记录阀值
	 * @param limitFeature
	 * @param target
	 */
	public void recordThreashold();
	
	/***
	 * 停止压测操作
	 */
	public void stopTask();
	
	/***
	 * 提交采集任务
	 * @param fetch
	 * @return
	 */
	public FetchFeature submitFetch(IFetchTask fetch);
	
	/***
	 * 等待恰当的停止时机
	 */
	public void waitForStop();
	
	/***
	 * 启动fetch
	 */
	public void runFetchs();
	
	/***
	 * 收集数据
	 */
	public void recordData();
	
	/***
	 * 手动压测入口
	 * @param feature
	 * @throws Exception
	 */
	public void doLoadrun(String ... feature)throws Exception;
	
	/***
	 * 自动压测入口
	 */
	public void runAutoControl();	

}
