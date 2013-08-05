package com.taobao.monitor.alarm.n.filter;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 ÏÂÎç04:12:51
 */
public class ScriptFilter extends Filter {

	private ScriptEngineManager engineManager = new ScriptEngineManager();

	private ScriptEngine scriptEngine = engineManager.getEngineByName("JavaScript");

	private static String scriptTemplate = "function valid(appId,keyId,siteId,value,time){" + "" + "}";

	public ScriptFilter(String script, long startExpiry, long endExpiry) {
		super(startExpiry, endExpiry);
		this.scriptTemplate = script;
	}

	@Override
	protected boolean valid(AlarmContext context) {

		try {
			scriptEngine.eval(this.scriptTemplate);

			Invocable inv = (Invocable) scriptEngine;
			ScriptInterface s = inv.getInterface(ScriptInterface.class);

			if (s.valid(context.getAppId(), context.getKeyId(), context.getSiteId(),
					context.getRecentlyValue(), context.getRecentlyDate().getTime(),context.getKeyJudge())) {
				return false;
			}

		} catch (ScriptException e) {
			e.printStackTrace();
		}

		return true;
	}

}
