
package com.taobao.csp.loadrun.web;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import com.taobao.csp.assign.job.JobListen;
import com.taobao.csp.assign.job.entry.JobReportEntry;
import com.taobao.csp.assign.master.JobFeature;
import com.taobao.csp.assign.master.Master;
import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.core.job.LoadrunJob;
import com.taobao.csp.loadrun.core.listen.LoadrunListen;
import com.taobao.csp.loadrun.core.result.ResultDetailCell;
import com.taobao.csp.loadrun.web.bo.CspLoadRunBo;
import com.taobao.csp.loadrun.web.util.LoadrunUtil;



/**
 * 
 * @author xiaodu
 * @version 2011-6-23 下午01:03:46
 */
public class LoadrunTaskMaster implements Runnable{
	
	
	private static final Logger logger = Logger.getLogger(LoadrunTaskMaster.class);
	
	
	private static String LOAD_NAME_PREFIX="LOAD_NAME_PREFIX";
	
	private LinkedBlockingQueue<TaskResource> taskQueue = new LinkedBlockingQueue<TaskResource>();
	
	private Map<String,TaskResource> needRunMap = new ConcurrentHashMap<String, TaskResource>();
	
	
	private Object lock = new Object();
	
	private Thread threadContainer = null;
	
	private Master master = new Master();
	
	private CspLoadRunBo cspLoadRunBo;
	
	
	private volatile boolean limitLoadrun = false;
	
	public CspLoadRunBo getCspLoadRunBo() {
		return cspLoadRunBo;
	}
	public void setCspLoadRunBo(CspLoadRunBo cspLoadRunBo) {
		this.cspLoadRunBo = cspLoadRunBo;
	}

	private LoadrunListen loadrunListen;
	
	
	public LoadrunListen getLoadrunListen() {
		return loadrunListen;
	}
	public void setLoadrunListen(LoadrunListen loadrunListen) {
		this.loadrunListen = loadrunListen;
	}
	
	
	
	


	public boolean isLimitLoadrun() {
		return limitLoadrun;
	}
	public void setLimitLoadrun(boolean limitLoadrun) {
		this.limitLoadrun = limitLoadrun;
	}
	public  LoadrunTaskMaster() {
		threadContainer = new Thread(this);
		threadContainer.setDaemon(false);
		threadContainer.setName("Thread - LoadrunTaskContainer");
		threadContainer.start();
		
		
		Thread thread = new Thread(new TaskRunTread());
		thread.setDaemon(false);
		thread.setName("Thread - LoadrunTaskContainer - TaskRunTread");
		thread.start();
		
	}
	
	
	public void init(){
		List<LoadRunHost> loadRunList = cspLoadRunBo.findAllLoadRunHost();
		for(LoadRunHost load:loadRunList){
			LoadrunTarget target = load.getTarget();
			try {
				addTask(target);				
			} catch (Exception e) {
				logger.error("init add loadrun :"+target.getAppId()+" "+target.getAppName()+" "+target.getCronExpression()+" "+target.getTargetIp()+" "+target.getLoadrunType(),e);
			}
		}
	}
	
	
	
	public Master getLoadMaster(){
		return master;
	}
	
	
	
	
	
	
	
	
	public void modifyTask(LoadrunTarget target) throws Exception {
		String taskname = getTaskName(target.getAppId());
		TaskResource task = needRunMap.get(taskname);
		if(task ==null){
			throw new Exception(target.getAppId()+" 不存在! ");
		}
		
//		if(task.isRun){
//			throw new Exception("正在执行不能修改!");
//		}
		CronExpression e = new CronExpression(target.getCronExpression());
		task.nextFireDate = e.getNextValidTimeAfter(new Date());
		task.target = target;
		synchronized (lock) {
			lock.notifyAll();
		}
		
		logger.info("modifyTask loadrun :"+target.getAppId()+" "+target.getAppName()+" "+target.getCronExpression()+" "+target.getTargetIp()+" "+target.getLoadrunType());
		
		
	}
	
	public void deleteTask(LoadrunTarget target) throws Exception{
		String taskname = getTaskName(target.getAppId());
		TaskResource task = needRunMap.get(taskname);
		if(task!=null){
			if(task.isRun){
				throw new Exception("正在执行不能修改!");
			}
			needRunMap.remove(taskname);
			
		}
		

		synchronized (lock) {
			lock.notifyAll();
		}
		
		logger.info("deleteTask loadrun :"+target.getAppId()+" "+target.getAppName()+" "+target.getCronExpression()+" "+target.getTargetIp()+" "+target.getLoadrunType());
		
	}
	
	public void addTask(LoadrunTarget target) throws Exception{
		String taskname = getTaskName(target.getAppId());
		TaskResource task = needRunMap.get(taskname);
		if(task!=null){
			throw new Exception("已经存在!");
			
		}
		
		task = new TaskResource();
		
		CronExpression e = new CronExpression(target.getCronExpression());
		
		
		task.isRun = false;
		task.nextFireDate = e.getNextValidTimeAfter(new Date());
		task.target = target;
		needRunMap.put(taskname, task);
		
		
		synchronized (lock) {
			lock.notifyAll();
		}
		
		logger.info("addTask loadrun :"+target.getAppId()+" "+target.getAppName()+" "+target.getCronExpression()+" "+target.getTargetIp()+" "+target.getLoadrunType());
	}
	
	
	public String getTaskName(int appId){
		return appId+LOAD_NAME_PREFIX;
	}
	
	public boolean isRun(int appId) throws Exception{
		String taskname = getTaskName(appId);
		TaskResource task = needRunMap.get(taskname);
		if(task == null){
			throw new Exception("不存在!");
		}
		return task.isRun;
		
	}
	
	
	public void runLoadTask(int appId) throws Exception{
		String taskname = getTaskName(appId);
		TaskResource task = needRunMap.get(taskname);
		if(task == null){
			throw new Exception("不存在!");
		}
		submitLoadrunTask(task,true);
	}
	
	
	public void submitLoadrunTask(TaskResource task,boolean manual) throws Exception{
		
		logger.info("当前 limitLoadrun :"+limitLoadrun+"manual:"+manual);
		if(limitLoadrun&&!manual){
			if(!task.target.getLoadrunType().equals(AutoLoadType.httpLoad)){
				logger.info("非HTTP 方式，暂停自动压测");
				return ;
			}
		}
		
		
		LoadrunUtil.checkTargetMessage(task.target);
		
		if(master.getSlave().size() ==0 ){
			throw new Exception("当前无Slave 连接，无法提供服务!");
		}
		
		if(task.isRun){
			throw new Exception("已经在运行!");
		}
		
		taskQueue.add(task);
		task.isRun = true;
		
		logger.info("submitLoadrunTask loadrun :"+task.target.getAppId()+" "+task.target.getAppName()+" "+task.target.getCronExpression()+" "+task.target.getTargetIp()+" "+task.target.getLoadrunType());

	}
	
	
	private void saveLoadrunResult(JobReportEntry jobReportEntry){
		
		Object obj1 = jobReportEntry.getJob().getDetail();
		Object obj2 = jobReportEntry.getMessage();
		
		logger.info("obj1:" + obj1.getClass().getName() + "obj2:" + obj2.getClass().getName());
		
		// 压测结果
		if(obj1 instanceof LoadrunTarget && obj2 instanceof Map){
			
			LoadrunTarget target = (LoadrunTarget)obj1;
			Map<ResultKey, Double> result = (Map<ResultKey, Double>)obj2;
			loadrunListen.reportLoad(jobReportEntry.getJob().getJobId(),target, result);
		}
		
		// 压测详情
		if(obj1 instanceof LoadrunTarget && obj2 instanceof List){
			
			LoadrunTarget target = (LoadrunTarget)obj1;
			List<ResultDetailCell> result = (List<ResultDetailCell>)obj2;
			
			logger.info("result detail size:" + result.size());
			loadrunListen.reportLoadDetail(jobReportEntry.getJob().getJobId(),target, result);
		}
		
		// 阀值信息
		if (obj1 instanceof LoadrunTarget && obj2 instanceof String) {
			LoadrunTarget target = (LoadrunTarget)obj1;
			String loadrunId = (String) obj2;
			loadrunListen.reportLoadThreshold(loadrunId, target);
		}
		
		
	}
	
	private void endLoadTask(String loadrunId,TaskResource t){
		t.isRun = false;
		loadrunListen.complete(loadrunId,t.target);		
		logger.info("endLoadTask next  "+t.nextFireDate+" loadrun :"+t.target.getAppId()+" "+t.target.getAppName()+" "+t.target.getCronExpression()+" "+t.target.getTargetIp()+" "+t.target.getLoadrunType());
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	private void loseLoadTask(String loadrunId,TaskResource t,Object e){
		t.isRun = false;
		loadrunListen.error(loadrunId,t.target,e);		
		logger.info("loseLoadTask :"+e+" ,loadrun task:"+t.target.getAppId()+" "+t.target.getAppName()+" "+t.target.getCronExpression()+" "+t.target.getTargetIp()+" "+t.target.getLoadrunType());
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	
	private class TaskRunTread implements Runnable{

		public void run() {
			
			while(true){
				try {
					final TaskResource t = taskQueue.poll(1, TimeUnit.HOURS);
					if(t != null){
						
						
//						if(t.target.getLoadrunType().equals(AutoLoadType.httpLoad)){
//							try{
//								Set<String> ipSet = OpsFreeHostCache.get().getIpSetNoCache(t.target.getOpsField(), t.target.getOpsName());
//								
//								for(String ip:ipSet){
//									if(RemoteCommonUtil.authenticate(ip, t.target.getTargetUserName(), t.target.getTargetPassword())){
//										t.target.setTargetIp(ip);
//										break;
//									}
//								}
//							}catch (Exception e) {
//								// TODO: handle exception
//							}
//						}
						
						
						JobFeature feature = master.assignJob(new LoadrunJob(t.target));
						feature.addJobListen(new JobListen(){
							@Override
							public void listenReport(JobReportEntry jobReportEntry) {
								
								switch(jobReportEntry.getJobState()){
									case Start:loadrunListen.start(jobReportEntry.getJob().getJobId(),t.target);break;
									case Complete:endLoadTask(jobReportEntry.getJob().getJobId(),t);break;
									case Error:;loadrunListen.error(jobReportEntry.getJob().getJobId(),t.target, jobReportEntry.getMessage());break;
									case Running:;break;
									case Report:saveLoadrunResult(jobReportEntry);break;
									case Lose:loseLoadTask(jobReportEntry.getJob().getJobId(),t,jobReportEntry.getMessage());break;
									case Exception:loadrunListen.error(jobReportEntry.getJob().getJobId(),t.target, jobReportEntry.getMessage());break;
								}
							}});
					}
				} catch (InterruptedException e1) {
				}
			}
			
		}
		
	}
	
	
	private class TaskResource{
		
		private Date nextFireDate;
		
		private LoadrunTarget target;
		
		private boolean isRun = false;
		
	}

	public void run() {
		while(true){
			long nextFire = Long.MAX_VALUE;
			for(Map.Entry<String,TaskResource> entry:needRunMap.entrySet()){
				if(entry.getValue().target.getLoadAuto() == 1){
					if(!entry.getValue().isRun){
						long nextFireTime = entry.getValue().nextFireDate.getTime();
						long needTime = nextFireTime - System.currentTimeMillis();
						if(needTime <=0){
							try {
								submitLoadrunTask(entry.getValue(),false);
							} catch (Exception e) {
								logger.info("提交任务失败!",e);
								loadrunListen.error("noId", entry.getValue().target, e);
							}
							CronExpression e;
							try {
								e = new CronExpression(entry.getValue().target.getCronExpression());
								entry.getValue().nextFireDate = e.getNextValidTimeAfter(new Date());
							} catch (ParseException e1) {
								logger.error(" loadrun CronExpression :"+entry.getValue().target.getAppId()+" "+entry.getValue().target.getAppName()+" "+entry.getValue().target.getCronExpression(),e1);
							}
							continue;
						}
						logger.info(entry.getValue().target.getAppName()+"-"+entry.getValue().target.getAppId()+": wait next task time: "+needTime);
						if(nextFire > needTime ){
							nextFire = needTime;
						}
					}else{
						CronExpression e;
						try {
							e = new CronExpression(entry.getValue().target.getCronExpression());
							entry.getValue().nextFireDate = e.getNextValidTimeAfter(new Date());
						} catch (ParseException e1) {
							logger.error(" loadrun CronExpression :"+entry.getValue().target.getAppId()+" "+entry.getValue().target.getAppName()+" "+entry.getValue().target.getCronExpression(),e1);
						}
					}
				}
			}
			
			if(nextFire <0){
				nextFire = 10000;
			}
			
			logger.info("LoadrunTaskMaster wait next task time: "+nextFire);
			synchronized (lock) {
				try {
					
					lock.wait(nextFire);
				} catch (InterruptedException e) {
				}
			}
		}
	}

}
