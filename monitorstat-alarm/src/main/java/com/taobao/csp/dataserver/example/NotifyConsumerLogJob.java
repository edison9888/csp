
/**
 * monitorstat-monitor
 */
package com.taobao.csp.dataserver.example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.BufferedReader2;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 * SendMessageCount_RA_S���ɿ��첽�ɹ���   
SendMessageCount_RA_F���ɿ��첽ʧ�ܣ�   
SendMessageCount_S��ͬ���ɹ���   
SendMessageCount_WS��ͬ�����͵ȴ�����200ms�ɹ���   
SendMessageCount_F��ͬ������ʧ�ܣ�   
SendMessageCount_RE��ͬ�����ͽ��һ�Σ�   
SendMessageCount_NC��ͬ������û�����ӣ�   
SendMessageCount_Timeout��ͬ�����ͳ�ʱ�� 
 *
 * ����4:04:03
 */
public class NotifyConsumerLogJob {
	
	private static final Logger logger = Logger.getLogger(NotifyConsumerLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	private String appname = null;
	private String hostip = null;
	public NotifyConsumerLogJob(String appName,String ip){
		this.appname = appName;
		this.hostip = ip;
	} 
	
	Map<String,Map<String,Map<String,NotifyEntry>>> timeConsumerMap = new HashMap<String, Map<String,Map<String,NotifyEntry>>>();
	
	private class NotifyEntry{
		
		private int ra_s =0;
		private int ra_s_rt =0;
		private int ra_f=0;
		private int s=0;
		private int s_rt=0;
		private int s_f=0;
		private int ws=0;
		private int timeout=0;
		private int re=0;
		private int nc=0;
		
		
		
	}
	
	public void analyseLine(String line) throws ParseException{
		
		if(line.startsWith("SendMessageCount")){
			String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
			String groupId = logResult[1];
			String type = logResult[2];
			String rt = logResult[3];
			String pv = logResult[4];
//			String time = logResult[5].substring(0, 16);
			String time = rTimeFormat.format(new Date());
			
			Map<String,Map<String,NotifyEntry>> groupMap = timeConsumerMap.get(time);
			if(groupMap == null){
				groupMap = new HashMap<String, Map<String,NotifyEntry>>();
				timeConsumerMap.put(time, groupMap);
			}
			Map<String,NotifyEntry> typeMap = groupMap.get(groupId);
			if(typeMap == null){
				typeMap = new HashMap<String, NotifyEntry>();
				groupMap.put(groupId, typeMap);
			}
			
			NotifyEntry entry = typeMap.get(type);
			if(entry == null){
				entry = new NotifyEntry();
				typeMap.put(type, entry);
			}
			if("SendMessageCount_S".equals(logResult[0])){
				entry.s+=Integer.parseInt(pv);
				entry.s_rt+=Integer.parseInt(rt);
								
				return ;
			}
			if("SendMessageCount_WS".equals(logResult[0])){
				entry.ws+=Integer.parseInt(pv);
				return ;
			}
			if("SendMessageCount_F".equals(logResult[0])){
				entry.s_f+=Integer.parseInt(pv);
				return ;
			}
			if("SendMessageCount_RE".equals(logResult[0])){				
				entry.re+=Integer.parseInt(pv);
				return ;
			}
			if("SendMessageCount_NC".equals(logResult[0])){
				entry.nc+=Integer.parseInt(pv);
				return ;
			}
			if("SendMessageCount_Timeout".equals(logResult[0])){
				entry.timeout+=Integer.parseInt(pv);
				return ;
			}
			if("HSF-SendMessageCount_RA_S".equals(logResult[0])){
				entry.ra_s+=Integer.parseInt(pv);
				entry.ra_s_rt+=Integer.parseInt(rt);
				return ;
			}
			if("SendMessageCount_RA_F".equals(logResult[0])){
				entry.ra_f+=Integer.parseInt(pv);
				return ;
			}
		}
		// SendMessageCount_FNG-P-TC-TRADEPLUSTRADE-PLUS=>1400-attributes-changed112012-03-05 00:51:13tp183134.cm3
		
	}
	
	
	public void submit(){
		
		for(Map.Entry<String,Map<String,Map<String,NotifyEntry>>> entry:timeConsumerMap.entrySet()){
			try{
			long time = rTimeFormat.parse(entry.getKey()).getTime();
			Map<String,Map<String,NotifyEntry>> groupMap = entry.getValue();
			for(Map.Entry<String,Map<String,NotifyEntry>> group:groupMap.entrySet()){
				String groupName = group.getKey();
				Map<String,NotifyEntry> typeMap = group.getValue();
				for(Map.Entry<String,NotifyEntry> method:typeMap.entrySet()){
					String typeName = method.getKey();
					NotifyEntry value = method.getValue();
					
					List<String> keylist = new ArrayList<String>();
					List<Object> valuelist = new ArrayList<Object>();
					List<ValueOperate> oplist = new ArrayList<ValueOperate>();
					
					if(value.ra_s  >0){
						keylist.add("ra_s");
						valuelist.add(value.ra_s);
						oplist.add(ValueOperate.ADD);
					}
					if(value.ra_s_rt >0){
						keylist.add("ra_s_rt");
						valuelist.add(value.ra_s_rt/value.ra_s);
						oplist.add(ValueOperate.AVERAGE);
					}
					if(value.ra_f  >0){
						keylist.add("ra_f");
						valuelist.add(value.ra_f);
						oplist.add(ValueOperate.ADD);
					}
					if(value.s  >0){
						keylist.add("s");
						valuelist.add(value.s);
						oplist.add(ValueOperate.ADD);
					}
					if(value.s_rt  >0){
						keylist.add("s_rt");
						valuelist.add(value.s_rt/value.s);
						oplist.add(ValueOperate.AVERAGE);
					}					
					if(value.s_f >0){
						keylist.add("s_f");
						valuelist.add(value.s_f);
						oplist.add(ValueOperate.ADD);
					}
					if(value.ws  !=-1){
						keylist.add("ws");
						valuelist.add(value.ws);
						oplist.add(ValueOperate.ADD);
					}
					if(value.timeout >0){
						keylist.add("timeout");
						valuelist.add(value.timeout);
						oplist.add(ValueOperate.ADD);
					}
					if(value.re  >0){
						keylist.add("re");
						valuelist.add(value.re);
						oplist.add(ValueOperate.ADD);
					}
					if(value.nc  >0){
						keylist.add("nc");
						valuelist.add(value.nc);
						oplist.add(ValueOperate.ADD);
					}
					
					try {
						CollectDataUtilMulti.collect(appname, hostip, time, new String[]{KeyConstants.NOTIFY_CONSUMER,groupName,typeName}, 
								new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, keylist.toArray(new String[]{}), valuelist.toArray(),oplist.toArray(new ValueOperate[]{}));
					} catch (Exception e) {
						logger.error("����ʧ��", e);
					}
					
				}
			}}catch (Exception e) {
				logger.error("����notify ����",e);
			}
		}
		
		
	
		
		
		
	}
	
	
	
	
	
	public static void main(String[] args){
		
		try {
			
			Map<String,HostPo> map = CspCacheTBHostInfos.get().getHostInfoMapByOpsName("itemcenter");
			for(Map.Entry<String, HostPo> entry:map.entrySet()){
				
				NotifyConsumerLogJob job = new NotifyConsumerLogJob("itemcenter",entry.getKey());
				
				BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\monitor.log"), '\02');
				String line = null;
				while((line=reader.readLine())!=null){
					job.analyseLine(line);
				}
				job.submit();
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	

}
