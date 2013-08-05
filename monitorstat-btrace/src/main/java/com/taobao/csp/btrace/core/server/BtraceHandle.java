
package com.taobao.csp.btrace.core.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.btrace.core.TranformConfig;
import com.taobao.csp.btrace.core.packet.Command;
import com.taobao.csp.btrace.core.packet.FieldMessageCommand;
import com.taobao.csp.btrace.core.packet.MessageCommand;
import com.taobao.csp.btrace.core.packet.RemoveCommand;
import com.taobao.csp.btrace.core.packet.SingletonFieldCommand;
import com.taobao.csp.btrace.core.packet.TransformerClassCommand;
import com.taobao.csp.btrace.core.packet.TransformerConfigCommand;

/**
 * 
 * @author xiaodu
 * @version 2011-8-18 上午10:13:09
 * 
 * modify by zhongting	2011-10-24 下午18:49:17
 * 1.新增fieldName相关逻辑
 * 2.去掉System.out,Server端新增Log4j打印信息
 */
public class BtraceHandle implements Runnable{
	
	Logger logger = Logger.getLogger(BtraceHandle.class);
	
	private static final int SUCCESS = 0;
	private static final int FAILURE = -1;
	
	//类型，用来标识传输的类型
	public static final int METHOD_TYPE = 0;				
	public static final int SINGLETON_FIELD_TYPE = 1;		//单例或被容器持有的类型
	public static final int PROTOTYPE_FIELD_TYPE = 2;		//非只有，可注入的类型
	
	private Socket clientSocket = null;
	
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	
	private Thread thread = null;
	
	private BtraceServer btraceServer = null;
	
	private BtraceServerProfiler btraceServerProfiler = null;
	private FieldBtraceServerProfiler fieldBtraceServerProfiler = null;
	
	private int btraceSuccess = FAILURE;		//当客户端与Web Server连接建立后，设置为SUCCESS
	
	private boolean run = true;
	
	//用来记录当前某一个Target JVM的所有监控操作，通过数据结构TranformConfig记录。
	private Map<String,TranformConfig> transformerMap = new ConcurrentHashMap<String, TranformConfig>();
	
	public Map<String,TranformConfig> getTransformerMap() {
		return transformerMap;
	}

	public BtraceServerProfiler getBtraceServerProfiler() {
		return btraceServerProfiler;
	}
	
	public FieldBtraceServerProfiler getFieldBtraceServerProfiler() {
		return fieldBtraceServerProfiler;
	}

	public BtraceHandle(Socket socket) throws IOException{
		this.clientSocket = socket;
		this.oin = new ObjectInputStream(clientSocket.getInputStream());
		this.oout = new ObjectOutputStream(clientSocket.getOutputStream());
		this.btraceServerProfiler = new BtraceServerProfiler();
		this.fieldBtraceServerProfiler = new FieldBtraceServerProfiler();
	}
	
	public void startup(BtraceServer btraceServer){
		this.btraceServer = btraceServer;
		thread = new Thread(this);
		thread.start();
	}
	
	
	
	public void stop(){
		run = false;
		if(clientSocket != null){
			btraceServer.removeBtraceHandle(clientSocket.getInetAddress().getHostAddress());
			try {
				clientSocket.close();
			} catch (IOException e1) {
				logger.error("stop发生错误",e1);
			}
		}
	}
	
	
	public void sendCommand(Command command) throws Exception{
		command.writeMessage(oout);
	}
	
	/**
	 * 
	 * @param classPattern		被监控类名称（"/"分割）
	 * @param methodPattern		被监控方法名称正则
	 * @param transformerClass	methodAdapter代理类（"."分割）
	 * @param fieldName			被监控的属性的名称，可以为null或""，表示只监控Mothod
	 * @param isStatic			被监控的属性是否是static
	 * @return 					false时表示与客户端的Socket连接还未建立，true表示Socket建立成功
	 * @throws Exception		出现异常则表示Socket传输时发生异常
	 */
	public boolean sendTransformer(String classPattern, String methodPattern, String transformerClass,
			 String fieldName, boolean isStatic, int type) throws Exception{
		if(btraceSuccess != SUCCESS) {	//新增校验，连接未建立则不发送
			logger.error("调用sendTransformer出错，与Target JVM的Socket连接尚未建立！");
			return false;
		}
		
		//为防止socket发送失败，过滤为null的情况
		fieldName 	  = (fieldName != null )? fieldName : "";
		methodPattern = (methodPattern != null )? methodPattern : "";
		
		String tranformId = UUID.randomUUID().toString();
		
		if(type == BtraceHandle.PROTOTYPE_FIELD_TYPE || type == BtraceHandle.METHOD_TYPE) {
			TransformerConfigCommand cmd = new TransformerConfigCommand( tranformId, classPattern, methodPattern, 
					transformerClass, fieldName, isStatic, false);
			sendCommand(cmd);	
		} else if(type == BtraceHandle.SINGLETON_FIELD_TYPE){
			SingletonFieldCommand cmd = new SingletonFieldCommand( tranformId, classPattern, fieldName, isStatic, false);
			sendCommand(cmd);	
		} else {
			logger.error("调用sendTransformer出错，type错误！type=" + type);
			return false;
		}
		
		TranformConfig config = new TranformConfig();
		config.setClassPatternName(classPattern);
		config.setId(tranformId);
		config.setMethodPatternName(methodPattern);
		config.setTransformerMethod(transformerClass);
		config.setStatic(isStatic);
		config.setFieldNameAndTypeByFieldName(fieldName);	//根据fieldName的值，内部设置type和fieldName属性	
		transformerMap.put(tranformId, config);
		return true;
	}
	
	//移除调用
	public void removeTransformer(String id) throws Exception{
		RemoveCommand remove = new RemoveCommand(id);
		sendCommand(remove);
		transformerMap.remove(id);
	}

	@Override
	public void run() {
		
		while(run){
			
			try {
				int flag = oin.readInt();
				if (flag != Command.TF_CLASS_FLAG) {
					logger.error("flag != Command.TF_CLASS_FLAG");
					throw new IOException("");
				}
				int type = oin.readInt();
				switch (type) {
					//TransformerProxy这个类的getClassTransform方法，制定类被注入后会有一个反馈
					case Command.TRANSFORMER_CLASS:		
						TransformerClassCommand cmd = new TransformerClassCommand();
						try {
							cmd.read(oin);
							String tranformId = cmd.getId();
							TranformConfig config = transformerMap.get(tranformId);
							if(config != null){
								config.getTranformClassSet().add(cmd.getClassName());
							}
						} catch (ClassNotFoundException cnfe) {
							logger.error("Command.TRANSFORMER_CLASS异常", cnfe);
							throw new IOException(cnfe);
						}
						logger.debug(cmd);
						break;
					case Command.UNSUPPORTED:
						this.setBtraceSuccess(FAILURE);
						logger.error("目标机器不支持transformer");
						break;
					case Command.SUCCESS:
						this.setBtraceSuccess(SUCCESS);
//						//接受到OKcommand的命令，开始注入我们需要的类，这个Case是可以去掉的。
//						try {
//							boolean flagLocal = sendTransformer("org/apache/velocity/app/VelocityEngine","merge.*","com.taobao.csp.btrace.core.script.ShowParametersMethod",
//									"com.taobao.csp.btrace.core.script.ShowFieldMethod", true);
//							System.out.println("结果flagLocal：" + flagLocal);
//							
//						} catch (Exception e1) {
//							logger.error("测试sendTransformer异常",e1);
//						}
						
						break;
					case Command.MESSAGE:
						MessageCommand message = new MessageCommand();
						try {
							message.read(oin);
							logger.debug(message.getMessage());
							btraceServerProfiler.putProfilerData(message.getMessage());
						} catch (ClassNotFoundException e) {
							logger.error("Command.MESSAGE 异常", e);
						}
						logger.debug(message);
						break;
					case Command.FIELD_MESSAGE:
						FieldMessageCommand fieldMessage = new FieldMessageCommand();
						try {
							fieldMessage.read(oin);
							logger.debug(fieldMessage.getMessage());
							fieldBtraceServerProfiler.putProfilerData(fieldMessage.getMessage());
						} catch (ClassNotFoundException e) {
						}
						logger.debug(fieldMessage);
						break;						
					default:
						//throw new RuntimeException("invalid command: " + type);
						break;
				}
			} catch (IOException e) {
				logger.debug(e);
				if(clientSocket !=null){
					btraceServer.removeBtraceHandle(clientSocket.getInetAddress().getHostAddress());
					try {
						clientSocket.close();
					} catch (IOException e1) {
						logger.debug(e1);
					}
				}
				break;
			}
		}
	}

	public int getBtraceSuccess() {
		return btraceSuccess;
	}

	private void setBtraceSuccess(int btraceSuccess) {
		this.btraceSuccess = btraceSuccess;
	}
	
	/**
	 * 清空transformerMap中的内容！
	 */
	public void clearTransformerMap() {
		this.transformerMap.clear();
	}
	
}
