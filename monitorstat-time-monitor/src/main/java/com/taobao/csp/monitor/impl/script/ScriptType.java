
package com.taobao.csp.monitor.impl.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author xiaodu
 * @version 2010-12-24 ÏÂÎç02:14:53
 */
public enum ScriptType {
	
	JavaScript("jsTemplate.js","JavaScript"),
	Ruby("rubyTemplate.rb","jruby"),
	Python("pythonTemplate.py","python");
	
	private String engineName;
	private String templateCentent;
	
	ScriptType(String file,String name){
		this.engineName = name;
		this.templateCentent = readerDefualtScriptTemplate(file);
	}	
	public String getEngineName() {
		return engineName;
	}
	public String getTemplateCentent() {
		return templateCentent;
	}
	
	private static String readerDefualtScriptTemplate(String tmplatePath){
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(tmplatePath)));
			String line = null;
			while((line = reader.readLine())!=null){
				sb.append(line);
				sb.append("\n");
			}
		} catch (IOException e) {
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return sb.toString();
		
	}
	
	
	
	

}
