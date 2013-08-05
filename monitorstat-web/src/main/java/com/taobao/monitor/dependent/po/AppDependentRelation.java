/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.dependent.po;


/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-6 - 下午03:25:16
 * @version 1.0
 */
public class AppDependentRelation {
	
	/** 依赖应用/被依赖应用名称 */
	private String name;
	
	/** 依赖应用/被依赖应用Ip */
	private String ip;
	
	/** 依赖应用/被依赖应用port */
	private int port;
	
	/** 依赖应用/被依赖应用site */
	private String site;
	
	/** 查询应用名称 */
	private String selfSite;
	
	/** 查询应用IP */
	private String selfIp;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return the selfName
	 */
	public String getSelfSite() {
		return selfSite;
	}

	/**
	 * @param selfName the selfName to set
	 */
	public void setSelfSite(String selfSite) {
		this.selfSite = selfSite;
	}

	/**
	 * @return the selfIp
	 */
	public String getSelfIp() {
		return selfIp;
	}

	/**
	 * @param selfIp the selfIp to set
	 */
	public void setSelfIp(String selfIp) {
		this.selfIp = selfIp;
	}

}
