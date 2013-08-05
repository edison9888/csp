package com.taobao.csp.loadrun.core.run;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.FetchFeature;
import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.fetch.IFetchTask;
import com.taobao.csp.loadrun.core.listen.DefaultLoadrunListen;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;
import com.taobao.csp.loadrun.core.result.FetchResult;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.core.result.ResultDetailKey;
import com.taobao.csp.loadrun.core.valid.DefaultValid;
import com.taobao.csp.loadrun.core.valid.Valid;

/**
 * 
 * @author xiaodu
 * @version 2011-6-22 下午02:49:23
 */
public abstract class BaseLoadrunTask implements ILoadrunTask {
	
	private static final Logger logger = Logger.getLogger(BaseLoadrunTask.class);

	/**
	 * 收集器 集合
	 */
	private Vector<FetchFeature> fetchFeatures = new Vector<FetchFeature>();

	/**
	 * 用于loadtask等待
	 * 
	 */
	private CountDownLatch count = null;

	private LoadrunTarget target;

	protected LoadrunListen loadrunListen = new DefaultLoadrunListen();;

	private Map<ResultKey, Double> resultMap = new ConcurrentHashMap<ResultKey, Double>();
	
	private List<ResultDetailCell> resultDetail = new CopyOnWriteArrayList<ResultDetailCell>();

	private volatile boolean  taskRun = true;
	
	private volatile int loadrunOrder = 0;
	

	private CountDownLatch loadDown = null;
	
	private Object monitorLock = new Object();
	
	private Object collectDetailLock = new Object();
	
	private volatile boolean doload = false;
	
	private String loadrunId;
	
	
	public String getLoadrunId(){
		return loadrunId;
	}
	
	public boolean isDoload() {
		return doload;
	}

	public Vector<FetchFeature> getFetchFeatures() {
		return fetchFeatures;
	}

	protected void startLoad() {
		loadDown = new CountDownLatch(1);
		doload = true;
		logger.info("开始 BaseLoadrunTask ");
		
	}

	protected void endLoad() {
		logger.info("开始结束 BaseLoadrunTask ");
		if (loadDown != null) {
			loadDown.countDown();
		}
		doload = false;
		logger.info("结束 BaseLoadrunTask完成");
	}

	protected void waitLoad() {
		logger.info("等待 BaseLoadrunTask结束");

		if (loadDown != null) {
			try {
				loadDown.await();
			} catch (InterruptedException e) {
			}
		}

	}

	/***
	 * stopTask有4种情况
	 * 1、自动压测的自动停止
	 * 2、手动压测的手动停止
	 * 3、到达阀值
	 * 4、自动压测的手动停止
	 * 只需要停止一次
	 */
	public synchronized void stopTask() {
		if(taskRun){
			this.recordData();
			taskRun = false;
			logger.info("停止 BaseLoadrunTask ");
			waitLoad();
			closeFetch();
		}
		synchronized (monitorLock) {
			monitorLock.notifyAll();
		}
		
		synchronized (collectDetailLock) {
			collectDetailLock.notifyAll();
		}
	}

	protected boolean isTaskRun() {
		return taskRun;
	}

	public LoadrunTarget getTarget() {
		return target;
	}

	public BaseLoadrunTask(LoadrunTarget target) throws Exception {
		this(target,UUID.randomUUID().toString());
	}
	
	public BaseLoadrunTask(LoadrunTarget target,String loadrunid) throws Exception {
		this.target = target;
		this.loadrunId = loadrunid;
		init(target);
	}
	
	public void runAutoControl(){
		try {
			this.autoControl(target.getLoadFeature());
		} catch (Exception e) {
			loadrunListen.error(this.getLoadrunId(),target, e);
		}
		this.stopTask();
		
	}
	
	/**
	 * maxCpu:75;maxLoad:4;maxQps:4000;maxRest:400;
	 * 
	 * @param limitFeature
	 * @return
	 */
	protected Valid parseValid(String limitFeature) {

		if (limitFeature == null) {
			return null;
		}

		DefaultValid valid = new DefaultValid();
		String[] features = limitFeature.split(";");

		Map<String, Double> map = new HashMap<String, Double>();
		for (String f : features) {
			String[] v = f.split(":");
			try{
				map.put(v[0].toLowerCase(), Double.parseDouble(v[1]));
			}catch (Exception e) {
			}
		}

		for (ResultKey key : ResultKey.values()) {

			Double d = map.get(key.name().toLowerCase());
			if (d != null) {
				valid.putValid(key, d);
			}
		}
		return valid;
	}

	public void init(LoadrunTarget target) throws Exception {
		Valid myValid = parseValid(target.getLimitFeature());
		Set<String> classes = target.getFetchClasses();
		for (String clazz : classes) {
			try {
				Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(clazz);
				Constructor<?> constructor = c.getConstructor(LoadrunTarget.class);
				Object obj = constructor.newInstance(target);
				if (obj instanceof IFetchTask) {
					IFetchTask task = (IFetchTask) obj;
					FetchFeature feature = submitFetch(task);
					feature.addValidFetch(myValid);
				}
			} catch (Exception e) {
				loadrunListen.error(this.getLoadrunId(),target, e);
				throw e;
			}
		}
		
		MonitorThread t = new MonitorThread();
		t.setName("LoadTash - MonitorThread");
		t.start();
	}

	public void addLoadrunListen(LoadrunListen listen) {
		this.loadrunListen = listen;
	}
	
	public void recordThreashold() {
		loadrunListen.reportLoadThreshold(getLoadrunId(), target);
	}

	public void closeFetch() {
		for (FetchFeature f : fetchFeatures) {
			f.close();
		}
	}
	
	
	private void fillFetchValue(){
		synchronized (resultMap) {
			for (FetchFeature f : fetchFeatures) {
				FetchResult result = f.getResult();
				resultMap.putAll(result.getResult());
			}
		}
	}
	
	private void fillFetchValueDetail(){
		synchronized (resultDetail) {
			for (FetchFeature f : fetchFeatures) {
				Map<ResultDetailKey, ResultDetailCell> result = f.getResultDetail();
				resultDetail.addAll(result.values());
				
				// 清空为下一次收集
				result.clear();
			}
		}
	}
	
	public Map<ResultKey, Double> getLoadResult(){
		
		fillFetchValue();
		
		return resultMap;
	}
	
	public List<ResultDetailCell> getLoadResultDetail(){
		
		fillFetchValueDetail();
		
		return resultDetail;
	}
	
	
	protected abstract void autoControl(String feature) throws Exception;

	public void recordData() {
		loadrunOrder++;
		
		this.getTarget().setLoadrunOrder(loadrunOrder);
		
		LoadrunResult result = new LoadrunResult();
		result.setAppId(this.getTarget().getAppId());
		result.setLoadrunType(this.getTarget().getLoadrunType());
		result.setTargetIp(this.getTarget().getTargetIp());

		for (Map.Entry<ResultKey, Double> entry : getLoadResult().entrySet()) {
			ResultKey key = entry.getKey();
			Double value = entry.getValue();
			System.out.println(key+":"+value);
		}
		
		loadrunListen.reportLoad(this.getLoadrunId(),this.getTarget(), getLoadResult());
		loadrunListen.reportLoadDetail(this.getLoadrunId(),this.getTarget(), getLoadResultDetail());
		resultDetail.clear();
	}

	public FetchFeature submitFetch(IFetchTask fetch) {
		FetchFeature feature = new FetchFeature(fetch);
		fetchFeatures.add(feature);
		return feature;
	}

	public void waitForStop() {
		if (count != null) {
			try {
				count.await();
			} catch (InterruptedException e) {
			}
		}
		loadrunListen.complete(this.getLoadrunId(),target);
	}

	public void runFetchs() {
		if (fetchFeatures.size() > 0) {
			loadrunListen.start(this.getLoadrunId(),target);
			count = new CountDownLatch(fetchFeatures.size());
			for (FetchFeature fetch : fetchFeatures) {
				fetch.runFetch(count);
			}
		}
		synchronized (this) {
			try {
				this.wait(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	
	private class MonitorThread extends Thread{
		
		public void run(){
			lable:
			while(taskRun){			
				for(FetchFeature feature :fetchFeatures){
					if(!feature.doValid()){
						logger.info("开始"+feature.getTaskName()+" 任务 限制验证不通过 任务开始停止 .... ");
						stopTask();
						break lable;
					}
					
					if (feature.fetchException()) {
						logger.info(feature.getTaskName() + "采集异常");
						stopTask();
						break lable;
					}
				}
				synchronized (monitorLock) {
					try {
						monitorLock.wait(10*1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}		
	}
}
