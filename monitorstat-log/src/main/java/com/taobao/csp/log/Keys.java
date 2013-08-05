package com.taobao.csp.log;


/**
 * 
 * @author xiaodu
 *
 * обнГ1:55:14
 */
public class Keys {
	
	private String[] keys = null;
	
	private String catKeys;

	public Keys(String ... keys){
		this.keys = keys;
		catKeys = getCatKeys();
	}

	public String[] getKeys() {
		return keys;
	}

	@Override
	public int hashCode() {
		return catKeys.hashCode();
	}
	
	private String getCatKeys(){
		if(keys == null){
			return "key&empty";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<keys.length;i++){
			if(i == keys.length-1){
				sb.append(keys[i]);
			}else{
				sb.append(keys[i]).append(MonitorConstants.SPLITTER_CHAR);
			}
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		if(obj instanceof Keys){
			Keys k = (Keys)obj;
			String k1 = this.catKeys;
			String k0 = k.catKeys;
			return k1.equals(k0);
		}
		return false;
		
	}

	@Override
	public String toString() {
		return catKeys;
	}
	
	
	public String getString(){
		StringBuilder sb = new StringBuilder();
		if(keys != null){
			for(String k:keys){
				String tmp = k;
				if(tmp!=null){
					tmp = tmp
							.replaceAll(MonitorConstants.SPLITTER_CHAR, "").replaceAll("\n","");
				}
				sb.append(tmp).append(MonitorConstants.SPLITTER_CHAR);
			}
		}else{
			sb.append("key&empty");
		}
		return sb.toString();
	}
	
	
	
	
	
	

	
}
