package com.taobao.csp.hadoop.job.gclog;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

public class ReadGcResult {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String uri = args[0];  
		Configuration conf = new Configuration();  
		FileSystem fs = FileSystem.get(URI.create(uri), conf);  
		Path path = new Path(uri);  

		SequenceFile.Reader reader = null;  
		try {  
			reader = new SequenceFile.Reader(fs, path, conf);//���� SequenceFile.Reader ����  
			Writable key = (Writable)  
					ReflectionUtils.newInstance(reader.getKeyClass(), conf);//getKeyClass()���Sequence��ʹ�õ�����  
			Writable value = (Writable)//ReflectionUtils.newInstace �õ������ļ�ֵ��ʵ��  
					ReflectionUtils.newInstance(reader.getValueClass(), conf);//ͬ��  
			long position = reader.getPosition();  
			while (reader.next(key, value)) { //next��������������ȡ��¼ ֱ�����귵��false  
				String syncSeen = reader.syncSeen() ? "*" : "";//�滻�����ַ� ͬ��  
				String rowKey = key.toString();
				// json
//				System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key, value);  
				position = reader.getPosition(); // beginning of next record  
			}  
			System.out.println("over*******");
		} finally {  
			IOUtils.closeStream(reader);  
		}  
	}
}
