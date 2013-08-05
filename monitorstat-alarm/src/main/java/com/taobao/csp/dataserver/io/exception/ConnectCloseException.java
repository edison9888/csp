
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.exception;

/**
 * @author xiaodu
 *
 * обнГ10:33:17
 */
public class ConnectCloseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4734300012896585839L;

	public ConnectCloseException(){
		super();
	}
	
	public ConnectCloseException(String msg){
		super(msg);
	}
	
	public ConnectCloseException(String message, Throwable cause){
		super(message,cause);
	}
	
	public ConnectCloseException(Throwable cause){
		super(cause);
	}
}
