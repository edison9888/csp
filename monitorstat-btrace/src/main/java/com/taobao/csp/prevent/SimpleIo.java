package com.taobao.csp.prevent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xsocket.connection.BlockingConnectionPool;
import org.xsocket.connection.IBlockingConnection;

public class SimpleIo {
	private final static BlockingConnectionPool pool = new BlockingConnectionPool();
	private final static int TIMEOUT = 30000;
	
	
	public static List<ClassDetail> queryPreventClass(String remoteAddr,String port,String taskId ) {
		
		StringBuffer cmd = new StringBuffer();
		IBlockingConnection bc = null;
		String response = null;
		
		cmd.append("QUERYPREVENT").append("@").append(taskId).append("\01\02");
		
		try {
			bc = pool.getBlockingConnection(remoteAddr, Integer.valueOf(port),TIMEOUT);
			bc.setAutoflush(true);
			bc.write(cmd.toString());
			response = bc.readStringByDelimiter("\01\02");
			
		} catch (Exception e) {
			
		}finally {
			try {
				if (null != bc) {
					if (bc.isOpen())
						bc.close();
					pool.destroy(bc);
				}
			} catch (IOException e) {
			}
		}
		
		List<ClassDetail> list = new ArrayList<ClassDetail>();
		if(response!=null){
			
			String[] msg =  response.split("@");
			if(msg.length==2){
				String[] tmp =msg[1].split(",");
				for(String c:tmp){
					ClassDetail detail = new ClassDetail();
					String[] cm = c.split(":");
					detail.setClassName(cm[0]);
					detail.setMethod(cm[1]);
					try{
						detail.setDelay(Integer.valueOf(cm[2]));
					}catch (Exception e) {
					}
					list.add(detail);
				}
			}
		}
		return list;
	}
	

}
