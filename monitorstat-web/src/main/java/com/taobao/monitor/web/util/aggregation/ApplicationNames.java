package com.taobao.monitor.web.util.aggregation;

public enum ApplicationNames {
	TP("tp",322), TF_BUY("tf_buy",330), TF_TM("tf_tm",323), CART("cart",341), IC("ic",0), UIC("uic",0), DIAMOND("diamond",0), CONFIG_SERVER(
			"configServer",0), CAT_SERVER("catServer",0), ALIPAY("alipay",0), TCMAIN("tcmain",0), TB_SESSION_TAIR(
			"TBSession-Tair",0), GP3_TAIR("gp3-Tair",0), DB_MISC("dbMisc",0),DB_CART("dbCart",0),DB_SHOP("dbShop",0);

	private final String name;
	private int appId ; 

	private ApplicationNames(String name,int appId) {

		this.name = name;
		this.appId = appId ;
	}

	public String appName() {

		return this.name;
	}
	public int appId(){
		return appId;
	}
}
