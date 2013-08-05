
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

import com.taobao.jprof.Arith;
import com.taobao.jprof.ThreadProfilerData;






/**
 * 不管stack 关系 直接按照名称归类
 * @author xiaodu
 * @version 2010-6-28 上午11:47:09
 */
public class SortMethodJprofData {
	

	
	private class SortData implements Comparable<SortData>{	
		public float max;
		public float min;
		public long size = 0;
		public String name;		
		public Stack<Long> valueStack = new Stack<Long>();		
		public long getValue(){
			long tmp=0;
			for(Long v:valueStack){
				tmp+=v;
			}
			return tmp;
		}
		
		public int compareTo(SortData o) {		
			
			if(this.getValue()>o.getValue()){				
				return -1;
			}else{
				return 1;
			}	
		}		
	}
	
	private Map<String,SortData> cacheMethodMap = new HashMap<String,SortData>();
	
	private String outPath;
	private String inPath;
	
	
	public SortMethodJprofData(String inPath,String outPath){
		this.inPath = inPath;
		this.outPath = outPath;
		reader();
	}
	public SortMethodJprofData(ThreadProfilerData threadProfilerData ){
		merge(threadProfilerData);
	}
	
	public void printResult(){	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outPath));
			
			List<SortData> list = new ArrayList<SortData>();
			list.addAll(cacheMethodMap.values());
			Collections.sort(list);
			
			for(SortData data:list){				
				StringBuilder sb = new StringBuilder();
				
				int v_0to0 = 0;
				Double v_0to0_all = 0d;
				
				int v_0to2 = 0;
				Double v_0to2_all = 0d;
				
				int v_2to4 = 0;
				Double v_2to4_all = 0d;
				
				int v_4to10 = 0;
				Double v_4to10_all = 0d;
				
				int v_10to20 = 0;
				Double v_10to20_all = 0d;
				
				int v_20to30 = 0;
				Double v_20to30_all = 0d;
				
				int v_30to40 = 0;
				Double v_30to40_all = 0d;
				
				int v_40up = 0;
				Double v_40up_all = 0d;
				
				
				Double all=0d;
				Stack<Long>  vStack = data.valueStack;
				for(Long v:vStack){
					all = Arith.add(all, v);
					if(v==0){
						v_0to0++;
						v_0to0_all = Arith.add(v_0to0_all, v);
					}else if(v<2000000){
						v_0to2++;
						v_0to2_all = Arith.add(v_0to2_all, v);
					}else if(v<4000000){
						v_2to4++;
						v_2to4_all = Arith.add(v_2to4_all, v);
					}else if(v<10000000){
						v_4to10++;
						v_4to10_all = Arith.add(v_4to10_all, v);
					}else if(v<20000000){
						v_10to20++;
						v_10to20_all = Arith.add(v_10to20_all, v);
					}else if(v<30000000){
						v_20to30++;
						v_20to30_all = Arith.add(v_20to30_all, v);
					}else if(v<40000000){
						v_30to40++;
						v_30to40_all = Arith.add(v_30to40_all, v);
					}else{
						v_40up++;
						v_40up_all = Arith.add(v_40up_all, v);
					}
				}
					
					all = Arith.div(all, 1000000,2);
					v_0to0_all = Arith.div(v_0to0_all, 1000000,4);
					v_0to2_all = Arith.div(v_0to2_all, 1000000,4);
					v_2to4_all = Arith.div(v_2to4_all, 1000000,4);
					v_4to10_all = Arith.div(v_4to10_all,  1000000,4);
					v_10to20_all = Arith.div(v_10to20_all,  1000000,4);
					v_20to30_all = Arith.div(v_20to30_all,  1000000,4);
					v_30to40_all = Arith.div(v_30to40_all, 1000000,4);
					v_40up_all = Arith.div(v_40up_all,  1000000,4);
				
				sb.append(data.name)				   			
				   .append('\t')
				   .append(Arith.div(all,data.size,2))
				   .append('\t')
				   .append(data.size)
				   .append('\t')
				   .append(v_0to0).append("(").append(0).append(")").append("(").append(Arith.div(v_0to0, data.size,2)*100).append("%)")
				   .append('\t')
				   .append(v_0to2).append("(").append(v_0to2>0?(Arith.div(v_0to2_all,v_0to2,2)):0).append(")").append("(").append(Arith.div(v_0to2, data.size,2)).append(")")
				   .append('\t')
				   .append(v_2to4).append("(").append(v_2to4>0?(Arith.div(v_2to4_all,v_2to4,2)):0).append(")").append("(").append(Arith.div(v_2to4, data.size,2)).append(")")
				   .append('\t')
				   .append(v_4to10).append("(").append(v_4to10>0?(Arith.div(v_4to10_all,v_4to10,2)):0).append(")").append("(").append(Arith.div(v_4to10, data.size,2)).append(")")
				   .append('\t')
				   .append(v_10to20).append("(").append(v_10to20>0?(Arith.div(v_10to20_all,v_10to20,2)):0).append(")").append("(").append(Arith.div(v_10to20, data.size,2)).append(")")
				   .append('\t')
				   .append(v_20to30).append("(").append(v_20to30>0?(Arith.div(v_20to30_all,v_20to30,2)):0).append(")").append("(").append(Arith.div(v_20to30, data.size,2)).append(")")
				   .append('\t')
				   .append(v_30to40).append("(").append(v_30to40>0?(Arith.div(v_30to40_all,v_30to40,2)):0).append(")").append("(").append(Arith.div(v_30to40, data.size,2)).append(")")
				   .append('\t')				   
				   .append(v_40up).append("(").append(v_40up>0?(Arith.div(v_40up_all,v_40up,2)):0).append(")").append("(").append(Arith.div(v_40up, data.size,2)).append(")")
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
					System.out.println(line);
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
	
	
	private void merge(String classMethodName,long useTime,int stacknum,long threadid){
		SortData data = cacheMethodMap.get(classMethodName);
		if(data==null){
			data = new SortData();
			data.name = classMethodName;
			data.max = useTime;
			data.min = useTime;
			data.size++;
			data.valueStack.add(useTime);
			cacheMethodMap.put(classMethodName, data);
		}else{
			data.valueStack.add(useTime);
			if(useTime>data.max)
				data.max = useTime;
			if(data.min>useTime)
				data.min = useTime;
			data.size++;
		}		
	}
	
	
	
	private void merge(ThreadProfilerData threadProfilerData ){
//		Stack<long[]> link = threadProfilerData.mProfileData;
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
		SortMethodJprofData data = new SortMethodJprofData(inpath,outPath);
		data.printResult();
		
	}
	
	

}
