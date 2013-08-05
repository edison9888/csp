
package com.taobao.monitor.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author xiaodu
 * @version 2011-3-25 ÏÂÎç04:10:03
 */
public class RunAppMore {
	
	
	public static void main(String[] args){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(args[0]));
			String line = null;
			
			while((line = reader.readLine())!= null){
				
				String[] tmp = line.split(" ");
				if(tmp.length ==2){
					RunApp app = new RunApp();
					app.execute(tmp[0], tmp[1]);
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
