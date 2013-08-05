package com.taobao.csp.hadoop.job.accesslog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import com.taobao.csp.hadoop.biz.PvLogFormatCache;
import com.taobao.csp.hadoop.biz.PvLogFormatEnum;
import com.taobao.csp.hadoop.util.LocalUtil;

public class AccessLogJob extends Configured implements Tool {
	
	private static Logger logger = Logger.getLogger(AccessLogJob.class);
	
	private static String spliter = "$$";
	
	
	public static class Map extends MapReduceBase implements Mapper<Text, Text, Text, Text>{
		
		private SimpleDateFormat sdfInfo = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm",Locale.ENGLISH);
		// 一般应用的正则匹配
		private Pattern pattern = Pattern.compile("(\\[\\d{2}\\/\\w{3}\\/\\d{4}:\\d{2}:\\d{2})");	
		
		private SimpleDateFormat wirelessSdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
		// 无线应用的正则匹配
		private Pattern wireleseePattern = Pattern.compile("(\\d{4}:\\d{2}:\\d{2}:\\d{2}:\\d{2})");
		
		private SimpleDateFormat sdfMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		public void map(Text key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String appHost = key.toString();
			String line = value.toString();
			
			try {
				String appName = appHost.split(":")[0];
				String hostIp = appHost.split(":")[1];
				
				PvLogFormatEnum pvLogFormatType = PvLogFormatCache.getPvLogFormat(appName, line);
				String collectTimeStr = parseLogLineCollectTime(line, pvLogFormatType);
				if (collectTimeStr == null) {
					return;
				}
				
				String useTimeStr = "";
				String requestUrl = "";
				String httpCode = " ";
				String sourseUrl = "";
				if (pvLogFormatType == PvLogFormatEnum.GALAXY) {
					String[] tokens = line.split("\\|\\|");
					int i = tokens.length;
					if (i == 22 || i == 19 || i == 25) {
						useTimeStr = tokens[1];
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
						useTimeStr = info[1];
						requestUrl = LocalUtil.cleanUrl(tokens[1].split(" ")[1]);
						httpCode = info[2];
						sourseUrl = LocalUtil.cleanUrl(tokens[3]);
					} else if(tokens.length == 3) {
						String[] info = tokens[0].split(" ");
						useTimeStr = info[1];
						requestUrl = LocalUtil.cleanUrl(tokens[1].split(" ")[1]);
						httpCode = info[2];
						sourseUrl = "null";
					} else {
						return;
					}
				} else {
					String[] tokens = line.split("\"");
					useTimeStr = tokens[0].split(" ")[1];
					requestUrl = LocalUtil.cleanUrl(tokens[1].split(" ")[1]);
					httpCode = tokens[2].trim().split(" ")[0];
					sourseUrl = LocalUtil.cleanUrl(tokens[3]);
				}
				
				// 如果状态码不为3个数字，则剔除
				if (httpCode.length() != 3 || !StringUtils.isNumeric(httpCode)) {
					httpCode = " ";
				}
				
				long useTime = 0L;
				// 如果存在小数点，，则将其转化为微秒单位
				try {
					if (!useTimeStr.equals("-")) {
						if (useTimeStr.indexOf(".") > -1) {
							useTime = (long)(Float.parseFloat(useTimeStr)*1000*1000);
						} else {
							useTime = Long.parseLong(useTimeStr);
						}
					}
				} catch (NumberFormatException e) {
					logger.error("日志装换错误，日志行：" + line, e);
					return;
				}
				
				// 主信息
				Text mainKey = new Text(AccessReduceTypeEnum.MAIN + spliter + appName + hostIp + spliter + collectTimeStr);
				Text mainValye = new Text(String.valueOf(useTime));
				output.collect(mainKey, mainValye);
				
				// httpcode信息
				Text codeKey = new Text(AccessReduceTypeEnum.CODE + spliter + appName + spliter + httpCode);
				Text codeValye = new Text("1");
				output.collect(codeKey, codeValye);
				
				// source url信息
				Text sourceKey = new Text(AccessReduceTypeEnum.SOURCE + spliter + appName + spliter + sourseUrl);
				Text sourceValye = new Text("1");
				output.collect(sourceKey, sourceValye);
				
				// request url信息
				Text requestKey = new Text(AccessReduceTypeEnum.REQUEST + spliter + appName + spliter + requestUrl);
				Text requestValye = new Text("1");
				output.collect(requestKey, requestValye);
				
			} catch (Exception e) {
				logger.warn("appName + line", e);
			}
		}
		
		private String parseLogLineCollectTime(String line, PvLogFormatEnum pvLogFormat) {
			try {
				Matcher m;
				if (pvLogFormat == PvLogFormatEnum.GALAXY) {
					m = wireleseePattern.matcher(line);
					if(m.find()){
						return sdfMin.format(wirelessSdf.parse(m.group(1)));
					} else {
						return null;
					}
				} else {
					m = pattern.matcher(line);
					if(m.find()){
						return sdfMin.format(sdfInfo.parse(m.group(1)));
					} else {
						return null;
					}
				}
			} catch (Exception e) {
				logger.error("分析日志行：" + line + " 的日期时发生异常", e);
				return null;
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text>  {
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String rKey = key.toString();
			String [] keyTokens = rKey.split("\\$\\$");
			String reduceType = keyTokens[0];
			
			// Main reduce
			if (AccessReduceTypeEnum.MAIN.toString().equals(reduceType)) {
				long useTime = 0l;
				long pv = 0l;
				while (values.hasNext()) {
					pv++;
					Text rValue = values.next();
					useTime += Long.parseLong(rValue.toString());
				}
				Text outKey = new Text(rKey);
				Text outValue = new Text(pv + spliter + useTime);
				output.collect(outKey, outValue);
			} else {
				long num = 0l;
				while (values.hasNext()) {
					values.next();
					num++;
				}
				Text outKey = new Text(rKey);
				Text outValue = new Text(String.valueOf(num));
				output.collect(outKey, outValue);
			} 
			
		}

	}
	
	public static void main(String[] args) throws Exception {
		
		int exitCode = ToolRunner.run(new AccessLogJob(), args);
		System.exit(exitCode);
		
	}

	@Override
	public int run(String [] args) {
		if (args.length != 2) {
			System.err.println("need input path argument and output path argument.");
			return 1;
		}
		
		JobConf conf = new JobConf(getConf(), AccessLogJob.class);
		
		conf.setJobName("csp-access-log-job");
		
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
		conf.setNumReduceTasks(10);
		
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

