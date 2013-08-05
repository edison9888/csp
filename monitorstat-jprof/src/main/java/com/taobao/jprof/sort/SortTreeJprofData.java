
package com.taobao.jprof.sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.taobao.jprof.Arith;
import com.taobao.jprof.ThreadProfilerData;


/**
 * 
 * @author xiaodu
 * @version 2010-6-28 上午11:47:09
 */
public class SortTreeJprofData {
	
	
	private class SortData {		
		private SortData previous;
		private Map<String, SortData> nextMap = new HashMap<String, SortData>();
		private String classMethodName;
		private long allSum;
		private float max;
		private float min;
		private long size = 0;
		private long threadid=0;
		private int stackIndex = 0;
		
		public double getAverage(){
			if(size!=0){
				return Arith.div(allSum, size*1000000);
			}else{
				return -1;
			}
		}

			
	}
	
	private Map<String,SortData> cacheHeadMap = new HashMap<String,SortData>();
	private Map<String,SortData> cacheMethodMap = new HashMap<String,SortData>();
	
	private String outPath;
	private String inPath;
	
	
	public SortTreeJprofData(String inPath,String outPath){
		this.inPath = inPath;
		this.outPath = outPath;
		reader();
	}
	public SortTreeJprofData(ThreadProfilerData threadProfilerData ){
		merge(threadProfilerData);
	}
	
	public void printResult(){	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outPath));
			for(Map.Entry<String, SortData> entry:cacheHeadMap.entrySet()){				
				StringBuilder sb = new StringBuilder();				
				SortData head = entry.getValue();
				digui(head,sb,0);
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
				if(first){
					merageFrist(data[0],Long.parseLong(data[1]),Integer.parseInt(data[3]),Long.parseLong(data[2]));
					first = false;
				}else{
					merge(data[0],Long.parseLong(data[1]),Integer.parseInt(data[3]),Long.parseLong(data[2]));
				}
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return threadProfilerData;
	}
	
	
	private void digui(SortData  next,StringBuilder sb,int index){		
		for(int i=0;i<index;i++){
			sb.append("-");
		}
		index++;
		sb.append(next.classMethodName)				   			
		   .append('\t')
		   .append(next.getAverage())
		    .append('\t')
		   .append(next.max)	
		   .append('\t')
		   .append(next.min)		
		   .append("\n");
		if(next.nextMap.size()>0){
			for(Map.Entry<String, SortData> nextEntry:next.nextMap.entrySet()){
				digui(nextEntry.getValue(),sb,index);				
			}
		}
	}
	int currentStackNum = 0;
	SortData currentSortData = null;
	long currentThreadId = 0;
	
	private void merageFrist(String classMethodName,long useTime,int stacknum,long threadid){
		currentThreadId = threadid;
		currentStackNum = stacknum;
		SortData head = new SortData();
		head.classMethodName = classMethodName;
		head.allSum = useTime;
		head.max = useTime;
		head.min = useTime;
		head.size++;
		head.stackIndex = stacknum;
		head.threadid = threadid;
		currentSortData = head;
		cacheHeadMap.put(classMethodName, head);
		cacheMethodMap.put(classMethodName+":"+stacknum, head);		
	}
	
	private void merge(String classMethodName,long useTime,int stacknum,long threadid){
		SortData data = cacheMethodMap.get(classMethodName+":"+stacknum);
		if(data!=null){
			data.allSum += useTime;
			if(useTime>data.max)
				data.max = useTime;
			if(data.min>useTime)
				data.min = useTime;
			data.size++;
			currentSortData = data;
			currentStackNum = stacknum;
			currentThreadId = threadid;
		}else{
			if(currentThreadId == threadid){				
				if(stacknum-currentStackNum==1){//表示为下一层									
					if(currentSortData!=null){						
						SortData next = currentSortData.nextMap.get(classMethodName);
						if(next==null){
							next = new SortData();
							next.classMethodName = classMethodName;
							next.allSum = useTime;
							next.max = useTime;
							next.min = useTime;
							next.stackIndex = stacknum;
							next.threadid = threadid;
							next.size++;							
							next.previous = currentSortData;							
							currentSortData.nextMap.put(classMethodName, next);
							cacheMethodMap.put(classMethodName+":"+stacknum, next);
						}else{
							next.allSum += useTime;
							if(useTime>next.max)
								next.max = useTime;
							if(next.min>useTime)
								next.min = useTime;
							next.size++;
						}
						currentStackNum = stacknum;	
						currentSortData = next;
					}
				}else if(stacknum-currentStackNum==0){//表示同层						
					if(currentSortData.classMethodName.equals(classMethodName)){//同层一样
						currentSortData.allSum += useTime;
						if(useTime>currentSortData.max)
							currentSortData.max = useTime;
						if(currentSortData.min>useTime)
							currentSortData.min = useTime;
						currentSortData.size++;
					}else{
						//不一样
						if(currentSortData.previous==null){
							SortData head = new SortData();
							head.classMethodName = classMethodName;
							head.allSum = useTime;
							head.max = useTime;
							head.min = useTime;
							head.size++;
							head.stackIndex = stacknum;
							head.threadid = threadid;
							currentSortData = head;
							cacheHeadMap.put(classMethodName, head);
							cacheMethodMap.put(classMethodName+":"+stacknum, head);	
						}else{
							SortData next = new SortData();
							next.classMethodName = classMethodName;
							next.allSum = useTime;
							next.max = useTime;
							next.min = useTime;
							next.stackIndex = stacknum;
							next.threadid = threadid;
							next.size++;							
							next.previous = currentSortData.previous;							
							currentSortData.previous.nextMap.put(classMethodName, next);
							currentSortData = next;
						}
					}
				}else if(stacknum-currentStackNum<0){//表示需要回归
					for(int i=currentStackNum - stacknum;i>=0;i--){
						if(currentSortData!=null){
							currentSortData = currentSortData.previous;
						}
					}
					if(currentSortData!=null){//表示回归找到自己的stack index						
						SortData next = currentSortData.nextMap.get(classMethodName);
						if(next==null){
							next = new SortData();
							next.classMethodName = classMethodName;
							next.allSum = useTime;
							next.max = useTime;
							next.min = useTime;
							next.stackIndex = stacknum;
							next.threadid = threadid;
							next.size++;							
							next.previous = currentSortData;							
							currentSortData.nextMap.put(classMethodName, next);
							cacheMethodMap.put(classMethodName+":"+stacknum, next);
						}else{
							next.allSum += useTime;
							if(useTime>next.max)
								next.max = useTime;
							if(next.min>useTime)
								next.min = useTime;
							next.size++;
						}
						currentStackNum = stacknum;	
						currentSortData = next;						
						
					}else{//表示没有找到，重新做为一个head
						SortData head = new SortData();
						head.classMethodName = classMethodName;
						head.allSum = useTime;
						head.max = useTime;
						head.min = useTime;
						head.size++;
						head.stackIndex = stacknum;
						head.threadid = threadid;
						currentSortData = head;
						currentStackNum = stacknum;
						cacheHeadMap.put(classMethodName, head);
						cacheMethodMap.put(classMethodName+":"+stacknum, head);	
					}
				}
			}else{
				SortData head = new SortData();
				head.classMethodName = classMethodName;
				head.allSum = useTime;
				head.max = useTime;
				head.min = useTime;
				head.size++;
				head.stackIndex = stacknum;
				head.threadid = threadid;
				currentSortData = head;
				currentStackNum = stacknum;
				cacheHeadMap.put(classMethodName, head);
				cacheMethodMap.put(classMethodName+":"+stacknum, head);	
			}
			
			
			
			
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
		SortTreeJprofData data = new SortTreeJprofData(inpath,outPath);
		data.printResult();
		
	}
	

}
