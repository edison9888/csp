package com.taobao.monitor.web.ss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;


import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * xiaoxie 2011-3-3
 */
public class SsdetailReportManager {
    private static Logger log = Logger.getLogger(SsdetailReportManager.class);

    public static List<StableSwitchGroup> report(String app, String port, String command) {
    	Long startTime = System.currentTimeMillis();
        List<StableSwitchGroup> ssgs = new ArrayList<StableSwitchGroup>();
        List<HostPo> hplist = CspCacheTBHostInfos.get().getHostInfoListByOpsName(app);
        for (HostPo hp : hplist) {
//            String result =  getRemoteResult("http://v127179.sqa.cm4.tbsite.net:8080" + command);
			String result = getRemoteResult("http://" + hp.getHostIp() + ":" + port + command);
            if(result == null)log.info(hp.getHostIp()+": getResponse Error!");
            if(result.matches("GET ERROR!(.*)"))log.info(hp.getHostIp()+result);
            if(result == null || result.matches("GET ERROR!(.*)"))result="";
            result = cleanwhiteSpace(result);
            String[] parseResult = result.split(" ");
            int len = parseResult.length;
            StableSwitchGroup ssg = new StableSwitchGroup(hp.getHostIp(), result);
            for (int i = 9; i < len; i+=9) {
                StableSwitch ss = new StableSwitch();
                if(parseResult[i].matches("^com_taobao(.*)"))continue;
                if(parseResult[i].matches("^HSF_LOG(.*)"))continue;
                if(parseResult[i].matches("^([0-9]*)#(.*)"))continue;
                ss.setKey(parseResult[i]);
                ss.setCountValve(Integer.parseInt(parseResult[i + 1]));
                ss.setCount(Integer.parseInt(parseResult[i + 2]));
                ss.setPass(Integer.parseInt(parseResult[i + 3]));
                ss.setQps(Integer.parseInt(parseResult[i + 4]));
                ss.setBlock(Integer.parseInt(parseResult[i + 5]));
                ss.setAvgValve(Integer.parseInt(parseResult[i + 6]));
                ss.setAvg(Integer.parseInt(parseResult[i + 7]));
                ss.setType(Integer.parseInt(parseResult[i + 8]));
                ssg.addStableSwitchs(ss);
                String tmpStr =ss.display();
                log.info(tmpStr);
            }
            ssgs.add(ssg);
        }
        Long endTime = System.currentTimeMillis();
        log.info("report 运行时间:"+(endTime-startTime));
        return ssgs;
    }
    public static String[] setValue(String app, String[] hosts, String hostgroup, String port, String command) {
        if (port == null || command == null) {
            return null;
        }
        if (hosts != null && hosts.length >= 1&&!hosts[0].trim().equals("")) {
            String[] result = new String[hosts.length];
            for (int i = 0; i < hosts.length; i++) {
                String ip = hosts[i];
                result[i] = getRemoteResult("http://" + ip + ":" + port + command);
//                result[i] =  getRemoteResult("http://v127179.sqa.cm4.tbsite.net:8080" + command);
            }

            return result;
        } else if (hostgroup != null){
            int group = Integer.parseInt(hostgroup);
            if (0 == group) {
                return applySiteRoom(app, port, command, null);
            }
            if (3 == group) {
                return applySiteRoom(app, port, command, "CM3");
            }
            if (4 == group) {
                return applySiteRoom(app, port, command, "CM4");
            }
        }
        return null;
    }
    private static String[] applySiteRoom(String app, String port, String command, String room) {
        List<HostPo> hostPos = CspCacheTBHostInfos.get().getHostInfoListByOpsName(app);
        for (HostPo hp : hostPos) {
            if (room != null && !room.equals(hp.getHostSite().toUpperCase())) {
                continue;
            }
//           getRemoteResult("http://v127179.sqa.cm4.tbsite.net:8080" + command);
             getRemoteResult("http://" + hp.getHostIp() + ":" + port + command);
        }
        if (room == null) {
            return new String[]{"Apply to ALL machine!"};
        } else {
            return new String[]{"Apply to " + room + " machine!"};
        }
       
    }

    private static String getRemoteResult(String url) {
    	log.info(url);
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(50);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(50);
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

    private static String cleanwhiteSpace(String source) {
        StringBuffer buffer = new StringBuffer();
        boolean hastwowhitespace = false;
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (!Character.isWhitespace(c)) {
                if (hastwowhitespace) {
                    buffer.append(' ');
                    hastwowhitespace = false;
                }
                buffer.append(c);
            } else {
                hastwowhitespace = true;
                if (source.length() == (i + 1)) {
                    buffer.append(' ');
                }
            }
        }
        return buffer.toString();
    }

	public static StableSwitchGroup reportOne(String app, String port, String command){
    	List<HostPo> hplist = CspCacheTBHostInfos.get().getHostInfoListByOpsName(app);
    	StableSwitchGroup ssg=new StableSwitchGroup("0.0.0.0","");
    	 for (HostPo hp : hplist) {
//    		            String result =  getRemoteResult("http://v127179.sqa.cm4.tbsite.net:8080" + command);
    					String result = getRemoteResult("http://" + hp.getHostIp() + ":" + port + command);
    		            if(result == null)log.info(hp.getHostIp()+": getResponse Error!");
    		            if(result.matches("GET ERROR!(.*)"))log.info(hp.getHostIp()+result);
    		            if(result == null || result.matches("GET ERROR!(.*)"))continue;
    		            result = cleanwhiteSpace(result);
    		            String[] parseResult = result.split(" ");
    		            int len = parseResult.length;
    		             ssg = new StableSwitchGroup(hp.getHostIp(), result);
    		            for (int i = 9; i < len; i+=9) {
    		                StableSwitch ss = new StableSwitch();
    		                if(parseResult[i].matches("^com_taobao(.*)"))continue;
    		                if(parseResult[i].matches("^HSF_LOG(.*)"))continue;
    		                if(parseResult[i].matches("^([0-9]*)#(.*)"))continue;
    		                ss.setKey(parseResult[i]);
    		                ss.setCountValve(Integer.parseInt(parseResult[i + 1]));
    		                ss.setCount(Integer.parseInt(parseResult[i + 2]));
    		                ss.setPass(Integer.parseInt(parseResult[i + 3]));
    		                ss.setQps(Integer.parseInt(parseResult[i + 4]));
    		                ss.setBlock(Integer.parseInt(parseResult[i + 5]));
    		                ss.setAvgValve(Integer.parseInt(parseResult[i + 6]));
    		                ss.setAvg(Integer.parseInt(parseResult[i + 7]));
    		                ss.setType(Integer.parseInt(parseResult[i + 8]));
    		                ssg.addStableSwitchs(ss);
    		                String tmpStr =ss.display();
    		                log.info(tmpStr);
    		            }
    		            break;
    	 }
    	return ssg;
    }
    public static String CollectIPs(String appName){
    	String IPs="";
    	List<HostPo> hplist =CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName);
    	for(HostPo hp : hplist){
    		IPs+=hp.getHostIp()+",\n";
    	}
    	return IPs;
    }
    public static void main(String args[]){
    	Logger log = Logger.getLogger("just test");
    	SsdetailReportManager.report("detail", "7001", "/command.sph?command=show");
    	List<HostPo> hplist = CspCacheTBHostInfos.get().getHostInfoListByOpsName("detail");
    	for(HostPo hp : hplist){
    		log.info(hp.getHostIp());
    	}
    	return;
    }
}
