
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.monitor.assist.AppListen;
import com.taobao.csp.monitor.assist.CollectorListen;
import com.taobao.csp.monitor.assist.GroupServiceAssist;
import com.taobao.csp.monitor.assist.ServiceAssist;
import com.taobao.csp.monitor.impl.DefaultAssignmentPolicyImpl;
import com.taobao.csp.monitor.impl.DefaultJobExecutorImpl;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.TimeConfAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.po.TimeConfPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * 
 * ����Э����
 *  ��Ҫ����Ϊ 
 *    �õ���Ӧ��ִ���� Executor
 *    ��ȡ��������е�Job
 *    ���ݷ������ ����JOB��Executor
 * 
 * 
 * @author xiaodu
 *
 * ����2:25:46
 */
public class GroupMaker implements AppListen,CollectorListen{
	
	private static final Logger logger =  Logger.getLogger(GroupMaker.class);
	
	private String groupName;
	
	private List<String> executorList =new ArrayList<String>() ;
	
	private JobExecutor selfExecutor;
	
	private AssignmentPolicy assignmentPolicy;
	
	private ServiceAssist serviceAssist;
	
	
	public Set<String> monitorApps = new HashSet<String>();
	
	
	private String executorNode = null;
	
	
	public String getGroupName() {
		return groupName;
	}

	public GroupMaker(String groupName){
		this.groupName = groupName;
		assignmentPolicy = new DefaultAssignmentPolicyImpl(groupName);
		
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			logger.error("��ȡ������ַʧ��", e);
			System.exit(-1);
		}
		selfExecutor = new DefaultJobExecutorImpl(address.getHostAddress(),this);
		serviceAssist = new GroupServiceAssist(groupName,address.getHostAddress());
		serviceAssist.registerCollector();
		
	} 
	
	/**
	 * ���� collector 
	 *@author xiaodu
	 *TODO
	 */
	public void startup(){
		
		//���� ִ���ߵļ�������ȡ��������ͬ�����µ�ִ����ip�б�����ȡ�Լ���ִ������
		initGroupJobs();
		
		serviceAssist.addAppListen(this);
		
		serviceAssist.addCollectorListen(this);
		
	}
	
	
	public void stop(){
		selfExecutor.stop();
		serviceAssist.logoutCollector();
		
		logger.info("GroupMaker is stop");
	}
	
	/**
	 * ��ʼ�����groupmaker���job
	 *@author xiaodu
	 *TODO
	 */
	public synchronized void initGroupJobs(){
		logger.info(this.groupName+" initGroupJobs");
		
		//���ͷ���������
		selfExecutor.releaseAllJob();
		
		executorList = serviceAssist.findGroupCollectors();
		
		Collections.sort(executorList);
		
		List<JobInfo> groupJobList = queryGroupJobs();//��ȡȫ������������
		if(groupJobList == null){
			return ;
		}
		
		for(JobInfo job:groupJobList){
			String eName = assignmentPolicy.assign(job, executorList);
			if(eName == null){
				continue;
			}
			logger.info("assignmentPolicy to"+eName+job.getAppName()+":"+job.getIp()+":"+job.getJobName()+":"+job.getFilepath());
			if(selfExecutor.getName().equals(eName)){
				selfExecutor.assignJob(job);
			}
		}
	}
	

	
	/**
	 * ִ����������
	 *@author xiaodu
	 *TODO
	 */
	public void activateHeart(Map<String,String> map){
		map.put("executorList", executorList.size()+"");
		serviceAssist.heartbeat(map);
	}

	
	
	/**
	 * ��ѯ����飬��������������б�
	 * @return
	 */
	public List<JobInfo> queryGroupJobs(){
		
		List<JobInfo> jobInfoList = new ArrayList<JobInfo>();
		
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
		
		logger.info("find app size:"+appList.size());
		
		List<String> monitorApp = serviceAssist.findApps();
		
		if(monitorApp == null){
			return jobInfoList;
		}
		
		logger.info("find monitor app size:"+monitorApp.size());
		
		for(AppInfoPo appPo:appList){
			if(monitorApp.contains(appPo.getAppName())){//
				logger.info(this.groupName+" find monitor app:"+appPo.getAppName());
				List<JobInfo>  list = queryAppGroupJobs(appPo);
				if(list != null){
					jobInfoList.addAll(list);
				}
			}
		}
		return jobInfoList;
	}
	
	
	/**
	 * ��ѯ���Ӧ����Ҫ��صĸ�����Ϣ
	 * @return
	 */
	public List<JobInfo> queryAppGroupJobs(AppInfoPo appPo){
		
		List<JobInfo> jobInfoList = new ArrayList<JobInfo>();
	
		List<HostPo> list = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appPo.getAppName());

		if(list ==null||list.size()==0){
			logger.error(appPo.getAppName()+" no host list");
			return null;
		}
		
		
		List<TimeConfPo> timeConf = TimeConfAo.get().findTimeConfByAppId(appPo.getAppId());
		Map<String,JobInfo> fileMap = new HashMap<String, JobInfo>();
		
		//�ϲ���ȡ��ͬ�ļ�������
		for(TimeConfPo conf:timeConf){
			String filePath = conf.getFilePath();
			JobInfo job = fileMap.get(filePath);
			if(job == null){
				job = new JobInfo(appPo.getAppName()+conf.getFilePath());
				fileMap.put(filePath, job);
				job.setAppName(appPo.getAppName());
				job.setCollectorType(conf.getObtainType()+"");
				job.setFilepath(conf.getFilePath());
				job.setAnalyseFrequency(conf.getAnalyseFrequency());
				job.setJobName(conf.getAliasLogName());
				
				job.setLinebreaks('\n');
				if("\\t".equals(conf.getSplitChar())){
					job.setLinebreaks('\t');
				}
				if(	"\\02".equals(conf.getSplitChar())){
					job.setLinebreaks('\02');
				}
			}else{
				if(job.getAnalyseFrequency() >conf.getAnalyseFrequency()){
					job.setAnalyseFrequency(conf.getAnalyseFrequency());
				}
				job.setJobName(conf.getAliasLogName()+"_"+job.getJobName());
			}
			
			AnalyseInfo info = new AnalyseInfo();
			
			info.setAnalyseClass(conf.getClassName());
			
			info.setAnalyseDetail(conf.getAnalyseFuture());
			info.setAnalyseType(conf.getAnalyseType()+"");
			job.getAnalyseList().add(info);
		}
		
		if("tair".equals(appPo.getAppType())){
			//���û���
			for(Map.Entry<String,JobInfo> entry:fileMap.entrySet()){
				JobInfo job = entry.getValue();
				job.setCollectorType("44");
				JobInfo tmp = new JobInfo(appPo.getAppName()+"CMX"+job.getFilepath());
				try {
					BeanUtils.copyProperties(tmp, job);
				} catch (Exception e) {
					logger.error("copy JobInfo properties", e);
				} 
				tmp.getAnalyseList().addAll(job.getAnalyseList());
				
				tmp.setIp("");
				tmp.setSite("CMX");
				tmp.setSshUserName(appPo.getLoginName());
				tmp.setSshPassword(appPo.getLoginPassword());
				tmp.setNodePath(executorNode);
				jobInfoList.add(tmp);
			}
			fileMap.clear();
		}
		
		
		
//		//default job
		JobInfo defaultJob = new JobInfo(appPo.getAppName()+"top -b -i -n2 -d 1");
		defaultJob.setAppName(appPo.getAppName());
		defaultJob.setCollectorType("2");
		defaultJob.setFilepath("top -b -i -n2 -d 1");
		defaultJob.setAnalyseFrequency(120);
		defaultJob.setJobName("top_job");
		defaultJob.setLinebreaks('\n');
		
		AnalyseInfo info = new AnalyseInfo();
		info.setAnalyseClass("com.taobao.csp.monitor.impl.analyse.top.TopLogAnalyse");
		info.setAnalyseDetail("");
		info.setAnalyseType("1");
		defaultJob.getAnalyseList().add(info);
		
		fileMap.put(defaultJob.getFilepath(), defaultJob);
		
		
		
		//���û���
		for(Map.Entry<String,JobInfo> entry:fileMap.entrySet()){
			JobInfo job = entry.getValue();
			
			for(HostPo po:list){
				JobInfo tmp = new JobInfo(appPo.getAppName()+po.getHostIp()+job.getFilepath());
				try {
					BeanUtils.copyProperties(tmp, job);
				} catch (Exception e) {
					logger.error("copy JobInfo properties", e);
				} 
				tmp.getAnalyseList().addAll(job.getAnalyseList());
				
				tmp.setIp(po.getHostIp());
				tmp.setSite(po.getHostSite());
				tmp.setSshUserName(appPo.getLoginName());
				tmp.setSshPassword(appPo.getLoginPassword());
				tmp.setNodePath(executorNode);
				jobInfoList.add(tmp);
			}
		}
		return jobInfoList;
	}
	
	

	public synchronized void appChange(List<String> apps) {
		initGroupJobs();
	}

	public synchronized void appAdd(String app) {
//		if(app ==null||"".equals(app.trim())){
//			return ;
//		}
//		AppInfoPo appPo = AppInfoAo.get().getAppInfoByAppName(app);
//		if(appPo != null){
//			List<JobInfo> list = queryAppGroupJobs(appPo);
//			selfExecutor.releaseAppJob(app);
//			if(list != null){
//				for(JobInfo job:list){
//					selfExecutor.assignJob(job);
//				}
//			}
//		}
		
		initGroupJobs();
		
	}

	public synchronized void appDelete(String app) {
//		selfExecutor.releaseAppJob(app);
		
		initGroupJobs();
	}

	public synchronized void collectorChange(List<String> collectors) {
		
		if(collectors == null||collectors.size()==0){
			return ;
		}
		logger.info(this.groupName+" activateExecutorsChange");
		Collections.sort(collectors);
		//��� ִ���ߵ��б����仯������ִ����λ�÷��ֱ仯
		executorList = collectors;
		initGroupJobs();
	}
	
	

}
