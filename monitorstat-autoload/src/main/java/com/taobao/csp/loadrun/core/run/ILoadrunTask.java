
package com.taobao.csp.loadrun.core.run;

import com.taobao.csp.loadrun.core.FetchFeature;
import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;

/***
 * ѹ�������task�ӿ�
 * @author youji.zj
 * @2012-07-01
 */
public interface ILoadrunTask extends ILoadrun{
	
	/***
	 * ��ʼ��ѹ��task
	 * @param target
	 * @throws Exception
	 */
	public void init(LoadrunTarget target) throws Exception;
	
	/***
	 * ע��task�ļ���
	 * @param listen
	 */
	public void addLoadrunListen(LoadrunListen listen);
	
	/***
	 * ��¼��ֵ
	 * @param limitFeature
	 * @param target
	 */
	public void recordThreashold();
	
	/***
	 * ֹͣѹ�����
	 */
	public void stopTask();
	
	/***
	 * �ύ�ɼ�����
	 * @param fetch
	 * @return
	 */
	public FetchFeature submitFetch(IFetchTask fetch);
	
	/***
	 * �ȴ�ǡ����ֹͣʱ��
	 */
	public void waitForStop();
	
	/***
	 * ����fetch
	 */
	public void runFetchs();
	
	/***
	 * �ռ�����
	 */
	public void recordData();
	
	/***
	 * �ֶ�ѹ�����
	 * @param feature
	 * @throws Exception
	 */
	public void doLoadrun(String ... feature)throws Exception;
	
	/***
	 * �Զ�ѹ�����
	 */
	public void runAutoControl();	

}
