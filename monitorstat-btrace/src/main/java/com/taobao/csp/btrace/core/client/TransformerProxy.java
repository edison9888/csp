
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
 * @version 2011-8-16 下午02:25:32
 */
public class TransformerProxy {
	
	private Map<String,TranformConfig> transformerClassInfoMap = new ConcurrentHashMap<String, TranformConfig>();
	
	//单例的map自己单独维护
	private Map<String,TranformConfig> singletonMap = new ConcurrentHashMap<String, TranformConfig>();
	private final int FINAL_TIMETERVAL = 500;		//单位毫秒
	
//	ExecutorService pool = Executors.newFixedThreadPool(3);		//使用线程池貌似关闭线程有问题，暂时使用标记吧
	boolean isOn = true;		//开始默认为true
	
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
	
	//传入一个client端的socket，用来给server发消息，只是初始化而已，同时开启两个线程
	public TransformerProxy(Instrumentation inst,Socket clientSocket) throws IOException{
		this.inst = inst;
		this.former = new BtraceTransformer(this);	//注入Class的适合后需要使用
		this.clientSocket = clientSocket;
		
		this.oout = new ObjectOutputStream(clientSocket.getOutputStream());
		this.oin = new ObjectInputStream(clientSocket.getInputStream());
		
		//客户端开启一个线程，检测和WebServer的通讯
		Thread handle = new Thread(){
			
			public void run(){
				
				while(true){
					try {
						int flag = oin.readInt();
						if (flag != Command.TF_CLASS_FLAG) {//transfercommond的前缀
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
									//instrumentation的retransformClasses的方法的包装类
									retransformerClassName(cmd.getClassPatternName());
								} catch (ClassNotFoundException cnfe) {
									throw new IOException(cnfe);
								}								
								break;
							case Command.SINGLETON_CLASS:		//单例的情况
								SingletonFieldCommand singletonCmd = new SingletonFieldCommand();
								try {
									singletonCmd.read(oin);
									//应该不需要向线程中放
									TranformConfig info = new TranformConfig();
									info.setClassPatternName(singletonCmd.getClassPatternName());
									info.setMethodPatternName("");
									info.setTransformerMethod("");
									info.setFieldNameAndTypeByFieldName(singletonCmd.getFieldName());
									info.setStatic(singletonCmd.isStatic());
									info.setId(singletonCmd.getId());
									singletonMap.put(singletonCmd.getId(), info);
									//目前只能通过反射得到静态类的静态属性，isStatic暂不起作用
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
		
		//线程定期读取方法的信息
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
		
		//线程定期读取方法的信息
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
		
		//线程定期读取属性的信息
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
		
		singletonThread.start();	//singleton的Filed的轮训线程
		thread.start();				//Method发送线程
		thread2.start();			//Field发送线程
		
//		pool.execute(singletonThread);
//		pool.execute(thread);
//		pool.execute(thread2);

	}
	
	//重载，发送Method消息	
	public synchronized void doCommand(Command command){
		try {
			command.writeMessage(oout);
		} catch (Exception e) {
			termination();
		}
	}
	
	//重载，发送Field消息
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
	 * 退出 ，在socket 断开或接收到停止指令 ，释放资源
	 */
	public void termination(){
		unregisterTransformer();
		
		try {
			if(clientSocket.isConnected())
				clientSocket.close();
		} catch (IOException e) {
		}
		isOn = false;	//标记为false，终止3个轮训线程
		//pool.shutdownNow();		//关闭线程
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
	 * 添加一个需要进行 远程替换的classname，把"."转换为"/"
	 * @param className
	 */
	public void retransformerClassName(String classNamePattern) {
		
		if(inst.isRetransformClassesSupported()){
			//返回加载的所有Class的数组
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
							//跳到重新转换提供类那里，就是跳到ASM的那个代理的部分。
							inst.retransformClasses(clazz);
						} catch (Throwable e) {
							System.out.println(name+" 无法使用retransform");
							e.printStackTrace();
						}
					}
				}
			}
		}
	}	
	
	/**
	 * 遍历transformerClassInfoMap，找到还未被注入的类。
	 * @param classname
	 * @param classfileBuffer
	 * @return
	 */
	public byte[] getClassTransform(String classname, byte[] classfileBuffer){
		
		//如果要找的类在transformerClassInfoMap，则做一些处理
		for(Map.Entry<String,TranformConfig> entry:transformerClassInfoMap.entrySet()){
			
				String id = entry.getKey();
				TranformConfig info = entry.getValue();
				String classPatternName = info.getClassPatternName();
				String methodPattern = info.getMethodPatternName();
				Pattern p = Pattern.compile(classPatternName);
				if(p.matcher(classname).matches()){
					try{
						if(!classTransformed(classname)){	//如果这个类未被注入过，就执行如下代码
							//使用ASM开始调用
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
						System.out.println("transform 异常："+classname);
						e.printStackTrace();
					}
				}
		}
		return classfileBuffer;
	}
	
	//重载，存放Method信息的队列	
	public static void acceptProfilerInfo(ProfilerInfo info){
		try {
			queue.put(info);
		} catch (InterruptedException e) {
		}
	}
	
	//重载，存放Field信息的队列
	public static void acceptProfilerInfo(FieldProfilerInfo info){
		try {
			fieldQueue.put(info);
		} catch (InterruptedException e) {
		}
	}	

	/**
	 * 单例的情况，添加单例到列表中
	 */
	public static void addSingleToList() {
		
	}
	
}
