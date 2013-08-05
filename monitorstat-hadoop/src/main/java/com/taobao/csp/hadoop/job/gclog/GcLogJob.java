package com.taobao.csp.hadoop.job.gclog;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import com.taobao.csp.hadoop.job.gclog.gcparser.GCDataStore;
import com.taobao.csp.hadoop.job.gclog.gcparser.GCMetric;
import com.taobao.csp.hadoop.job.gclog.gcparser.GCParserDriverHadoop;
import com.taobao.csp.hadoop.util.LocalUtil;

public class GcLogJob extends Configured implements Tool {
	private static Logger logger = Logger.getLogger(GcLogJob.class);
	private static String spliter = "$$";
	private static String spliterRegex = "\\$\\$";
	private static String YOUNG_GC = "Young GC";
	private static String INITIAL_MARK = "Initial Mark";
	private static String REMARK = "Remark";
	private static String FULLGC = "Full GC";
	private static String CONCURRENT_MARK = "Concurrent-mark";
	private static String PRECLEAN = "CMS-concurrent-preclean";
	private static String SWEEP = "CMS-concurrent-sweep";
	private static String RESET = "CMS-concurrent-reset";

	private static String YOUNG_GC_SIZE = "YoungSize";
	private static String YOUNG_GC_HEAP = "YoungHeapSize";
	private static String FULL_GC_SIZE = "FullSize";

	public static class Map extends MapReduceBase implements Mapper<Text, Text, Text, Text> {

		public void map(Text key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			try {
				String[] values = key.toString().split(":");
				String appName = values[0];
//				String ip = values[1];

				GCParserDriverHadoop driver = new GCParserDriverHadoop();
				driver.parseForOneLine(value.toString());
				GCDataStore gcData = (GCDataStore) driver.gc_stats();

				//日志实际时间
				String time = null;		//FIXME 实现待斟酌
				if(gcData.data(GCMetric.ygc_logtime) != null) {
					time = gcData.data(GCMetric.ygc_logtime).longValue() + "";
				} else if(gcData.data(GCMetric.fgc_logtime) != null) {
					time = gcData.data(GCMetric.fgc_logtime).longValue() + "";
				}
//				if(time != null) {
//					logger.info("time=" + time + ";value=" + value.toString());
//				} else {
//					logger.info("value.toString()=" + value.toString());	
//				}

				final String staticPreKey = combineKeyName(GCReduceTypeEnum.GC_COST_TIME.toString(), appName);
				/** gc pauses */
				Double ygData = gcData.data(GCMetric.ygc_time);
				writeGcTimeDataIntoFile(output, ygData, combineKeyName(staticPreKey,YOUNG_GC), time);

				Double imData = gcData.data(GCMetric.cms_im_time);
				writeGcTimeDataIntoFile(output, imData, combineKeyName(staticPreKey,INITIAL_MARK), null);

				Double rmData = gcData.data(GCMetric.cms_rm_time);
				writeGcTimeDataIntoFile(output, rmData, combineKeyName(staticPreKey,REMARK), null);

				Double fgData = gcData.data(GCMetric.fgc_time);
				writeGcTimeDataIntoFile(output, fgData, combineKeyName(staticPreKey,FULLGC), time);

				/** concurrent gc activity */
				/*cms concurrent mark*/
				Double cmData = gcData.data(GCMetric.cms_cm_a_time);
				writeGcTimeDataIntoFile(output, cmData, combineKeyName(staticPreKey,CONCURRENT_MARK), null);

				/*CMS-concurrent-preclean*/
				Double cpData = gcData.data(GCMetric.cms_cp_a_time);
				writeGcTimeDataIntoFile(output, cpData, combineKeyName(staticPreKey,PRECLEAN), null);

				/*CMS-concurrent-sweep*/
				Double csData = gcData.data(GCMetric.cms_cs_a_time);
				writeGcTimeDataIntoFile(output, csData, combineKeyName(staticPreKey,SWEEP), null);

				/*CMS-concurrent-reset*/
				Double crData = gcData.data(GCMetric.cms_cr_a_time);
				writeGcTimeDataIntoFile(output, crData, combineKeyName(staticPreKey,RESET), null);

				/** heap size */
				String preKeyYoung = combineKeyName(GCReduceTypeEnum.GC_MEMRORY.toString(), appName);
				String preKeyOld = combineKeyName(GCReduceTypeEnum.GC_MEMRORY.toString(), appName);
				String preKeyTotal = combineKeyName(GCReduceTypeEnum.GC_MEMRORY.toString(), appName);

				writeMemoryDataIntoFile(output, gcData.data(GCMetric.yg_used_beg), gcData.data(GCMetric.yg_used_end), combineKeyName(preKeyYoung,YOUNG_GC_SIZE), time);
				writeMemoryDataIntoFile(output, gcData.data(GCMetric.th_used_beg), gcData.data(GCMetric.th_used_end), combineKeyName(preKeyTotal,YOUNG_GC_HEAP), time);
				writeMemoryDataIntoFile(output, gcData.data(GCMetric.pg_used_beg), gcData.data(GCMetric.pg_used_end), combineKeyName(preKeyOld,FULL_GC_SIZE), time);

			} catch (Exception e) {
				logger.error("gclogjob map error,key=" + key.toString() + ";value=" + value.toString() + "**", e);
			}
		}

		//		private void saveHeapInfoToFile(OutputCollector<Text, Text> output, ArrayList<Double> beginList, ArrayList<Double> endList, String keyName) throws IOException {
		//			if(beginList.size() == endList.size() && beginList.size() != 0) {
		//				for(int i=0; i<beginList.size(); i++) {
		//					Text gcKey = new Text(keyName);
		//					Text gcValue = new Text(String.valueOf(beginList.get(i) - endList.get(i)));
		//					output.collect(gcKey, gcValue);
		//				}
		//			}

		private void writeMemoryDataIntoFile(OutputCollector<Text, Text> output, Double beginSize, Double endSize, String keyName, String time) throws Exception {
			if(beginSize != null && endSize != null) {
				final Text gcValue = new Text(String.valueOf(beginSize - endSize));
				Text gcKey = new Text(keyName);
				output.collect(gcKey, gcValue);

				if(time != null) {
					try {
						String timeKey = combineKeyName(GCReduceTypeEnum.TIMELINE.toString(), keyName, time);
						Text timeGcKey = new Text(timeKey);
						output.collect(timeGcKey, gcValue);
					} catch (Exception e) {
						logger.error("saveFirstDataIntoFile时间错误", e);
					}
				}
			}
		}

		private void writeGcTimeDataIntoFile(OutputCollector<Text, Text> output, Double data, final String keyName, String time) throws Exception {
			if(data != null) { 
				final Text gcValue = new Text("" + data.doubleValue());

				Text gcKey = new Text(keyName);
				output.collect(gcKey, gcValue);

				//修改key的前缀，记录次数
				String timesPreKey = replaceFirstKey(keyName,GCReduceTypeEnum.TIMES.toString());

				final Text timesValue = new Text("1");
				output.collect(new Text(timesPreKey), timesValue);

				if(time != null) {	//按分钟记录次数和具体的值
					try {
						String timesKey = combineKeyName(GCReduceTypeEnum.TIMELINE.toString(), timesPreKey, time);
						output.collect(new Text(timesKey), timesValue);

						String timeKey = combineKeyName(GCReduceTypeEnum.TIMELINE.toString(), keyName, time);
						Text timeGcKey = new Text(timeKey);
						output.collect(timeGcKey, gcValue);
					} catch (Exception e) {
						logger.error("saveFirstDataIntoFile时间错误", e);
					}
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>  {

		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String rKey = key.toString();
			try {
				String[] keyTokens = rKey.split(spliterRegex);	//3位
				String type = null;
				String collectType = null;

				if(keyTokens.length == 3) {
					//key=GC_COST_TIME$$detail$$Young GC
					type = keyTokens[0];
				} else if(keyTokens.length == 5 || keyTokens.length == 4) {
					//有时间和无时间两种
					//key=TIMELINE$$GC_COST_TIME$$detail$$Young GC$$1359688860000
					//key=TIMELINE$$GC_COST_TIME$$detail$$Young GC
					type = keyTokens[1];
					collectType = keyTokens[0];
				} 

				if(GCReduceTypeEnum.GC_COST_TIME.toString().equals(type)) {
					double durationSec = 0;
					long gcTimes = 0;
					while (values.hasNext()) {
						gcTimes++;
						Text rValue = values.next();
						durationSec += Double.parseDouble(rValue.toString());
					}
					final Text gcValue = new Text(durationSec + ";" + gcTimes);	//总时间，次数
					output.collect(key, gcValue);
				} else if(GCReduceTypeEnum.GC_MEMRORY.toString().equals(type)) {
					double durationSec = 0;
					long gcTimes = 0;
					while (values.hasNext()) {
						gcTimes++;
						Text rValue = values.next();
						durationSec += Double.parseDouble(rValue.toString());
					}
					final Text gcValue = new Text(durationSec + ";" + gcTimes + ";" + rKey.hashCode());	//总时间，次数
					output.collect(key, gcValue);
				} else if(GCReduceTypeEnum.TIMES.toString().equals(type)){
					long gcTimes = 0;
					while (values.hasNext()) {
						values.next();	
						gcTimes++;
					}
					output.collect(key, new Text(gcTimes + ""));
				}
			} catch (Exception e) {
				System.err.println("gclogjob reduce error,key=" + key.toString()  + "\n" + LocalUtil.getThrowableTrace(e));
			}
		}
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new GcLogJob(), args);
		System.exit(exitCode);
		//		double dd= 1;
		//		for(int i=0; i<Integer.MAX_VALUE; i++) {
		//			dd += Integer.MAX_VALUE;
		//			System.out.println(dd);
		//		}
		//		System.out.println(Double.parseDouble("-2.2957256393696777E11"));

		//		String rKey="TIMELINE$$GC_COST_TIME$$detail$$Young GC$$1359688860000";
		//		String rKey2="TIMELINE$$GC_COST_TIME$$detail$$Young GC";
		//		System.out.println(rKey.substring(0,rKey.lastIndexOf(spliter)));
		//		System.out.println(rKey2.substring(0,rKey2.lastIndexOf(spliter)));

		//		String[] keyTokens = rKey.split(spliterRegex);
		//		String activityName = keyTokens[1];
		//		String appName = keyTokens[0];
		//		System.out.println(activityName);
		//		System.out.println(appName);

		//		String preKey = combineKeyName(GCReduceTypeEnum.GCSIZE.toString(), "detail");
		//		System.out.println(preKey);
		//		System.out.println(combineKeyName(preKey,YOUNG_GC_SIZE));
	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("need input path argument and output path argument.");
			return 1;
		}

		System.err.println("run方法开始了...");
		JobConf conf = new JobConf(getConf(), GcLogJob.class);
		conf.setJobName("csp-gc-log-job");

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
		conf.setNumReduceTasks(20);

		conf.setInputFormat(SequenceFileInputFormat.class); 
		conf.setOutputFormat(SequenceFileOutputFormat.class);	// 原来是TextOutputFormat.class
		try {
			FileSystem fstm = FileSystem.get(conf);
			Path outDir = new Path(args[1]);
			if(!args[0].equals(args[1]))
				fstm.delete(outDir, true);

			List<Path> inpaths = new LinkedList<Path>();
			Path inputPath = new Path(args[0]);
			LocalUtil.getAllFiles(conf, inputPath, inpaths); // 填充输入文件
			for(Path path : inpaths) {
				System.err.println("文件名称:" + path.getName());
			}
			FileInputFormat.setInputPaths(conf, inpaths.toArray(new Path[0]));
			FileOutputFormat.setOutputPath(conf, new Path(args[1]));
			JobClient.runJob(conf);
		} catch (Exception e) {
			System.err.print("job run error:\n" + LocalUtil.getThrowableTrace(e));
		}
		return 0;
	}

	private static String combineKeyName(String... args) throws Exception {
		if(args == null || args.length == 0) {
			throw new Exception("args == null");
		}
		StringBuilder sb = new StringBuilder();
		for(String key : args) {
			sb.append(key).append(spliter);
		}
		return sb.substring(0, sb.lastIndexOf(spliter));
	}

	private static String replaceFirstKey(String originKey, String key2) throws Exception {
		if(originKey == null || key2 == null) {
			throw new Exception("key == null");
		}
		int index = originKey.indexOf(spliter);
		if(index<0) {
			return combineKeyName(key2, originKey);
		} else {
			return combineKeyName(key2, originKey.substring(index + 2));
		}
	}
}
