
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author xiaodu
 *
 * 下午3:40:36
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DOMConfigurator.configure("time_log4j.xml");
		
		if(args==null||args.length==0){
			System.out.println("请输入分组标记");
			System.exit(-1);
		}
		String mc = args[0];
//		String mc = "CM3";
		final GroupMaker maker = new GroupMaker(mc);
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				maker.stop();
			}
		});
		maker.startup();
	}

}
