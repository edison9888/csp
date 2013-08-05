
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.notify;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.BufferedReader2;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 * SendMessageCount_RA_S（可靠异步成功）   
SendMessageCount_RA_F（可靠异步失败）   
SendMessageCount_S（同步成功）   
SendMessageCount_WS（同步发送等待超过200ms成功）   
SendMessageCount_F（同步发送失败）   
SendMessageCount_RE（同步发送结果一次）   
SendMessageCount_NC（同步发送没有连接）   
SendMessageCount_Timeout（同步发送超时） 
 *
 * 下午4:04:03
 */
public class NotifyConsumerLogJob extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(NotifyConsumerLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public NotifyConsumerLogJob(String appName,String ip,String f){
		super( appName,ip, f);
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
	
	public void analyseOneLine(String line){
		analyseOneLine(line, '\01');
	}
	
	
	public void analyseOneLine(String line,char split) {
		
		if(line.startsWith("SendMessageCount")){
			String[] logResult = StringUtils.splitPreserveAllTokens(line, split);
			String groupId = logResult[1];
			String type = logResult[2];
			String rt = logResult[3];
			String pv = logResult[4];
			String time = logResult[5].substring(0, 16);
			
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
		
		Map<Long,NotifyEntry> allMap = new HashMap<Long, NotifyEntry>();
		
		Map<Long,Map<String,NotifyEntry>> allGroupMap = new HashMap<Long, Map<String,NotifyEntry>>();
		
		
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
					
					//总量
					NotifyEntry all = allMap.get(time);
					if(all == null){
						all = new NotifyEntry();
						allMap.put(time, all);
					}
					
					all.ra_s +=value.ra_s;
					all.ra_s_rt +=value.ra_s_rt;
					all.ra_f+=value.ra_f;
					all.s+=value.s;
					all.s_rt+=value.s_rt;
					all.s_f+=value.s_f;
					all.ws+=value.ws;
					all.timeout+=value.timeout;
					all.re+=value.re;
					all.nc+=value.nc;
					//组总量
					Map<String,NotifyEntry> g = allGroupMap.get(time);
					if(g == null){
						g = new HashMap<String, NotifyEntry>();
						allGroupMap.put(time, g);
					}
					
					NotifyEntry gall = g.get(groupName);
					if(gall == null){
						gall = new NotifyEntry();
						g.put(groupName, gall);
					}
					gall.ra_s +=value.ra_s;
					gall.ra_s_rt +=value.ra_s_rt;
					gall.ra_f+=value.ra_f;
					gall.s+=value.s;
					gall.s_rt+=value.s_rt;
					gall.s_f+=value.s_f;
					gall.ws+=value.ws;
					gall.timeout+=value.timeout;
					gall.re+=value.re;
					gall.nc+=value.nc;
					fillvalue(value,keylist,valuelist,oplist);
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.NOTIFY_CONSUMER,groupName,typeName}, 
								new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.ALL}, keylist.toArray(new String[]{}), valuelist.toArray(),oplist.toArray(new ValueOperate[]{}));
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
					
				}
			}}catch (Exception e) {
				logger.error("分析notify 出错",e);
			}
			
		}
		
		
		for(Map.Entry<Long,NotifyEntry> entry: allMap.entrySet()){
			long time = entry.getKey();
			NotifyEntry value = entry.getValue();
			List<String> keylist = new ArrayList<String>();
			List<Object> valuelist = new ArrayList<Object>();
			List<ValueOperate> oplist = new ArrayList<ValueOperate>();
			fillvalue(value,keylist,valuelist,oplist);
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.NOTIFY_CONSUMER}, 
						new KeyScope[]{KeyScope.ALL}, keylist.toArray(new String[]{}), valuelist.toArray(),oplist.toArray(new ValueOperate[]{}));
			} catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
		
		
		for(Map.Entry<Long,Map<String,NotifyEntry>> entry: allGroupMap.entrySet()){
			long time = entry.getKey();
			for(Map.Entry<String,NotifyEntry> groupEntry:entry.getValue().entrySet()){
				String groupName = groupEntry.getKey();
				NotifyEntry value = groupEntry.getValue();
				List<String> keylist = new ArrayList<String>();
				List<Object> valuelist = new ArrayList<Object>();
				List<ValueOperate> oplist = new ArrayList<ValueOperate>();
				fillvalue(value,keylist,valuelist,oplist);
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.NOTIFY_CONSUMER,groupName}, 
							new KeyScope[]{KeyScope.NO,KeyScope.ALL}, keylist.toArray(new String[]{}), valuelist.toArray(),oplist.toArray(new ValueOperate[]{}));
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
				
			}
		}
		
		
	}
	
	
	private void fillvalue(NotifyEntry value,List<String> keylist,List<Object> valuelist,List<ValueOperate> oplist ){
		
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
		
	}
	
	
	
	@Override
	public void release() {
		timeConsumerMap.clear();
	}
	
	
	
	
	public static void main(String[] args){
		
		try {
			
			Map<String,HostPo> map = CspCacheTBHostInfos.get().getHostInfoMapByOpsName("tripagent");
			for(Map.Entry<String, HostPo> entry:map.entrySet()){
				
				NotifyConsumerLogJob job = new NotifyConsumerLogJob("tripagent",entry.getKey(),"");
				
				BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\monitor.log"), '\02');
				String line = null;
				while((line=reader.readLine())!=null){
					job.analyseOneLine(line);
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