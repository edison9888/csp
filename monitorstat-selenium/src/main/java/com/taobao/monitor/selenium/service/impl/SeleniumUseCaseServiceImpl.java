/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.taobao.monitor.selenium.dao.UseCaseDao;
import com.taobao.monitor.selenium.dao.model.SeleniumRc;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.service.SeleniumRcService;
import com.taobao.monitor.selenium.service.SeleniumUseCaseService;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-6-3 - ����10:58:20
 * @version 1.0
 */
@Service
public class SeleniumUseCaseServiceImpl implements SeleniumUseCaseService {

	@Resource(name="useCaseDao")
	private UseCaseDao useCaseDao;
	
	@Resource(name="seleniumRcServiceImpl")
	private SeleniumRcService rcService;
	
	/**
	 * @param adapterName
	 * @param adapterTime
	 * @return
	 * 2011-5-30 - ����11:04:18
	 */
	@Override
	public UseCase getUseCaseText(String adapterName, long adapterTime) {
		return useCaseDao.getUseCaseText(adapterName, adapterTime);
	}

	/**
	 * @param useCaseText
	 * 2011-5-30 - ����01:39:15
	 */
	@Override
	public int addUseCase(UseCase useCase) {
		return useCaseDao.addUseCase(useCase);
	}
	
	/**
	 * @param useCaseName
	 * @return
	 * 2011-6-3 - ����10:58:20
	 */
	@Override
	public UseCase getUseCasePlugByName(String useCaseName) {
		return useCaseDao.getUseCasePlugByName(useCaseName);
	}

	/**
	 * @param useCaseName
	 * @return
	 * 2011-6-3 - ����02:39:16
	 */
	@Override
	public int getUseCaseIdByName(String useCaseName) {
		return useCaseDao.getUseCaseIdByName(useCaseName);
	}

	/**
	 * @return
	 * 2011-6-3 - ����04:12:35
	 */
	@Override
	public List<UseCase> queryAllUseCase() {
		List<SeleniumRc> rcList = rcService.listRcServices();
		Map<Integer, SeleniumRc> rcMap = new HashMap<Integer, SeleniumRc>();
		for(SeleniumRc rc:rcList){
			rcMap.put(rc.getId(), rc);
		}
		List<UseCase> ucList = useCaseDao.queryAllUseCase();
		for(UseCase uc:ucList){
			SeleniumRc tempRc = rcMap.get(uc.getRcId());
			uc.setRc(tempRc==null?(new SeleniumRc()):tempRc);
		}
		return ucList;
	}

	/**
	 * @param useCaseText
	 * 2011-6-8 - ����05:43:54
	 */
	@Override
	public void updateUseCase(UseCase useCase) {
		useCaseDao.updateUseCase(useCase);
	}
	
	/**
	 * @param useCase
	 * 2011-6-9 - ����07:08:30
	 */
	@Override
	public void deleteUseCase(int useCaseId) {
		useCaseDao.deleteUseCase(useCaseId);
	}
	
	/**
	 * @param useCaseId
	 * @return
	 * 2011-6-9 - ����03:35:22
	 */
	@Override
	public List<UseCase> getDependUcByRcId(int rcId) {
		return useCaseDao.getDependUcByRcId(rcId);
	}
	
	/**
	 * @param useCaseId
	 * @return
	 * 2011-6-9 - ����07:20:56
	 */
	@Override
	public UseCase getUseCaseByPrimaryKey(int useCaseId) {
		return useCaseDao.getUseCaseByPrimaryKey(useCaseId);
	}

	/**
	 * @return
	 * 2011-6-29 - ����10:04:18
	 */
	@Override
	public Map<Integer, UseCase> getUseCaseMap() {
		List<UseCase> list = useCaseDao.queryAllUseCase();
		Map<Integer, UseCase> map = new HashMap<Integer, UseCase>();
		for(UseCase uc : list){
			map.put(uc.getUseCaseId(), uc);
		}
		return map;
	}

}
