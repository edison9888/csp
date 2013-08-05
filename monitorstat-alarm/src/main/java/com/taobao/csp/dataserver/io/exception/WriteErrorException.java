
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.io.exception;

/**
 * @author xiaodu
 *
 * обнГ10:33:17
 */
public class WriteErrorException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -324802050011193318L;

	public WriteErrorException(){
		super();
	}
	
	public WriteErrorException(String msg){
		super(msg);
	}
	
	public WriteErrorException(String message, Throwable cause){
		super(message,cause);
	}
	
	public WriteErrorException(Throwable cause){
		super(cause);
	}

}
