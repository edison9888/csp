
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
 * Fetch�Ľӿ�
 * @author youji.zj 2012-06-20
 *
 */
public interface IFetchTask {
	
	/***
	 * ��ʼfetch���� 
	 * @param down Ϊ����fetch������
	 */
	public void doTask(CountDownLatch down);
	
	/***
	 * ��ȡ����
	 * @throws IOException
	 */
	public void fetch()throws IOException ;
	
	/***
	 * �Ի�ȡ�������ݽ��з���
	 * @param line
	 */
	public void analyse(String line);
	
	/***
	 * �洢��������ȡ�Ľ��
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void putData(ResultKey key,Double value,Date time);
	
	/***
	 * �洢��������ȡ����ϸ��Ϣ,�߱�����value
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void putDetailData(ResultDetailKey key, double value1, double value2);
	
	/***
	 * �洢��������ȡ����ϸ��Ϣ,ֻ�߱�һ��value�����cpu��load, �����time�ֶ�
	 * @param key 
	 * @param value
	 * @param time
	 */
	public void putDetailData(ResultDetailKey key, double value1);
	
	/***
	 * ��ȡfetch�������
	 * @return
	 */
	public FetchResult getResult();
	
	/***
	 * ��ȡfetch�����������ϸ��Ϣ
	 * @return
	 */
	public Map<ResultDetailKey, ResultDetailCell> getResultDetail();
	
	/***
	 * ֹͣfetch
	 */
	public void stopFetch();	

	/***
	 * fetch�ļ���
	 * @param listen
	 */
	public void addFetchListen(FetchListen listen);
	
	/***
	 * fetch��������Ϣ
	 * @return
	 */
	public String getTaskName();
	
	/***
	 * �Ƿ��ڲɼ���
	 * @return
	 */
	public boolean isRun();

}
