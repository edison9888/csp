
package com.taobao.csp.btrace.core;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * 
 * @author xiaodu
 * @version 2011-8-24 上午11:40:52
 * 这个类的主要作用就是重写Object的toObjectString，用于序列化传输
 */
public class BtraceUtils {
	
		
	public static String toMapString(Map map) throws IOException {
		if(map == null){
			return null;
		}
		
		boolean first = true;
		Iterator iter=map.entrySet().iterator();
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		while(iter.hasNext()){
            if(first)
                first = false;
            else
            	sb.append(',');
			Map.Entry entry=(Map.Entry)iter.next();
			sb.append('\"');
			sb.append(escape(String.valueOf(entry.getKey())));
			sb.append('\"');
			sb.append(':');
			toObjectString(entry.getValue());
		}
		sb.append('}');
		return sb.toString();
	}
	
	
	

	public static String toObjectString(Object value) {
		try{
			if(value == null)
				return "null";
			
			if(value instanceof String)
				return "\""+escape((String)value)+"\"";
			
			if(value instanceof Double){
				if(((Double)value).isInfinite() || ((Double)value).isNaN())
					return "null";
				else
					return value.toString();
			}
			
			if(value instanceof Float){
				if(((Float)value).isInfinite() || ((Float)value).isNaN())
					return "null";
				else
					return value.toString();
			}		
			
			if(value instanceof Number)
				return value.toString();
			
			if(value instanceof Boolean)
				return value.toString();
			
			if(value instanceof Map)
				return toMapString((Map)value);
			
			if(value instanceof List)
				return toListString((List)value);
			if(value instanceof Collection)
				return toCollectionString((Collection)value);
			
			return value.toString();
		}catch (Exception e) {
			return null;
		}
	}
	
	public static String toCollectionString(Collection list)throws IOException {
		if(list == null)
			return "null";
		
        boolean first = true;
        StringBuffer sb = new StringBuffer();
		Iterator iter=list.iterator();
        
        sb.append('[');
		while(iter.hasNext()){
            if(first)
                first = false;
            else
                sb.append(',');
            
			Object value=iter.next();
			if(value == null){
				sb.append("null");
				continue;
			}
			sb.append(toObjectString(value));
		}
        sb.append(']');
		return sb.toString();
	}
	
	public static String toListString(List list)throws IOException {
		if(list == null)
			return "null";
		
        boolean first = true;
        StringBuffer sb = new StringBuffer();
		Iterator iter=list.iterator();
        
        sb.append('[');
		while(iter.hasNext()){
            if(first)
                first = false;
            else
                sb.append(',');
            
			Object value=iter.next();
			if(value == null){
				sb.append("null");
				continue;
			}
			sb.append(toObjectString(value));
		}
        sb.append(']');
		return sb.toString();
	}
	
	
	/**
	 * Escape quotes, \, /, \r, \n, \b, \f, \t and other control characters (U+0000 through U+001F).
	 * @param s
	 * @return
	 */
	public static String escape(String s){
		if(s==null)
			return null;
        StringBuffer sb = new StringBuffer();
        escape(s, sb);
        return sb.toString();
    }
	
	  /**
     * @param s - Must not be null.
     * @param sb
     */
	public static void escape(String s, StringBuffer sb) {
		for(int i=0;i<s.length();i++){
			char ch=s.charAt(i);
			switch(ch){
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
                //Reference: http://www.unicode.org/versions/Unicode5.1.0/
				if((ch>='\u0000' && ch<='\u001F') || (ch>='\u007F' && ch<='\u009F') || (ch>='\u2000' && ch<='\u20FF')){
					String ss=Integer.toHexString(ch);
					sb.append("\\u");
					for(int k=0;k<4-ss.length();k++){
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				}
				else{
					sb.append(ch);
				}
			}
		}//for
	}

}
