package com.taobao.csp.depend.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.dao.CspAppHsfDependProvideDao;
import com.taobao.csp.depend.po.hsf.AppConsumerSummary;
import com.taobao.csp.depend.po.hsf.AppExceptionListPo;
import com.taobao.csp.depend.po.hsf.AppProviderSummary;
import com.taobao.monitor.common.util.CspKeyTransfer;

public class HsfProviderAo {
	private static final Logger logger =  Logger.getLogger(HsfProviderAo.class);
	
	private static HsfProviderAo ao = new HsfProviderAo();
	private HsfProviderAo(){}
	public static HsfProviderAo get() {
		return ao;
	}
	
	private CspAppHsfDependProvideDao dao = new CspAppHsfDependProvideDao();

	/**
	 * �����ڻ�ȡ������HSF Provider���ݵ�Ӧ���б�
	 * @param collect_time
	 * @return
	 */
	public List<String> getAppListFromProvider(String collect_time) {
		return dao.getAppListFromProvider(collect_time);
	}
	
	/**
	 * ����Ӧ������ʱ�䣬���ط����ĵ��ô�������������customerApp������Ч
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public Map<String,Long> getHsfProvideMethods(String provideAppName, String collectDay){
		Map<String,Long> map = new HashMap<String,Long>();
		List<AppExceptionListPo> list = dao.getHsfProvideMethods(provideAppName, collectDay);
		for(AppExceptionListPo po: list) {
			try {
				map.put(CspKeyTransfer.changeDependHsfProviderKeyToEagleeyeKey(po.getKeyname()), po.getAllnum());
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		return map;
	}
	
	/*************************��ȡcsp_app_dep_hsf_provide_summary��ص���Ϣ*******************************************************/

	public List<AppProviderSummary> getHsfSummaryInfoList(String collect_time){
		return dao.getHsfSummaryInfoList(collect_time);
	}
	
	public AppProviderSummary getHsfSummaryInfo(String collect_time, String provider_name) {
		return dao.getHsfSummaryInfo(collect_time, provider_name);
	}
}
