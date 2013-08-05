
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.example;

import java.util.Calendar;
import java.util.Date;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;

/**
 * @author xiaodu
 *
 * обнГ5:12:07
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			for(int i=0;i<1000;i++){
				for(int h=0;h<10;h++){
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					System.out.println(cal.getTime().getTime());
					System.out.println(cal.getTimeInMillis());
					System.out.println("****************");
					
					CollectDataUtilMulti.collect("login", "172.24.155.118", cal.getTime().getTime(), 
							new String[]{"PV","http://login.taobao.com/member/login.jhtml"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("hesper", "10.13.44.21", cal.getTime().getTime(), 
							new String[]{"PV","http://list.taobao.com/search_auction.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("hesper", "10.13.44.22", cal.getTime().getTime(), 
							new String[]{"PV","http://list.taobao.com/market/sp.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("search", "10.13.44.23", cal.getTime().getTime(), 
							new String[]{"PV","http://s.taobao.com/search"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("detail", "10.13.44.24", cal.getTime().getTime(), 
							new String[]{"PV","http://item.taobao.com/item.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("buy", "10.13.44.25", cal.getTime().getTime(), 
							new String[]{"PV","http://buy.taobao.com/auction/buy_now.jhtml"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("buy", "10.13.44.26", cal.getTime().getTime(), 
							new String[]{"PV","http://buy.taobao.com/auction/buy_now.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("buy", "10.13.44.27", cal.getTime().getTime(), 
							new String[]{"PV","http://buy.taobao.com/auction/buy.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});
					CollectDataUtilMulti.collect("consign", "10.13.44.28", cal.getTime().getTime(), 
							new String[]{"PV","http://wuliu.taobao.com/user/consign.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{133 + h*10,153 + h*10});					
					System.out.println("gg");
					Thread.sleep(60000);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
