
package com.taobao.csp.time.tair.impl;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.common.lang.diagnostic.Profiler;
import com.taobao.csp.time.custom.arkclient.ArkDomain;
import com.taobao.csp.time.tair.TairCacheResult;
import com.taobao.csp.time.tair.TairCacheService;
import com.taobao.monitor.common.ao.center.CspUserInfoAo;
import com.taobao.monitor.common.po.CspUserInfoPo;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.impl.mc.MultiClusterTairManager;

/**
 * ���������֧��tair2.3�����ϰ汾����֧��tair2.2
 *
 */
public class TairCacheServiceV23Impl implements TairCacheService {
	// ��λ���� ����ʧЧʱ��
	public static final int EXPIRE_TIME = 3600;
	public static final int NAMESPACE_MOCK_SESSION = 494;
	private static final Log log = LogFactory.getLog(TairCacheServiceV23Impl.class);

	private volatile MultiClusterTairManager defaultTairManager;

	private String configID;

	private boolean dynamicConfig;

	public void init() {
		if (defaultTairManager == null) {
			// ��飬�����Ҫ������ȫ����ʼ��TairManager
			if (StringUtil.isNotBlank(configID)) {
				MultiClusterTairManager mcTairManager = new MultiClusterTairManager();
				mcTairManager.setConfigID(configID); //��Ӧdiamond�ϵ�dataid�� groupidȱʡΪdataid-GROUP
				mcTairManager.setDynamicConfig(dynamicConfig);  //�ǳ���Ҫ����Ҫ����
				try {
				    mcTairManager.init();
					this.defaultTairManager = mcTairManager;
				} catch (Exception e) {
					log.error("init DefaultTairManager fail:", e);
				}
			} else {
				log.error("init tdbm config not complente");
			}
		}
	}

	@Override
	public TairCacheResult getS(String key) {
		int nameSpace = NAMESPACE_MOCK_SESSION;
		Profiler.enter("TairCacheServiceV23Impl get key: " + key + " nameSpace: " + nameSpace);

		if (null == this.defaultTairManager) {
			init();
		}
		TairCacheResult result = null;
		try {
			result = new TairCacheResult();
			Result<DataEntry> tairResult = defaultTairManager.get(nameSpace, key);
			if (tairResult.isSuccess()
					&& tairResult.getRc().getCode() == ResultCode.SUCCESS.getCode()) {
				if (null != tairResult.getValue()) {
					Object obj = tairResult.getValue().getValue();
					result.setObject(obj);
				}
				result.setSuccess(true);
			} else {
				result.setResultCode(tairResult.getRc().getMessage());
			}
		} finally {
			Profiler.release();
		}

		return result;
	}
	
	
	@Override
	public TairCacheResult putS(String key, Serializable value) {
		int nameSpace = NAMESPACE_MOCK_SESSION;
		Profiler.enter(" put key: " + key + " nameSpace: " + nameSpace);

		if (null == this.defaultTairManager) {
			init();
		}
		TairCacheResult result = null;
		try {
			result = new TairCacheResult();
			//����˫��������Ĵ��� invalid��ȥɾ��˫����������  ֱ��put�Ļ�ֻ��һ�� ����һ���������ܾ�����������
			this.invalidateS(key);
			ResultCode tairResult = defaultTairManager.put(nameSpace, key, value, EXPIRE_TIME );
			if (tairResult.isSuccess()) {
				result.setSuccess(true);
				result.setObject(value);
			} else {
				result.setResultCode(tairResult.getMessage());
			}
		} finally {
			Profiler.release();
		}
		return result;
	}

	@Override
	public TairCacheResult invalidateS(String key) {
		int namespace = NAMESPACE_MOCK_SESSION;
		Profiler.enter("TairCacheServiceV23Impl invalidate key: " + key + " nameSpace: " + namespace);

		if (null == this.defaultTairManager) {
			init();
		}
		TairCacheResult result = null;
		try {
			result = new TairCacheResult();
			ResultCode tairResult = defaultTairManager.invalid(namespace, key);
			if (tairResult.isSuccess()) {
				result.setSuccess(true);
			} else {
				result.setResultCode(tairResult.getMessage());
			}
		} finally {
			Profiler.release();
		}

		return result;
	}

	public CspUserInfoPo getCspUserInfo(HttpServletRequest request) {
		CspUserInfoPo user = null;
		String mail = ArkDomain.getArkUserEmail(request);
		try {
		
			// �ȴ�Tair�л�ȡ�����tair��û�У���DB�в�ѯ��Ȼ�����tair
			TairCacheResult tairRes = getS(mail);
			if (tairRes.isSuccess()) {
				user = (CspUserInfoPo) tairRes.getObject();
				return user;
			}

			user = CspUserInfoAo.get().findCspUserInfoByMail(mail);
			if (user != null) {
				putS(mail, user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (user == null) {// csp��û�е�ǰ�û�
				user = new CspUserInfoPo();
				user.setAccept_apps("");
				user.setMail(mail);
				user.setPermission_desc("");
				user.setPhone("");
				user.setPhone_feature("3#1#00:00#23:59$3#2#18:00#23:59$3#3#18:00#23:59$3#4#18:00#23:59$3#5#18:00#23:59$3#6#18:00#23:59$3#7#00:00#23:59$");
				user.setWangwang("");
				user.setWangwang_feature("0#1#00:00#23:59$0#2#00:00#23:59$0#3#00:00#23:59$0#4#00:00#23:59$0#5#00:00#23:59$0#6#00:00#23:59$0#7#00:00#23:59$");
			}
		}

		return user;
	}

	public String getConfigID() {
		return configID;
	}

	public void setConfigID(String configID) {
		this.configID = configID;
	}

	public boolean getDynamicConfig() {
		return dynamicConfig;
	}

	public void setDynamicConfig(boolean dynamicConfig) {
		this.dynamicConfig = dynamicConfig;
	}
	
}

