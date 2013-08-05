package com.taobao.csp.capacity.test;

import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.util.TBProductCache;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String appS = "aladdin,aliadmin,baoxianinscross,cart,detail,detailskip,durex,itemcenter,malldetail,malldetailskip,matrixfa,mercury,mtop,picturecenter,rulerun,shopsystem,snsju,tadget,tbskip,tf_buy,tf_tm,tmallbuy,tmallcart,topapi,toptim,tradeapi,tradeplatform,uicfinal,ump,wwuic";
		
		String [] apps = appS.split(",");
		
		for (String appName : apps) {
			ProductLine line = TBProductCache.getProductLineByAppName(appName);
			System.out.println(appName + " pe:" + line.getPe() + " tl:" + line.getTeamLeader());
		}

		
		

	}

}
