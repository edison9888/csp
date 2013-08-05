///**
// * 
// */
//package com.taobao.csp.time;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.Field;
//import java.net.URLDecoder;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.Map.Entry;
//
//import javax.script.ScriptException;
//
//import net.sf.ezmorph.bean.MorphDynaBean;
//import net.sf.ezmorph.object.MapToDateMorpher;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import net.sf.json.JSONSerializer;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//
//import com.taobao.csp.dataserver.Constants;
//import com.taobao.csp.dataserver.memcache.entry.DataEntry;
//import com.taobao.csp.dataserver.merge.BaseQueryData;
//import com.taobao.csp.dataserver.merge.MergeServerData;
//import com.taobao.csp.dataserver.packet.ResponsePacket;
//import com.taobao.csp.dataserver.packet.request.standard.QueryChildRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QueryHostRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QueryMultiRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyChildRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyHostRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlyMultiRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QueryRecentlySingleRequest;
//import com.taobao.csp.dataserver.packet.request.standard.QuerySingleRequest;
//import com.taobao.csp.dataserver.packet.response.standard.QueryChildResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QueryHostResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QueryMultiResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyChildResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyHostResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlyMultiResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QueryRecentlySingleResponse;
//import com.taobao.csp.dataserver.packet.response.standard.QuerySingleResponse;
//import com.taobao.csp.dataserver.util.Util;
//
///**
// *@author wb-lixing 2012-3-19 ����05:00:20
// */
//public class QueryUtil {
//
//	public static void main(String[] args) throws Exception {
//		Map map = QueryUtil.queryChildRealTime("detail", "PV");
//		System.out.println("-------------map: "+ map);
//	}
//	
//	/**
//	 * ����ĳ��key��ĳ̨�����ϵĵ�������key�Ĺ�ϵ�����صĽṹ��
//	 * <childkey, <collecttime, <properties,value>>
//	 * @param keyName
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, DataEntry>> queryRecentlyChildHostRealTime(String appName,String key,String ip) throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		paramList.add(ip);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, DataEntry>>) p1(methodName, paramList
//				.toArray());
//	}
//	
//	/**
//	 * ����ĳ��key��ĳ̨�����ϵĵ�������key�Ĺ�ϵ�����صĽṹ��
//	 * <childkey, <collecttime, <properties,value>>
//	 * @param keyName
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, Map<String, Object>>> queryChildHostRealTime(String appName,String key,String ip) throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		paramList.add(ip);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, Map<String, Object>>>) p1(methodName, paramList
//				.toArray());
//	}
//	
//	
//	/**
//	 * ����ĳ��key��������key�Ĺ�ϵ�����صĽṹ��
//	 * <childkey, <properties,value>>
//	 * @param keyName
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, DataEntry>> queryRecentlyChildRealTime(String appName,String key) throws Exception{
//
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, DataEntry>>) p1(methodName, paramList
//				.toArray());
//	} 
//	
//	/**
//	 * ���Ҷ��key��������key�Ĺ�ϵ�����صĽṹ��
//	 * <childkey, <properties,value>>
//	 * @param keyName
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiChildRealTime(String appName,String[] keys) throws Exception{
//			
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(keys);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, DataEntry>>) p1(methodName, paramList
//				.toArray());
//	} 
//	
//	
//	
//	
//	/**
//	 * ��������ѯ��������,���������ֵ
//	 * @param keyName
//	 * @param ipList
//	 * @param columnNames
//	 * @return				<ip,<propertyName,value>>
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, DataEntry>> queryRecentlyHostRealTime(String appName,String key, List<String> ipList) throws Exception{
//
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		paramList.add(ipList);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, DataEntry>>) p1(methodName, paramList
//				.toArray());
//	} 
//	
//	
//	
//	/**
//	 * �ṩ���ʻ������ݽӿ�ʵ�� ,���������һ��ֵ
//	 * @param keyName	��key��ѯ��key������
//	 * @return			<propertyName,value>
//	 * @throws Exception
//	 */
//	public static Map<String, DataEntry> queryRecentlySingleRealTime(String appName,String key) throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, DataEntry>) p1(methodName, paramList
//				.toArray());
//	} 
//	
//	
//	/**
//	 * ͬʱ������Ӧ�õĶ��key ����ʱֻ���������һ��ֵ
//	 * @param apps
//	 * @param keys
//	 * @return 	
//	 * valueMap�ṹ��<key,<propertyName,value>>
//	 * key: ��Ҫ��ȡ��key������
//	 * propertyName:	����������
//	 * value:			��Ϥֵ������ʱ������
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiRealTime(String appName,List<String> keys)throws Exception{
//		
//		List<String> apps = new ArrayList<String>();
//		apps.add(appName);
//		return queryRecentlyMultiRealTime(apps,keys);
//		
//	}
//
//	
//	
//	/**
//	 * ͬʱ������Ӧ�õĶ��key ����ʱֻ���������һ��ֵ
//	 * @param apps
//	 * @param keys
//	 * @return 	
//	 * valueMap�ṹ��<key,<propertyName,value>>
//	 * key: ��Ҫ��ȡ��key������
//	 * propertyName:	����������
//	 * value:			��Ϥֵ������ʱ������
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, DataEntry>> queryRecentlyMultiRealTime(List<String> apps,List<String> keys)throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(apps);
//		paramList.add(keys);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, DataEntry>>) p1(methodName, paramList
//				.toArray());
//	}
//	
//	
//	
//	
//	/**
//	 * ͬʱ������Ӧ�õĶ��key
//	 * @param apps
//	 * @param keys
//	 * @return 	
//	 * valueMap�ṹ��<key,<time,<propertyName,value>>>
//	 * key: ��Ҫ��ȡ��key������
//	 * time:			�ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm
//	 * propertyName:	����������
//	 * value:			��Ϥֵ������ʱ������
//	 *
//	 * @throws Exception
//	 */
//	public static Map<String,Map<String, Map<String, Object>>> queryMultiRealTime(List<String> apps,List<String> keys)throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(apps);
//		paramList.add(keys);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, Map<String, Object>>>) p1(methodName, paramList
//				.toArray());
//		
//	}
//	/**
//	 * ͬʱ����һ��Ӧ�õĶ��key
//	 * @param apps
//	 * @param keys
//	 * @return 	
//	 * valueMap�ṹ��<key,<time,<propertyName,value>>>
//	 * key: ��Ҫ��ȡ��key������
//	 * time:			�ռ�ʱ�䣬��ʽΪ:yyyyMMddHHmm
//	 * propertyName:	����������
//	 * value:			��Ϥֵ������ʱ������
//	 *
//	 * @throws Exception
//	 */
//	public static Map<String,Map<String, Map<String, Object>>> queryMultiRealTime(String appName,List<String> keys)throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(keys);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, Map<String, Object>>>) p1(methodName, paramList
//				.toArray());
//		
//	}
//	
//	
//	/**
//	 * �ṩ���ʻ������ݽӿ�ʵ�� 
//	 * @param keyName	��key��ѯ��key������
//	 * @return			<time,<propertyName,value>>
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, Object>> querySingleRealTime(String appName,String key) throws Exception{
//		
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, Object>>) p1(methodName, paramList
//				.toArray());
//	} 
//
//	
//	/**
//	 * ��������ѯ��������
//	 * @param keyName
//	 * @param ipList
//	 * @param columnNames
//	 * @return				<ip,<time,<propertyName,value>>>
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, Map<String, Object>>> queryHostRealTime(String appName,String key, List<String> ipList) throws Exception{
//
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		paramList.add(ipList);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, Map<String, Object>>>) p1(methodName, paramList
//				.toArray());
//	} 
//	
//	/**
//	 * ����ĳ��key��������key�Ĺ�ϵ�����صĽṹ��
//	 * <childkey, <collecttime, <properties,value>>
//	 * @param keyName
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, Map<String, Object>>> queryChildRealTime(String appName,String key) throws Exception{
//
//		List<Object> paramList = new ArrayList<Object>();
//		paramList.add(appName);
//		paramList.add(key);
//		String methodName = Thread.currentThread().getStackTrace()[1]
//				.getMethodName();
//		return (Map<String, Map<String, Map<String, Object>>>) p1(methodName, paramList
//				.toArray());
//	} 
//	
//	
//	/**
//	 * ���Ҷ��key��������key�Ĺ�ϵ�����صĽṹ��
//	 * <childkey, <collecttime, <properties,value>>
//	 * @param keyName
//	 * @throws Exception
//	 */
//	public static Map<String, Map<String, Map<String, Object>>> queryMultiChildRealTime(String appName,String[] keys) throws Exception{
//
//		
//		Map<String, Map<String, Map<String, Object>>> map = new HashMap<String, Map<String,Map<String,Object>>>();
//		
//		for(String k:keys){
//			Map<String, Map<String, Map<String, Object>>> m = queryChildRealTime(appName,k);
//			map.putAll(m);
//		}
//		
//		return map;
//	} 
//
//
//	
//
//	/**
//	 * 1����������װΪurl 2�����ݷ��صĴ���ת��Ϊ�����Java����
//	 * 
//	 * @throws IOException
//	 * @throws ScriptException
//	 * @throws IllegalAccessException
//	 * @throws NoSuchFieldException
//	 * @throws IllegalArgumentException
//	 * @throws SecurityException
//	 */
//	public static Object p1(String methodName, Object[] params)
//			throws ScriptException, IOException, SecurityException,
//			IllegalArgumentException, NoSuchFieldException,
//			IllegalAccessException {
//		String url = assembleParamsToUrl(methodName, params);
//		String respJsonStr = process(url);
//		Map map1 = backToJava(methodName, respJsonStr);
//		someMapToDataEntry(methodName, map1);
//
//		return map1;
//	}
//
//	/**
//	 * ���ݷ���������ĳЩmapתΪDataEntry
//	 * 
//	 * @author wb-lixing 2012-3-22 ����05:36:07
//	 *@param map
//	 */
//	private static void someMapToDataEntry(String methodName, Map sourceMap) {
//		if (contains(methodNames[0], methodName)) {
//			mapToDataEntryKind2(sourceMap);
//		} else if (contains(methodNames[1], methodName)) {
//			mapToDataEntryKind1(sourceMap);
//		} 
//		
//	}
//	/**
//	 * Map<String,Map<String,Object>>  -->  Map<String, DataEntry>
//	 */
//	public static void mapToDataEntryKind1(Map map) {
//		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
//			Map.Entry entry = (Entry) iter.next();
//			Map map1 = (Map) entry.getValue();
//			DataEntry de = mapToDataEntry(map1);
//			map.put(entry.getKey(), de);
//		}
//	}
//	
//	/**
//	 *Map<String, Map<String, Map<String,Object>>  --> Map<String, Map<String, DataEntry>>  
//	 * 
//	 */
//	public static void mapToDataEntryKind2(Map map1) {
//		for (Iterator iter = map1.entrySet().iterator(); iter.hasNext();) {
//			Map.Entry entry = (Entry) iter.next();
//			Map map2 = (Map) entry.getValue();
//			mapToDataEntryKind1(map2);
//		}
//	}
//	public static DataEntry mapToDataEntry(Map map) {
//		DataEntry de = new DataEntry();
//		de.setTime((Long) map.get("time"));
//		de.setValue(map.get("value"));
//		return de;
//	}
//
//	/**
//	 *@author wb-lixing 2012-3-22 ����04:22:04
//	 *@param methodName
//	 *@param respJsonStr
//	 * @throws IllegalAccessException
//	 * @throws NoSuchFieldException
//	 * @throws IllegalArgumentException
//	 * @throws SecurityException
//	 */
//	private static Map backToJava(String methodName, String respJsonStr)
//			throws SecurityException, IllegalArgumentException,
//			NoSuchFieldException, IllegalAccessException {
//		JSONObject jo = JSONObject.fromObject(respJsonStr);
//		Object obj = JSONSerializer.toJava(jo);
//		MorphDynaBean mdb = (MorphDynaBean) obj;
//		Map map = (Map) JSONUtil.replaceDynaBeanToMap(mdb);
//
//		return map;
//	}
//
//	/**
//	 * 1����һ�δ���ʱ��obj����Ϊnull 2�����̣� �ж϶���
//	 * 
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 */
//	public static Map recurHandle(Object obj) throws SecurityException,
//			NoSuchFieldException, IllegalArgumentException,
//			IllegalAccessException {
//
//		if (MorphDynaBean.class.equals(obj.getClass())) {
//			MorphDynaBean bean = (MorphDynaBean) obj;
//			Field f = MorphDynaBean.class.getField("dynaValues");
//			f.setAccessible(true);
//			Object innerObj = (Object) f.get(bean);
//			if (innerObj == null) {
//				return null;
//			}
//
//			recurHandle(innerObj);
//
//		} else {
//			// List����
//			List list = (List) obj;
//
//			if (list != null && list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
//					Object innerObj = list.get(i);
//					MorphDynaBean bean = (MorphDynaBean) innerObj;
//					recurHandle(obj);
//				}
//
//			}
//
//		}
//		return null;
//	}
//
//	public static Map dynaBeanToMap(MorphDynaBean bean)
//			throws SecurityException, NoSuchFieldException,
//			IllegalArgumentException, IllegalAccessException {
//		Field f = MorphDynaBean.class.getField("dynaValues");
//		f.setAccessible(true);
//		Map map = (Map) f.get(bean);
//		return map;
//	}
//
//	/**
//	 * ��������װΪurl
//	 * 
//	 * @author wb-lixing 2012-3-22 ����02:51:13
//	 * @param params
//	 * @param methodName
//	 */
//	private static String assembleParamsToUrl(String methodName, Object[] params) {
//		Map<String, Object> urlParams = new HashMap<String, Object>();
//		String methodParamV = JSONArray.fromObject(params).toString();
//		String paramPart = "";
//		paramPart += "methodName=" + methodName + "&methodParam="
//				+ methodParamV;
//		return serverUrlPrefix + "&" + paramPart;
//	}
//
//	/** ����url������json�� */
//	public static String process(String requestInfo) throws ScriptException,
//			IOException {
//		double id = Math.random();
//		System.out.println("HTTP Component Send#####id: "+id+"#### ����������Ϣ��URL��"+ requestInfo);
//		Map<String, Object> result = parseUrlAndParams(requestInfo);
//		Map<String, String> params = (Map<String, String>) result.get("params");
//		String url = (String) result.get("url");
//		//System.out.println("---------------params: " + params);
//		//System.out.println("---------------url: " + url);
//		String jsonStr = grab(url, params,id);
//		return jsonStr;
//	}
//
//	/**
//	 *�ܲ���"��url�Ͳ������ֲ𿪺�ֱ�ӽ���httpcomponent" �����Ͳ��ö���Բ�����֡���װ
//	 * 
//	 * @throws UnsupportedEncodingException
//	 */
//	public static Map<String, Object> parseUrlAndParams(String requestInfo)
//			throws UnsupportedEncodingException {
//
//		Map<String, Object> result = new HashMap<String, Object>();
//		// �Ե�һ���ʺ�Ϊ�磬ǰ��Ϊurl
//		int bound = requestInfo.indexOf('?');
//		String url = requestInfo.substring(0, bound);
//		String paramPart = requestInfo.substring(bound + 1);
//		result.put("url", url);
//		Map<String, String> params = new HashMap<String, String>();
//		// &����regex�б����ַ������Կ�ֱ��ʹ��
//		String[] paramPairs = paramPart.split("&");
//		for (String paramPair : paramPairs) {
//			String[] nv = paramPair.split("=");
//
//			// �Բ������н��롣��Ϊ�´η���HTTP���󣬻��ٴζ������룬���ظ�ִ�б��룬�������ͬ���ַ���
//			params.put(nv[0], URLDecoder.decode(nv[1], "gbk"));
//
//		//	System.out.println("parseUrlAndParams()  name: " + nv[0]+ "\t value: " + URLDecoder.decode(nv[1], "gbk"));
//		}
//		result.put("params", params);
//		return result;
//
//	}
//
//	/** ץȡ���ݣ�ʹ��HttpComponent����POST���� */
//	public static String grab(String url, Map<String, String> params, double id) {
//	
//		HttpClient hc = new DefaultHttpClient();
//		
//		HttpPost post = new HttpPost(url);
//		System.out.println("HTTP Component Send#####id: "+id+"#### url��"+ url);
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//
//		for (Map.Entry<String, String> param : params.entrySet()) {
//			System.out.println("HTTP Component Send#####id: "+id+"#### ��������"+ param.getKey() +"\t ����ֵ: "+ param.getValue() );
//			nvps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
//		}
//		// �����entity����ӦhttpЭ���е�entity��һ��entity��Ӧ�������
//		UrlEncodedFormEntity entity = null;
//		try {
//			entity = new UrlEncodedFormEntity(nvps, "gbk");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//
//		post.setEntity(entity);
//		String responseStr = null;
//		try {
//			HttpResponse hr = hc.execute(post);
//			HttpEntity he = hr.getEntity();
//
//			InputStream in = he.getContent();
//			responseStr = inToStr(in);
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println("HTTP Component Send#####id: "+id+"#### ����ֵ��"+ responseStr );
//		hc.getConnectionManager().shutdown();
//		return responseStr;
//	}
//
//	public static String inToStr(InputStream in) throws IOException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		byte[] bs = new byte[1024];
//		int len = 0;
//		while ((len = in.read(bs)) != -1) {
//			baos.write(bs, 0, len);
//		}
//		// �ر���
//		in.close();
//		byte[] baosbs = baos.toByteArray();
//		return new String(baosbs, "GBK");
//	}
//
//	public static class JSONUtil {
//		/**
//		 * 
//		 * 
//		 * 1���ݹ����MorphDynaBean��List,������MorphDynaBeanתΪMap
//		 * 
//		 * 
//		 * 2.1:�������ֵ����ΪMorphDynaBean��List���ݹ����replaceDynaBeanToMap(Object
//		 * obj)��Ȼ���䷵��ֵ�ŵ��滻���Map��List�� 2.2: ����Ǽ����͵�ֵ��ֱ�ӽ�ֵ�ŵ��滻���Map��List��
//		 * 
//		 * 3�����ô˷���ǰ������null����Ϊ�ݹ�����У�����һ����Ҫ��null������ʱ�򶼲��ã����Ծ�û���ڷ����Ŀ�ͷ��null
//		 * 4����������һ����MorphDynaBean��List
//		 * 
//		 * @throws NoSuchFieldException
//		 * @throws SecurityException
//		 * @throws IllegalAccessException
//		 * @throws IllegalArgumentException
//		 */
//		public static Object replaceDynaBeanToMap(Object obj)
//				throws SecurityException, NoSuchFieldException,
//				IllegalArgumentException, IllegalAccessException {
//
//			if (obj.getClass().equals(MorphDynaBean.class)) {
//				MorphDynaBean oldContainer = (MorphDynaBean) obj;
//				Map newContainer = new HashMap();
//				// ��ȡMorphDynaBean��Ӧ��Map
//				Field f = MorphDynaBean.class.getDeclaredField("dynaValues");
//				f.setAccessible(true);
//				Map mapOfOldContainer = (Map) f.get(oldContainer);
//
//				Set<String> keys = mapOfOldContainer.keySet();
//				for (String key : keys) {
//					Object v1 = mapOfOldContainer.get(key);
//					Object v2 = null;
//					if (v1 != null
//							&& (v1.getClass().equals(MorphDynaBean.class) || v1
//									.getClass().equals(ArrayList.class))) {
//						v2 = replaceDynaBeanToMap(v1);
//					} else {
//						v2 = v1;
//
//					}
//					newContainer.put(key, v2);
//				}
//				return newContainer;
//			} else if (obj.getClass().equals(ArrayList.class)) {
//				ArrayList oldContainer = (ArrayList) obj;
//				List newContainer = new ArrayList();
//
//				for (int i = 0; i < oldContainer.size(); i++) {
//					Object v1 = oldContainer.get(i);
//					Object v2 = null;
//
//					if (v1 != null
//							&& (v1.getClass().equals(MorphDynaBean.class) || v1
//									.getClass().equals(ArrayList.class))) {
//						v2 = replaceDynaBeanToMap(v1);
//					} else {
//						v2 = v1;
//					}
//					newContainer.add(v2);
//				}
//				return newContainer;
//			}
//
//			return null;
//		}
//
//	}
//
//	public static boolean contains(String[] arr, String str) {
//		for (int i = 0; i < arr.length; i++) {
//			if (arr[i].equals(str)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public static String[][] methodNames = {
//			{ "queryRecentlyMultiRealTime", "queryRecentlyChildRealTime",
//					"queryRecentlyMultiChildRealTime",
//					"queryRecentlyHostRealTime",
//					"queryRecentlyChildHostRealTime" },
//			{ "queryRecentlySingleRealTime" },
//			{ "queryMultiRealTime", "queryChildRealTime", "queryHostRealTime",
//					"queryMultiChildRealTime", "queryChildHostRealTime" },
//			{ "querySingleRealTime" } };
//	public static final String serverUrlPrefix = "http://110.75.2.189:9999/monitorstat-time-web/time/apache/monitor_data_v2.jsp?className=com.taobao.csp.dataserver.query.QueryUtil";
//}
