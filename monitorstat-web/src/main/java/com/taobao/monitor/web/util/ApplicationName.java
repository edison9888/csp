/**
* <p>Title: ApplicationName.java</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2010</p>
* <p>Company: taobao</p>
* @author tom
* @����11:13:06 - 2010-5-14
* @version 1.0
*/
package com.taobao.monitor.web.util;

public enum ApplicationName {
	LIST("listhost"),//�������ҳ��
	SHOP_SYSTEM("shopsystemhost"),//����ϵͳ
	ITEM("itemhost"),
	BUY("buyhost"),
	TRADEMGR("trademgrhost"),//���׹���
	IC("ichost"),//��Ʒ����
	TBUIC("tbuichost"),//�û�����
	SHOP_CENTER("shopcenterhost"),//��������
	TC("tchost"),//��������
	DESIGN_CENTER("designcenter");
	
	private final String value;

	public String getValue() {
		return value;
	}
	ApplicationName(String value){
		this.value = value;
	}
	
}
