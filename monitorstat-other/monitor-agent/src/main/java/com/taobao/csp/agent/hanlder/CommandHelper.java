package com.taobao.csp.agent.hanlder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CommandHelper {

	
	

	/**
	 * 返回数据 格式为
	 * 
	 * TYPE##MESSAGE
	 * 
	 * TYPE:ERROR,SUCCESS
	 * 
	 * @param command
	 * @return
	 */
	public static String execute(String type,String command) {
		ProcessBuilder builder = null;
		if("c".equals(type)){
			 builder = new ProcessBuilder(new String[]{"/bin/bash","-c",command});
		}else{
			builder = new ProcessBuilder(new String[]{"/bin/bash",command});
		}
		StringBuffer msg = new StringBuffer();
		InputStream in = null;
		InputStream error = null;
		Process process = null;
		try {
			//builder.redirectErrorStream(true);
			process = builder.start();
			in = process.getInputStream();
			
			error = process.getErrorStream();
			
			CountDownLatch latch = new CountDownLatch(2);
			
			ThreadStream thread = new ThreadStream(latch,in);
			thread.start();
			ThreadStream errorthread = new ThreadStream(latch,error);
			errorthread.start();
			try {
				latch.await(10, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
			}
			
			try{
				if(process.exitValue() <0){
					throw new Exception("启动进程返回-1");
				}
			}catch (Exception e) {
			}
			
			
			if(thread.isEnd()){
				msg.append("SUCCESS").append("##").append(thread.sb.toString());
			}else{
				if(thread.isAlive()){
					thread.interrupt();
				}
				if(errorthread.isAlive()){
					errorthread.interrupt();
				}
				msg.append("ERROR").append("##").append("指令执行失败或"+errorthread.sb.toString());
			}
			
		} catch (Throwable e) {
			msg.append("ERROR").append("##").append("指令执行失败! 可能原因为:"+e.getMessage());
			e.printStackTrace();
		}finally{
			if(in !=null){
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if(error !=null){
				try {
					error.close();
				} catch (IOException e) {
				}
			}
			
			if(process!=null){
				process.destroy();
			}
		}
		return msg.toString();
	}
	
	
	public static  class ThreadStream extends Thread{
		private CountDownLatch count =null;
		private InputStream is;
		private StringBuffer sb = new StringBuffer();
		private boolean end;
		
		public boolean isEnd(){
			return end;
		}
		public ThreadStream(CountDownLatch count,InputStream is){
			this.count = count;
			this.is = is;
		}
		public void run(){
			String line = null;
			BufferedReader inBr = new BufferedReader(new InputStreamReader(is));
			
			try {
				while (null != (line = inBr.readLine())) {
					if (!"".equals(line)) {
						sb.append(line).append("\n");
					}
					if (line.indexOf("Log directory is:") > -1)
						break;
				}
			} catch (IOException e) {
			}
			end = true;
			count.countDown();
		}
	}

}
