package com.taobao.wwnotify.biz.ww;


public class AliWWMessage {
	private boolean m_bInit = false;

	private Ice.Communicator m_ic = null;
	private Ice.ObjectPrx m_base = null;
	private WWMessageInterfacePrx m_prx = null;

	private String m_strCaller = null;
	private String m_strServiceType = null;
	private String m_strConnectInfo = null;

	 public AliWWMessage(
	 String strCaller,
	 String strServiceType,
	 String strConnectInfo)
	 {
		 super();
		 this.m_strCaller = strCaller;
		 this.m_strServiceType = strServiceType;
		 this.m_strConnectInfo = strConnectInfo;
	 }

	public int Init(String strConnectInfo) {
		if (true == m_bInit) {
			return 0;
		}
		m_strConnectInfo = strConnectInfo;
		int iRet = 0;

		try {
			m_ic = Ice.Util.initialize();
			m_base = m_ic.stringToProxy(m_strConnectInfo);
			m_prx = WWMessageInterfacePrxHelper.checkedCast(m_base);
			if (null == m_prx) {
				iRet = -1;
				throw new Exception("CheckedCast failed. Proxy is null");
			}
			iRet = 0;
		} catch (Ice.LocalException e) {
			e.printStackTrace();
			iRet = -2;
		} catch (Exception e) {
			e.printStackTrace();
			iRet = -9;
		} finally {
			if (0 == iRet) {
				m_bInit = true;
			} else {
				m_bInit = false;
			}
		}
		return iRet;
	}

	public void Destroy() {
		if (m_bInit == false) {
			return;
		}
		try {
			m_ic.destroy();
		} catch (Ice.CloseConnectionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			m_ic = null;
		}
	}

	public boolean CheckConnectionActive() {
		if (true == m_bInit) {
			return true;
		}
		try {
			m_prx.ice_ping();
		} catch (Exception e) {
			e.printStackTrace();
			Destroy();
			return false;
		}

		return true;
	}

	/**
	 * ��������������Ϣ
	 * 
	 * @param strFromId
	 *            ��Ϣ�ķ����ߵĳ�ID������վΪ "cnalichn"+ID��
	 *            �Ա�Ϊ"cntaobao"+ID����վ������Ϣ�������Ⱦ���������
	 * @param strToId
	 *            ��Ϣ�Ľ����ߵĳ�ID��ͬ�ϡ�
	 * @param strMessage
	 *            ��Ϣ���ݣ�ΪGBK���룬���ݵ���ȷ���ɵ��÷���֤��
	 * @param saveType
	 *            ��������߲����ߣ��Ƿ��������Ϣ���´ε�¼�յ���1--�棬0--���档
	 * @param strRetMessage
	 *            �������õķ��ش�����������Ϣ��
	 * @return
	 */
	public int SendWWMessage(String strFromId, String strToId,
			String strMessage, int saveType, Ice.StringHolder strRetMessage) {
		int iRet = 0;
		if (CheckConnectionActive() == false) {
			iRet = Init(m_strConnectInfo);
			if (iRet != 0) {
				return iRet;
			}
		}
		try {
			iRet = m_prx.SendWWMessage(m_strCaller, m_strServiceType,
					strFromId, strToId, strMessage, saveType, strRetMessage);

			return iRet;
		} catch (Exception e) {
			e.printStackTrace();
			m_bInit = false;
			return -3;
		}
	}

	/**
	 * �������뵯������Ϣ�����罻�����ѵ���Ϣ��
	 * 
	 * @param strToId
	 *            ��Ϣ�Ľ����ߵĳ�ID,����վΪ "cnalichn"+ID��
	 *            �Ա�Ϊ"cntaobao"+ID����վ������Ϣ�������Ⱦ���������
	 * @param strMessage
	 *            ��Ϣ���ݡ���ʽΪ(ʱ��||����||����) "20091127173030||ϵͳ֪ͨ||���Ľ��׳ɹ���ɡ�"
	 * @param saveType
	 *            �Ƿ�����ߡ�0--���棬1--�档�����������ǰ�����ߣ���һ�ε�¼ʱ�յ���
	 * @param strRetMessage
	 *            �������õķ��ش�����������Ϣ��
	 * @return
	 */
	public int SendNotifyMessage(String strToId, String strMessage,
			int saveType, Ice.StringHolder strRetMessage) {
		int iRet = 0;
		if (CheckConnectionActive() == false) {
			iRet = Init(m_strConnectInfo);
			if (iRet != 0) {
				return iRet;
			}
		}
		try {
			iRet = m_prx.SendNotifyMessage(m_strCaller, m_strServiceType,
					strToId, strMessage, saveType, strRetMessage);

			return iRet;
		} catch (Exception e) {
			e.printStackTrace();
			m_bInit = false;
			return -3;
		}
	}

	/**
	 * ������Ļ���½ǵ�ϵͳ������Ϣ
	 * 
	 * @param adParam
	 *            ҵ�����������˵����ο��ĵ���
	 * @param strToId
	 *            �����߳�ID��
	 * @param strRetMessage
	 *            �������õķ��ش�����������Ϣ��
	 * @return
	 */

	public int SendTipMessage(STADPUSH adParam, String strToId,
			Ice.StringHolder strRetMessage) {
		int iRet = 0;
		if (CheckConnectionActive() == false) {
			iRet = Init(m_strConnectInfo);
			if (iRet != 0) {
				return iRet;
			}
		}
		try {
			iRet = m_prx.SendTipMessage(m_strCaller, m_strServiceType, adParam,
					strToId, strRetMessage);

			return iRet;
		} catch (Exception e) {
			e.printStackTrace();
			m_bInit = false;
			return -3;
		}
	}

}
