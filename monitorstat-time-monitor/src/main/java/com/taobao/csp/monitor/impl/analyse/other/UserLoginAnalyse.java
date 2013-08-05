
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.Arith;

/**
 * @author xiaodu
 * 
 * 分析loginAnalysis.log 日志
 * 
 * 0,218.205.229.145,,,trustLogin,,,trustLogin,https,,,,,,,,null,jiechunrongdz,XUserLoginSuccess,2012-06-28 14:41:07
 *
 * 下午2:42:39
 */
public class UserLoginAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(UserLoginAnalyse.class);

	/**
	 * @param appName
	 * @param ip
	 */
	public UserLoginAnalyse(String appName, String ip,String feature) {
		super(appName, ip,feature);
	}
	//map<time,map<登陆类型,int[成功，失败]>>
	private Map<String,Map<String,int[]>> timeMap = new HashMap<String, Map<String,int[]>>();

	@Override
	public void analyseOneLine(String line) {
		
		String[] tmp = StringUtils.splitPreserveAllTokens(line,",");
		
		if(tmp.length<18){
			return ;
		}
		
		String time = tmp[tmp.length-1];
		String logintype = tmp[7];
		String loginstat = tmp[tmp.length-2];
		
		String ftime = time.substring(0,16);
		
		Map<String,int[]> typeMap = timeMap.get(ftime);
		if(typeMap == null){
			typeMap = new HashMap<String, int[]>();
			timeMap.put(ftime, typeMap);
		}
		
		int[] stat = typeMap.get(logintype);
		if(stat == null){
			stat =new int[2];
			typeMap.put(logintype, stat);
		}
		
		if("XUserLoginSuccess".equals(loginstat)){
			stat[0]++;
		}else if("XUserLoginFail".equals(loginstat)){
			stat[1]++;
		}
		
	}

	@Override
	public void submit() {

		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		for(Map.Entry<String,Map<String,int[]>> entry:timeMap.entrySet()){
			String time = entry.getKey();
			
			for(Map.Entry<String,int[]> typeEntry:entry.getValue().entrySet()){
				String key = typeEntry.getKey();
				int[] c = typeEntry.getValue();
				
				float r = (float)Arith.div(c[1],c[0], 3);
				
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), sdf1.parse(time).getTime(), new String[]{"LoginType",key},
							new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"success","fail","rate"}, 
							new Object[]{c[0],c[1],r},new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD,ValueOperate.AVERAGE});
				}catch (Exception e) {
					logger.error("发送失败", e);
				}
				
				
			}
			
		}
		
	}
	
	
	public static void main(String[] args){
		UserLoginAnalyse a = new UserLoginAnalyse("","","");
		a.analyseOneLine("0,218.205.229.145,,,trustLogin,,,trustLogin,https,,,,,,,,null,jiechunrongdz,XUserLoginSuccess,2012-06-28 14:41:07");
		a.analyseOneLine("104602860,58.213.51.35,C59de8e7e0f9c25cf486d87d49939cc76,a9b68887c72bfb3f62203239a0a97967,Ctr,tb,,Ctr,https,,,,,http://www.taobao.com/,,,1C7508595D79,str196135,XUserLoginSuccess,2012-06-28 15:04:55");
		a.analyseOneLine("474687709,122.233.235.77,,8e3a728c913c8962d011e65c306a808d,loginByIm,AliIM,7.00.13T,loginByIm,https,,,,,,,,null,淘版屋:迎迎,XUserLoginSuccess,2012-06-28 15:04:55");
	}

	@Override
	public void release() {
		timeMap.clear();
	}

}
