package com.taobao.monitor.time.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class TairKeyValidator {
	public static String TAIR_KEY_PREFIX = "Tair";
	///////////tradeplatform
	/**
	 * tair��keyֵ kdbtc_dtair,kdbtc2_dtair,kdbtc3_dtair,	ldbtc4_dtair
	 * ƥ���ʽgroup_kdbtc3`1002 
	 * ƥ���ʽgroup_ldbtc4
	 */
	private static String tpSnapShotTairInfix = "dbtc";
	private static String tpSnapShotTairDesc = "���׿���Tair�澯"; 

	private static String tpUicTairInfix = "group3";
	private static String tpUicTairDesc = "����UIC��tair�澯"; 
	
	private static String tpTradeTairInfix = "group_1";
	private static String tpTradeTairDesc = "��������ȥ��У���tair";
	
	private static String tpDetailTairInfix = "comm";
	private static String tpDetailTairDesc = "detailҳ��ĳɽ���¼��tair";
	
	private static String tpMseckilTairInfix = "mseckill";
	private static String tpMseckilTairDesc = "ͳ�Ƴ��������tair";
	
	private static String tpSeckilTairRegex = ".+[^m]seckill";
	private static String tpSeckilTairDesc = "��ɱ �������������ȷ���ջ���ʱ������  ����sid������֤����֣�����count������ֹ�����ҳ������õ�count������С��������ƣ�����auctionTitle�Ĳ�ѯ���Ƶ�tair";
	
	private static String tpItemTairInfix = "group1";
	private static String tpItemTairDesc = "���׹���tair��ƽ���˿�ʱ�䡢ȥO���ص�tair��";
	
	///misccenter
	/**
	 * tair��keyֵ kdbtc_dtair,kdbtc2_dtair,kdbtc3_dtair,	ldbtc4_dtair
	 * ƥ���ʽgroup_kdbtc3`1002 
	 * ƥ���ʽgroup_ldbtc4
	 */
	private static String mcGroup1TairInfix = "group_1";
	private static String mcGroup1TairDesc = "���׹���tair��ƽ���˿�ʱ�䡢ȥO���ء�";
	
	private static String mcMseckilTairInfix = "mseckill";
	private static String mcMseckilTairDesc = "��ɱҵ�񡢳�����C2B���г���tair";
	
	private static String mcMcommTairInfix = "mcomm";
	private static String mcMcommTairDesc = "ֱ�䱦����ѡҵ�񡢹��ﳵ����桢���������׺�Լ��tair";
	
	private static String mcMarketTairInfix = "market";
	private static String mcMarketTairDesc = "�������ļ���Ϣ�����tair";
	
	private static String mcItemTairInfix = "group1";
	private static String mcItemTairDesc = "���׹���tair������ƽ���˿�ʱ�䡢ȥO���ء�";
	
	///cart
	/**
	 * tair��keyֵ 
	 * ƥ���ʽgroup_kdbtc3`1002 
	 * ƥ���ʽgroup_ldbtc4
	 */
	private static String cartGroup3TairInfix = "group3";
	private static String cartGroup3TairDesc = "Uic tair"; 
	
	private static String cartCommTairInfix = "comm";
	private static String cartCommTairDesc = "mini���ﳵ���ֺ��б��tair";
	
	private static String cartItemTairInfix = "group1";
	private static String cartItemTairDesc = "��ѯ��Ʒ��tair"; 
	
	///tradeface
	/**
	 * tair��keyֵ 
	 * ƥ���ʽgroup_kdbtc3`1002 
	 * ƥ���ʽgroup_ldbtc4
	 */
	private static String tfMseckilTairInfix = "mseckil";
	private static String tfMseckilTairDesc = "��ɱ�ã���Ʒ����������ֻ�������û��μӵİ�����������315���tair"; 
	
	private static String tfMcommTairInfix = "mcomm";
	private static String tfMcommTairDesc = "ͳ�Ƶ�ǰ��������������ߵ���Ʒ���а�,�������á��������˷�ģ�塢Umidtair���洢�û����⽻����Ϣ��������ʱ��������б�htm��ʷ�ⶩ��Ĭ���б�ġ�����ƾ֤�������ÿ���ط����ż�¼ tair���޸ļ۸�����������桢HTM���ҷ���Ƶ�ʿ��ƹ��á���ע���û���������رս��׺��������б����ơ����װ�ȫ";
	
	private static String tfSnscommTairInfix = "snscomm";
	private static String tfSnscommTairDesc = "�ۻ���tair";
	
	private static String tfSeckillTairInfix = "seckill";
	private static String tfSeckillTairDesc = "��ɱ��Ⱥ������ҳͷcount���رս��׺��������б����Ƶ�tair";
	
	private static String tfMarketTairInfix = "market";
	private static String tfMarketTairDesc = "��������Ϸ��xml�ļ����ݡ�������ҵ���tair"; 
	
	private static String tfItemTairInfix = "group1";
	private static String tfItemTairDesc = "��Ʒ�����tair";
	
	private static String tfUicTairInfix = "group3";
	private static String tfUicTairDesc = "Uic tair"; 
	
	private static String tfSessionTairInfix = "session";
	private static String tfSessionTairDesc = "Uic Session Tair"; 
	
	//tradelogs
	/**
	 * tair��keyֵ 
	 * ƥ���ʽgroup_kdbtc3`1002 
	 * ƥ���ʽgroup_ldbtc4
	 */
	private static String tlSeckilTairInfix = "seckil";
	private static String tlSeckilTairDesc = "����count[���ҵ����Ѽ�������ҵ����Ѽ�������Ʒ���ۼ��۳���]��tair"; 
	
	private static String tlMcommTairInfix = "mcomm";
	private static String tlMcommTairDesc = "��Ʒ������ɽ���¼��tair";
	
	private static String tlTradeTairInfix = "group_1";
	private static String tlTradeTairDesc = "���׻��滯��tair"; 
	
	/**
	 * �ж��Ƿ���tair��keyֵ
	 * @param keyName
	 * @return keyDesc
	 */
	public static boolean isTairKey(String keyName){
		return (StringUtils.isNotBlank(keyName) && keyName.startsWith(TAIR_KEY_PREFIX));
	}
	
	/**
	 * ͨ��appName��keyֵ��ȡkeyֵ������
	 * @param keyName
	 * @return keyDesc
	 */
	public static String getTairKeyDescByAppNameAndKeyName(String appName, String keyName){
		if("tradeplatform".equals(appName)){
			return getTradeplatformTairKeyDesc(keyName);
		}
		if("tf_buy".equals(appName) || "tf_tm".equals(appName)){
			return getTradefaceTairKeyDesc(keyName);
		}
		if("cart".equals(appName)){
			return getCartTairKeyDesc(keyName);
		}
		if("misccenter".equals(appName)){
			return getMisccenterTairKeyDesc(keyName);
		}
		if("tradelogs".equals(appName)){
			return getMisccenterTairKeyDesc(keyName);
		}
		return ("δ����Ӧ��:"+appName);
	}
	
	/**
	 * ͨ��keyֵ��ȡkeyֵ������
	 * @param keyName
	 * @return keyDesc
	 */
	public static String getTradeplatformTairKeyDesc(String keyName){
		if(keyName.contains(tpSnapShotTairInfix)){
			return tpSnapShotTairDesc;
		}
		if(keyName.contains(tpUicTairInfix)){
			return tpUicTairDesc;
		}
		if(keyName.contains(tpTradeTairInfix)){
			return tpTradeTairDesc;
		}
		if(keyName.contains(tpDetailTairInfix)){
			return tpDetailTairDesc;
		}
		if(keyName.contains(tpMseckilTairInfix)){
			return tpMseckilTairDesc;
		}
		if( Pattern.matches(tpSeckilTairRegex, keyName) ){
			return tpSeckilTairDesc;
		}
		if(keyName.contains(tpItemTairInfix)){
			return tpItemTairDesc;
		}
		return (keyName+"δ����ҵ����");
	}
	
	public static String getMisccenterTairKeyDesc(String keyName){
		if(keyName.contains(mcGroup1TairInfix)){
			return mcGroup1TairDesc;
		}
		if(keyName.contains(mcMseckilTairInfix)){
			return mcMseckilTairDesc;
		}
		if(keyName.contains(mcMcommTairInfix)){
			return mcMcommTairDesc;
		}
		if(keyName.contains(mcMcommTairInfix)){
			return mcMcommTairDesc;
		}
		if(keyName.contains(mcMarketTairInfix)){
			return mcMarketTairDesc;
		}
		if(keyName.contains(mcItemTairInfix)){
			return mcItemTairDesc;
		}
		return (keyName+"δ����ҵ����");
	}
	
	public static String getCartTairKeyDesc(String keyName){
		if(keyName.contains(cartGroup3TairInfix)){
			return cartGroup3TairDesc;
		}
		if(keyName.contains(cartCommTairInfix)){
			return cartCommTairDesc;
		}
		if(keyName.contains(cartItemTairInfix)){
			return cartItemTairDesc;
		}
		return (keyName+"δ����ҵ����");
	}
	
	public static String getTradefaceTairKeyDesc(String keyName){
		if(keyName.contains(tfMseckilTairInfix)){
			return tfMseckilTairDesc;
		}
		if(keyName.contains(tfMcommTairInfix)){
			return tfMcommTairDesc;
		}
		if(keyName.contains(tfSnscommTairInfix)){
			return tfSnscommTairDesc;
		}
		if(keyName.contains(tfSeckillTairInfix)){
			return tfSeckillTairDesc;
		}
		if(keyName.contains(tfMarketTairInfix)){
			return tfMarketTairDesc;
		}
		if(keyName.contains(tfItemTairInfix)){
			return tfItemTairDesc;
		}
		if(keyName.contains(tfUicTairInfix)){
			return tfUicTairDesc;
		}
		if(keyName.contains(tfSessionTairInfix)){
			return tfSessionTairDesc;
		}
		return (keyName+"δ����ҵ����");
	}
	
	public static String getTradelogsTairKeyDesc(String keyName){
		if(keyName.contains(tlSeckilTairInfix)){
			return tlSeckilTairDesc;
		}
		if(keyName.contains(tlMcommTairInfix)){
			return tlMcommTairDesc;
		}
		if(keyName.contains(tlTradeTairInfix)){
			return tlTradeTairDesc;
		}
		return (keyName+"δ����ҵ����");
	}
}
