
package com.taobao.monitor.common.messagesend;
/**
 * 
 * @author xiaodu
 * @version 2011-2-12 ÏÂÎç03:54:33
 */
public class MessageSendFactory {
	
	
	public static MessageSend create(MessageSendType type){
		
		
		switch(type){
			case Email:
				return new EmailMessageSend();
			case Phone:
				return new PhoneMessageSend();
			case WangWang:
				return new WangwangMessageSend();
		}
		return null;
	}

}
