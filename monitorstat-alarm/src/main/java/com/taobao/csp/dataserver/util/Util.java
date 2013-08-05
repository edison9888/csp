
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.util;

import java.io.File;
import java.net.InetSocketAddress;

import com.taobao.csp.dataserver.Constants;

/**
 * @author xiaodu
 *
 * 下午1:04:43
 */
public class Util {

	
	private static final int GB=1024*1024*1024;

	public static int getHomeFreeSpace(){
		return getFreeSpace("/home");
	}
	public static int getFreeSpace(String path){
		
		File win = new File(path);
//        System.out.println(win.getPath());
//		System.out.println(win.getName());
		
//		System.out.println("Free space = " + (win.getFreeSpace()/(1024*1024*1024)));
//		System.out.println("Usable space = " +( win.getUsableSpace()/(1024*1024*1024)));
//		System.out.println("Total space = " +( win.getTotalSpace()/(1024*1024*1024)));
//		
		return (int) (win.getUsableSpace()/GB);
	}
	
	
	public static float converFloat(Object value){
		
		if(value instanceof Integer ) {
			Integer a = (Integer)value;
			return a;
		} else if(value instanceof Long) {
			Long a = (Long)value;
			return a;
		} else if(value instanceof Float) {
			Float a = (Float)value;
			return a;
		} else if(value instanceof Double) {
			Double a = (Double)value;
			return a.floatValue();
		} 
		return 0;
	}
	
	
	public static String combinBaseLineRowID(String appName,String keyName ,String propertyName,String cm){
		return appName+Constants.S_SEPERATOR+keyName+Constants.S_SEPERATOR+propertyName+Constants.S_SEPERATOR+cm.toUpperCase();
	}
	
	public static String combinBaseLineRowID(String appName,String keyName ,String propertyName){
		return appName+Constants.S_SEPERATOR+keyName+Constants.S_SEPERATOR+propertyName;
	}
	
	public static String combinHostKeyName(String appName,String keyName,String ip){
		return appName+Constants.S_SEPERATOR+keyName+Constants.S_SEPERATOR+ip;
	}
	
	public static String combinHostKeyName(String keyName,String ip){
		return keyName+Constants.S_SEPERATOR+ip;
	}
	
	public static String combinAppKeyName(String appName,String keyName){
		return appName+Constants.S_SEPERATOR+keyName;
	}
	public static String combinAppKeyName(String appName,Integer keyId){
		return appName+Constants.S_SEPERATOR+keyId;
	}


	public static String idToAddress(long id) {
		StringBuffer host = new StringBuffer(30);

		host.append((id & 0xff)).append('.');
		host.append(((id >> 8) & 0xff)).append('.');
		host.append(((id >> 16) & 0xff)).append('.');
		host.append(((id >> 24) & 0xff));

		int port = (int) ((id >> 32) & 0xffff);

		return host.append(":").append(port).toString();
	}

	public static long hostToLong(String host, int port) {
		if (host == null) {
			return 0;
		}

		try {
			String[] a = host.split(":");

			if (a.length >= 2) {
				port = Integer.parseInt(a[1].trim());
			}

			if (port == -1) {
				return 0;
			}

			InetSocketAddress addr = new InetSocketAddress(a[0], port);

			if ((addr == null) || (addr.getAddress() == null)
					|| (addr.getPort() == 0)) {
				return 0;
			}

			byte[] ip = addr.getAddress().getAddress();
			long address = (addr.getPort() & 0xffff);

			int ipa = 0;
			ipa |= ((ip[3] << 24) & 0xff000000);
			ipa |= ((ip[2] << 16) & 0xff0000);
			ipa |= ((ip[1] << 8) & 0xff00);
			ipa |= (ip[0] & 0xff);

			if (ipa < 0)
				address += 1;
			address <<= 32;
			return address + ipa;
		} catch (Exception e) {
		}

		return 0;
	}
	
	/**
	 * 两个Object相加的的工具函数，目前只支持Integer，Long，Float，Double四种类型。
	 * obj和obj2类型必须一致，否则会抛出异常。
	 * @param obj
	 * @param obj2
	 * @return
	 * @throws Exception
	 */
	public static Object add(Object obj, Object obj2) throws Exception {
		if(obj == null && obj2 == null) {
			return null;
		} else if (obj == null && obj2 != null) {
			return obj2;
		} else if (obj != null && obj2 == null) {
			return obj;
		}
		
		if(obj instanceof Integer && obj2 instanceof Integer) {
			Integer a = (Integer)obj;
			Integer b = (Integer)obj2;
			return a + b;
		} else if(obj instanceof Long && obj2 instanceof Long) {
			Long a = (Long)obj;
			Long b = (Long)obj2;
			return a + b;
		} else if(obj instanceof Float && obj2 instanceof Float) {
			Float a = (Float)obj;
			Float b = (Float)obj2;
			return a + b;
		} else if(obj instanceof Double && obj2 instanceof Double) {
			Double a = (Double)obj;
			Double b = (Double)obj2;
			return a + b;
		}else if(obj instanceof String && obj2 instanceof String) {
			String a = (String)obj;
			String b = (String)obj2;
			return a + b;
		}  else {
			throw new Exception("两个参数类型不一致或为Integer，Long，Float，Double以外的类型");
		}  
	}
	
	/**
	 * 减操作
	 * @param obj	被减数
	 * @param obj2	减数
	 * @return
	 * @throws Exception
	 */
	public static Object sub(Object obj, Object obj2) throws Exception {
		if(obj == null && obj2 == null) {
			return null;
		} else if (obj == null && obj2 != null) {
			return obj2;
		} else if (obj != null && obj2 == null) {
			return obj;
		}
		
		if(obj instanceof Integer && obj2 instanceof Integer) {
			Integer a = (Integer)obj;
			Integer b = (Integer)obj2;
			return a - b;
		} else if(obj instanceof Long && obj2 instanceof Long) {
			Long a = (Long)obj;
			Long b = (Long)obj2;
			return a - b;
		} else if(obj instanceof Float && obj2 instanceof Float) {
			Float a = (Float)obj;
			Float b = (Float)obj2;
			return a - b;
		} else if(obj instanceof Double && obj2 instanceof Double) {
			Double a = (Double)obj;
			Double b = (Double)obj2;
			return a - b;
		} else {
			throw new Exception("两个参数类型不一致或为Integer，Long，Float，Double以外的类型");
		}  
	}
	
	/**
	 * 乘法操作
	 * @param obj	被乘数
	 * @param obj2	乘数
	 * @return
	 * @throws Exception
	 */
	public static Object mul(Object obj, Object obj2) throws Exception {
		if(obj == null && obj2 == null) {
			return null;
		} else if (obj == null && obj2 != null) {
			return obj2;
		} else if (obj != null && obj2 == null) {
			return obj;
		}
		
		if(obj instanceof Integer && obj2 instanceof Integer) {
			Integer a = (Integer)obj;
			Integer b = (Integer)obj2;
			return a*b;
		} else if(obj instanceof Long && obj2 instanceof Long) {
			Long a = (Long)obj;
			Long b = (Long)obj2;
			return a*b;
		} else if(obj instanceof Float && obj2 instanceof Float) {
			Float a = (Float)obj;
			Float b = (Float)obj2;
			return a*b;
		} else if(obj instanceof Double && obj2 instanceof Double) {
			Double a = (Double)obj;
			Double b = (Double)obj2;
			return a*b;
		} else {
			throw new Exception("两个参数类型不一致或为Integer，Long，Float，Double以外的类型");
		}  
	}	
	
	/**
	 * 除法操作
	 * @param obj	被除数
	 * @param obj2	除数
	 * @return
	 * @throws Exception
	 */
	public static Object div(Object obj, Object obj2) throws Exception {
		if(obj == null && obj2 == null) {
			return null;
		} else if (obj == null && obj2 != null) {
			return obj2;
		} else if (obj != null && obj2 == null) {
			return obj;
		}
		
		if(obj instanceof Integer && obj2 instanceof Integer) {
			Integer a = (Integer)obj;
			Integer b = (Integer)obj2;
			return a/b;
		} else if(obj instanceof Long && obj2 instanceof Long) {
			Long a = (Long)obj;
			Long b = (Long)obj2;
			return a/b;
		} else if(obj instanceof Float && obj2 instanceof Float) {
			Float a = (Float)obj;
			Float b = (Float)obj2;
			return a/b;
		} else if(obj instanceof Double && obj2 instanceof Double) {
			Double a = (Double)obj;
			Double b = (Double)obj2;
			return a/b;
		} else {
			throw new Exception("两个参数类型不一致或为Integer，Long，Float，Double以外的类型");
		}  
	}	
	
	
	/**
	 * 除法操作
	 * @param obj	被除数
	 * @param obj2	除数
	 * @return
	 * @throws Exception
	 */
	public static Object average(Object obj, Object obj2) throws Exception {
		if(obj == null && obj2 == null) {
			return null;
		} else if (obj == null && obj2 != null) {
			return obj2;
		} else if (obj != null && obj2 == null) {
			return obj;
		}
		
		if(obj instanceof Integer && obj2 instanceof Integer) {
			Integer a = (Integer)obj;
			Integer b = (Integer)obj2;
			return (a+b)/2;
		} else if(obj instanceof Long && obj2 instanceof Long) {
			Long a = (Long)obj;
			Long b = (Long)obj2;
			return  (a+b)/2;
		} else if(obj instanceof Float && obj2 instanceof Float) {
			Float a = (Float)obj;
			Float b = (Float)obj2;
			return  (a+b)/2;
		} else if(obj instanceof Double && obj2 instanceof Double) {
			Double a = (Double)obj;
			Double b = (Double)obj2;
			return  (a+b)/2;
		} else {
			throw new Exception("两个参数类型不一致或为Integer，Long，Float，Double以外的类型");
		}  
	}	
	
	
	private static final int MURMURHASH_M = 0x5bd1e995;
	
	 public static long hash(byte[] key) {
		    int len = key.length;
		    int h = 97 ^ len;
		    int index = 0;

		    while (len >= 4) {
		      int k = (key[index] & 0xff) | ((key[index + 1] << 8) & 0xff00)
		        | ((key[index + 2] << 16) & 0xff0000)
		        | (key[index + 3] << 24);

		      k *= MURMURHASH_M;
		      k ^= (k >>> 24);
		      k *= MURMURHASH_M;
		      h *= MURMURHASH_M;
		      h ^= k;
		      index += 4;
		      len -= 4;
		    }

		    switch (len) {
		      case 3:
		        h ^= (key[index + 2] << 16);

		      case 2:
		        h ^= (key[index + 1] << 8);

		      case 1:
		        h ^= key[index];
		        h *= MURMURHASH_M;
		    }

		    h ^= (h >>> 13);
		    h *= MURMURHASH_M;
		    h ^= (h >>> 15);
		    return ((long) h & 0xffffffffL);
		  }
	
}
