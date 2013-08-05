
package com.taobao.monitor.web.backup;
/**
 * 
 * @author xiaodu
 * @version 2011-1-11 ионГ10:18:32
 */
public class CreateTable {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String table = 
		"drop table if exists ms_monitor_backup_${MMdd};"+
		"create table ms_monitor_backup_${MMdd}"+
		"(app_id int,key_id int, m_data varchar(64),collect_time datetime) engine = MYISAM default charset = GBK;"+
		"create index idx_ms_backup_${MMdd} on ms_monitor_backup_${MMdd}(app_id,key_id,collect_time);";
		
		
		
		
		
		for(int i=1;i<=12;i++){
			
			for(int d=1;d<=31;d++){
				
				String time = (i<10?"0"+i:i+"")+(d<10?"0"+d:d+"");;
				
				System.out.println(table.replaceAll("\\$\\{MMdd\\}", time));
				
			}
			
			
		}

	}

}
