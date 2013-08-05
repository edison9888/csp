
package com.taobao.csp.loadrun.core.fetch;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.listen.FetchListen;
import com.taobao.csp.loadrun.core.result.FetchResult;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/***
 * Fetch的接口
 * @author youji.zj 2012-06-20
 *
 */
public interface IFetchTask {
	
	/***
	 * 开始fetch任务 
	 * @param down 为所有fetch所共有
	 */
	public void doTask(CountDownLatch down);
	
	/***
	 * 获取数据
	 * @throws IOException
	 */
	public void fetch()throws IOException ;
	
	/***
	 * 对获取到的数据进行分析
	 * @param line
	 */
	public void analyse(String line);
	
	/***
	 * 存储分析所提取的结果
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void putData(ResultKey key,Double value,Date time);
	
	/***
	 * 存储分析所提取的详细信息,具备两个value
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void putDetailData(ResultDetailKey key, double value1, double value2);
	
	/***
	 * 存储分析所提取的详细信息,只具备一个value，针对cpu和load, 存放在time字段
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void putDetailData(ResultDetailKey key, double value1);
	
	/***
	 * 获取fetch分析结果
	 * @return
	 */
	public FetchResult getResult();
	
	/***
	 * 获取fetch分析结果的详细信息
	 * @return
	 */
	public Map<ResultDetailKey, ResultDetailCell> getResultDetail();
	
	/***
	 * 停止fetch
	 */
	public void stopFetch();	

	/***
	 * fetch的监听
	 * @param listen
	 */
	public void addFetchListen(FetchListen listen);
	
	/***
	 * fetch的描述信息
	 * @return
	 */
	public String getTaskName();
	
	/***
	 * 是否在采集中
	 * @return
	 */
	public boolean isRun();

}
