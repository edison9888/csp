package com.taobao.monitor.web.ss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

public class TestHosts {
	private static Logger log = Logger.getLogger(TestHosts.class);
	public static String testHosts(String appName){	
		List<HostPo> hpList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName);
		Integer hostNum=hpList.size();
		Integer hostErrorNum=0;
		Long startTime = System.currentTimeMillis();
		Long HostErrorWasteTime = 0L;
		for(HostPo hp : hpList){
			String ip = hp.getHostIp();
			Long getStartTime = System.currentTimeMillis();
			String result = getRemoteResult("http://"+ip+":7001"+"/command.sph?command=show");
//			String result = getRemoteResult("http://v127179.sqa.cm4.tbsite.net:8080/command.sph?command=show");
			if(result.matches("GET ERROR!(.*)")){
				hostErrorNum++;
				Long getEndTime = System.currentTimeMillis();
				HostErrorWasteTime+=(getEndTime-getStartTime);
			}
		}
		Long endTime = System.currentTimeMillis();
		return appName+"hostNum:"+hostNum+"\n"+appName+"hostErrorNum:"+hostErrorNum+"\n"+"ErrorProportion:"+hostErrorNum/hostNum+"\n"+"Total runtime:"+(endTime-startTime)+"\n"+"Total wastetime for HostError:"+HostErrorWasteTime+"\n"+"TimeWaste Proportion:"+HostErrorWasteTime/(endTime-startTime);
	}
	private static String getRemoteResult(String url) {
    	log.info(url);
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(10);
        GetMethod getMethod = new GetMethod(url);
        String response = null;
        int status = 0;
        try {
            status = httpClient.executeMethod(getMethod);
            log.info(status);
            InputStream resStream = getMethod.getResponseBodyAsStream();  
            BufferedReader br = new BufferedReader(new InputStreamReader(resStream));  
            StringBuffer resBuffer = new StringBuffer();  
            String resTemp = "";  
            while((resTemp = br.readLine()) != null){  
                resBuffer.append(resTemp);  
            }  
            response = resBuffer.toString();  
        } catch (HttpException e) {
            log.error("HttpException", e);
        } catch (IOException e) {
            log.error("IOException", e);
        }finally{
        	getMethod.releaseConnection();
        }
        if(status == 200) return response;
        return "GET ERROR! "+status;
    }
}
