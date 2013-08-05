package com.taobao.csp.cost.po;

import java.text.DecimalFormat;

public class BasePo {
	
	protected final static DecimalFormat DF_DOUBLE = new DecimalFormat("###,###.##");
	
	protected final static DecimalFormat DF_DOUBLE_4 = new DecimalFormat("###,###.####");
	
	protected final static DecimalFormat DF_LONG = new DecimalFormat("###,###");
	
	private int num;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	

}
