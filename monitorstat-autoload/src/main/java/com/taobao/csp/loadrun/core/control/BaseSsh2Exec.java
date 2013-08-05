
package com.taobao.csp.loadrun.core.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Session;

import com.taobao.csp.loadrun.core.shell.BaseShell;

/**
 * 
 * @author xiaodu
 * @version 2011-5-30 上午09:38:43
 */
public abstract class BaseSsh2Exec extends BaseShell{
	
	private Logger log = Logger.getLogger(BaseSsh2Exec.class);
	
	private static final  String  endStr = "]$";
	
	private Session session = null;
	
	private OutputStream out = null;
	private InputStream in = null;

	public OutputStream getOut() {
		return out;
	}

	public InputStream getIn() {
		return in;
	}

	public Session getSession() {
		return session;
	}

	public BaseSsh2Exec(String targetip,String userName,String password) throws Exception {
		super(targetip,userName,password);
		initSshConnect();
		init();
	}
	
	
	public void init() throws IOException{
		int x_width = 90;
		int y_width = 30;
		
		if(this.getTargetSSHConn()!=null)
			session = this.getTargetSSHConn().openSession();
		else
			throw new IOException("不存在连接:"+this.getTargetIp()+"/"+this.getTargetUserName()+"/"+this.getTargetPassword());

		session.requestPTY("vt100", x_width, y_width, 0, 0, null);
		session.startShell();
		
		out = session.getStdin();
		in = session.getStdout();
		
		String str = read(this.getIn(),1500);
		log.info(str);
		doResponse(str);
	}
	
	
	
	protected void doResponse(String str) throws IOException{
		
		if(str.indexOf("Are you sure you want to continue connecting")>-1){
			write("yes",getOut());
			str = read(getIn(),1500);
			log.info("Are you sure you want to continue connecting:"+str);
			check(str);
		}
		if(str.indexOf("Enter passphrase for key")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			log.info("Enter passphrase for key:"+str);
			check(str);
		}		
		if(str.indexOf("Password")>-1 || str.indexOf("password")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			log.info("Password:"+str);
			check(str);
		}
		if(str.indexOf("口令")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			log.info("口令:"+str);
			check(str);
		}
		if(str.indexOf("密码")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			log.info("密码:"+str);
			check(str);
		}
	}
	
	private void check(String str) throws IOException{
		if(str.indexOf("try again")>-1){
			throw new IOException("密码输入错误!");
		}
	}
	
	
	public abstract void doCommand() throws IOException;
	
	protected void doCommand(List<String> commands ) throws IOException { };
	
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
					//System.out.println(str);
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
