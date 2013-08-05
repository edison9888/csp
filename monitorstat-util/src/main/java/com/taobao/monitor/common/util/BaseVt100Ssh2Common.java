
package com.taobao.monitor.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * 
 * @author xiaodu
 * @version 2011-5-30 上午09:38:43
 */
public abstract class BaseVt100Ssh2Common {
	
	private Logger log = Logger.getLogger(BaseVt100Ssh2Common.class);
	
	private static final  String  endStr = "]$";
	
	private Session session = null;
	private Connection conn = null;
	
	private OutputStream out = null;
	private InputStream in = null;
	
	private String targetIp = null;
	
	
	public String getTargetIp() {
		return targetIp;
	}

	public OutputStream getOut() {
		return out;
	}

	public InputStream getIn() {
		return in;
	}

	public Session getSession() {
		return session;
	}

	public BaseVt100Ssh2Common(String targetip) throws IOException {
		conn = new Connection("login1.cm3.taobao.org");
		conn.connect(null,2000,2000);
		boolean b = conn.authenticateWithPassword("xiaodu", "Hello_123");
		if(b){
			session = conn.openSession();
		}else{
			throw new IOException("账户密码或名称不正确!");
		}
		this.targetIp = targetip;
		init();
	}
	
	
	public void init() throws IOException{
		int x_width = 90;
		int y_width = 30;

		session.requestPTY("vt100", x_width, y_width, 0, 0, null);
		session.startShell();
		
		out = session.getStdin();
		in = session.getStdout();
		
		String str = read(this.getIn(),1500);//登录跳板机
		log.info(str);
		write("ssh "+this.targetIp,getOut());
		str = read(getIn(),1500);//ssh 跳转
		log.info(str);
		if(str.indexOf("Are you sure you want to continue connecting")>-1){
			write("yes",getOut());
			str = read(getIn(),1500);
			log.info(str);
		}
		if(str.indexOf("Enter passphrase for key")>-1){
			write("Hello_123",getOut());
			str = read(getIn(),1500);
			log.info(str);
		}
		str = read(getIn(),1500);//
		log.info(str);
	}
	
	
	
	protected void doResponse(String str) throws IOException{
		
		if(str.indexOf("Are you sure you want to continue connecting")>-1){
			write("yes",getOut());
			str = read(getIn(),1500);
		}
		if(str.indexOf("Enter passphrase for key")>-1){
			write("Hello_123",getOut());
			str = read(getIn(),1500);
		}		
		if(str.indexOf("Password:")>-1){
			write("Hello_123",getOut());
			str = read(getIn(),1500);
		}
		if(str.indexOf("口令:")>-1){
			write("Hello_123",getOut());
			str = read(getIn(),1500);
		}
	}
	
	public abstract void doCommon() throws IOException;
	
	public void close(){
		if(conn!=null)
			conn.close();
	}
	
	protected void write(String shellCommon,OutputStream out) throws IOException{
		out.write(shellCommon.getBytes());//第二步 输入指令
		out.write('\n');
	}
	
	protected String read(InputStream in,int waitTime){
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
					resultLen = in.available();
					if(resultLen <=0){
						break;
					}
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
