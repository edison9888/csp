
package com.taobao.monitor.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * 
 * @author xiaodu
 * @version 2011-3-10 下午06:28:37
 */
public class RemoteCommonUtil {
	
	public static boolean authenticate(String ip,String userName,String password){
		Connection con = null;
		try{
			con = new Connection(ip);
			con.connect(null,8000,8000);
			boolean b = con.authenticateWithPassword(userName,password);
			return b;
		}catch (Exception e) {
			return false;
		}finally{
			if(con!= null){
				con.close();
			}
		}
	}
	
	
	public static String excute(String ip,String comm) throws Exception{
		Connection con = null;
			try{
				con = new Connection(ip);
				con.connect(null,8000,8000);
				boolean b = con.authenticateWithPassword("nobody","look");
				if(b){
					StringBuilder sb = new StringBuilder();
					Session s = con.openSession();
					s.execCommand(comm);
					InputStream stdout = new StreamGobbler(s.getStdout());			
					BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
					String line = null;
					while((line=br.readLine())!=null){
						sb.append(line);
						sb.append('\n');	
					}
					return sb.substring(0, sb.length()-1);
				}else{
					throw new  Exception("执行 指令:["+comm+"]"+ip+"nobody/look用户或密码不正确!") ;
				}
			}catch (Exception e) {
				throw new  Exception("执行 指令:["+comm+"]"+ip+"nobody/look "+e.getMessage()) ;
			}finally{
				if(con!= null){
					con.close();
				}
			}
	}
	
	public static void excute(String ip,String userName,String psw,String comm,CallBack call) throws Exception{
		Connection con = null;
			try{
				con = new Connection(ip);
				con.connect(null,8000,8000);
				boolean b = con.authenticateWithPassword(userName,psw);
				if(b){
					Session s = con.openSession();
					s.execCommand(comm);
					InputStream stdout = new StreamGobbler(s.getStdout());			
					BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
					String line = null;
					while((line=br.readLine())!=null){
						call.doLine(line);
					}
				}else{
					throw new  Exception("执行 指令:["+comm+"]"+ip+""+userName+"/"+psw+"用户或密码不正确!") ;
				}
			}catch (Exception e) {
				throw new  Exception("执行 指令:["+comm+"]"+ip+""+userName+"/"+psw+" "+e.getMessage()) ;
			}finally{
				if(con!= null){
					con.close();
				}
			}
	}
	
	
	public static void getFile(String ip,String userName,String psw,String remoteFilepath,String localFilePath)throws Exception{
		
		File file = new File(localFilePath);
		File parentFile = file.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		Connection con = null;
		try{
			con = new Connection(ip);
			con.connect(null,8000,8000);
			boolean b = con.authenticateWithPassword(userName,psw);
			if(b){
				SCPClient scp = con.createSCPClient();
				scp.get(remoteFilepath, new FileOutputStream(localFilePath));
			}else{
				throw new  Exception("scp "+localFilePath+" "+remoteFilepath+" 获取文件执行出错:"+ip+""+userName+"/"+psw+"用户或密码不正确!") ;
			}
		}catch (Exception e) {
			throw new  Exception("scp "+localFilePath+" "+remoteFilepath+" 获取文件执行出错:"+ip+""+userName+"/"+psw+" "+e.getMessage()) ;
		}finally{
			if(con!= null){
				con.close();
			}
		}
	}
	
	
	public static void sendFile(String ip,String userName,String psw,String localFilePath,String remoteFilepath) throws Exception{
		
		Connection con = null;
		try{
			con = new Connection(ip);
			con.connect(null,8000,8000);
			boolean b = con.authenticateWithPassword(userName,psw);
			if(b){
				SCPClient scp = con.createSCPClient();
				scp.put(localFilePath, remoteFilepath);
			}else{
				throw new  Exception("scp "+localFilePath+" "+remoteFilepath+" 发送文件执行出错:"+ip+""+userName+"/"+psw+"用户或密码不正确!") ;
			}
		}catch (Exception e) {
			throw new  Exception("scp "+localFilePath+" "+remoteFilepath+" 发送文件执行出错:"+ip+""+userName+"/"+psw+" "+e.getMessage()) ;
		}finally{
			if(con!= null){
				con.close();
			}
		}
		
	}
	
	
	public static void getFile(String ip,String remoteFilepath,String localFilePath)throws Exception{
		getFile(ip,"nobody","look",remoteFilepath,localFilePath);
	}
	
	
	
	public static void excute(String ip,String comm,CallBack call) throws Exception{
		excute( ip, "nobody","look",comm, call);
	}
	
	
public interface CallBack{
	public void doLine(String line);
}

}
