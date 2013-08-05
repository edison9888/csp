package com.taobao.www.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;

public class Test {

	public String dateDiff(String startTime, String endTime, String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long nh = 1000 * 60 * 60;// 一小时的毫秒数
		long nm = 1000 * 60;// 一分钟的毫秒数
		long ns = 1000;// 一秒钟的毫秒数
		long diff = 0;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff % nd / nh + "小时" + diff % nd % nh / nm + "分钟" + diff % nd
				% nh % nm / ns + "秒";
	}

	

	public  void writeFile(BufferedReader br){
		 FileReader fr;
		try {
			fr = new FileReader("c:\\2012test.txt");
			br=new BufferedReader(fr);//建立BufferedReader对象，并实例化为br
			String Line=br.readLine();//从文件读取一行字符串
			//判断读取到的字符串是否不为空
			while(Line!=null){
				Line=br.readLine();//从文件中继续读取一行数据
			}
			br.close();//关闭BufferedReader对象
			fr.close();//关闭文件
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	
	public static void main(String[] args) {
		// new Test().dateDiff(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), "2011-12-23 12:25:36", "yyyy-MM-dd HH:mm:ss");


	}

}