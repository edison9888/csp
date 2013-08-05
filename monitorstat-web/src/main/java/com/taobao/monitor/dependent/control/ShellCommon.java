
package com.taobao.monitor.dependent.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * 
 * @author xiaodu
 * @version 2011-4-29 上午11:41:44
 */
public class ShellCommon {
	

	private static final  String  endStr = "]$\40\33[m";
	
	
	private String hostname;
	private String username;
	private String password;
	
	private Connection conn = null;
	
	public ShellCommon(String ip,String username,String password) throws IOException{
		this.hostname = ip;
		this.username = username;
		this.password = password;
		connect();
	}
	
	
	private void connect() throws IOException{
		conn = new Connection(hostname);
		conn.connect(null,2000,2000);
		boolean res = conn.authenticateWithPassword(username, password);
		if(!res){
			throw new IOException("验证不通过!");
		}
	}
	
	public String doCommon(String shellCommon) throws IOException{
		
			
		Session sess = conn.openSession();
		int x_width = 90;
		int y_width = 30;

		sess.requestPTY("vt100", x_width, y_width, 0, 0, null);
		sess.startShell();
		
		OutputStream out = sess.getStdin();
		InputStream in = sess.getStdout();
		
		String result = read(in);//第一步 登录
		
		//System.out.println(result);
		out.write(shellCommon.getBytes());//第二步 输入指令
		out.write('\n');
		
		result = read(in);
	//	System.out.println(result);		
		if(result.indexOf("口令")>-1){
			out.write(password.getBytes());//第三步 输入指令
			out.write('\n');
			result = read(in);
		}
	//	System.out.println(result);
		sess.close();
		return result;
	}
	
	
	public String doCommons(String[] shellCommons) throws IOException{
		
		
		Session sess = conn.openSession();
		int x_width = 90;
		int y_width = 30;

		sess.requestPTY("vt100", x_width, y_width, 0, 0, null);
		sess.startShell();
		
		OutputStream out = sess.getStdin();
		InputStream in = sess.getStdout();
		
		String result = read(in);//第一步 登录
		
		for(String comm:shellCommons){
		
			//System.out.println(result);
			out.write(comm.getBytes());//第二步 输入指令
			out.write('\n');
			
			result = read(in,100);
		//	System.out.println(result);		
			if(result.indexOf("口令")>-1){
				out.write(password.getBytes());//第三步 输入指令
				out.write('\n');
				result = read(in,100);
			}
		}
	//	System.out.println(result);
		sess.close();
		return result;
	}
	
	
	
	public void close(){
		if(conn!=null){
			conn.close();
		}
	}
	
	private String read(InputStream in){
		return read(in,5*1000);
	}
	
	
	
	private String read(InputStream in,int waitTime){
		byte[] reader_buffer = new byte[2048];
		int write_pos = 0;
		byte[] buff = new byte[8192];
		StringBuffer sb = new StringBuffer();
		while(true){
			try {				
				int resultLen = in.available();
				if(resultLen <=0){
					try {
						Thread.sleep(waitTime+1);
					} catch (InterruptedException e) {
					}
//					resultLen = in.available();
//					if(resultLen <=0){
//						break;
//					}
				}

				int len = in.read(buff);
				if(len < 0 ){					
					continue;
				}
				
				int space_available = reader_buffer.length - write_pos; //剩余				
				if (space_available < len){
					int need_space = write_pos + len;
					byte[] new_buffer = reader_buffer;
					if (need_space > reader_buffer.length)
					{
						int inc = need_space / 3;
						inc = (inc < 256) ? 256 : inc;
						inc = (inc > 8192) ? 8192 : inc;
						new_buffer = new byte[need_space + inc];
					}
					
					if(write_pos != 0){//第一次读取
					}else{
						System.arraycopy(reader_buffer, 0, new_buffer, 0, write_pos);
					}
					reader_buffer = new_buffer;
				}
								
				System.arraycopy(buff, 0, reader_buffer, write_pos, len);
				write_pos += len;
				if(len < buff.length ){
					String str = new String(reader_buffer,0,write_pos);	
					sb.append(str);
					System.out.println(str);
					
					if(write_pos >=6){
						byte[] lastStrByte = new byte[6];
						System.arraycopy(reader_buffer, write_pos-6, lastStrByte, 0, 6);
						String lastStr = new String(lastStrByte);
						if(lastStr.equals(endStr)){
							break;
						}
						
					}
					
					
				}
				write_pos = 0;
				
			} catch (IOException e) {
			}
			
		}
		return sb.toString();
		
		
	}
	

}
