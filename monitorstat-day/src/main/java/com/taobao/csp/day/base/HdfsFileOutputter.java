package com.taobao.csp.day.base;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.log4j.Logger;

/***
 * write to hdfs
 * 
 * @time 2012-12-12
 * @author youji.zj
 *
 */
public class HdfsFileOutputter {
	
	private static Logger logger = Logger.getLogger(HdfsFileOutputter.class);
	
	private static volatile String spoutPrefix = "yj";
	
	private static final String directory = "/group/webserverlog/csp/";
	
	private static final CompressionCodec DEFAULT_CODEC = new DefaultCodec();
	
	private static final int WRITER_SYN_INTERVAL = 1000;
	
	private static volatile int writeNum = 0;
	
	private static volatile SequenceFile.Writer writer;
	
	private static volatile String timeIdentity;
	
	private static Object lockObject = new Object();
	
	public static synchronized void registerSpout(String spoutName) {
		logger.info("registerFileName:" + spoutName);
		String spoutSufix = "-" + spoutName;
		spoutPrefix = spoutPrefix + spoutSufix;
	}
	
	/***
	 * 
	 * @param lineKey  detail:127.0.0.1
	 * @param logType
	 * @param content
	 * @param line
	 */
	public static void output(String lineKey, YuntiLogType logType, String line) {
		
		synchronized (lockObject) {
			Calendar calendar = Calendar.getInstance();
			int minute = calendar.get(Calendar.MINUTE);
			calendar.set(Calendar.MINUTE, minute / 5 * 5);
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm");
			String time = sf.format(calendar.getTime());
			
			// 时间转动建新文件
			if (!time.equals(timeIdentity)) {
				timeIdentity = time;
				String pathS = getPath(time, logType);
				String hadoopHome = "/home/youji.zj/app/hadoop-0.19.1-201210-r207340";
				
				Configuration conf = new Configuration();
			    conf.addResource(new Path(hadoopHome + "/conf/hadoop-site.xml"));
				Path path = new Path(pathS);
				
				try {
					if (writer != null) {
						IOUtils.closeStream(writer);
					}
					
					writeNum = 0;
					FileSystem fileSystem = FileSystem.get(conf);
					writer = SequenceFile.createWriter(fileSystem, conf, path,
							Text.class, Text.class,
							SequenceFile.CompressionType.BLOCK, DEFAULT_CODEC);
				} catch (Exception e) {
					logger.error(e);
				}
			}

			if (writer == null) {
				logger.error("writer is null");
				return;
			}
			
			try {
				writeNum++;
				Text key = new Text();
				key.set(lineKey);
				Text value = new Text();
				value.set(line);
				writer.append(key, value);

				if (writeNum > WRITER_SYN_INTERVAL) {
					writer.sync();
					writeNum = 0;
				}

			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/***
	 * 
	 * @param time yyyyMMdd HH:mm
	 * @return
	 */
	private static String getPath(String time, YuntiLogType logType) {
		String path = null;
		try {
			String yyyyMMdd = time.split(" ")[0];
			String hourMinute = time.split(" ")[1];
			String hour = hourMinute.split(":")[0];
			String minute = hourMinute.split(":")[1];
			
			path = directory + logType + "/" + yyyyMMdd + "/" + hour + "/" + minute + "/" + spoutPrefix + "-" + UUID.randomUUID().toString() + ".log";
		}catch (Exception e) {
			logger.error(e);
			return null;
		}
		
		return path;
	}
	
	public static void main(String [] args) throws Exception {
		System.out.println(Integer.parseInt("02")  / 5 * 5 );
		System.out.println(YuntiLogType.webserver);
		
		Calendar calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		calendar.set(Calendar.MINUTE, minute / 5 * 5);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd HH:mm");
		String time = sf.format(calendar.getTime());
		
		System.out.println(getPath(time, YuntiLogType.webserver));
		
		output("127.0.0.1", YuntiLogType.webserver, "dsadada\ndsada");
	}

}
