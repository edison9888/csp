
package com.taobao.csp.btrace;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.taobao.csp.btrace.core.packet.OkayCommand;
import com.taobao.csp.btrace.core.server.BtraceServer;

/**
 * 
 * @author xiaodu
 * @version 2011-8-29 ÏÂÎç12:53:00
 */
public class Test {
	
	
	public static void main(String[] args){
		
		
		try {
			final Socket clientSocket = new Socket("127.0.0.1",BtraceServer.SERVER_PORT);
			 ObjectOutputStream oout = new ObjectOutputStream(clientSocket.getOutputStream());
			 OkayCommand o = new OkayCommand();
			 o.writeMessage(oout);
			 
			 
			 
			 Thread thread = new Thread(){
				 public void run(){
					 ObjectInputStream oin;
					try {
						oin = new ObjectInputStream(clientSocket.getInputStream());
						 oin.readInt();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				 }
				 
			 };
			 thread.start();
			
			 System.out.println();
			 
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
