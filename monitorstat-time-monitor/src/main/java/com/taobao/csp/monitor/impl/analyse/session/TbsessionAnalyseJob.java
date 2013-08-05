
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.session;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * @author xiaodu
 *
 * ����2:32:33
 */
public class TbsessionAnalyseJob extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(TbsessionAnalyseJob.class);
	
	private Map<Long,Map<String,Integer>> timeMap = new HashMap<Long, Map<String,Integer>>();
	
	private //��ʼ������key
	String[] initialKeyList = new String[]{"��tair����", "��tairʧ�ܴ���", "дtair����",
			"��ȡcookie����", "дcookie����", "��ȫǩ������", "��ȫǩ����ƥ�����",
			"��ȫǩ����ƥ�䣺cookie��ֵ��tairû��ֵ", "��ȫǩ����ƥ�䣺cookieû��ֵ��tair��ֵ",
			"�Զ��л���tair�л�cookie����ȡ����", "�ֶ��л�cookie��д��Ĵ���", "��tairʧ�ܼ������еĴ���"};

	/**
	 * @param appName
	 * @param ip
	 */
	public TbsessionAnalyseJob(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String line) {
		String[] messages = line.split(" ");
		String millSeconds = messages[2];
		Long time = (Long.parseLong(millSeconds));
		
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		time = cal.getTimeInMillis();
		
		Map<String,Integer> dataMap = timeMap.get(time);
		if(dataMap == null){
			dataMap = new HashMap<String, Integer>();
			timeMap.put(time, dataMap);
		}
		
		String info = messages[5];
		//��ȡ�ؼ���Ϣ

		String[]  _tmpLines = info.split("\\|");


		//ѭ���������йؼ���
		for ( int i = 0; i < _tmpLines.length - 1; i++) {

			String subString = _tmpLines[i];
			String[] subList = subString.split("\\=");

			String key = subList[0];
			if ("Rtr".equals(subList[0])) {
				key = "��tair����";
			} else if ("rtr/F".equals(subList[0]) ) {
				key = "��tairʧ�ܴ���";
			} else if ("Wtr".equals(subList[0])) {
				key = "дtair����";
			} else if ("Rcoo".equals(subList[0])) {
				key = "��ȡcookie����";
			} else if ("Wcoo".equals(subList[0])) {
				key = "дcookie����";
			} else if ("Sgn".equals(subList[0])  ) {
				key = "��ȫǩ������";
			} else if ("sgn/F".equals(subList[0])  ) {
				key = "��ȫǩ����ƥ�����";
			} else if ("sgn/FT".equals(subList[0]) ) {
				key = "��ȫǩ����ƥ�䣺cookie��ֵ��tairû��ֵ";
			} else if ("sgn/FC".equals(subList[0])  ) {
				key = "��ȫǩ����ƥ�䣺cookieû��ֵ��tair��ֵ";
			} else if ("tairTcookie".equals(subList[0]) ) {
				key = "�Զ��л���tair�л�cookie����ȡ����";
			} else if ("TairMCookie".equals(subList[0] )) {
				key = "�ֶ��л�cookie��д��Ĵ���";
			} else if ("errorCount".equals(subList[0]) ) {
				key = "��tairʧ�ܼ������еĴ���";
			}
			
			Integer data = dataMap.get(key);
			if(data == null){
				dataMap.put(key, Integer.parseInt(subList[1]));
			}else{
				dataMap.put(key, Integer.parseInt(subList[1])+data);
			}
		}
	}

	@Override
	public void submit() {
		for (Map.Entry<Long,Map<String,Integer>> entry : timeMap.entrySet()) {
			Long time = entry.getKey();
			Map<String, Integer> valueMap = entry.getValue();
			
			
			for(String key:initialKeyList){
				
				Integer data = valueMap.get(key);
				if(data == null){
					data =0;
				}
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.TB_SESSION,key},
							new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"E-times"}, 
							new Object[]{data},new ValueOperate[]{ValueOperate.ADD});
				} catch (Exception e) {
					logger.error("����ʧ��", e);
				}
				
			}
		}
	}

	@Override
	public void release() {
		
		timeMap.clear();
		
	}
	
	public static void main(String[] args) {
		try {
			
			TbsessionAnalyseJob job = new TbsessionAnalyseJob("itemcenter","172.24.12.99","");
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\monitorstat-time-monitor\\target\\session.log"));
			String line = null;
			while((line=reader.readLine())!=null){
				
				
				job.analyseOneLine(line);
				
			}
			
			job.submit();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
