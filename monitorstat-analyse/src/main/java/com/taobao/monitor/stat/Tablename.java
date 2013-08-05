
package com.taobao.monitor.stat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.taobao.monitor.common.util.TableNameConverUtil;

/**
 * 
 * @author xiaodu
 * @version 2010-5-31 ÏÂÎç02:07:55
 */
public class Tablename {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sql ="2011-04-28 00:00";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			System.out.println(sdf.parse(sql).getTime());;
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		System.out.println(TableNameConverUtil.formatDayTableName(sql));
//		
//		try {
//			AnalyseDateConfig config = new AnalyseDateConfig();
//			
//			config.getAppMap().get("webwangwang");
//			
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}

}
