package com.taobao.csp.loadrun.core.control;

/***
 * 要修改线上机器配置的控制类
 * @author youji.zj
 * @version 2012-06-23
 *
 */
public interface IControl {
	
	/*** 往控制器添加属性 ***/
	public void putAttribute(ControlAtrribute attr, String value);
	
	/*** 获取控制器的属性 ***/
	public String getAttribute(ControlAtrribute attr);
	
	/*** 备份 ***/
	public boolean backup() throws Exception;
	
	/*** 运行 ***/
	public void control() throws Exception;
	
	/*** 恢复 ***/
	public boolean reset() throws Exception;
	

}
