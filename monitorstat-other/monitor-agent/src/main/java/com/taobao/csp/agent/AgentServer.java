package com.taobao.csp.agent;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.channels.ClosedChannelException;

import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;

import com.taobao.csp.agent.hanlder.CommandHelper;
import com.taobao.csp.agent.hanlder.FileCreateHelpser;


public class AgentServer {
	private IServer server = null;
	private int port = 18899; 
	
	private static AgentServer agentServer = new AgentServer();
	
	private AgentServer(){
		
		try {
			server = new Server(port,new AgentServerHanlder());
			server.start();
			System.out.println("启动 xsocket port:"+port);
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static AgentServer startup(){
		return agentServer;
	}
	
	
	
	public class AgentServerHanlder implements IDataHandler{

		public boolean onData(INonBlockingConnection nbc) throws IOException,
				BufferUnderflowException, ClosedChannelException,
				MaxReadSizeExceededException {
			String cmd = nbc.readStringByDelimiter("\01\02");
			System.out.println(cmd);
			if (cmd.startsWith("command@")) {
				String command = cmd.substring(8,cmd.length());
				System.out.println("command:"+command);
				nbc.write(CommandHelper.execute("c",command));
			}else if (cmd.startsWith("shell@")) {
				String command = cmd.substring(6,cmd.length());
				System.out.println("shell:"+command);
				nbc.write(CommandHelper.execute("shell",command));
			}else if(cmd.startsWith("file@")){
				String fileInfo = cmd.substring(5,cmd.length());
				String[] tmp = fileInfo.split("#");
				if(tmp.length >=2){
					String filePath = tmp[0];
					String fileName = tmp[1];
					
					StringBuilder content = new StringBuilder();
					
					for(int i=2;i<tmp.length;i++){
						content.append(tmp[i]);
					}
					nbc.write(FileCreateHelpser.execute(filePath, fileName, content.toString()));
				}
				
				
			}else{
				
			}
			nbc.write("\01\02");
			return false;
		}

	}
	
	public static void main(String[] args){
		AgentServer.startup();
	}

}
