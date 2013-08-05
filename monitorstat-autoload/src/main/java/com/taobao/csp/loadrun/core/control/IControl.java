package com.taobao.csp.loadrun.core.control;

/***
 * Ҫ�޸����ϻ������õĿ�����
 * @author youji.zj
 * @version 2012-06-23
 *
 */
public interface IControl {
	
	/*** ��������������� ***/
	public void putAttribute(ControlAtrribute attr, String value);
	
	/*** ��ȡ������������ ***/
	public String getAttribute(ControlAtrribute attr);
	
	/*** ���� ***/
	public boolean backup() throws Exception;
	
	/*** ���� ***/
	public void control() throws Exception;
	
	/*** �ָ� ***/
	public boolean reset() throws Exception;
	

}
