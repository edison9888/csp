
package com.taobao.monitor.common.messagesend;
/**
 * 
 * @author xiaodu
 * @version 2011-2-12 ����03:55:05
 */
public interface MessageSend {
	/**
	 * 
	 * @param target ����Ŀ��
	 * @param title ����
	 * @param context ����
	 */
	public void send(String target,String title,String context);

}
