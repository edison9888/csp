
package com.taobao.csp.loadrun.core.fetch;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.analyser.AbstractAnalyser;
import com.taobao.csp.loadrun.core.fetch.analyser.AnalyseFactory;
import com.taobao.csp.loadrun.core.fetch.fetcher.FetcherFactory;
import com.taobao.csp.loadrun.core.fetch.fetcher.IFetcher;
import com.taobao.csp.loadrun.core.listen.DefaultFetchListen;
import com.taobao.csp.loadrun.core.listen.FetchListen;
import com.taobao.csp.loadrun.core.result.FetchResult;
import com.taobao.csp.loadrun.core.result.FetchResultImpl;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;

/***
 * ����fetchʵ��
 * @author youji.zj 2012-06-20
 *
 */
public abstract class BaseFetchTaskImpl implements IFetchTask{
	
	private static final Logger logger = Logger.getLogger(BaseFetchTaskImpl.class);
	
	/*** �ɼ��� ***/
	private IFetcher fetcher;
	
	/*** ������ ***/
	private AbstractAnalyser analyser;
	
	private FetchListen fetchListen = new DefaultFetchListen();
	
	/*** �洢ѹ���� ***/
	private FetchResult fetchResult = new FetchResultImpl();
	
	/*** �洢ѹ�����ϸ��Ϣ ***/
	private Map<ResultDetailKey, ResultDetailCell> fetchResultDetaul = new ConcurrentHashMap<ResultDetailKey, ResultDetailCell>();
	
	private boolean fetchStop = false;
	
	private Object fetchlock = new Object();
	
	private CountDownLatch fetchDown  = null;
	
	public BaseFetchTaskImpl(LoadrunTarget target, String command) {
		this.fetcher = FetcherFactory.createFetcher(this, target, command);
		this.analyser = AnalyseFactory.createAnalyser(this, target);
	}
	
	@Override
	public void doTask(CountDownLatch down) {
		ThreadFetchTask task = new ThreadFetchTask(down);
		task.start();
		logger.info("��ʼ"+getTaskName()+" ����...");
		
		fetchDown = new CountDownLatch(1);	
	}
	
	@Override
	public void fetch() throws IOException {
		fetcher.fetch();
	}
	
	@Override
	public void analyse(String line) {
		if (analyser != null) {
			analyser.analyse(line);
		}
	}
	
	@Override
	public synchronized void stopFetch(){
		
		if(!fetchStop){
			fetcher.end();
			
			fetchStop = true;
			
			synchronized (fetchlock) {
				fetchlock.notifyAll();
			}
			
			try {
				logger.info("��ʼ�ȴ�ֹͣ"+getTaskName()+" ����...");
				fetchDown.await();  // �ȴ��ɼ��̵߳Ľ���
			} catch (InterruptedException e) {
			}
			
			logger.info("ֹͣ"+getTaskName()+" ����ɹ�");
		}
	}
	
	@Override
	public void putData(ResultKey key,Double value,Date time) {
		fetchResult.put(key, value, time);
	}
	
	@Override
	public void putDetailData(ResultDetailKey key, double count, double time) {
		ResultDetailCell cell = new ResultDetailCell();
		cell.setCount(count);
		cell.setTime(time);
		cell.setmKey(key.getmKey());
		cell.setsKey(key.getsKey());
		cell.setCollectTime(key.getCollectTime());
		
		if (fetchResultDetaul.containsKey(key)) {
			ResultDetailCell oldValue = fetchResultDetaul.get(key);
			oldValue.add(cell);
		} else {
			fetchResultDetaul.put(key, cell);
		}
	}
	
	@Override
	public void putDetailData(ResultDetailKey key, double time) {
		ResultDetailCell cell = new ResultDetailCell();
		cell.setTime(time); // ֻ�е���value�ķŵ�time�ֶ�
		cell.setCount(1);  // count����Ĭ��1
		cell.setmKey(key.getmKey());
		cell.setsKey(key.getsKey());
		cell.setCollectTime(key.getCollectTime());
		
		if (fetchResultDetaul.containsKey(key)) {
			ResultDetailCell oldValue = fetchResultDetaul.get(key);
			oldValue.average(cell);
		} else {
			fetchResultDetaul.put(key, cell);
		}
	}
	
	@Override
	public FetchResult getResult() {
		return fetchResult;
	}
	
	@Override
	public Map<ResultDetailKey, ResultDetailCell> getResultDetail() {
		return fetchResultDetaul;
	}
	
	@Override
	public void addFetchListen(FetchListen listen) {
		this.fetchListen = listen;
	}
	
	@Override
	public boolean isRun() {
		return fetcher.isRun();
	}
	
	/***
	 * ִ�вɼ��������ڲ����߳� 
	 * @author youji.zj
	 *
	 */
	private class ThreadFetchTask extends Thread{
		
		private CountDownLatch taskDown;
		
		public ThreadFetchTask(CountDownLatch down){
			this.taskDown = down;
			
		}
		public void run(){
			
			logger.info("��ʼ�ɼ��߳� "+getTaskName()+" ");
			
			fetchListen.start();
			try {
				fetcher.start();
				fetch();
			} catch (Exception e) {
				fetchListen.error(e);
			} finally {
				fetcher.end();
			}
			
			this.taskDown.countDown();
			
			synchronized (fetchlock) {
				fetchlock.notifyAll();
			}
			
			fetchListen.complete();
			
			fetchDown.countDown();
			
			logger.info("�����ɼ��߳� "+getTaskName()+" ");
		}
		
	}

}
