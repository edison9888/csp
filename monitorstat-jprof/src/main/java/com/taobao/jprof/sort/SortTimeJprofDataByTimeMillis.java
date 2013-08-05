
package com.taobao.jprof.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.taobao.jprof.ThreadProfilerData;






/**
 * 
 * @author xiaodu
 * @version 2010-6-28 ����11:47:09
 */
public class SortTimeJprofDataByTimeMillis {
	
	private class MethodStack{
		private String methodName;
		private long useTime;
		private long threadId;
		private long stackNum;
		
		
		
		
	}
	

	
	
	private Map<String,TimeSortData> cacheMethodMap = new HashMap<String,TimeSortData>();
	
	
	public List<TimeSortData> getTimeSortData(){
		List<TimeSortData> list = new ArrayList<TimeSortData>();
		list.addAll(cacheMethodMap.values());
		Collections.sort(list);
		
		return list;
	}
	
	
	private String outPath;
	private String inPath;
	
	
	public SortTimeJprofDataByTimeMillis(String inPath,String outPath){
		this.inPath = inPath;
		this.outPath = outPath;
		reader();
	}
	public SortTimeJprofDataByTimeMillis(ThreadProfilerData threadProfilerData ){
		merge(threadProfilerData);
	}
	
	//0-10   10 -20  20-30 30-40 40
	public void printResult(){	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outPath));
			
			List<TimeSortData> list = new ArrayList<TimeSortData>();
			list.addAll(cacheMethodMap.values());
			Collections.sort(list);
			
			for(TimeSortData data:list){				
				StringBuilder sb = new StringBuilder();
				
				int v_0to0 = 0;
				long v_0to0_all = 0;
				
				int v_0to2 = 0;
				long v_0to2_all = 0;
				
				int v_2to4 = 0;
				long v_2to4_all = 0;
				
				int v_4to10 = 0;
				long v_4to10_all = 0;
				
				int v_10to20 = 0;
				long v_10to20_all = 0;
				
				int v_20to30 = 0;
				long v_20to30_all = 0;
				
				int v_30to40 = 0;
				long v_30to40_all = 0;
				
				int v_40up = 0;
				long v_40up_all = 0;
				
				
				long all=0;
				Stack<Long>  vStack = data.valueStack;
				for(Long v:vStack){
					all+=v;
					if(v==0){
						v_0to0++;
						v_0to0_all+=v;
					}else if(v<2){
						v_0to2++;
						v_0to2_all+=v;
					}else if(v<4){
						v_2to4++;
						v_2to4_all+=v;
					}else if(v<10){
						v_4to10++;
						v_4to10_all+=v;
					}else if(v<20){
						v_10to20++;
						v_10to20_all+=v;
					}else if(v<30){
						v_20to30++;
						v_20to30_all+=v;
					}else if(v<40){
						v_30to40++;
						v_30to40_all+=v;
					}else{
						v_40up++;
						v_40up_all+=v;
					}
				}
				
				sb.append(data.name)				   			
				   .append('\t')
				   .append(all/(float)data.size)
				   .append('\t')
				   .append(data.size)
				   .append('\t')
				   .append(v_0to0).append("(").append(v_0to0>0?(v_0to0_all/(float)v_0to0):0).append(")").append("(").append(v_0to0/(float)data.size).append(")")
				   .append('\t')
				   .append(v_0to2).append("(").append(v_0to2>0?(v_0to2_all/(float)v_0to2):0).append(")").append("(").append(v_0to2/(float)data.size).append(")")
				   .append('\t')
				   .append(v_2to4).append("(").append(v_2to4>0?(v_2to4_all/(float)v_2to4):0).append(")").append("(").append(v_2to4/(float)data.size).append(")")
				   .append('\t')
				   .append(v_4to10).append("(").append(v_4to10>0?(v_4to10_all/(float)v_4to10):0).append(")").append("(").append(v_4to10/(float)data.size).append(")")
				   .append('\t')
				   .append(v_10to20).append("(").append(v_10to20>0?(v_10to20_all/(float)v_10to20):0).append(")").append("(").append(v_10to20/(float)data.size).append(")")
				   .append('\t')
				   .append(v_20to30).append("(").append(v_20to30>0?(v_20to30_all/(float)v_20to30):0).append(")").append("(").append(v_20to30/(float)data.size).append(")")
				   .append('\t')
				   .append(v_30to40).append("(").append(v_30to40>0?(v_30to40_all/(float)v_30to40):0).append(")").append("(").append(v_30to40/(float)data.size).append(")")
				   .append('\t')				   
				   .append(v_40up).append("(").append(v_40up>0?(v_40up_all/(float)v_40up):0).append(")").append("(").append(v_40up/(float)data.size).append(")")
				   .append("\n");
				writer.write(sb.toString());
			}			
			writer.flush();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private ThreadProfilerData reader(){
		ThreadProfilerData threadProfilerData = new ThreadProfilerData();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inPath));
			String line = null;
			boolean first = true;
			while((line=reader.readLine())!=null){
				if(line.startsWith("##")){
					line = line.substring(line.lastIndexOf("##"), line.length());
					line = line.substring(line.indexOf("\t")+1, line.length());
					if(line.equals("")){
						continue;
					}					
				}
				String tmp = line.replaceAll("-", "");
				String[] data = tmp.split("\t");
				if(data.length!=4){
					//System.out.println(line);
					continue;
				}
				merge(data[0],Long.parseLong(data[1]),Integer.parseInt(data[3]),Long.parseLong(data[2]));
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return threadProfilerData;
	}
	
	List<MethodStack> threadList = new ArrayList<MethodStack>();
	long currentthreadId = -1;
	private void merge(String classMethodName,long useTime,int stacknum,long threadid){
		if(currentthreadId!=threadid){
			currentthreadId=threadid;
			doMerge();
		}else{
			MethodStack m = new MethodStack();
			m.methodName = classMethodName;
			m.useTime = useTime;
			m.threadId =threadid;
			m.stackNum = stacknum;
			threadList.add(m);
		}		
	}
	private void doMerge(){
		
		List<MethodStack> threadListTmp = new ArrayList<MethodStack>();
		
		HT:
		for(int i=0;i<threadList.size();i++){
//			
//			MethodStack m_tmp = new MethodStack();
//			m_tmp.methodName = 
			
			MethodStack m = threadList.get(i);
			long statck = m.stackNum;			
			for(int j=i+1;j<threadList.size();j++){				
				MethodStack tmp = threadList.get(j);
				long tmpStack = tmp.stackNum;
				if(tmpStack-statck==1){//��ʾΪ������һ��
					m.useTime-=tmp.useTime;
					
				}else if(tmpStack-statck<=0){
					continue HT;
				}
			}
		}
	for(int i=0;i<threadList.size();i++){
		MethodStack m = threadList.get(i);
		TimeSortData sortData = cacheMethodMap.get(m.methodName);
		if(sortData==null){
			sortData = new TimeSortData();			
			sortData.name = m.methodName;
			sortData.valueStack.add(m.useTime);	
			sortData.size++;
			cacheMethodMap.put(m.methodName, sortData);
		}else{
			sortData.valueStack.add(m.useTime);	
			sortData.size++;
		}
	}
	
	threadList.clear();
		
		
		
		
	}
	
	
	private void merge(ThreadProfilerData threadProfilerData ){
//		JprofStack<Long> link = threadProfilerData.mProfileData;
//		while(link.size()>0){
//			long[] data = link.pop();
//			long methodid = data[0];
//			long starttime = data[1];
//			long endtime = data[2];
//			long stacknum = data[3];			
//			long useTime = (endtime - starttime);			
//			String info = JProfMethodCache.getClasssMethodInfo((int)methodid);
//			merge(info,useTime,(int)stacknum,methodid);
//		}
	}
	
	
	public static void main(String[] args){
		
		if(args.length!=2){
			System.out.println("need inpath and outpath");
			System.exit(0);
		}		
		String inpath = args[0];
		String outPath = args[1];
		SortTimeJprofDataByTimeMillis data = new SortTimeJprofDataByTimeMillis(inpath,outPath);
		data.printResult();
		
	}
	

}
