package com.taobao.csp.depend.job.eagleeye;

import com.taobao.monitor.common.util.Constants;


public class EagleeyeApiFactory {
	private EagleeyeApiFactory(){}
	public static EagleeyeAnalyse getEagleeyeAnalyseByType(String api_type) throws Exception {
		if(api_type.equals(Constants.API_CHILD_APP)) {
			return EagleeyeChildAppAnalyse.get();
		} else if(api_type.equals(Constants.API_CHILD_KEY)) {
			return EagleeyeChildKeyAnalyse.get();
		} else if(api_type.equals(Constants.API_FATHER_APP)) {
			return EagleeyeFatherAppAnalyse.get();
		} else if(api_type.equals(Constants.API_FATHER_KEY)) {
			return EagleeyeFatherKeyAnalyse.get();
		} 
		throw new Exception("api_type¿‡–Õ¥ÌŒÛ,api_type=" + api_type);
	}
}
