
package com.taobao.csp.btrace.web.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

/**
 * 
 * @author xiaodu
 * @version 2011-5-30 ����09:38:43
 */
public class BaseSsh2Exec {
	
	private String targetIp = null;
	
	private String targetUserName = null;
	
	private String targetPassword = null;
	
	private Connection targetSSHConn = null;
	
	
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
		this.targetIp = targetip;
		this.targetUserName = userName;
		this.targetPassword = password;
		initSshConnect();
	}
	
	/**
	 * ��ʼ������
	 * @throws Exception
	 */
	protected void initSshConnect() throws Exception{		
		if(this.targetSSHConn!=null){
			this.targetSSHConn.close();
		}		
		if(this.targetIp==null){
			throw new Exception("��Զ�̻���IP");			
		}
		if(this.targetPassword==null||this.targetUserName==null){
			throw new Exception(this.targetIp+":�޵�½��������");			
		}		
		this.targetSSHConn = new Connection(this.targetIp);	
		
		this.targetSSHConn.connect(null,10000,10000);
		boolean isAuthenticated = this.targetSSHConn.authenticateWithPassword(this.targetUserName,this.targetPassword);
		
		if(!isAuthenticated){
			 File keyfile = new File("/home/admin/.ssh/id_rsa");
			 isAuthenticated = this.targetSSHConn.authenticateWithPublicKey("admin",keyfile,"nopsw");
		}
		
		if (isAuthenticated == false){
			throw new Exception(this.targetUserName+"/"+this.targetPassword+" �޷���½�� "+this.targetIp);
		}
		
		int x_width = 90;
		int y_width = 30;
		
		if(this.targetSSHConn!=null)
			session = this.targetSSHConn.openSession();
		else
			throw new IOException("����������:"+this.targetIp+"/"+this.targetUserName+"/"+this.targetPassword);

		session.requestPTY("vt100", x_width, y_width, 0, 0, null);
		session.startShell();
		
		out = session.getStdin();
		in = session.getStdout();
		
		String str = read(this.getIn(),1500);
		doResponse(str);
		
	}
	
	
	protected String doResponse(String str) throws IOException{
		
		if(str.indexOf("Are you sure you want to continue connecting")>-1){
			write("yes",getOut());
			str = read(getIn(),1500);
			check(str);
		}
		if(str.indexOf("Enter passphrase for key")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			check(str);
		}		
		if(str.indexOf("Password")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			check(str);
		}
		if(str.indexOf("����")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			check(str);
		}
		if(str.indexOf("����")>-1){
			write(this.getTargetPassword(),getOut());
			str = read(getIn(),1500);
			check(str);
		}
		return str;
	}
	
	private void check(String str) throws IOException{
		if(str.indexOf("try again")>-1){
			throw new IOException("�����������!");
		}
	}
	
	
	public String doCommand(String command) throws IOException{
		
		StringBuffer sb = new StringBuffer();
		
		if(!"admin".equals(this.getTargetUserName())){
			write("sudo -u admin -H "+command,out);			
		}else{
			write(command,out);			
		}
		String str = read(getIn(),1500);
		sb.append(this.doResponse(str));
		str = read(getIn(),1500);
		sb.append(this.doResponse(str));
		return sb.toString();
	}
	
	
	
	private void write(String shellCommon,OutputStream out) throws IOException{
		out.write(shellCommon.getBytes());//�ڶ��� ����ָ��
		out.write('\n');
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
					resultLen = in.available();
					if(resultLen <=0){
						break;
					}
				}

				int len = in.read(buff);
				if(len < 0 ){					
					continue;
				}
				
				int space_available = reader_buffer.length - write_pos; //ʣ��				
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
					
					if(write_pos != 0){//��һ�ζ�ȡ
					}else{
						System.arraycopy(reader_buffer, 0, new_buffer, 0, write_pos);
					}
					reader_buffer = new_buffer;
				}
								
				System.arraycopy(buff, 0, reader_buffer, write_pos, len);
				write_pos += len;
				if(len < buff.length ){
					String str = new String(reader_buffer,0,write_pos,"GBK");	
					sb.append(str);
					//System.out.println(str);
					if(write_pos >=6){
						byte[] lastStrByte = new byte[6];
						System.arraycopy(reader_buffer, write_pos-6, lastStrByte, 0, 6);
						String lastStr = new String(lastStrByte,"GBK");
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

	public String getTargetPassword() {
		return targetPassword;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public Connection getTargetSSHConn() {
		return targetSSHConn;
	}
	
}
