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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-3 - ����10:15:43
 * @version 1.0
 */
public interface SeleniumUseCaseService {
	/**
	 * ������������
	 * 
	 * @param useCaseText
	 * 2011-5-30 - ����01:39:00
	 */
	public int addUseCase(UseCase useCase);
	
	/**
	 * �޸Ĳ�������
	 * 
	 * @param useCaseText
	 * 2011-5-30 - ����01:39:00
	 */
	public void updateUseCase(UseCase useCase);
	
	/**
	 * ɾ����������
	 * 
	 * @param useCaseText
	 * 2011-5-30 - ����01:39:00
	 */
	public void deleteUseCase(int useCaseId);
	
	/**
	 * ��ȡ������Դ�ļ�
	 * @param adapterName
	 * @param adapterTime
	 * @return
	 * 2011-5-30 - ����10:44:41
	 */
	public UseCase getUseCaseText(String adapterName, long adapterTime);

	/**
	 * ��ѯ���е�UC
	 * 
	 * @return
	 * 2011-6-3 - ����04:11:33
	 */
	public List<UseCase> queryAllUseCase();
	
	/**
	 * ��ȡ����Map
	 * 
	 * @return
	 * 2011-6-29 - ����10:03:46
	 */
	public Map<Integer, UseCase> getUseCaseMap();
	
	/**
	 * ���ݲ����������ֻ�ȡ��������selenium rc�������������Ϣ
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - ����01:27:17
	 */
	public UseCase getUseCasePlugByName(String useCaseName);
	
	
	/**
	 * ���ݲ����������ֻ�ȡ��������Id
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - ����01:27:17
	 */
	public int getUseCaseIdByName(String useCaseName);
	
	/**
	 * ����selenium server ID��ȡ������������
	 * 
	 * @param id
	 * @return
	 * 2011-6-9 - ����03:32:10
	 */
	public List<UseCase> getDependUcByRcId(int rcId);
	
	/**
	 * ��ȡ����������Ϣ&&��ϸ��Ϣ
	 * 
	 * @param useCaseName
	 * @return
	 * 2011-5-30 - ����01:27:17
	 */
	public UseCase getUseCaseByPrimaryKey(int useCaseId);

}
