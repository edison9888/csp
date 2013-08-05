
package com.taobao.csp.assign.slave;

import java.util.UUID;

import org.apache.log4j.Logger;

/**
 * 
 * @author denghaichuan.pt
 * @version 2012-3-29
 */
public class SlaveMain {
	private static final Logger logger = Logger.getLogger(SlaveMain.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String ip = "127.0.0.1";
		int port = 22881;
		String localSite = "CM3";
		int threadCount = 1;
		
		if (args.length == 1) {
			ip = args[0];
		} else if (args.length == 2) {
			ip = args[0];
			localSite = args[1];
		} else if (args.length == 3) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
			localSite = args[2];
		} else if (args.length == 4) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
			localSite = args[2];
			threadCount = Integer.parseInt(args[3]);
		} else if (args.length > 5){
			logger.info("slave" + args);
			return;
		}
		
		String slaveId = UUID.randomUUID().toString();
		logger.info("Æô¶¯µÄsalve Id Îª :" + slaveId);
		Slave slave1 = new Slave(ip,port,slaveId,localSite, threadCount);
		slave1.startup();
		
	}

}
