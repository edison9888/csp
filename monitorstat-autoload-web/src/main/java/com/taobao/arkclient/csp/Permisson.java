package com.taobao.arkclient.csp;

/***
 * ѹ��Ȩ�޳���
 * 
 * Ȩ�����õĹ���
 * addLoadrun:ALL;
 * editLoadrun:ALL(or appId sequence splitted by comma);
 * deleteLoadrun:ALL(or appId sequence splitted by comma);
 * autoLoadrun:ALL(or appId sequence splitted by comma);
 * manualLoadrun:ALL(or appId sequence splitted by comma);
 * 
 * @author youji.zj
 * 2011-10-20
 *
 */
public class Permisson {
	
	/***
	 *  ����ѹ��Ӧ�ã�type��
	 */
	public static final String ADD_APP = "addLoadrun";
	
	/***
	 * �޸�ѹ��Ӧ�ã�type��
	 */
	public static final String EDIT_APP = "editLoadrun";
	
	/***
	 *  ɾ��ѹ��Ӧ�ã�type��
	 */
	public static final String DELETE_APP = "deleteLoadrun";
	
	/*** 
	 * �Զ�ѹ�⣨type��
	 */
	public static final String AUTO_LOAD = "autoLoadrun";
	
	/***
	 * �ֶ�ѹ�⣨type��
	 */
	public static final String MANUAL_LOAD = "manualLoadrun";
	
	
	/***
	 * Ȩ�޵�aim�����ĳЩӦ�õĻ�aim���Զ��ŷָ���Ӧ�����У��磨1��2��3��
	 * aim��NO_AIM ��˵��ӵ������Ӧ�õ�Ȩ�ޣ����߸�ȫ�²���Ծ���Ӧ��
	 */
	public static final String NO_AIM = "ALL";

}
