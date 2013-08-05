
package com.taobao.monitor.common.messagesend;
/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午03:55:05
 */
public interface MessageSend {
	/**
	 * 
	 * @param target 发送目标
	 * @param title 标题
	 * @param context 内容
	 */
	public void send(String target,String title,String context);

}
