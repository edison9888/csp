//
//package com.taobao.monitor.stat.analyse;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.monitor.stat.config.HostInfo;
//import com.taobao.monitor.stat.content.ReportContent;
//import com.taobao.monitor.stat.content.ReportContentInterface;
//import com.taobao.monitor.common.util.Constants;
//import com.taobao.monitor.common.util.UtilShell;
//import com.taobao.monitor.stat.util.HostContents;
//
///**
// * 对应用的所有Ip 进行ping ，取得平均时间 
// * @author xiaodu
// * @version 2010-4-8 上午11:36:47
// */
//public class PingAnalyse extends Analyse {
//	
//	private static final Logger logger =  Logger.getLogger(PingAnalyse.class);
//	
//	
//	public PingAnalyse(String appName) {
//		super(appName);
//	}
//
//	public void analyseLogFile(ReportContentInterface content) {
//				
//		Map<String,List<HostInfo>> hostMap = HostContents.get().getHostMap(this.getAppName());
//		
//		if(hostMap==null){
//			logger.warn(this.getAppName()+"无法取得机器列表");
//			return ;
//		}
//		
//		logger.info("开始 ping analyse:"+this.getAppName()+"");
//		
//		Iterator<Map.Entry<String,List<HostInfo>>> it = hostMap.entrySet().iterator();
//		
//		while(it.hasNext()){
//			Map.Entry<String,List<HostInfo>> entry = it.next();
//			
//			String hostSize = entry.getKey();
//			
//			logger.info("开始 ping analyse:"+this.getAppName()+" 机房:"+hostSize+" 机器数:"+entry.getValue().size());
//			
//			List<HostInfo> hostList = entry.getValue();
//			
//			List<PingInfo> pingInfoList = new ArrayList<PingInfo>();			
//			for(HostInfo host:hostList){
//				String ip = host.getHostIp();
//				PingInfo info = getPingIpTime(ip);
//				pingInfoList.add(info);
//			}
//			
//			
//			String key1 = "PING_"+hostSize+"_responetime_"+Constants.AVERAGE_MACHINE_FLAG;
//			String key2 = "PING_"+hostSize+"_packetLoss_"+Constants.AVERAGE_MACHINE_FLAG;//表示ping 失败
//			String key3 = "PING_"+hostSize+"_packetReceive_"+Constants.AVERAGE_MACHINE_FLAG;
//			float count=0;
//			int size = 0;
//			
//			int receiveCount = 0;
//			int transmittedCount = 0;
//			
//			for(PingInfo info:pingInfoList){				
//				if(info.getReceived()>0){
//					for(Float time:info.getPingTimeList()){
//						count=count+time;
//						size++;
//					}				
//					receiveCount+=info.getReceived();
//					transmittedCount+=info.getTransmitted();
//				}else{
//					content.putReportDataByCount(this.getAppName(), key2,info.getIp(), this.getCollectDate());//表示ping 失败
//				}
//							
//				
//			}
//			
//			if(size>0){
//				float aveTime = count/size;				
//				content.putReportDataByCount(this.getAppName(), key1, aveTime+"", this.getCollectDate());
//			}
//			
//			if(transmittedCount>0){				
//				float r= Float.parseFloat(receiveCount+"");
//				float t= Float.parseFloat(receiveCount+"");
//				float ave = (r/t)*100;
//				content.putReportDataByCount(this.getAppName(), key3, ave+"", this.getCollectDate());				
//			}
//			
//			
//		}
//
//	}
//	
//	/**
//	 * 
//	 * 
//	 * 
//	 * PING 172.19.201.2 (172.19.201.2) 56(84) bytes of data.
//		64 bytes from 172.19.201.2: icmp_seq=0 ttl=63 time=0.166 ms
//		64 bytes from 172.19.201.2: icmp_seq=1 ttl=63 time=0.136 ms
//		64 bytes from 172.19.201.2: icmp_seq=2 ttl=63 time=0.136 ms
//		64 bytes from 172.19.201.2: icmp_seq=3 ttl=63 time=0.132 ms
//		64 bytes from 172.19.201.2: icmp_seq=4 ttl=63 time=0.131 ms
//		
//		--- 172.19.201.2 ping statistics ---
//		5 packets transmitted, 5 received, 0% packet loss, time 4000ms
//		rtt min/avg/max/mdev = 0.131/0.140/0.166/0.015 ms, pipe 2
//	 * 
//	 * 
//	 * 
//	 * 
//	 * 
//	 * 
//	 * 
//	 * PING 172.19.201.44 (172.19.201.44) 56(84) bytes of data.
//
//		--- 172.19.201.44 ping statistics ---
//		5 packets transmitted, 0 received, 100% packet loss, time 4000ms
//	 * @param ip
//	 * @return -1 表示ping 不tong
//	 */
//	private PingInfo getPingIpTime(String ip){
//		
//		logger.info("ping ip:"+ip);
//		
//		String shell = "ping -c 5 "+ip;
//		
//		String result = UtilShell.comandReturnResult(shell);
//		
//		
//		Pattern pattern = Pattern.compile("time=([\\d\\.]+) ms");
//		Matcher matcher = pattern.matcher(result);
//		List<Float> pingTimeList = new ArrayList<Float>();
//		while(matcher.find()){
//			String time = matcher.group(1);
//			try{
//				float timeFloat = Float.parseFloat(time);
//				pingTimeList.add((timeFloat*1000));
//			}catch(Exception e){
//				
//			}
//		}
//		
//		
//		Pattern pattern1 = Pattern.compile(" ([\\d]{1}) received");
//		Matcher matcher1 = pattern1.matcher(result);
//		String received=null;
//		while(matcher1.find()){
//			received = matcher1.group(1);
//		}
//		
//		
//		PingInfo info = new PingInfo();
//		info.setIp(ip);
//		info.setPingTimeList(pingTimeList);
//		if(received!=null){
//			try{
//				info.setReceived(Integer.parseInt(received));
//			}catch(Exception e){
//				
//			}
//		}
//		logger.info("ping ip: Transmitted:5,Received:"+info.getReceived());
//		info.setTransmitted(5);
//		return info;
//	}
//	
//	private class PingInfo{
//		
//		private String ip;
//		private List<Float> pingTimeList;
//		private int received=0;
//		private int transmitted=5;
//		public String getIp() {
//			return ip;
//		}
//		public void setIp(String ip) {
//			this.ip = ip;
//		}
//		public List<Float> getPingTimeList() {
//			return pingTimeList;
//		}
//		public void setPingTimeList(List<Float> pingTimeList) {
//			this.pingTimeList = pingTimeList;
//		}
//		public int getReceived() {
//			return received;
//		}
//		public void setReceived(int received) {
//			this.received = received;
//		}
//		public int getTransmitted() {
//			return transmitted;
//		}
//		public void setTransmitted(int transmitted) {
//			this.transmitted = transmitted;
//		}
//		
//		
//		
//	}
//
//	@Override
//	protected void insertToDb(ReportContentInterface content) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//
//}
