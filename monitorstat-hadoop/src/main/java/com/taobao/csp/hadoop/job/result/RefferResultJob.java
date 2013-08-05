package com.taobao.csp.hadoop.job.result;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.taobao.csp.hadoop.util.LocalUtil;

public class RefferResultJob extends Configured implements Tool {

public static class Map extends MapReduceBase implements Mapper<Text, LongWritable, Text, LongWritable>{
		
		public void map(Text key, LongWritable value, OutputCollector<Text, LongWritable> output, Reporter reporter)
				throws IOException {
				output.collect(new Text(key), new LongWritable(value.get()));
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
		
		int exitCode = ToolRunner.run(new RefferResultJob(), args);
		System.exit(exitCode);
		
	}

	@Override
	public int run(String [] args) {
		if (args.length != 2) {
			System.err.println("need input path argument and output path argument.");
			return 1;
		}
		
		JobConf conf = new JobConf(getConf(), RefferResultJob.class);
		
		conf.setJobName("csp-access-log-result-job");
		
		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(LongWritable.class);

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(LongWritable.class);

		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
		conf.setNumReduceTasks(1);
		
		conf.setInputFormat(TextInputFormat.class); 
		conf.setOutputFormat(TextOutputFormat.class);

		try {
			FileSystem fstm = FileSystem.get(conf);
			Path outDir = new Path(args[1]);
			fstm.delete(outDir, true);

			List<Path> inpaths = new LinkedList<Path>();
			Path inputPath = new Path(args[0]);
			LocalUtil.getAllFiles(conf, inputPath, inpaths); // ÃÓ≥‰ ‰»ÎŒƒº˛
			
			FileInputFormat.setInputPaths(conf, inpaths.toArray(new Path[0]));
			FileOutputFormat.setOutputPath(conf, new Path(args[1]));
			JobClient.runJob(conf);
		} catch (Exception e) {
			System.err.print("job run error:\n" + LocalUtil.getThrowableTrace(e));
		}
		
		return 0;
	}

}
