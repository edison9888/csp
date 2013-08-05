/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-2 - ����06:05:08
 * @version 1.0
 */
@Service
public class SeleniumBeanService implements ApplicationContextAware {

	public static ApplicationContext applicationContext;  
	
	/**
	 * @param applicationContext
	 * @throws BeansException
	 * 2011-6-2 - ����06:05:08
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;

	}

	/**
	 * ����bean name��ȡע���bean��ʵ�� 
	 * 
	 * @param name
	 * @return
	 * @throws BeansException
	 * 2011-6-2 - ����06:05:51
	 */
	public static Object getBean(String name) throws BeansException { 
		return applicationContext.getBean(name); 
    }   
}
