
package com.taobao.csp.btrace.core.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.csp.btrace.core.BtraceMethodCache;
import com.taobao.csp.btrace.core.FieldProfilerInfo;
import com.taobao.csp.btrace.core.MethodInfo;
import com.taobao.csp.btrace.core.ProfilerInfo;
import com.taobao.csp.btrace.core.TranformConfig;
import com.taobao.csp.btrace.core.packet.Command;
import com.taobao.csp.btrace.core.packet.FieldMessageCommand;
import com.taobao.csp.btrace.core.packet.MessageCommand;
import com.taobao.csp.btrace.core.packet.OkayCommand;
import com.taobao.csp.btrace.core.packet.RemoveCommand;
import com.taobao.csp.btrace.core.packet.SingletonFieldCommand;
import com.taobao.csp.btrace.core.packet.TransformerClassCommand;
import com.taobao.csp.btrace.core.packet.TransformerConfigCommand;
import com.taobao.csp.btrace.core.packet.UnSupportedCommand;
import com.taobao.csp.btrace.core.script.FieldBtraceClassAdapter;
import com.taobao.csp.btrace.core.script.MethodBtraceClassAdapter;
import com.taobao.csp.objectweb.asm.ClassAdapter;
import com.taobao.csp.objectweb.asm.ClassReader;
import com.taobao.csp.objectweb.asm.ClassWriter;

/**
 * @author xiaodu
 * @version 2011-8-16 ����02:25:32
 */
public class TransformerProxy {
	
	private Map<String,TranformConfig> transformerClassInfoMap = new ConcurrentHashMap<String, TranformConfig>();
	
	//������map�Լ�����ά��
	private Map<String,TranformConfig> singletonMap = new ConcurrentHashMap<String, TranformConfig>();
	private final int FINAL_TIMETERVAL = 500;		//��λ����
	
//	ExecutorService pool = Executors.newFixedThreadPool(3);		//ʹ���̳߳�ò�ƹر��߳������⣬��ʱʹ�ñ�ǰ�
	boolean isOn = true;		//��ʼĬ��Ϊtrue
	
	public static LinkedBlockingQueue<ProfilerInfo> queue = new LinkedBlockingQueue<ProfilerInfo>();
	public static LinkedBlockingQueue<FieldProfilerInfo> fieldQueue = new LinkedBlockingQueue<FieldProfilerInfo>();
	
	private Instrumentation inst;
	
	private ClassFileTransformer former = null;
	
	private ObjectInputStream oin;
	private ObjectOutputStream oout;
	
	private boolean retransformClasses = false;
	
	private Socket clientSocket = null;
	
	
	public boolean classTransformed(String className){
		
		for(Map.Entry<String,TranformConfig> entry:transformerClassInfoMap.entrySet()){
			
			if(entry.getValue().getTranformClassSet().contains(className)){
				return true;
			}
			
		}
		return false;
	}
	
	//����һ��client�˵�socket��������server����Ϣ��ֻ�ǳ�ʼ�����ѣ�ͬʱ���������߳�
	public TransformerProxy(Instrumentation inst,Socket clientSocket) throws IOException{
		this.inst = inst;
		this.former = new BtraceTransformer(this);	//ע��Class���ʺϺ���Ҫʹ��
		this.clientSocket = clientSocket;
		
		this.oout = new ObjectOutputStream(clientSocket.getOutputStream());
		this.oin = new ObjectInputStream(clientSocket.getInputStream());
		
		//�ͻ��˿���һ���̣߳�����WebServer��ͨѶ
		Thread handle = new Thread(){
			
			public void run(){
				
				while(true){
					try {
						int flag = oin.readInt();
						if (flag != Command.TF_CLASS_FLAG) {//transfercommond��ǰ׺
							throw new IOException("");
						}
						int type = oin.readInt();
						switch (type) {
							case Command.TRANSFORMER_CONFIG:
								
								TransformerConfigCommand cmd = new TransformerConfigCommand();
								try {
									cmd.read(oin);
									TranformConfig info = new TranformConfig();
									info.setClassPatternName(cmd.getClassPatternName());
									info.setMethodPatternName(cmd.getMethodPatternName());
									info.setTransformerMethod(cmd.getMethodTransformer());
									info.setFieldNameAndTypeByFieldName(cmd.getFieldName());
									info.setStatic(cmd.isStatic());
									info.setId(cmd.getId());
									transformerClassInfoMap.put(cmd.getId(), info);
									//instrumentation��retransformClasses�ķ����İ�װ��
									retransformerClassName(cmd.getClassPatternName());
								} catch (ClassNotFoundException cnfe) {
									throw new IOException(cnfe);
								}								
								break;
							case Command.SINGLETON_CLASS:		//���������
								SingletonFieldCommand singletonCmd = new SingletonFieldCommand();
								try {
									singletonCmd.read(oin);
									//Ӧ�ò���Ҫ���߳��з�
									TranformConfig info = new TranformConfig();
									info.setClassPatternName(singletonCmd.getClassPatternName());
									info.setMethodPatternName("");
									info.setTransformerMethod("");
									info.setFieldNameAndTypeByFieldName(singletonCmd.getFieldName());
									info.setStatic(singletonCmd.isStatic());
									info.setId(singletonCmd.getId());
									singletonMap.put(singletonCmd.getId(), info);
									//Ŀǰֻ��ͨ������õ���̬��ľ�̬���ԣ�isStatic�ݲ�������
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
								break;
							case Command.EXIT:
								termination();
								break;
							case Command.REMOVE:
								RemoveCommand remove = new RemoveCommand();
								try {
									remove.read(oin);
									TranformConfig info = transformerClassInfoMap.remove(remove.getId());
									retransformerClassName(info.getClassPatternName());
								} catch (ClassNotFoundException cnfe) {
									throw new IOException(cnfe);
								}	
								break;								
							default:
								break;
						}
						
						
					} catch (IOException e) {
						termination();
						break;
					}
				}
				
				
			}
		};
		handle.start();
		
		//�̶߳��ڶ�ȡ��������Ϣ
		Thread singletonThread = new Thread(){
			public void run(){
				while(isOn){
					try {
						for(String key : singletonMap.keySet()) {
							TranformConfig config = singletonMap.get(key); 
							if(config != null) {
								SingleFieldProfiler.getStaticFieldValue(config.getClassPatternName(),
										config.getFieldName());								
							}
						}
						TimeUnit.MILLISECONDS.sleep(FINAL_TIMETERVAL);
					} catch (InterruptedException e) {
					}
				}
			}
		};		
		
		//�̶߳��ڶ�ȡ��������Ϣ
		Thread thread = new Thread(){
			public void run(){
				while(isOn){
					try {
						ProfilerInfo info = queue.take();
						MethodInfo methodInfo = BtraceMethodCache.mCacheMethods.elementAt(info.getMethodId());
						info.setMethodInfo(methodInfo);
						MessageCommand cmd = new MessageCommand(info);
						doCommand(cmd);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		
		//�̶߳��ڶ�ȡ���Ե���Ϣ
		Thread thread2 = new Thread(){
			public void run(){
				while(isOn){
					try {
						FieldProfilerInfo info = fieldQueue.take();
						FieldMessageCommand cmd = new FieldMessageCommand(info);
						doCommand(cmd);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		
		singletonThread.start();	//singleton��Filed����ѵ�߳�
		thread.start();				//Method�����߳�
		thread2.start();			//Field�����߳�
		
//		pool.execute(singletonThread);
//		pool.execute(thread);
//		pool.execute(thread2);

	}
	
	//���أ�����Method��Ϣ	
	public synchronized void doCommand(Command command){
		try {
			command.writeMessage(oout);
		} catch (Exception e) {
			termination();
		}
	}
	
	//���أ�����Field��Ϣ
	public synchronized void doCommand(FieldMessageCommand command){
		try {
			command.writeMessage(oout);
		} catch (Exception e) {
			termination();
		}
	}	
	
	public void registerTransformer(){
		if(inst.isRetransformClassesSupported()){
			inst.addTransformer(former, true);
			retransformClasses = true;
			doCommand(new OkayCommand());
		}else{
			termination();
			doCommand(new UnSupportedCommand());
		}
	}
	
	public void unregisterTransformer(){
		if(retransformClasses)
			inst.removeTransformer(former);
	}
	
	
	/**
	 * �˳� ����socket �Ͽ�����յ�ָֹͣ�� ���ͷ���Դ
	 */
	public void termination(){
		unregisterTransformer();
		
		try {
			if(clientSocket.isConnected())
				clientSocket.close();
		} catch (IOException e) {
		}
		isOn = false;	//���Ϊfalse����ֹ3����ѵ�߳�
		//pool.shutdownNow();		//�ر��߳�
	}
	
	
	private boolean isModifiableClass(Class c,String cname) {
		
        if (c.isInterface() || c.isPrimitive() || c.isArray()) {
            return false;
        }
        if(!inst.isModifiableClass(c)){
        	return false;
        }
        
        if (cname.startsWith("com/taobao/csp/btrace")) {
            return false;
        }
        
        if (cname.startsWith("java")||cname.startsWith("javax")) {
            return false;
        }
        
        Class superClazz = c;
        while((superClazz = superClazz.getSuperclass())!=null){
			if(superClazz.getName().startsWith("groovy")){
				
				 return false;
			}
		}
		Class<?>[] interfaces = c.getInterfaces();
		if(interfaces !=null){
			for(Class clazz:interfaces){
				if (clazz.getName().startsWith("groovy")) {
		            return false;
		        }
			}
		}
        
        return true;
    }
	
	
	/**
	 * ���һ����Ҫ���� Զ���滻��classname����"."ת��Ϊ"/"
	 * @param className
	 */
	public void retransformerClassName(String classNamePattern) {
		
		if(inst.isRetransformClassesSupported()){
			//���ؼ��ص�����Class������
			Class[] classes = inst.getAllLoadedClasses();
			if(classes != null){
				for(Class clazz:classes){
					String name = clazz.getName().replaceAll("\\.", "/");
					if(!isModifiableClass(clazz,name)){
						continue;
					}
					Pattern p = Pattern.compile(classNamePattern);
					Matcher m = p.matcher(name);
					if(m.matches()){
						try {
							//��������ת���ṩ�������������ASM���Ǹ�����Ĳ��֡�
							inst.retransformClasses(clazz);
						} catch (Throwable e) {
							System.out.println(name+" �޷�ʹ��retransform");
							e.printStackTrace();
						}
					}
				}
			}
		}
	}	
	
	/**
	 * ����transformerClassInfoMap���ҵ���δ��ע����ࡣ
	 * @param classname
	 * @param classfileBuffer
	 * @return
	 */
	public byte[] getClassTransform(String classname, byte[] classfileBuffer){
		
		//���Ҫ�ҵ�����transformerClassInfoMap������һЩ����
		for(Map.Entry<String,TranformConfig> entry:transformerClassInfoMap.entrySet()){
			
				String id = entry.getKey();
				TranformConfig info = entry.getValue();
				String classPatternName = info.getClassPatternName();
				String methodPattern = info.getMethodPatternName();
				Pattern p = Pattern.compile(classPatternName);
				if(p.matcher(classname).matches()){
					try{
						if(!classTransformed(classname)){	//��������δ��ע�������ִ�����´���
							//ʹ��ASM��ʼ����
							ClassReader reader = new ClassReader(classfileBuffer);
							ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
							
							ClassAdapter adapter = null;
							if(info.getType() == TranformConfig.MONITOR_FIELD) {
								adapter = new FieldBtraceClassAdapter(writer, classname, methodPattern, 
										info.getTransformerMethod(), id, info.getFieldName(), info.isStatic(), this);
							} else {
								adapter = new MethodBtraceClassAdapter(writer, classname, methodPattern,
										info.getTransformerMethod(), id, this);
							}
							
							reader.accept(adapter, 0);
							byte[] result = writer.toByteArray();
							
							info.getTranformClassSet().add(classname);
							doCommand(new TransformerClassCommand(info.getId(),classname));
							
							return result;
						}
					}catch (Throwable e) {
						System.out.println("transform �쳣��"+classname);
						e.printStackTrace();
					}
				}
		}
		return classfileBuffer;
	}
	
	//���أ����Method��Ϣ�Ķ���	
	public static void acceptProfilerInfo(ProfilerInfo info){
		try {
			queue.put(info);
		} catch (InterruptedException e) {
		}
	}
	
	//���أ����Field��Ϣ�Ķ���
	public static void acceptProfilerInfo(FieldProfilerInfo info){
		try {
			fieldQueue.put(info);
		} catch (InterruptedException e) {
		}
	}	

	/**
	 * �������������ӵ������б���
	 */
	public static void addSingleToList() {
		
	}
	
}
