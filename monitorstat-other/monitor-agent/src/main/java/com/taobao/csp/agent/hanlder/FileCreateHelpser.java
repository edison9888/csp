package com.taobao.csp.agent.hanlder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileCreateHelpser {

	public static String execute(String filePath,String fileName,String content) {
		StringBuilder sb = new StringBuilder();
		BufferedWriter writer =  null;
		try{
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdirs();
			}
			File config = new File(file,fileName);
			config.setReadable(true, false);
			writer = new BufferedWriter(new FileWriter(config) );
			writer.write(content);
			sb.append("SUCCESS").append("##").append("配置文件创建成功");
		}catch (Exception e) {
			sb.append("ERROR").append("##").append("配置文件创建失败:"+e.getMessage());
		}finally{
			if(writer!=null){
					try {
						writer.close();
					} catch (IOException e) {
					}
				}
			
		}
		return sb.toString();
	}

}
