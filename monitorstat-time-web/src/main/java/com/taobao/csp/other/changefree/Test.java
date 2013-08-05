
/**
 * monitorstat-time-web
 */
package com.taobao.csp.other.changefree;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author xiaodu
 *
 * ионГ11:14:15
 */
public class Test {

	/**
	 *@author xiaodu
	 * @param args
	 *TODO
	 */
	public static void main(String[] args) {
		
		List<ChangeFree> list = ChangeFreeInfo.get().getRecentlyChangeFree();
		for(ChangeFree cf:list){
			System.out.println(cf.getTitle()+":"+cf.getEnd_time());
			
		}

	}

}
