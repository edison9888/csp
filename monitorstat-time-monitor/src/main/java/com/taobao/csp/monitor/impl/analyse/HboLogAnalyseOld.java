
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taobao.csp.monitor.impl.analyse.hsf.HsfConsumerLogJob;
import com.taobao.csp.monitor.impl.analyse.hsf.HsfProviderLogJob;
import com.taobao.csp.monitor.impl.analyse.notify.NotifyConsumerLogJob;
import com.taobao.csp.monitor.impl.analyse.search.SearchConsumerLogJob;
import com.taobao.csp.monitor.impl.analyse.tair.TairConsumerLogJob;

/**
 * @author xiaodu
 *
 * ÏÂÎç7:12:31
 */
public class HboLogAnalyseOld extends AbstractDataAnalyse{
	
	private HsfConsumerLogJob hsfconsumer = null;
	
	private HsfProviderLogJob hsfprovider = null;
	
	private NotifyConsumerLogJob notifyLog = null;
	
	private SearchConsumerLogJob search = null;
	
	private TairConsumerLogJob tair = null;
	
	

	/**
	 * @param appName
	 * @param ip
	 */
	public HboLogAnalyseOld(String appName, String ip,String f) {
		super(appName, ip,f);
		hsfconsumer = new HsfConsumerLogJob(appName, ip,f);
		hsfprovider = new HsfProviderLogJob(appName, ip,f);
		notifyLog = new NotifyConsumerLogJob(appName, ip,f);
		search = new SearchConsumerLogJob(appName, ip,f);
		tair = new TairConsumerLogJob(appName, ip,f);
	}

	@Override
	public void analyseOneLine(String line) {
		
		if(line.indexOf("JVM")>-1)return;
		
		hsfconsumer.analyseOneLine(line,'\t');
		hsfprovider.analyseOneLine(line,'\t');
		notifyLog.analyseOneLine(line,'\t');
		search.analyseOneLine(line,'\t');
		tair.analyseOneLine(line,'\t');
		
	}

	@Override
	public void submit() {
		
		hsfconsumer.submit();
		hsfprovider.submit();
		notifyLog.submit();
		search.submit();
		tair.submit();
		
	}

	@Override
	public void release() {
		hsfconsumer.release();
		hsfprovider.release();
		notifyLog.release();
		search.release();
		tair.release();
	}
	
public static void main(String[] args){
		
	HboLogAnalyseOld job = new HboLogAnalyseOld("detail","172.17.134.4","");
		
			String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\work\\csp\\monitorstat-time-monitor\\target\\monitor.log")));
			int i = 0;
			while((line=reader.readLine())!=null){
			//	System.out.println(line);
				job.analyseOneLine(line);
				i++;
				if(i>10000){
					job.submit();
					job.release();
					Thread.sleep(60000);
					i=0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
