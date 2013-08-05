
package com.taobao.csp.assign.master;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.taobao.csp.assign.job.IJob;
import com.taobao.csp.assign.job.JobReportType;
import com.taobao.csp.assign.job.entry.JobReportEntry;
import com.taobao.csp.assign.packet.PacketType;
import com.taobao.csp.assign.packet.RequestPacket;

/**
 * 
 * @author xiaodu
 * @version 2011-7-4 上午11:08:33
 */
public class SlaveProxy implements ISlaveProxy{
	
	
	private static final Logger logger = Logger.getLogger(SlaveProxy.class);
	
	
	private String slaveId;
	
	private boolean state;//当前状态
	
	private int capacity =1;// 能力的容量 即最多同时执行的job个数
	
	public IoSession getSession() {
		return session;
	}

	private IoSession session;
	
	private Master master;
	
	private Map<String,JobFeature> runMap = new ConcurrentHashMap<String, JobFeature>();
	
	private String slaveSiteName; // slave的机房
	
	
//	/**
//	 * 等待响应集合
//	 */
//	private static ConcurrentHashMap<String, ArrayBlockingQueue<ResponsePacket>> responses=
//		new ConcurrentHashMap<String, ArrayBlockingQueue<ResponsePacket>>();
	
	public String getSlaveSiteName() {
		return slaveSiteName;
	}
	/**
	 * slave 的执行容量
	 * @return
	 */
	public int getCapacity(){
		return capacity;
	}
	/**
	 * 当前执行的job数量
	 * @return
	 */
	public int getJobNum(){
		return runMap.size();
	}
	
	
	public String getSlaveId() {
		return slaveId;
	}
	
	public SlaveProxy(String id,IoSession session,Master master, String slaveSiteName){
		this.slaveId = id;
		this.session = session;
		this.master = master;
		this.slaveSiteName = slaveSiteName.split("#")[0];
		this.capacity = Integer.parseInt(slaveSiteName.split("#")[1]);
	}
	
	public synchronized JobFeature submitJob(final IJob job){
		
		final JobFeature jobFeature = new JobFeature(job);
		runMap.put(jobFeature.getJobId(), jobFeature);
		
		ByteArrayOutputStream byteBuffered = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(byteBuffered);
			oos.writeObject(job);
			oos.close();
		} catch (IOException e) {
		}
		byte[] bs = byteBuffered.toByteArray();
		RequestPacket p = new RequestPacket(bs,PacketType.Job);
		WriteFuture future = session.write(p);
		future.addListener(new IoFutureListener<IoFuture>(){
			public void operationComplete(IoFuture future) {
				WriteFuture wfuture = (WriteFuture) future;
				if (wfuture.isWritten()) {
					return;
				}
				logger.info("SlaveID:"+slaveId+"$"+session.getRemoteAddress().toString()+"中的Job:"+job.getJobId()+" 提交失败!");
				jobReport(new JobReportEntry(job,JobReportType.Error,"无法将"+job.getDetail().toString()+" 发送到目标Slave："+SlaveProxy.this.getSession().getRemoteAddress().toString()));
			}});
		
		return jobFeature;
	}
	
	public synchronized void stopJob(String jobId){
		RequestPacket p = new RequestPacket(jobId,PacketType.stopJob);
		session.write(p);
	}
	
	
	public synchronized Set<JobFeature> jobs(){
		Set<JobFeature> set = new HashSet<JobFeature>();
		set.addAll(runMap.values());
		return set;
	}
	
	public void jobReport(JobReportEntry jobReportEntry){
		JobFeature j = runMap.get(jobReportEntry.getJob().getJobId());
		if(j == null){
			logger.error("丢失 job"+jobReportEntry.getJob().getJobId()+" type:"+jobReportEntry.getJobState());
			return ;
		}
		j.getListen().listenReport(jobReportEntry);
		
		switch(jobReportEntry.getJobState()){
			case Start:j.setRun(true);break;
			case Complete:endJob(jobReportEntry.getJob());break;
			case Error:;break;
			case Running:;break;
			case Report:;break;
		}
		
		
	}
	
	
	public synchronized boolean isfree(){
		if(runMap.size() <capacity){
			return true;
		}
		return false;
	}
	
	public void loseSlave(){
		for(Map.Entry<String,JobFeature> entry:runMap.entrySet()){
			entry.getValue().getListen().listenReport(new JobReportEntry(entry.getValue().getJob(),JobReportType.Lose,"失去Slave连接:"+session.getRemoteAddress()));
		}
	}


	public synchronized JobFeature endJob(IJob job) {
		logger.info("SlaveID:"+slaveId+"$"+session.getRemoteAddress().toString()+"中的Job:"+job.getJobId()+" 结束");
		JobFeature j = runMap.remove(job.getJobId());
		if(j == null){
			logger.info("SlaveID:"+slaveId+"$"+session.getRemoteAddress().toString()+"中的Job:"+job.getJobId()+" 找不到 无法删除");
		}
		master.lookme();
		return j;
	}
	
	
	public boolean cleanSlaveClassCache(){
		
		String uuid = UUID.randomUUID().toString();
//		ArrayBlockingQueue<ResponsePacket> queue = new ArrayBlockingQueue<ResponsePacket>(1);
//		
//		responses.put(uuid, queue);
		
		RequestPacket request = new RequestPacket(uuid,"",PacketType.CleanCache);
		WriteFuture wf = session.write(request);
		
//		try {
//			ResponsePacket packet = queue.poll(1, TimeUnit.MINUTES);
//			if(packet != null){
//				return (Boolean)packet.getEntry();
//			}
//			
//		} catch (InterruptedException e) {
//		}
		
		return true;
	}
	
	public String toString(){
		return "SlaveID:"+slaveId+"$"+session.getRemoteAddress().toString();
	}
	public Map<String, JobFeature> getRunMap() {
		return runMap;
	}


	

}
