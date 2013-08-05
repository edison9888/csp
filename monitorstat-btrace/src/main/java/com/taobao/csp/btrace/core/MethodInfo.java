
package com.taobao.csp.btrace.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-8-19 ÏÂÎç01:10:47
 */
public class MethodInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8999740880194680935L;

	private String className;
	
	private String methodName;
	
	private int codeLine;
	
	private String[] paramDesc ;
	
	private String returnDesc;
	
	private List<String> parametersNames = new ArrayList<String>(5);

	public List<String> getParametersNames() {
		return parametersNames;
	}

	public void setParametersNames(List<String> parametersNames) {
		this.parametersNames = parametersNames;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public int getCodeLine() {
		return codeLine;
	}

	public void setCodeLine(int codeLine) {
		this.codeLine = codeLine;
	}

	public String[] getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String[] paramDesc) {
		this.paramDesc = paramDesc;
	}

	public String getReturnDesc() {
		return returnDesc;
	}

	public void setReturnDesc(String returnDesc) {
		this.returnDesc = returnDesc;
	}
	
	

}
