//package com.taobao.test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.management.RuntimeMXBean;
//
//import com.sun.tools.attach.AttachNotSupportedException;
//
//import sun.tools.attach.HotSpotVirtualMachine;
//
///**
// * 
// * @author zhongting.zy
// * 这个类通过使用tools.jar，来直接获取当前运行的属性的值
// */
//public class TestCount {
//
//	/**
//	 * @param args
//	 * @throws IOException 
//	 * @throws AttachNotSupportedException 
//	 */
//	public static void main(String[] args) throws AttachNotSupportedException, IOException {
//		
//		TestFieldThread2 thread2 = new TestFieldThread2();
//		thread2.start();
//		
//		RuntimeMXBean bean = java.lang.management.ManagementFactory.getRuntimeMXBean();
//		String name = bean.getName();
//		int index = name.indexOf('@');
//		String pid = name.substring(0, index);
//		
//		//这里要区分操作系统
//		HotSpotVirtualMachine machine = (HotSpotVirtualMachine) new sun.tools.attach.WindowsAttachProvider().attachVirtualMachine(pid);
//		InputStream is = machine.heapHisto("-all");
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		int readed;
//		byte[] buff = new byte[1024];
//		
//		while((readed = is.read(buff)) > 0)
//			baos.write(buff, 0, readed);
//		is.close();
//
//		machine.detach();
//		System.out.println(baos);
//	}
//
//}
