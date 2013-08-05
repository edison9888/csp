package com.taobao.csp.hadoop.job.accesslog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
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
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import com.taobao.csp.hadoop.biz.PvLogFormatCache;
import com.taobao.csp.hadoop.biz.PvLogFormatEnum;
import com.taobao.csp.hadoop.biz.UrlPatternMatcher;
import com.taobao.csp.hadoop.biz.UrlPatternMatcherImpl;
import com.taobao.csp.hadoop.util.LocalUtil;

public class AccessRefferJob2 extends Configured implements Tool {
	
	private static Logger logger = Logger.getLogger(AccessRefferJob2.class);
	
	private static String spliter = "###";
	
	private static final List<String>  attentionApps = new ArrayList<String>();
	
	static {
		attentionApps.add("tf_buy");
		attentionApps.add("cart");
		attentionApps.add("tf_tm");
		attentionApps.add("detail");
//		attentionApps.add("detailskip");
		attentionApps.add("hesper");
		attentionApps.add("mercury");
		attentionApps.add("shopsystem");
//		attentionApps.add("delivery");
		
		attentionApps.add("tmallbuy");
		attentionApps.add("tmallcart");
		attentionApps.add("malldetail");
		attentionApps.add("malldetailskip");
//		attentionApps.add("malllist");
	}
	
	
	public static class Map extends MapReduceBase implements Mapper<Text, Text, Text, LongWritable>{
		
		private final UrlPatternMatcher urlMatcher = new UrlPatternMatcherImpl().fromProperties("urlPattern");
		
		public void map(Text key, Text value, OutputCollector<Text, LongWritable> output, Reporter reporter)
				throws IOException {
			String appHost = key.toString();
			String line = value.toString();
			
			try {
				String appName = appHost.split(":")[0];
				
				if (!attentionApps.contains(appName)) {
					return;
				}
				
				PvLogFormatEnum pvLogFormatType = PvLogFormatCache.getPvLogFormat(appName, line);
				
				String requestUrl = "";
				String httpCode = " ";
				String sourseUrl = "";
				if (pvLogFormatType == PvLogFormatEnum.GALAXY) {
					String[] tokens = line.split("\\|\\|");
					int i = tokens.length;
					if (i == 22 || i == 19 || i == 25) {
						requestUrl = LocalUtil.cleanUrl(tokens[4].split(" ")[1]);
						httpCode = tokens[5];
						sourseUrl = LocalUtil.cleanUrl(tokens[7]);
						
					} else {
						return;
					}
				} else if (pvLogFormatType == PvLogFormatEnum.LOGIN) {
					String[] tokens = line.split("\"");
					if (tokens.length >= 4) {
						String[] info = tokens[0].split(" ");
						requestUrl = LocalUtil.cleanUrl(tokens[1].split(" ")[1]);
						httpCode = info[2];
						sourseUrl = LocalUtil.cleanUrl(tokens[3]);
					} else if(tokens.length == 3) {
						String[] info = tokens[0].split(" ");
						requestUrl = LocalUtil.cleanUrl(tokens[1].split(" ")[1]);
						httpCode = info[2];
						sourseUrl = "null";
					} else {
						return;
					}
				} else {
					String[] tokens = line.split("\"");
					requestUrl = LocalUtil.cleanUrl(tokens[1].split(" ")[1]);
					httpCode = tokens[2].trim().split(" ")[0];
					sourseUrl = LocalUtil.cleanUrl(tokens[3]);
				}
				
				// 如果状态码不为3个数字，则剔除
				if (httpCode.length() != 3 || !StringUtils.isNumeric(httpCode)) {
					return;
				}
				
				if (!isAvailableUrl(sourseUrl) || !isAvailableUrl(requestUrl)) {
					return;
				}
				
				Text mainKey = new Text(appName  + spliter + urlMatcher.getMatchedUrl(requestUrl) + spliter + urlMatcher.getMatchedUrl(sourseUrl));
				output.collect(mainKey, new LongWritable(1));
			} catch (Exception e) {
				logger.warn(line, e);
			}
		}
		
		private boolean isAvailableUrl(String url) {
			boolean isAvailable = true;
			
			if (url == null || url.length() < 15) {
				isAvailable = false;
			}
			
			return isAvailable;
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, LongWritable, Text, LongWritable>  {
		public void reduce(Text key, Iterator<LongWritable> values, OutputCollector<Text, LongWritable> output, Reporter reporter)
				throws IOException {
			String rKey = key.toString();
			
			long count = 0;
			while (values.hasNext()) {
				LongWritable value = values.next();
				count += value.get();
			}
			
			output.collect(new Text(rKey), new LongWritable(count));
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		int exitCode = ToolRunner.run(new AccessRefferJob2(), args);
		System.exit(exitCode);
		
	}

	@Override
	public int run(String [] args) {
		if (args.length != 2) {
			System.err.println("need input path argument and output path argument.");
			return 1;
		}
		
		JobConf conf = new JobConf(getConf(), AccessRefferJob2.class);
		
		conf.setJobName("csp-access-log-job");
		
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(LongWritable.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(LongWritable.class);

		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
		conf.setNumReduceTasks(50);
		
		conf.setInputFormat(SequenceFileInputFormat.class); 
		conf.setOutputFormat(TextOutputFormat.class);

		try {
			FileSystem fstm = FileSystem.get(conf);
			Path outDir = new Path(args[1]);
			fstm.delete(outDir, true);

			List<Path> inpaths = new LinkedList<Path>();
			Path inputPath = new Path(args[0]);
			LocalUtil.getAllFiles(conf, inputPath, inpaths); // 填充输入文件
			
			FileInputFormat.setInputPaths(conf, inpaths.toArray(new Path[0]));
			FileOutputFormat.setOutputPath(conf, new Path(args[1]));
			JobClient.runJob(conf);
		} catch (Exception e) {
			System.err.print("job run error:\n" + LocalUtil.getThrowableTrace(e));
		}
		
		return 0;
	}
}

