
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.exception;

/**
 * @author xiaodu
 *
 * обнГ10:33:17
 */
public class NoServerException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -324802050011193318L;

	public NoServerException(){
		super();
	}
	
	public NoServerException(String msg){
		super(msg);
	}
	
	public NoServerException(String message, Throwable cause){
		super(message,cause);
	}
	
	public NoServerException(Throwable cause){
		super(cause);
	}

}
