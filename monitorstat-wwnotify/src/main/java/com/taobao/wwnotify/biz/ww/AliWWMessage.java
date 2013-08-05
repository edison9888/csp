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
	 * 发送旺旺聊天消息
	 * 
	 * @param strFromId
	 *            消息的发送者的长ID，中文站为 "cnalichn"+ID，
	 *            淘宝为"cntaobao"+ID。跨站发送消息必须事先经过审批。
	 * @param strToId
	 *            消息的接受者的长ID。同上。
	 * @param strMessage
	 *            消息内容，为GBK编码，内容的正确性由调用方保证。
	 * @param saveType
	 *            如果接收者不在线，是否存离线消息，下次登录收到。1--存，0--不存。
	 * @param strRetMessage
	 *            方法调用的返回错误码描述信息。
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
	 * 发送中央弹出框消息，比如交易提醒的消息。
	 * 
	 * @param strToId
	 *            消息的接受者的长ID,中文站为 "cnalichn"+ID，
	 *            淘宝为"cntaobao"+ID。跨站发送消息必须事先经过审批。
	 * @param strMessage
	 *            消息内容。格式为(时间||标题||内容) "20091127173030||系统通知||您的交易成功完成。"
	 * @param saveType
	 *            是否存离线。0--不存，1--存。接收者如果当前不在线，下一次登录时收到。
	 * @param strRetMessage
	 *            方法调用的返回错误码描述信息。
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
	 * 发送屏幕右下角的系统浮出消息
	 * 
	 * @param adParam
	 *            业务参数，具体说明请参考文档。
	 * @param strToId
	 *            接受者长ID。
	 * @param strRetMessage
	 *            方法调用的返回错误码描述信息。
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
