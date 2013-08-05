
package com.taobao.monitor.web.jprof;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.ao.MonitorJprofAo;
import com.taobao.monitor.web.core.po.JprofClassMethodStack;
import com.taobao.monitor.web.core.po.JprofHost;
import com.taobao.monitor.web.util.Md5;

            
/**
 * 
 * @author xiaodu
 * @version 2010-8-12 ÏÂÎç03:19:54
 */
public class JprofStack {
	private static final Logger logger =  Logger.getLogger(JprofStack.class);
	Map<String,StringBuilder> stackMap = new HashMap<String,StringBuilder>();
	
	
	public void intoDb(String jprofilePath,JprofHost host,String collectDay){
		
		MonitorJprofAo.get().deleteJprofClassMethodStack(host.getAppName(), collectDay);
		parseStack(jprofilePath);
		
		logger.info(host.getAppName()+":"+jprofilePath+":"+collectDay+" classsize:"+stackMap.size());
		
		for(Map.Entry<String, StringBuilder> entry:stackMap.entrySet()){
			StringBuilder sb = entry.getValue();
			
			String md5 = Md5.getMD5(sb.toString().getBytes());
			
						
			String[] lines =  sb.toString().split("\n");
			String root = null;
			String parent = null;
			for(int i=0;i<lines.length;i++){
				String tmp = lines[i];
				String[] m = tmp.split("\t");
				String[] className = m[0].split(":");
				
				if(i == 0){					
					root = m[0];
					parent = m[0];
				}
				JprofClassMethodStack stack = new JprofClassMethodStack();
				stack.setClassName(className[0]);
				stack.setMethodName(className[1]);
				stack.setLineNum(Integer.parseInt(className[2]));
				stack.setParentClass(parent);
				stack.setMd5(md5);
				stack.setRootParentClass(root);	
				stack.setAppName(host.getAppName());
				stack.setCollectDay(collectDay);
				stack.setStackNum(Integer.parseInt(m[3]));
				if(i != 0){
					parent = m[0];
				}				
				MonitorJprofAo.get().addClassMethodStack(stack);
			}
			
			
		}
		
		
	}
	
	
	private void parseStack(String jprofilePath){
		long currentthreadId = -1;	
		long preStackNum = -1;
		boolean isFirst = true;
		
		StringBuilder sbKey = new StringBuilder();
		StringBuilder sbvalue = new StringBuilder();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(jprofilePath));
			String line = null;
			while((line=reader.readLine())!=null){
				if(line.startsWith("##")){
					line = line.substring(line.lastIndexOf("##"), line.length());
					line = line.substring(line.indexOf("\t")+1, line.length());
					if(line.equals("")){
						continue;
					}					
				}
				//System.out.println(line);
				if(line.indexOf("com/taobao/hesper/biz/core/dataobject/DisplayCategoryPropertyDO:getFeature:150")>-1){
					
					//System.out.println(line);
					
				}
				
				String tmp = line.replaceAll("-", "");
				String[] data = tmp.split("\t");
				if(data.length!=4){
					continue;
				}
				String classMethodName = data[0];
				long  threadid = Long.parseLong(data[2]);
				long  stacknum = Long.parseLong(data[3]);
				do{
					if(currentthreadId!=threadid){
						currentthreadId=threadid;
						isFirst = true;
					}else{					
						if(isFirst){						
							sbKey.append(classMethodName);
							sbvalue.append(tmp).append("\n");
							preStackNum = stacknum;
							isFirst = false;
						}else{
							if(stacknum-preStackNum == 1){
								sbKey.append(classMethodName);
								sbvalue.append(tmp).append("\n");
								preStackNum = stacknum;
								isFirst = false;
							}else{							
								StringBuilder v = stackMap.get(sbKey.toString());
								if(v==null){
									stackMap.put(sbKey.toString(), sbvalue);
								}
								sbKey = new StringBuilder();
								sbvalue = new StringBuilder();
								isFirst = true;
							}
						}
					}	
				}while(isFirst);
				
			}	
			
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		
		
		
	}
	
	
	
public static void main(String[] args){
		
		JprofStack scp = new JprofStack();
		
		JprofHost host = new JprofHost();
		host.setAppName("list");
		
		
		scp.intoDb("D:\\tmp\\jprof\\jprof.txt", host,"");
		
	}
	
	

}
