package com.taobao.csp.monitor.impl.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2010-12-24 ÏÂÎç02:14:02
 */
public class ScriptAnalyseFactory {
	
	private static final Logger logger =  Logger.getLogger(ScriptAnalyseFactory.class);

	private static ScriptAnalyseFactory factory = new ScriptAnalyseFactory();

	public static ScriptAnalyseFactory get() {
		return factory;
	}

	private ScriptEngineManager engineManager = new ScriptEngineManager();

	private ScriptAnalyseFactory() {
	}

	public ScriptAnalyse getScriptAnalyse(ScriptType scriptType, String script) {
		ScriptEngine scriptEngine = engineManager.getEngineByName(scriptType.getEngineName());
		String s = scriptType.getTemplateCentent() + script;
		try {

			ScriptContext context = new SimpleScriptContext();
			context.setAttribute("scriptContain", new ScriptValueBox(), ScriptContext.ENGINE_SCOPE);
			scriptEngine.setContext(context);
			scriptEngine.eval(s);
			Invocable inv = (Invocable) scriptEngine;
			return inv.getInterface(ScriptAnalyse.class);
		} catch (ScriptException e) {
			logger.error(""+script,e);
		}
		return null;
	}

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\tmp\\Noname2.js")));
			String line = null;
			while((line=reader.readLine())!=null){
				sb.append(line).append("\n");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ScriptAnalyse analyseObject = ScriptAnalyseFactory.get().getScriptAnalyse(
				ScriptType.JavaScript,				sb.toString());
		analyseObject.analyseOneLine("19 13:22:53 1332134573767 totalCount n Rtr=6|Sgn=13|TairMCookie=194|");
		analyseObject.analyseOneLine("19 13:24:13 1332134653840 totalCount n Rtr=10|Sgn=17|Wtr=1|TairMCookie=212|");

		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		{
			Map<String,Map<String, Double>> map = analyseObject.getAverageKeyValue();
			for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){
				String time = entry.getKey();
				for(Map.Entry<String, Double> kvEntry:entry.getValue().entrySet()){
					System.out.println(time);
					try {
						sdf.parse(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		}
		{
			Map<String,Map<String, Long>> map = analyseObject.getCountKeyValue();
			for(Map.Entry<String,Map<String, Long>> entry:map.entrySet()){
				String time = entry.getKey();
				for(Map.Entry<String, Long> kvEntry:entry.getValue().entrySet()){
					System.out.println(time);
				}
			}
		}
		
		{
			Map<String,Map<String, String>> map = analyseObject.getTextKeyValue();
			for(Map.Entry<String,Map<String, String>> entry:map.entrySet()){
				String time = entry.getKey();
				for(Map.Entry<String, String> kvEntry:entry.getValue().entrySet()){
					System.out.println(time);
				}
			}
		}
	}
}
