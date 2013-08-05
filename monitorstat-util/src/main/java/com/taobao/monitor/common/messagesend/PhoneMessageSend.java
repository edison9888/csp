package com.taobao.monitor.common.messagesend;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 ÏÂÎç04:02:22
 */
public class PhoneMessageSend extends MessageSendImpl {
	private static final Logger logger = Logger.getLogger(PhoneMessageSend.class);

	@Override
	public void send(String target, String title, String context) {
		boolean b = true;
		int i = 0;
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}

			String url = this.getValue("monitor.habo.alert.url");
			String userName = this.getValue("monitor.habo.alert.userName");
			String password = this.getValue("monitor.habo.alert.password");
			Map parms = new HashMap();
			if (userName != null && !userName.equals(""))
				parms.put("userName", userName);
			if (userName != null && !userName.equals(""))
				parms.put("password", password);
			parms.put("alertType", "sms");
			parms.put("appName", "");
			parms.put("serverName", "");
			parms.put("sender", "Ð¡¶Ä");
			parms.put("value", "");
			parms.put("level", "");
			parms.put("alertCause", "");
			parms.put("msg", context);
			parms.put("subject", title);
			parms.put("groupCode", "");
			parms.put("smsList", target);
			String result = new String(getBody(url, parms));
			b = result.equalsIgnoreCase("ok");

			if (!b) {
				logger.error(result);
			}
			i++;
		} while (!b && i < 3);
	}

	
	public static void main(String[] args){
		PhoneMessageSend send = new PhoneMessageSend();
		send.send("13777468731", "test", "tst");
	}
}
