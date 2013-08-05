
package com.taobao.monitor.common.messagesend;

import com.taobao.wwnotify.biz.WwNotify;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 ÏÂÎç04:02:22
 */
public class WangwangMessageSend extends MessageSendImpl {

	@Override
	public void send(String target, String title, String context) {
		WwNotify.get().sendWWMessage(target, "CSPÆ½Ì¨", title, context);
	}

}
