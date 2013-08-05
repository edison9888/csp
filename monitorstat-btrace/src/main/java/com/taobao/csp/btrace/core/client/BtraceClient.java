package com.taobao.csp.btrace.core.client;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.jar.JarFile;

import com.taobao.test.TestFieldThread2;

import com.taobao.csp.btrace.core.server.BtraceServer;

/**
 * 
 * @author xiaodu
 * @version 2011-8-16 下午02:04:04
 */
public class BtraceClient {

	public static void premain(String args, Instrumentation inst) {
		startClient(args,inst);
		System.out.println("do ---- premain");
	}

	public static void agentmain(String args, Instrumentation inst) {
		startClient(args,inst);
		System.out.println("do ---- agentmain");
	}
	
    
	
	public static void startClient(String args,final Instrumentation inst){
		
//		//添加一个线程，用来做类监控使用
//		TestFieldThread2 testThread = new TestFieldThread2();		
//		testThread.start();		//开启测试线程
		
		String[] params = args.split(",");
		
		if(params.length !=2){
			
			System.out.println("输入参数不正确 args="+args);
			return ;
		}
		
		String ip = params[0];
		String btracePath = params[1];
		
		System.out.println("IP="+ip+" PORT="+BtraceServer.SERVER_PORT+" btracePath="+btracePath);
		
		try {
			inst.appendToBootstrapClassLoaderSearch(new JarFile(new File(btracePath)));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Socket clientSocket = null;
		try {
			clientSocket = new Socket(ip,BtraceServer.SERVER_PORT);
			
			TransformerProxy proxy = new TransformerProxy(inst,clientSocket);
			
			proxy.registerTransformer();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
