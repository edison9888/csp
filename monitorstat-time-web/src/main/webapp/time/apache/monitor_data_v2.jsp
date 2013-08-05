<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="java.lang.reflect.InvocationTargetException"%>
<%@ page import="java.lang.reflect.Method"%>

<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="net.sf.json.*"%>

<%
	MonitorDataV2 mdv2 = MonitorDataV2.get();
	//如果验证不通过返回
	if (!mdv2.validate(request)) {
		mdv2.writeToResponse(response, "");
		return;
	}

	Map<String, Object> parseResult = mdv2.parseParam(request);
	Object invokeResult = mdv2.invokeMethod(parseResult);
	mdv2.writeJSONToResponseJSONObject(response, invokeResult);
%>

<%!/**
	 *@author wb-lixing 2012-3-14 下午10:05:35
	 * 
	 解析参数，参数只有两种类型:String, String[] <br/>
	 使用json格式表示methodParam：[methodParam1, methodParam2, methodParam3, ... ...]<br/>
	 当某个methodParamN为数组时，同样也会用json格式来表示。<br/>
	 *         className要指定全类名
	 * 请求示例1：http://localhost:8100/monitor-data-v2/monitor_data_v2.jsp?className=com.A&methodName=a&methodParam=[%27i%20am%20param1%27]
	 请求示例2：http://localhost:8100/monitorstat-time-web/time/apache/monitor_data_v2.jsp?className=com.taobao.csp.dataserver.query.QueryUtil&methodName=queryRecentlyChildRealTime&methodParam=[%27detail%27,%27PV%27]
	 请求示例3：http://110.75.2.189:9999/monitorstat-time-web/time/apache/monitor_data_v2.jsp?className=com.taobao.csp.dataserver.query.QueryUtil&methodName=queryRecentlyChildRealTime&methodParam=[%27detail%27,%27PV%27]
	
	 */

	static class MonitorDataV2 {
		public static MonitorDataV2 fhi = new MonitorDataV2();

		private MonitorDataV2() {
		}

		public static MonitorDataV2 get() {
			return fhi;
		}

		/**Map<methodName,Map<paramIndex,paramClass>>
		paramIndex begin from 0
		 */
		/**
		public static Map<String,Map<String,Class>> fixedParamTypes = null;
		
		static {
			String jsonStr = "queryRecentlyHostRealTime:{2,'List'},queryRecentlyMultiRealTime";
			
				
		}
		 */

		/**只有这两个要把List转数组*/
		public static String[] fixedMethodNames = {
				"queryRecentlyMultiChildRealTime",
				"queryRecentlyMultiChildRealTime" };

		public static void main(String[] args) throws ClassNotFoundException,
				SecurityException, NoSuchMethodException {
			String methodParam = "['param1','param2',['param3_1', 'param3_2']]";

			// System.out.println(a.replaceAll("\\[|\\]", ""));
			JSONArray mpj = JSONArray.fromObject(methodParam);
			Object obj = JSONSerializer.toJava(mpj);

			List list = (List) obj;
			for (Object o : list) {
				System.out.println(o.getClass());
			}
			// Class clazz = Class.forName("");

			// clazz.getDeclaredMethod("m1");
		}

		/**
		 * 调用方法
		 * 
		 * @throws NoSuchMethodException
		 * @throws SecurityException
		 * @throws ClassNotFoundException
		 * @throws InvocationTargetException
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 */
		public Object invokeMethod(Map<String, Object> methodInfo)
				throws SecurityException, NoSuchMethodException,
				ClassNotFoundException, IllegalArgumentException,
				IllegalAccessException, InvocationTargetException {
			String className = (String) methodInfo.get("className");
			String methodName = (String) methodInfo.get("methodName");
			Object[] methodParamObjs = (Object[]) methodInfo
					.get("methodParamObjs");
			Class[] methodParamTypes = (Class[]) methodInfo
					.get("methodParamTypes");
			System.out.println("className: " + className);
			Class clazz = Class.forName(className);
			Method method = clazz.getDeclaredMethod(methodName,
					methodParamTypes);

			Object invokeResult = null;
			invokeResult = method.invoke(null, methodParamObjs);
			return invokeResult;

		}

		public Map<String, Object> parseParam(HttpServletRequest request) {
			Map<String, Object> parseResult = new HashMap<String, Object>();

			String className = request.getParameter("className");
			String methodName = request.getParameter("methodName");

			String methodParam = request.getParameter("methodParam");
			JSONArray mpj = JSONArray.fromObject(methodParam);
			List methodParamList = (List) JSONSerializer.toJava(mpj);
			// 方法参数 值
			Object[] methodParamObjs = new Object[methodParamList.size()];

			for (int i = 0; i < methodParamList.size(); i++) {
				Object o = methodParamList.get(i);
				if (o.getClass().equals(ArrayList.class)
						&& contains(fixedMethodNames, methodName)) {
					//转Array
					List<String> l2 = (List<String>) o;
					String[] a2 = new String[l2.size()];
					l2.toArray(a2);
					methodParamObjs[i] = a2;
				} else {
					methodParamObjs[i] = o;
				}
			}

			// 方法参数 类型
			Class[] methodParamTypes = new Class[methodParamObjs.length];
			for (int i = 0; i < methodParamObjs.length; i++) {
				Object o = methodParamObjs[i];
				Class c = o.getClass();
				if (c.equals(ArrayList.class)
						&& !contains(fixedMethodNames, methodName)) 
					methodParamTypes[i] = List.class ;
				else
					methodParamTypes[i] = c ;
			}
			parseResult.put("className", className);
			parseResult.put("methodName", methodName);
			parseResult.put("methodParamObjs", methodParamObjs);
			parseResult.put("methodParamTypes", methodParamTypes);
			return parseResult;
		}

		/**
		 * 验证参数
		 * 
		 * @return false 没有通过验证
		 * */

		public boolean validate(HttpServletRequest request) {
			String className = request.getParameter("className");
			String methodName = request.getParameter("methodName");
			String methodParam = request.getParameter("methodParam");

			if (isBlank(className) || isBlank(methodName)
					|| isBlank(methodParam)) {
				return false;
			}
			return true;
		}

		public boolean isBlank(String str) {
			return str == null || str.trim().equals("");
		}

		public void writeToResponse(HttpServletResponse response, String str)
				throws IOException {
			PrintWriter out = response.getWriter();
			out.print(str);
			out.flush();
		}

		public void writeJSONToResponse(HttpServletResponse response, String str)
				throws IOException {
			response.setContentType("application/json");
			writeToResponse(response, str);
		}

		public void writeJSONToResponseJSONObject(HttpServletResponse response,
				Object obj) throws IOException {

			JSONObject jsonObj = JSONObject.fromObject(obj);
			String jsonStr = jsonObj.toString();
			writeJSONToResponse(response, jsonStr);
		}

		public static boolean contains(String[] arr, String str) {
			for (int i = 0; i < arr.length; i++) {
				if (arr[i].equals(str)) {
					return true;
				}
			}
			return false;
		}
	}%>