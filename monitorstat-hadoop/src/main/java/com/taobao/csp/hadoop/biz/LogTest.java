package com.taobao.csp.hadoop.biz;

public class LogTest {
	
	/***
	 * 线上的前端日志目前有3种类型
	 * 1、正常的
	 * 2、login单独一种类型
	 * 3、无线应用
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// detail
		String url1 = "172.24.179.32 16808 - [14/Jan/2013:13:26:47 +0800] \"GET http://item.taobao.com/item.htm?spm=a1z10.3.17-201563341.84.97KTRL&id=2191673707&\" 200 13407 \"-\" \"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET4.0C; .NET4.0E; BRI/2; NP06; InfoPath.2; Maxthon/3.0)\" 115.201.60.126 MISS";
		String url2 = "61.172.148.113 0 - [14/Jan/2013:13:26:47 +0800] \"- http://item.taobao.com-\" 302 260 \"-\" \"-\" - -";
		String url3 = "125.39.223.44 3642 - [14/Jan/2013:13:31:51 +0800] \"GET http://item.taobao.com/auction/noitem.htm?itemid=17201100186&catid=0\" 200 24883 \"-\" \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0)\" - -";
		System.out.println(url1.split("\"").length);
		System.out.println(url2.split("\"").length);
		System.out.println(url3.split("\"").length);
		
		// login
		String url11 = "123.185.189.55 7 302 [14/Jan/2013:13:31:08 +0800] \"GET http://login.taobao.com/member/loginByIm.do\" - \"null\"";
		String url21 = "60.211.213.105 6 200 [14/Jan/2013:13:31:08 +0800] \"GET http://login.taobao.com/member/request_nick_check.do\" 29 \"https://login.taobao.com/member/login.jhtml?redirectURL=http%3A%2F%2Ftrade.taobao.com%2Ftrade%2Fitemlist%2Flist_bought_items.htm%3Fspm%3D2013.1.0.19.EvoSn7\"";
		String url31 = "124.128.196.138 34 200 [14/Jan/2013:13:31:09 +0800] \"POST http://login.taobao.com/member/login.jhtml\" 15140 \"https://login.taobao.com/member/login.jhtml?style=miniall&css_style=tmall&full_redirect=true&from=tmall&tpl_redirect_url=http%3A%2F%2Fdetail.tmall.com%2Fitem.htm%3Fspm%3Da230r.1.10.40.MOCgqP%26id%3D18595443808\"";
		System.out.println(url11.split("\"").length);
		System.out.println(url21.split("\"").length);
		System.out.println(url31.split("\"").length);
		
		// galaxy
		String url12 = "117.136.20.14||12856||-||2013:01:14:14:20:06||GET http://m.taobao.com/web_channel.htm?url=/act/other/huoyanandroid.php&ttid=12ewm339&sprefer=pmm697?imgType=mid&sid=656a9324a1928e925aed713bac5dafe0||200||1743||-||anclient|";
		String url22 = "122.86.241.14||25790||-||2013:01:14:14:20:06||GET http://m.taobao.com/trade/bought_item_lists.htm?status_id=5&v=*&ttid=203200%40taobao_android_3.4.3&sid=358de000543469cd639334dcd134589d&vm=clt&page=3&imei=867122017053863&pageSize=1&device_id=9c377611868338057c7506dbbfe62a2c3876ea67&imsi=460006140645529||200||683||-||anclient||du=400288743||ea=-;-;-;-||pp=-||m.taobao.com||anclient||-||-||-||-||-||tbclient for android||-||abtest=-||-||-||- -";
		String url32 = "211.137.185.9||24056||-||2013:01:14:14:20:06||GET http://search1.wap.taobao.com/s/search.htm?vm=nw&search_wap_mall=false&&pix=80x80&sort=_coefp&sp=0&page=2&q=%E4%B8%89%E6%98%9F9228%E6%89%8B%E6%9C%BA%E6%AD%A3%E5%93%81&n=5&imei=&imsi=&ttid=12020f@taobao_java_3.2.0_Oppo_11035_240400||200||1312||-||OPPOA613/2.07/12 Release/2011.6.14 Browser/Obigo/Q03C Profile/MIDP-2.0 Configuration/CLDC-1.1||du=9a98300fa28faa5c||ea=-;-;-;-||pp=-||-||-||-||-||10.151.169.212, 211.137.185.9||-||-||-||-||abtest=-||-||GPRS/EDGE||- -";
		System.out.println(url12.split("\\|\\|").length);
		System.out.println(url22.split("\\|\\|").length);
		System.out.println(url32.split("\\|\\|").length);
		
		System.out.println(url11.indexOf("||"));
	}

}
