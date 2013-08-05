
package com.taobao.monitor.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


/**
 * 
 * @author xiaodu
 * @version 2010-4-2 ����08:11:11
 */
public class UtilShell {
	private static final Logger logger =  Logger.getLogger(UtilShell.class);
	
	static{
		
		File file = new File("cut.sh");
		if(file.exists()){
			file.delete();
		}
		//file.setExecutable(true);	
		
		try {
			file.createNewFile();
			Writer writer = new FileWriter(file);
			writer.write("eval $1");
			writer.close();
		} catch (IOException e) {
			System.out.println("�޷����� cut shell �ű�����ϵͳ�˳�");
			logger.error("�޷����� cut shell �ű�����ϵͳ�˳�", e);
			System.exit(2);
		}
		//if(!file.canExecute()){
			System.out.println("cut.sh ����ִ��,����chmod +x");
			Runtime runtime = Runtime.getRuntime();
			try {
				Process proc = runtime.exec(new String[]{"chmod","+x","./cut.sh"});
				proc.waitFor();
			} catch (IOException e) {
				System.out.println("����cut.sh �޷����� X Ȩ�ޣ�ϵͳ�˳�");
				e.printStackTrace();
				logger.error("����cut.sh �޷����� X Ȩ�ޣ�ϵͳ�˳�", e);
				
				System.exit(2);
			} catch (InterruptedException e) {
				System.out.println("����cut.sh �޷����� X Ȩ�ޣ�ϵͳ�˳�");
				e.printStackTrace();
				logger.error("����cut.sh �޷����� X Ȩ�ޣ�ϵͳ�˳�", e);
				System.exit(2);
			}			
			System.out.println("cut.sh ���� X Ȩ��");
		//}
		
		
	}
	
	
	public static boolean comand(String strings){
		
		logger.info("ִ��shell:"+strings);
		
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec(new String[]{"./cut.sh",strings});			
			final InputStream in = proc.getInputStream();
			
			new Thread(){

				
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					try {
						while(reader.readLine()!=null){
							
						}
					} catch (IOException e) {
						logger.error("ִ��cur.sh", e);
					}finally{
						try {
							reader.close();
						} catch (IOException e) {
							logger.error("ִ��cur.sh", e);
						}
					}
				}
				
			}.start();
			
			final InputStream error = proc.getErrorStream();
			
			
			new Thread(){

				
				public void run() {
					BufferedReader reader = new BufferedReader(new InputStreamReader(error));
					try {
						String str = null;
						while((str=reader.readLine())!=null){
							logger.warn(str);
						}
					} catch (IOException e) {
						logger.error("ִ��cur.sh", e);
					}finally{
						try {
							reader.close();
						} catch (IOException e) {
							logger.error("ִ��cur.sh", e);
						}
					}
				}
				
			}.start();
			
			return proc.waitFor()==0;			
			
		} catch (IOException e) {
			logger.error("ִ��cur.sh", e);
		} catch (InterruptedException e) {
			logger.error("ִ��cur.sh", e);
		}
		return false;
	}
	
	
	
	public static String comandReturnResult(String strings){
		
		logger.info("ִ��shell:"+strings);
		
		final StringBuffer sb = new StringBuffer();
		
		final CountDownLatch latch = new CountDownLatch(2);
		
		
		Runtime runtime = Runtime.getRuntime();
		try {
			Process proc = runtime.exec(new String[]{"./cut.sh",strings});			
			final InputStream in = proc.getInputStream();
			
			new Thread(){

				
				public void run() {
					BufferedReader reader =null;
					try {
						reader = new BufferedReader(new InputStreamReader(in));
						String str = null;
						while((str=reader.readLine())!=null){
							sb.append(str);
							sb.append("\n");
						}
					} catch (IOException e) {
						logger.error("ִ��cur.sh", e);
					}finally{
						try {
							reader.close();
						} catch (IOException e) {
							logger.error("ִ��cur.sh", e);
						}
						latch.countDown();
					}
					
				}
				
			}.start();
			
			final InputStream error = proc.getErrorStream();
			
			
			new Thread(){
				
				public void run() {
					BufferedReader reader = null;
					try {
						reader = new BufferedReader(new InputStreamReader(error));
						String str = null;
						while((str=reader.readLine())!=null){
							//sb.append(str);
							//sb.append("\n");
							logger.warn(str);
						}
					} catch (IOException e) {
						logger.error("ִ��cur.sh", e);
					}finally{
						try {
							reader.close();
						} catch (IOException e) {
							logger.error("ִ��cur.sh", e);
						}
						latch.countDown();
					}
				}
				
			}.start();
			
			int result = proc.waitFor();
			latch.await(1, TimeUnit.HOURS);
			if(result==0){
				return sb.toString();
			}else{
				return null;
			}
			
		} catch (IOException e) {
			logger.error("ִ��cur.sh", e);
		} catch (InterruptedException e) {
			logger.error("ִ��cur.sh", e);
		}
		return null;
	}
	
	
	
	
	public static void main(String[] args){
//		if(UtilShell.comand("grep 2010-04-05T58:21 /home/xiaodu/logs//LOG_* >> /home/xiaodu/logs//TEP_LOG")){
//			System.out.println("UtilShell");
//		}
		
		
		System.out.println(comandReturnResult("ping www.baidu.com"));
		
		
	}

}
