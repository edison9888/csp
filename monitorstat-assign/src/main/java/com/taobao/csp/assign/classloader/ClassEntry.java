
package com.taobao.csp.assign.classloader;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-8-10 ÏÂÎç03:36:44
 */
public class ClassEntry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 266600500508497853L;

	private transient Class<?> clazz;
	
	private long createTime;
	
	private String className;
	
	private int fileSize;

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	
	
	

}
