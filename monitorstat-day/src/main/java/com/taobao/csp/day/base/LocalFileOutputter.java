package com.taobao.csp.day.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

public class LocalFileOutputter {
	
	private static Logger logger = Logger.getLogger(LocalFileOutputter.class);
	
	private static volatile String fileName = null;
	
	public static void registerFileName(String fName) {
		logger.info("registerFileName:" + fName);
		fileName = fName;
	}

	public synchronized static void output(String content) {
		if (fileName == null) {
			logger.warn("file name must not null!!!");
			return;
		}
		
		String path = "/home/youji.zj/test/" + fileName;
		File file = new File(path);
		
		OutputStream outputStream = null;
		BufferedWriter writer = null;
		try {
			outputStream = new FileOutputStream(file, true);
			writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			writer.write(content);
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		
		
	}
	
	public static void main(String [] args) throws Exception {
		File file = new File("d:\\aaa\\a.txt");
		OutputStream outputStream = new FileOutputStream(file, true);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		writer.write("dsadadada\n");
		writer.write("xxxxxxxxxxx\n");
		writer.close();
		
	}

}
