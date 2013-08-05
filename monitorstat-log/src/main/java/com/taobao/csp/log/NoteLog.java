
/**
 * 
 */
package com.taobao.csp.log;

/**
 * @author xiaodu
 *
 */
public interface NoteLog<T> {
	
	public int NoteLongType = 1;
	
	public int NoteStringType =2;
	
	
	public int getNoteLogType();
	
	/**
	 * 根据这个数组的位置，代替上次记录的信息
	 * @param values
	 */
	public void addNote(T[] values );
	
	public String getString();
	
	
	
	


}
