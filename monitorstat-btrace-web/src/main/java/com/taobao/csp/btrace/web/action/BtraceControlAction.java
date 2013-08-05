
package com.taobao.csp.btrace.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.btrace.core.FieldProfilerInfo;
import com.taobao.csp.btrace.core.MethodInfo;
import com.taobao.csp.btrace.core.ProfilerInfo;
import com.taobao.csp.btrace.core.ThreadProfilerData;
import com.taobao.csp.btrace.core.TranformConfig;
import com.taobao.csp.btrace.core.server.BtraceHandle;
import com.taobao.csp.btrace.core.server.BtraceServer;
import com.taobao.csp.btrace.core.server.FieldBtraceServerProfiler;
import com.taobao.csp.btrace.core.server.FieldMapWraper;
import com.taobao.csp.btrace.core.server.MethodExecuteInfo;
import com.taobao.csp.btrace.core.server.MethodParamExecuteInfo;
import com.taobao.csp.btrace.po.ClassInfo;
import com.taobao.csp.btrace.web.Constants;
import com.taobao.csp.btrace.web.PidInfo;
import com.taobao.csp.btrace.web.util.BaseSsh2Exec;
import com.taobao.csp.btrace.web.util.RemoteCommonUtil;
import com.taobao.csp.btrace.web.util.RemoteCommonUtil.CallBack;

/**
 * 
 * @author xiaodu
 * @version 2011-8-24 下午04:30:47
 */
@Controller
@RequestMapping("/btrace/control.do")
public class BtraceControlAction extends BtraceBaseAction {

	Logger logger = Logger.getLogger(BtraceControlAction.class);

	@Resource(name="constants")
	private Constants constants;

	//处理类
	private final String handleMethodClass = "com.taobao.csp.btrace.core.script.ShowParametersMethod";
	private final String handleFieldClass = "com.taobao.csp.btrace.core.script.ShowFieldMethod";
	private final String handleStaticField = "StaticField";

	private final String successString = "success"; 
	private final String failureString = "failure"; 

	@RequestMapping(params="method=login")
	public ModelAndView login(String ip, String userName, String password) {
		ModelAndView model = new ModelAndView("/btrace/mainpage");	//跳到主显示页面
		model.addObject("ip", ip);
		model.addObject("userName", userName);
		model.addObject("password", password);
		logger.debug("test");
		return model;
	}	

	@RequestMapping(params="method=showPids")
	public void showHostPid(String ip, String userName, String password, HttpServletResponse response){

		//show jstack干什么的不清楚

		//		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		//		if(handle != null){	//可能就是这句导致的页面不存在
		//			return intoShowJstack(ip);
		//		}
		//
		//		if(!RemoteCommonUtil.authenticate(ip, userName, password)){
		//			ModelAndView model = new ModelAndView("/btrace/btrace_show_pid");
		//			model.addObject("message", "登陆失败!");
		//			return model;
		//		}
		JSONObject object = new JSONObject();		
		if(ip == null || ip == "") {	//第一次进入BtraceControlAction页面时，ip,userName,password 等信息可能为空
			object.put(successString, false);	
			object.put("message", "IP为空！");
			writeResponse(object.toString(), response);
			return ;	
		}

		if(!RemoteCommonUtil.authenticate(ip, userName, password)){
			object.put(successString, false);	
			object.put("message", "登陆失败！");
			writeResponse(object.toString(), response);
			return ;	
		}		

		final List<PidInfo> infoList = new ArrayList<PidInfo>();
		try {
			RemoteCommonUtil.excute(ip, userName, password, "ps -ef|grep java|grep -v 'grep'", new CallBack(){
				@Override
				public void doLine(String line) {
					PidInfo info = new PidInfo();
					line = line.replaceAll(" +", " ");
					String[] lines = line.split(" ");
					info.setProcessId(Integer.parseInt(lines[1]));
					info.setProcessUserName(lines[0]);
					info.setProcessDesc(line);
					infoList.add(info);
				}});
		} catch (Exception e) {
			logger.error("获取进程信息出错", e);
			//e.printStackTrace();
		}					

		object.put(successString, true);
		object.put("root", infoList);
		object.put("total", infoList.size());
		logger.debug(object.toString());
		System.out.println(object.toString());
		logger.debug(object.toString());
		//查询结果Ajax返回前台
		writeResponse(object.toString(), response);
		return ;
	}


	@RequestMapping(params="method=injectTrace")
	public void injectTrace(String ip, String pid, String userName, String password, HttpServletResponse response){


		JSONObject obj = new JSONObject();
		String key = "message";

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle != null) {
			obj.put(key, "already");
			//return intoShowJstack(ip);
			writeResponse(obj.toString(), response);
			return;
		}

		String clientBtraceName = "monitorstat-btrace-"+constants.getBtraceVersion()+".jar";
		String webInfoLibPath = BtraceControlAction.class.getResource("/").getPath();

		String localBtracePath = webInfoLibPath.substring(0, webInfoLibPath.lastIndexOf("WEB-INF")+7)+"/lib/"+clientBtraceName;
		String localBtraceSh = webInfoLibPath.substring(0, webInfoLibPath.lastIndexOf("WEB-INF")+7)+"/classes/btrace.sh";

		String localIp = constants.getBtraceServerIp();

		//comment 注释
		//		if(handle != null ){
		//			model.setViewName("/btrace/btrace_show_stack");
		//			model.addObject("ip", ip);
		//		}else{
		final StringBuffer sb = new StringBuffer();
		try {
			RemoteCommonUtil.sendFile(ip, userName, password, localBtracePath, "/tmp/");

			RemoteCommonUtil.sendFile(ip, userName, password, localBtraceSh, "/tmp/");

			System.out.println("#执行命令#" + "chmod 777 /tmp/"+clientBtraceName);
			RemoteCommonUtil.excute(ip, userName, password, "chmod 777 /tmp/"+clientBtraceName, new CallBack(){
				@Override
				public void doLine(String line) {
				}

			});
			System.out.println("#执行命令#" + "chmod 777 /tmp/btrace.sh");
			RemoteCommonUtil.excute(ip, userName, password, "chmod 777 /tmp/btrace.sh", new CallBack(){
				@Override
				public void doLine(String line) {
				}

			});


			BaseSsh2Exec exec = new BaseSsh2Exec(ip, userName, password);
			System.out.println("#执行命令#" + "sh /tmp/btrace.sh  /tmp/"+clientBtraceName+" "+pid+" "+localIp);
			String message = exec.doCommand("sh /tmp/btrace.sh  /tmp/"+clientBtraceName+" "+pid+" "+localIp);

			if(message.indexOf("BTRACE INJECT SUCCESS")>-1){
				obj.put(key, successString);
				//model.addObject("injectMessage", "success");
			}else{
				obj.put(key, failureString);
				//model.addObject("injectMessage", message);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		writeResponse(obj.toString(), response);

	}


	@RequestMapping(params="method=waitInject")
	public void waitInject(String ip,HttpServletResponse response){
		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		Map<String, String> map = new HashMap<String, String>();
		if(handle != null ){
			map.put("message", "ok");
		}else{
			map.put("message", "wait");
		}
		JSONObject json = JSONObject.fromObject(map);

		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
		}
		return ;
	}

	@RequestMapping(params="method=cleanThreadStack")
	public void cleanThreadStack(String ip,String threadId,HttpServletResponse response){
		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle !=null){
			try{
				ThreadProfilerData[] datas = handle.getBtraceServerProfiler().getThreadProfiler();

				String[] t = threadId.split("_");
				if(t.length ==2){
					int num = Integer.parseInt(t[0]);
					if(datas[num]!=null){
						datas[num].infoQueue.clear();
					}
				}
			}catch (Exception e) {
			}
		}

		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write("成功");
			response.flushBuffer();	
		} catch (IOException e) {
		}
		return ;

	}

	@RequestMapping(params="method=deleteTranformClass")
	public void deleteTranformClass(String tranformId, String ip, HttpServletResponse response){
		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle != null ){
			try {
				handle.removeTransformer(tranformId);
			} catch (Exception e) {
			}
		}

		JSONObject obj = new JSONObject();
		obj.put("message", successString);
		writeResponse(obj.toString(), response);
	}

	/**
	 * 注入方法
	 * @param ip				要监控的JVM的IP
	 * @param className			要监控的Class的名称
	 * @param methodName		要监控的方法的名称（正则支持考虑中）	
	 * @param response			Ajax返回的输出流
	 */
	@RequestMapping(params="method=tranformMethod")	
	public void tranformMethod(String ip, String className, String methodName, HttpServletResponse response){

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		String errorMsg = null;		//返回的异常信息

		String transformerClass = this.handleMethodClass;	//根据用户的输入选择不同的处理类


		boolean flag = false;
		if(handle != null ){
			try {
				//查看方法
				flag = handle.sendTransformer(className, methodName, transformerClass, null,
						false, BtraceHandle.METHOD_TYPE);
				if(!flag) {
					errorMsg = "Server与JVM(IP:" + ip + ")的Socket连接尚未建立！";
				} 
			} catch (Exception e) {
				errorMsg = "添加出现异常" + e.toString();
			}
		}else{
			errorMsg = ip + "已经断开连接!";
		}

		if(errorMsg != null) {
			logger.error(errorMsg);
		} else {
			errorMsg = successString;
		}

		JSONObject obj = new JSONObject();
		obj.put("message", errorMsg);
		String fieldUrl = "control.do?method=showMethodInfo&ip=%s&tarClassname=%s&tarMethodname=%s";
		fieldUrl = String.format(fieldUrl, ip, className, methodName);
		obj.put("pageURL", fieldUrl);
		logger.debug("弹出窗口的URL" + fieldUrl);
		writeResponse(obj.toString(), response);
	}
	/**
	 * 注入属性
	 * @param ip				要监控的JVM的IP
	 * @param className			要监控的Class的名称
	 * @param methodName		要监控的方法的名称（正则支持考虑中）	
	 * @param fieldName			属性名称，可以为null	
	 * @param isStatic			属性是否是静态属性	
	 * @param response			Ajax返回的输出流
	 */
	@RequestMapping(params="method=tranformField")
	public void tranformField(String ip, String className, String methodName, String fieldName, String isStatic, 
			HttpServletResponse response) {

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		String errorMsg = null;		//返回的异常信息

		String transformerClass = null;	//根据用户的输入选择不同的处理类
		int transformType = 0;

		if(methodName == null || methodName.trim().equals("")) {	//单例
			transformerClass = this.handleStaticField;
			transformType = BtraceHandle.SINGLETON_FIELD_TYPE;
		} else {	
			transformerClass = this.handleFieldClass;
			transformType = BtraceHandle.PROTOTYPE_FIELD_TYPE;
		}

		boolean bIsStatic = Boolean.valueOf(isStatic);	//是否静态

		boolean flag = false;
		if(handle != null ) {
			try {
				flag = handle.sendTransformer(className, methodName, transformerClass, fieldName , bIsStatic, transformType);
				if(!flag) {
					errorMsg = "Server与JVM(IP:" + ip + ")的Socket连接尚未建立！";
				} 
			} catch (Exception e) {
				errorMsg = "添加出现异常" + e.toString();
			}
		}else{
			errorMsg = ip + "已经断开连接!";
		}

		if(errorMsg != null) {
			logger.error(errorMsg);
		} else {
			errorMsg = successString;
		}

		JSONObject obj = new JSONObject();
		obj.put("message", errorMsg);
		String fieldUrl = "control.do?method=showFieldInfo&ip=%s&tarClassname=%s&tarFieldname=%s&tarMethodname=%s";
		fieldUrl = String.format(fieldUrl, ip, className, fieldName, methodName);
		obj.put("pageURL", fieldUrl);
		logger.debug("弹出窗口的URL" + fieldUrl);
		writeResponse(obj.toString(), response);
	}

	/**
	 * 断开远程注入程序
	 * @param ip
	 * @return
	 */
	@RequestMapping(params="method=stopBtrace")
	public void stopBtrace(String ip, HttpServletResponse response){

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle != null ){
			handle.stop();
			//如果停止注入，那么也应该移除所有已经注入的Class
			handle.clearTransformerMap();
		} 
		
		
		JSONObject obj = new JSONObject();
		obj.put("message", successString);
		writeResponse(obj.toString(), response);
	}



	@RequestMapping(params="method=intoShowJstack")
	public ModelAndView intoShowJstack(String ip){

		ModelAndView model = new ModelAndView("/btrace/btrace_show_stack");
		model.addObject("ip", ip);
		return model;

	}


	@RequestMapping(params="method=showJavaStack")
	public void showJavaStack(String ip,int threadNum,HttpServletResponse response){

		if(threadNum==0){
			threadNum = 1;
		}

		Map<String,List<ProfilerInfo>> map = new HashMap<String, List<ProfilerInfo>>();


		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle !=null){
			ThreadProfilerData[] datas = handle.getBtraceServerProfiler().getThreadProfiler();

			int num = 1;

			for(int i=0;i<datas.length;i++){
				ThreadProfilerData data= datas[i];
				if(data != null&&System.currentTimeMillis()-data.lastTime <150*60*1000){
					if(num == threadNum){
						List<ProfilerInfo> profilerInfoList = new ArrayList<ProfilerInfo>();

						Stack<ProfilerInfo> q = data.infoQueue;

						List<ProfilerInfo> list = new ArrayList<ProfilerInfo>();
						list.addAll(q);
						for(ProfilerInfo info :list){
							profilerInfoList.add(info);
						}
						map.put(i+"_Thread", profilerInfoList);

						break;
					}					
					num++;
				}
			}

		}


		JSONObject json = JSONObject.fromObject(map);

		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
		}
		return ;

	}

	/**
	 * 显示历史的传输指令
	 * @param ip
	 * @param response
	 */
	@RequestMapping(params="method=showTransformerMessage")
	public void showTransformerMessage(String ip,HttpServletResponse response){
		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle !=null){
			Map<String,TranformConfig> map = handle.getTransformerMap();


			JSONObject json = JSONObject.fromObject(map);

			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(json.toString());
				response.flushBuffer();	
			} catch (IOException e) {
			}
			return ;

		}
	}

	/**
	 * 显示当前虚拟机被注入的类
	 * @param ip
	 * @param response
	 */
	@RequestMapping(params="method=showTransformerClass")
	public void showTransformerClass(String ip,HttpServletResponse response){
		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		JSONObject object = new JSONObject();

		//		if(handle != null){
		//			Map<String,TranformConfig> map = handle.getTransformerMap();
		//			ArrayList<ClassInfo> list = new ArrayList<ClassInfo>();
		//			Set<String> keyset = map.keySet();
		//			for(String tranformId : keyset) {
		//				TranformConfig config = map.get(tranformId);
		//				if(config != null){
		//					Set<String> tranformClassSet = config.getTranformClassSet();
		//					for(String className : tranformClassSet) {
		//						list.add(new ClassInfo(tranformId, className));
		//					}				
		//				}
		//			}
		//			
		//			object.put("success", true);
		//			object.put("root", list);
		//			object.put("total", list.size());
		//		} else {
		//			object.put("success", false);
		//		}
		ArrayList<ClassInfo> list = new ArrayList<ClassInfo>();
		
		if(handle != null){
			Map<String,TranformConfig> map = handle.getTransformerMap();
			Set<String> keyset = map.keySet();
			for(String tranformId : keyset) {
				TranformConfig config = map.get(tranformId);
				Set<String> tranformClassSet = config.getTranformClassSet();
				//Set只是个反馈，直接判断是否非NULL即可
				String className = config.getClassPatternName();
				boolean isChanged = tranformClassSet != null; 
				list.add(new ClassInfo(tranformId, className, isChanged));
			}
		} 
		
		object.put(successString, true);
		object.put("root", list);
		object.put("total", list.size());
		
		logger.info(object.toString());
		this.writeResponse(object.toString(), response);
	}

	@RequestMapping(params="method=showJavaThreadNum")
	public void showJavaThreadNum(String ip,HttpServletResponse response){

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("ThreadNum", 0);
		if(handle !=null){
			ThreadProfilerData[] datas = handle.getBtraceServerProfiler().getThreadProfiler();
			int num = 0;
			for(int i=0;i<datas.length;i++){
				ThreadProfilerData data= datas[i];
				if(data != null){								
					num++;
				}
			}
			map.put("ThreadNum", num);
		}
		JSONObject json = JSONObject.fromObject(map);

		response.setContentType("text/html;charset=utf-8");
		try {
			response.getWriter().write(json.toString());
			response.flushBuffer();	
		} catch (IOException e) {
		}
		return ;
	}



	@RequestMapping(params="method=showMethodExecuteInfo")
	public void showMethodExecuteInfo(String ip,HttpServletResponse response){

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle !=null){
			Map<String,MethodExecuteInfo> mmap = handle.getBtraceServerProfiler().getMethodExecuteMap();

			JSONObject json = JSONObject.fromObject(mmap);
			System.out.println("----222" + json.toString());
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(json.toString());
				response.flushBuffer();	
			} catch (IOException e) {
			}
			return ;
		}
	}


	@RequestMapping(params="method=showParamMethodExecuteInfo")
	public void showParamMethodExecuteInfo(String ip,HttpServletResponse response){

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle !=null){
			List<MethodParamExecuteInfo> list = handle.getBtraceServerProfiler().getParamExecuteList();

			JSONArray json = JSONArray.fromObject(list);
			System.out.println(json.toString());
			response.setContentType("text/html;charset=utf-8");
			try {
				response.getWriter().write(json.toString());
				response.flushBuffer();	
			} catch (IOException e) {
			}
			return ;
		}
	}

	@RequestMapping(params="method=recordMethod")
	public void recordMethod(int methodId,String ip,HttpServletResponse response){
		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle !=null){
			handle.getBtraceServerProfiler().recordMethod(methodId);

			ThreadProfilerData[] datas = handle.getBtraceServerProfiler().getThreadProfiler();
			for(ThreadProfilerData data:datas){
				if(data !=null&&data.infoQueue.size()>0){
					Stack<ProfilerInfo> q = data.infoQueue;
					List<ProfilerInfo> list = new ArrayList<ProfilerInfo>();
					list.addAll(q);

					for(ProfilerInfo info:list){
						if(methodId == info.getMethodId()){
							MethodInfo pMethod = info.getMethodInfo();


							JSONObject json = JSONObject.fromObject( pMethod);

							response.setContentType("text/html;charset=utf-8");
							try {
								response.getWriter().write(json.toString());
								response.flushBuffer();	
							} catch (IOException e) {
							}
							return ;


						}
					}

				}
			}
		}
	}


	/**
	 * 从Web Server内存中数据结构查询值
	 * @param ip
	 * @param tarClassname
	 * @param tarFieldname
	 * @param tarMethodname
	 * @return
	 */
	@RequestMapping(params="method=showFieldInfo")
	public ModelAndView showFieldInfo(String ip, String tarClassname, String tarFieldname, String tarMethodname) {

		ModelAndView model = new ModelAndView();

		BtraceHandle handle = BtraceServer.get().getBtraceHandle(ip);
		if(handle != null) {
			model.addObject("findClass", successString);
			
			String key = FieldBtraceServerProfiler.generateKey(tarClassname,tarMethodname,tarFieldname);
			FieldMapWraper wraper = handle.getFieldBtraceServerProfiler().getClassMap().get(key);
			if(wraper != null) {
				HashMap<String, FieldProfilerInfo> fieldMap = wraper.getFieldMap();	//以ThreadID为主键
				System.out.println(fieldMap);

				Collection<FieldProfilerInfo> collection = fieldMap.values();
				FieldProfilerInfo[] objArray = (FieldProfilerInfo[]) collection.toArray(new FieldProfilerInfo[1]);
				List<FieldProfilerInfo> fieldList = Arrays.asList(objArray);
				System.out.println(fieldList.toString());				
				model.addObject("fieldList", fieldList);
			}
		} else {
			model.addObject("findClass", failureString);
		}

		//		List<FieldProfilerInfo> fieldList = new ArrayList();
		//		try {
		//			FieldProfilerInfo info = new FieldProfilerInfo();
		//			System.out.println(info);
		//			info.setStartValue("ddddddddd");
		//			fieldList.add(info);
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
		//		System.out.println(fieldList.toString());				
		//		model.addObject("fieldList", fieldList);

		model.setViewName("/btrace/fieldpage");
		model.addObject("ip", ip);
		model.addObject("tarClassname", tarClassname);
		model.addObject("tarFieldname", tarFieldname);
		model.addObject("tarMethodname", tarMethodname);


		return model;
	}


	//	public static void main(String[] args) {
	//		//测试json
	//		Set<String> tranformClassSet = new HashSet<String>();
	//		tranformClassSet.add("123");
	//		tranformClassSet.add("abc");
	//		
	//		JSONObject object = new JSONObject();
	//		object.put("success", true);
	//		object.put("root", tranformClassSet);
	//		object.put("total", tranformClassSet.size());
	//		
	//		System.out.println(object.toString());
	//	}

}
