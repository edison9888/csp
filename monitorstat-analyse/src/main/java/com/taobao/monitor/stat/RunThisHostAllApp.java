
package com.taobao.monitor.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.stat.util.Config;

/**
 * 
 * @author xiaodu
 * @version 2011-3-25 ÏÂÎç04:10:03
 */
public class RunThisHostAllApp {
	
	
	public static void main(String[] args){
		try {
			String HOSTNAME = System.getenv("HOSTNAME");
			
			
			if(HOSTNAME == null){
				System.exit(-1);
			}
				
			List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
			
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line = null;
			
			while((line = reader.readLine())!= null){
				for(AppInfoPo p:appList){
					RunApp app = new RunApp();
					app.execute(p.getOpsName(), line.trim());
					System.out.println(line);
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
