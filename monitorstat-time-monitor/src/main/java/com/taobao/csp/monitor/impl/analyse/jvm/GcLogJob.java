
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.jvm;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * @author xiaodu
 *
 * ÏÂÎç6:50:03
 */
public class GcLogJob extends AbstractDataAnalyse{
	
	private String smallgc = "[GC";
	private String fullgc = "[Full GC";
	private String cmsgc = "CMS-remark";
	
	private class GC{
		public int sgc;
		public int fgc;
		public int cgc;
		public float m;
		
	}
	
	
	public GcLogJob(String appName,String ip,String f){
		super(appName,ip, f);
	} 
	
	
	Map<String,GC> map = new HashMap<String,GC>();
	
	public void analyseOneLine(String line){
		
		String time = line.substring(0, 16);
		
		GC gc = map.get(time);
		if(gc == null){
			gc = new GC();
			map.put(time, gc);
		}
		
		int index = line.indexOf(smallgc);
		if(index >0){
			gc.sgc++;
			gc.m = analyseMemeroy(line);
			return ;
		}
		
		index = line.indexOf(cmsgc);
		if(index >0){
			gc.cgc++;
			
			return ;
		}
		index = line.indexOf(fullgc);
		if(index >0){
			gc.fgc++;
			
			return ;
		}
	}
	
	// 2012-05-16T09:51:19.011+0800: 518629.097: [GC 518629.097: [ParNew: 1368625K->3559K(1501888K), 0.0280510 secs] 1682311K->317272K(4057792K), 0.0283580 secs] [Times: user=0.09 sys=0.00, real=0.02 secs] 
	private float analyseMemeroy(String line){
		
		List<String> list = readerTimeSelectFileLine(line);
		for(String gc:list){
			if(gc.indexOf("GC")>-1){
				
				Pattern pattern = Pattern.compile("(.*[^\\d])(\\d+)K->(\\d+)K\\((\\d+)K\\)\\s*,\\s*([\\d\\.]+)\\s*(secs)");		
				Matcher matcher = pattern.matcher(gc);
				if(matcher.find()){								
					String name = matcher.group(1);				
					String old_value = matcher.group(2);
					String new_value = matcher.group(3);
					String cap = matcher.group(4);
					String time = matcher.group(5);
					
					BigDecimal b1 = new BigDecimal(new_value);
			        BigDecimal b2 = new BigDecimal(cap);
			        BigDecimal b3 = new BigDecimal(100);
					
					return b1.divide(b2,4,BigDecimal.ROUND_HALF_UP).multiply(b3).floatValue();
				}
			}
		}
		return -1;
	}
	
	private static List<String> readerTimeSelectFileLine(String line){		
		List<String> gcResultList = new ArrayList<String>();
		
		int len = line.length();
		char first = '[';
		char lase = ']';
		Stack<Character> stack = new Stack<Character>();
		for(int i=0;i<len;i++){
			if(line.charAt(i)==lase){
				String result = "";
				char _result = ' ';
				while((_result=stack.pop())!=first){
					result=_result+result;;
				}
				gcResultList.add(result);
			}else{			
				stack.add(line.charAt(i));
			}
			
		}	
		
		return gcResultList;		
	}
	
	
	public void submit(){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		for(Map.Entry<String,GC> entry:map.entrySet()){
			try{
				String time = entry.getKey();
				long t = sdf.parse(time).getTime();
				GC gc = entry.getValue();
				
				CollectDataUtilMulti.collect(getAppName(), getIp(), t, 
						new String[]{KeyConstants.JVMINFO}, new KeyScope[]{KeyScope.HOST}, new String[]{"JVMMEMORY","JVMGC","JVMFULLGC","JVMCMSGC"}, new Object[]{gc.m,gc.sgc,gc.fgc,gc.cgc},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				
				
			}catch (Exception e) {
			}
			
		}
	}
	@Override
	public void release() {
		map.clear();
	}
	
	public static void main(String[] args){
		
		
	try {
			
			
		GcLogJob job = new GcLogJob("itemcenter","123.123.13.13","");
			
			
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\gc.log"));
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