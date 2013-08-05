/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.monitor.selenium.dao.model.UseCase;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-3 - ����02:44:12
 * @version 1.0
 */
public class UseCaseCache extends SeleniumBeanService{

	private static Map<String, UseCase> ucNameMap = new ConcurrentHashMap<String, UseCase>();
	
	private static SeleniumUseCaseService seleniumUseCaseService;

	
	/**
	 * �����и���UC����ѯUC��Ϣ
	 * ע����Ҫ���������
	 * 
	 * @param ucName
	 * @return
	 * 2011-6-3 - ����03:51:58
	 */
	public static UseCase getUcByName(String ucName){
		synchronized(UseCaseCache.class){
			UseCase useCase = ucNameMap.get(ucName);
			if(useCase == null){
				resetCache();
				useCase = ucNameMap.get(ucName);
			}
			return useCase;
		}
	}
	
	public static void add(UseCase useCase){
		synchronized (useCase) {
			ucNameMap.put(useCase.getUseCaseName(), useCase);
		}
	}
	
	/**
	 * ���¼��ػ���
	 * 
	 * 2011-6-3 - ����04:07:39
	 */
	public static void resetCache(){
		seleniumUseCaseService = (SeleniumUseCaseService) getBean("seleniumUseCaseServiceImpl");
		List<UseCase> ucList = seleniumUseCaseService.queryAllUseCase();
		for(UseCase uc : ucList){
			ucNameMap.put(uc.getUseCaseName(), uc);
		}
	}
}
