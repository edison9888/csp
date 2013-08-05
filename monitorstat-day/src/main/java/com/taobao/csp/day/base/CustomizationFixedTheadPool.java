package com.taobao.csp.day.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/***
 * 带监听带过滤功能的线程池(对LogDataFetcherTask进行过滤)
 * 处理task的时候先对task.filterIdentity()
 * 与num字段进行取模
 * 如果计算结果等于mod，则正常提交该task
 * 否则不提交task
 * 
 * @author youji.zj
 * @version 1.0
 */
public class CustomizationFixedTheadPool extends ThreadPoolExecutor {
	
	public static Logger logger = Logger.getLogger(CustomizationFixedTheadPool.class);
	
	private List<FetcherListener> listeners = new ArrayList<FetcherListener>();
	
	/*** 用于过滤什么样的task可以由该线程池提交 task.filterIdentity() % num == mod则提交***/
	private int mod;
	
	/*** task.filterIdentity(),对该字段进行取模来进行过滤 ***/
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
	 * 提交task，并不是所有的task都会被提交执行，先进行校验
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
     * 对task进行校验
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
