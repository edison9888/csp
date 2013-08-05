
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
 * @version 2011-8-18 ����10:13:09
 * 
 * modify by zhongting	2011-10-24 ����18:49:17
 * 1.����fieldName����߼�
 * 2.ȥ��System.out,Server������Log4j��ӡ��Ϣ
 */
public class BtraceHandle implements Runnable{
	
	Logger logger = Logger.getLogger(BtraceHandle.class);
	
	private static final int SUCCESS = 0;
	private static final int FAILURE = -1;
	
	//���ͣ�������ʶ���������
	public static final int METHOD_TYPE = 0;				
	public static final int SINGLETON_FIELD_TYPE = 1;		//�������������е�����
	public static final int PROTOTYPE_FIELD_TYPE = 2;		//��ֻ�У���ע�������
	
	private Socket clientSocket = null;
	
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	
	private Thread thread = null;
	
	private BtraceServer btraceServer = null;
	
	private BtraceServerProfiler btraceServerProfiler = null;
	private FieldBtraceServerProfiler fieldBtraceServerProfiler = null;
	
	private int btraceSuccess = FAILURE;		//���ͻ�����Web Server���ӽ���������ΪSUCCESS
	
	private boolean run = true;
	
	//������¼��ǰĳһ��Target JVM�����м�ز�����ͨ�����ݽṹTranformConfig��¼��
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
				logger.error("stop��������",e1);
			}
		}
	}
	
	
	public void sendCommand(Command command) throws Exception{
		command.writeMessage(oout);
	}
	
	/**
	 * 
	 * @param classPattern		����������ƣ�"/"�ָ
	 * @param methodPattern		����ط�����������
	 * @param transformerClass	methodAdapter�����ࣨ"."�ָ
	 * @param fieldName			����ص����Ե����ƣ�����Ϊnull��""����ʾֻ���Mothod
	 * @param isStatic			����ص������Ƿ���static
	 * @return 					falseʱ��ʾ��ͻ��˵�Socket���ӻ�δ������true��ʾSocket�����ɹ�
	 * @throws Exception		�����쳣���ʾSocket����ʱ�����쳣
	 */
	public boolean sendTransformer(String classPattern, String methodPattern, String transformerClass,
			 String fieldName, boolean isStatic, int type) throws Exception{
		if(btraceSuccess != SUCCESS) {	//����У�飬����δ�����򲻷���
			logger.error("����sendTransformer������Target JVM��Socket������δ������");
			return false;
		}
		
		//Ϊ��ֹsocket����ʧ�ܣ�����Ϊnull�����
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
			logger.error("����sendTransformer����type����type=" + type);
			return false;
		}
		
		TranformConfig config = new TranformConfig();
		config.setClassPatternName(classPattern);
		config.setId(tranformId);
		config.setMethodPatternName(methodPattern);
		config.setTransformerMethod(transformerClass);
		config.setStatic(isStatic);
		config.setFieldNameAndTypeByFieldName(fieldName);	//����fieldName��ֵ���ڲ�����type��fieldName����	
		transformerMap.put(tranformId, config);
		return true;
	}
	
	//�Ƴ�����
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
					//TransformerProxy������getClassTransform�������ƶ��౻ע������һ������
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
							logger.error("Command.TRANSFORMER_CLASS�쳣", cnfe);
							throw new IOException(cnfe);
						}
						logger.debug(cmd);
						break;
					case Command.UNSUPPORTED:
						this.setBtraceSuccess(FAILURE);
						logger.error("Ŀ�������֧��transformer");
						break;
					case Command.SUCCESS:
						this.setBtraceSuccess(SUCCESS);
//						//���ܵ�OKcommand�������ʼע��������Ҫ���࣬���Case�ǿ���ȥ���ġ�
//						try {
//							boolean flagLocal = sendTransformer("org/apache/velocity/app/VelocityEngine","merge.*","com.taobao.csp.btrace.core.script.ShowParametersMethod",
//									"com.taobao.csp.btrace.core.script.ShowFieldMethod", true);
//							System.out.println("���flagLocal��" + flagLocal);
//							
//						} catch (Exception e1) {
//							logger.error("����sendTransformer�쳣",e1);
//						}
						
						break;
					case Command.MESSAGE:
						MessageCommand message = new MessageCommand();
						try {
							message.read(oin);
							logger.debug(message.getMessage());
							btraceServerProfiler.putProfilerData(message.getMessage());
						} catch (ClassNotFoundException e) {
							logger.error("Command.MESSAGE �쳣", e);
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
	 * ���transformerMap�е����ݣ�
	 */
	public void clearTransformerMap() {
		this.transformerMap.clear();
	}
	
}
