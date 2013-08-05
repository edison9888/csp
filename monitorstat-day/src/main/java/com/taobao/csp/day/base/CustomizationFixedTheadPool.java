package com.taobao.csp.day.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/***
 * �����������˹��ܵ��̳߳�(��LogDataFetcherTask���й���)
 * ����task��ʱ���ȶ�task.filterIdentity()
 * ��num�ֶν���ȡģ
 * �������������mod���������ύ��task
 * �����ύtask
 * 
 * @author youji.zj
 * @version 1.0
 */
public class CustomizationFixedTheadPool extends ThreadPoolExecutor {
	
	public static Logger logger = Logger.getLogger(CustomizationFixedTheadPool.class);
	
	private List<FetcherListener> listeners = new ArrayList<FetcherListener>();
	
	/*** ���ڹ���ʲô����task�����ɸ��̳߳��ύ task.filterIdentity() % num == mod���ύ***/
	private int mod;
	
	/*** task.filterIdentity(),�Ը��ֶν���ȡģ�����й��� ***/
	private int num;

	public List<FetcherListener> getListeners() {
		return listeners;
	}

	public CustomizationFixedTheadPool(int nThreads, int mod, int num) {
		super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.mod = mod;
		this.num = num;
	}
	
	public void registerListener(FetcherListener listener) {
		this.getListeners().add(listener);
	}
	
	/***
	 * �ύtask�����������е�task���ᱻ�ύִ�У��Ƚ���У��
	 * @param task
	 * @return
	 */
    public Future<?> submit(AbstractLogTask task) {
        if (!checkSumbit(task)) {
        	return null;
        }
        
        task.setListeners(this.getListeners());
        return super.submit(task);
    }
    
    /***
     * ��task����У��
     * @param task
     * @return
     */
	public boolean checkSumbit(AbstractLogTask task) {
		boolean passCheck = true;
		if (task == null)
			throw new NullPointerException();
		if (task.filterIdentity() % this.num != this.mod) {
			passCheck = false;
		}
		
		return passCheck;
	}
}
