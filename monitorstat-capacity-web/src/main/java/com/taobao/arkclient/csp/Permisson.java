package com.taobao.arkclient.csp;

/***
 * �����滮Ȩ�޳���
 * 
 * Ȩ�����õĹ���
 * addCapacityApp:ALL;
 * editCapacityApp:ALL(or appId sequence splitted by comma);
 * deleteCapacityApp:ALL(or appId sequence splitted by comma);
 * reinsertCapacityPv:ALL;
 * reflushCapacityRank:ALL;
 * 
 * @author youji.zj
 * 2011-10-20
 *
 */
public class Permisson {
	
	/***
	 *  ���Ӳ��������滮��Ӧ�ã�type��
	 */
	public static final String ADD_APP = "addCapacityApp";
	
	/***
	 * �޸Ĳ��������滮��Ӧ�ã�type��
	 */
	public static final String EDIT_APP = "editCapacityApp";
	
	/***
	 *  ɾ�����������滮��Ӧ�ã�type��
	 */
	public static final String DELETE_APP = "deleteCapacityApp";
	
	/*** 
	 * ���¼������pv��type��
	 */
	public static final String REINSERT_PV = "reinsertCapacityPv";
	
	/***
	 * ���¼����������У�type��
	 */
	public static final String REFLUSH_RANK = "reflushCapacityRank";
	
	
	/***
	 * Ȩ�޵�aim�����ĳЩӦ�õĻ�aim���Զ��ŷָ���Ӧ�����У��磨1��2��3��
	 * aim��NO_AIM ��˵��ӵ������Ӧ�õ�Ȩ�ޣ����߸�ȫ�²���Ծ���Ӧ��
	 */
	public static final String NO_AIM = "ALL";

}
