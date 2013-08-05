/**
**xiaodu
**/
package com.taobao.csp.dataserver;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.taobao.csp.dao.hbase.base.HBaseUtil;
import com.taobao.csp.dataserver.server.DataAcceptServer;
import com.taobao.csp.dataserver.util.DiamondUtil;

public class Main {
	private static final Logger logger = Logger.getLogger("zookeeper");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			HBaseUtil.addRow("CSP", "1", "1");
		} catch (IOException e) {
			System.out.println("Hbase 连接失败!");
			logger.error("Hbase 连接失败!");
			System.exit(-1);
		}
		DiamondUtil.diamondMainListener();
		
		int port = 16512;
		int accepts = 100;
		if(args.length>=2){
			port = Integer.parseInt(args[0]);
			accepts = Integer.parseInt(args[1]);
		}
		logger.info("zk配置文件生效...");
		final DataAcceptServer server = new DataAcceptServer(port,accepts);
		server.startup();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				server.stop();
			}
		});
		
	}
	

}
