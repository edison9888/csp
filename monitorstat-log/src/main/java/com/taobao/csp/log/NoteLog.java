
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
	 * ������������λ�ã������ϴμ�¼����Ϣ
	 * @param values
	 */
	public void addNote(T[] values );
	
	public String getString();
	
	
	
	


}
