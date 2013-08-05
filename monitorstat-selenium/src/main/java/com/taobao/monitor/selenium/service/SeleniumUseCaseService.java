/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.selenium.dao.model.UseCase;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-3 - 上午10:15:43
 * @version 1.0
 */
public interface SeleniumUseCaseService {
	/**
	 * 新增测试用例
	 * 
	 * @param useCaseText
	 * 2011-5-30 - 下午01:39:00
	 */
	public int addUseCase(UseCase useCase);
	
	/**
	 * 修改测试用例
	 * 
	 * @param useCaseText
	 * 2011-5-30 - 下午01:39:00
	 */
	public void updateUseCase(UseCase useCase);
	
	/**
	 * 删除测试用例
	 * 
	 * @param useCaseText
	 * 2011-5-30 - 下午01:39:00
	 */
	public void deleteUseCase(int useCaseId);
	
	/**
	 * 获取适配器源文件
	 * @param adapterName
	 * @param adapterTime
	 * @return
	 * 2011-5-30 - 上午10:44:41
	 */
	public UseCase getUseCaseText(String adapterName, long adapterTime);

	/**
	 * 查询所有的UC
	 * 
	 * @return
	 * 2011-6-3 - 下午04:11:33
	 */
	public List<UseCase> queryAllUseCase();
	
	/**
	 * 获取用例Map
	 * 
	 * @return
	 * 2011-6-29 - 上午10:03:46
	 */
	public Map<Integer, UseCase> getUseCaseMap();
	
	/**
	 * 根据测试用例名字获取用例运行selenium rc、运行浏览器信息
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - 下午01:27:17
	 */
	public UseCase getUseCasePlugByName(String useCaseName);
	
	
	/**
	 * 根据测试用例名字获取测试用例Id
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - 下午01:27:17
	 */
	public int getUseCaseIdByName(String useCaseName);
	
	/**
	 * 根据selenium server ID获取依赖它的用例
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - 下午03:32:10
	 */
	public List<UseCase> getDependUcByRcId(int rcId);
	
	/**
	 * 获取用例基本信息&&详细信息
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - 下午01:27:17
	 */
	public UseCase getUseCaseByPrimaryKey(int useCaseId);

}
