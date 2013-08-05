package com.taobao.csp.hadoop.result;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.taobao.csp.hadoop.util.LocalUtil;

public class ExtractReferTask {

	public final static Map<String, Long> combineResultM = new HashMap<String, Long>();
	
	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 1) {
			System.err.println("args error ....");
			return;
		}
		Path reduceResultD = new Path(args[0]);
		
		String hadoopHome = System.getenv("HADOOP_HOME");
		
		// Conf object will read the HDFS configuration parameters from these XML
	    // files. You may specify the parameters for your own if you want.
		Configuration conf = new Configuration();
	    conf.addResource(new Path(hadoopHome + "/conf/hadoop-site.xml"));
	    
	    FileSystem fileSystem = FileSystem.get(conf);
	    
	    List<Path> pathL = new ArrayList<Path>();
	    LocalUtil.getAllFiles(conf, reduceResultD, pathL);
	    
	    for (Path path : pathL) {
	    	BufferedReader br=new BufferedReader(new InputStreamReader(fileSystem.open(path)));
			try {
			  String line;
			  line=br.readLine();
			  while (line != null){
			    line = br.readLine();
			    
			    try {
			    	String [] kv = line.split("\\t");
				    String key = kv[0];
				    long value = Long.parseLong(kv[1]);
				   
				    if (combineResultM.containsKey(key)) {
				    	 long oldValue = combineResultM.get(key);
				    	 combineResultM.put(key, oldValue + value);
				    } else {
				    	combineResultM.put(key, value);
				    }
			    } catch (Exception e) {
					System.err.println(e);
				}
			  }
			} finally {
				// you should close out the BufferedReader
				br.close();
			}
	    }
	    
	    
	    standardOutput(1000);
	}
	
	public static void standardOutput(int limit) {
		
		for (Map.Entry<String, Long> entry : combineResultM.entrySet()) {
			String key = entry.getKey();
			Long value = entry.getValue();
			
			if (value >= limit) {
				System.out.println(key + "\t" + value);
			}
		}
	}
	
	public static void dbOutput(int limit) {
		
		for (Map.Entry<String, Long> entry : combineResultM.entrySet()) {
			String key = entry.getKey();
			Long value = entry.getValue();
			
			System.out.println(key + "\t" + value);
		}
	}
}
