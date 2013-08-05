
package com.taobao.csp.log.example;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.taobao.csp.log.MonitorLog;


public class Test {
	
	
//	HSF-Provider-Timeout=HSF-Provider-Timeout,com.taobao.item.service.ItemService:1.0.0,sellerSaveItem,,1,10361,2011-12-28 02:53:38,itemcenter172106.cm3
//	HSF-ConsumerDetail=HSF-ConsumerDetail,com.taobao.forest.service.RemoteWriteService:1.0.0,getValue,172.23.210.72,6,6,2011-12-28 00:01:37,itemcenter172106.cm3
//	SendMessageCount_F=SendMessageCount_F,P-IC-Item,ic-common=>NM-item-edit,596,1,2011-12-28 05:35:39,itemcenter172106.cm3
//	HSF-ProviderDetail=HSF-ProviderDetail,com.taobao.item.service.ItemService:1.0.0,sellerSaveItem,172.23.163.78,34,1641,2011-12-28 00:01:37,itemcenter172106.cm3
//	HSF-ProviderDetail-BizException=HSF-ProviderDetail-BizException,com.taobao.item.service.ItemService:1.0.0,sellerSaveItem,172.24.137.126,1,2075,2011-12-28 00:17:37,itemcenter172106.cm3
//	HSF-Consumer-HugeTimeout=HSF-Consumer-HugeTimeout,com.taobao.justice.hsf.IvmService:1.0.0,run,com.taobao.item.checkstep.StepManager@doPerform@82,1,1700,2011-12-28 02:29:38,itemcenter172106.cm3
//	threadPoolStats=threadPoolStats,reliableAsynSendMsgTP-1605718719,threadPoolStayInQueueTime,5679,1335,2011-12-28 00:01:37,itemcenter172106.cm3
//	TBRemoting=TBRemoting,ResponseTimeout,,,1,3008,2011-12-28 00:03:37,itemcenter172106.cm3
//	HSF-Consumer-Exception=HSF-Consumer-Exception,com.taobao.forest.service.RemoteWriteService:1.0.0,getValue,org.springframework.aop.support.AopUtils@invokeJoinpointUsingReflection@304,1,1564,2011-12-28 15:03:45,itemcenter172106.cm3
//	HSF-Consumer-Timeout=HSF-Consumer-Timeout,com.taobao.justice.hsf.IvmService:1.0.0,run,com.taobao.item.checkstep.StepManager@doPerform@82,1,158,2011-12-28 04:17:39,itemcenter172106.cm3
//	HSF-Consumer=HSF-Consumer,com.taobao.uic.common.service.userinfo.UicExtraReadService:1.0.0,getExtraUserByUserId,sun.reflect.GeneratedMethodAccessor289@invoke@-1,3,12,2011-12-28 00:01:37,itemcenter172106.cm3
//	SendMessageCount_RA_S=SendMessageCount_RA_S,P-IC-Item,ic-common=>NM_item_schedule,1,16,2011-12-28 00:01:37,itemcenter172106.cm3
//	HSF-Consumer-ActiveThread=HSF-Consumer-ActiveThread,JVM,MEMORY,SITUATION,4834.31,3269.81,2011-12-28 00:01:37,itemcenter172106.cm3
//	SendMessageCount_S=SendMessageCount_S,P-IC-Item,ic-common=>NM_item_changeQuantity,470,150,2011-12-28 00:01:37,itemcenter172106.cm3
//	AverageHttpPost_Check=AverageHttpPost_Check,level-2,level-3,52,8,2011-12-28 00:01:37,itemcenter172106.cm3
//	HSF-Provider-ActiveThread=HSF-Provider-ActiveThread,com.taobao.item.service.ItemService:1.0.0,sellerUpShelfItem,,1,1,2011-12-28 00:03:37,itemcenter172106.cm3
//	SendMessageCount_WS=SendMessageCount_WS,P-IC-Item,ic-common=>NM-item-add,202,1,2011-12-28 01:03:37,itemcenter172106.cm3

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Map<String,String> mapTitle = new HashMap<String, String>();
		try {
			MonitorLog.setWaitTime(11);
			MonitorLog.setCompress(true);
			while(true){
				BufferedReader2 reader = new BufferedReader2(new FileReader(new File("D:\\hsf.log.2011-12-28")),'\02');
				
				String line = null;
				long l = System.currentTimeMillis();
				int i=0;
				while((line =reader.readLine())!=null){
					String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
					String titleKey = logResult[0];
					i++;
					String title = mapTitle.get(titleKey);
					if(title == null){
						mapTitle.put(titleKey,StringUtils.replaceChars(line, '\01', ','));
					}
					if(line.startsWith("HSF-ConsumerDetail")){
						MonitorLog.addStat(new String[]{logResult[0],logResult[1],logResult[2],logResult[3]}, new Long[]{Long.parseLong(logResult[4]),Long.parseLong(logResult[5])});
					}
				
					if(line.startsWith("HSF-ProviderDetail")){
						MonitorLog.addStat(new String[]{logResult[0],logResult[1],logResult[2],logResult[3]}, new Long[]{Long.parseLong(logResult[4]),Long.parseLong(logResult[5])});
					}
				}
				
				Thread.sleep(1000);
			}
			
			
			
//			for(Map.Entry<String,String> entry:mapTitle.entrySet()){
//				System.out.println(entry.getKey()+"="+entry.getValue());
//			}
//			System.out.println(i+" ok "+(System.currentTimeMillis() - l));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
