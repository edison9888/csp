package com.taobao.wwnotify.biz.ww;

import java.util.EmptyStackException;
import java.util.Stack;

public class AliWWMessageMgr
{
	
	//做一个单体类
	private static AliWWMessageMgr m_instance = null; 
	
	//连接池中连接的数量
	private final int m_connNum = 5; 
	
	//连接串，测试环境和生产环境分别有一套。
	private final String m_strConnInfo = "AliWWMessageIAgentId:tcp -h 10.2.225.148 -p 33445 ";
	
	//用一个容器来保存连接，也可以用Vector或者其他的。
	private Stack<AliWWMessage> m_connPool;
	
	private boolean m_bInit = false;
	
	//调用者标识，该字符串由旺旺团队预先分配好，不能篡改。
	private String m_strCaller = "strCaller"; 
	
	//调用业务标识，该字符串由旺旺团队预先分配好，不能篡改。
	private String m_strServiceType = "strServiceType"; 
	
	//ICE连接串信息。
	private String m_strConnectInfo = null; 
	
	//建立一个单体类，并初始化连接。此类的main方法主要是用于测试连接的建立，
	//正式情况下，是由调用的入口去初始化，当然Init()只需要一次。
	public static void main(String[] args)
	{
		AliWWMessageMgr instance = AliWWMessageMgr.Instance();
		if (null == instance)
		{
			System.out.println("null ptr");
			return;
		}
		int iRet = instance.Init();
		System.out.println("Init return " + new Integer(iRet));
		return ;
	}
	
	public static AliWWMessageMgr Instance()
	{	
		if(m_instance==null){
			m_instance = new AliWWMessageMgr();
		}
		return m_instance;
	}
	
	public synchronized int Init()
	{
		if (m_bInit)
		{
			return 0;
		}
		
		int iRet = 0;
		System.out.println("init stack");
		m_connPool = new Stack<AliWWMessage>();
		
		for (int i = 0; i < m_connNum; ++i)
		{
			AliWWMessage sender = new AliWWMessage(
					m_strCaller, 
					m_strServiceType,
					m_strConnectInfo);
			iRet = sender.Init(m_strConnInfo);
			
			if (iRet != 0)
			{
				System.out.println("sender initialize failed.");
				//or should it throw exception?
				return -1;
			}
			m_connPool.push(sender);
		}
		
		m_bInit = true;
		return 0;
	}

	public synchronized void Destroy()
	{
		if(m_bInit == false)
		{
			return;
		}
		
		int iSize = m_connPool.size();
		for (int i =0; i < iSize; ++i)
		{
			m_connPool.get(i).Destroy();
		}
		m_connPool.clear();
	}
	
	public synchronized AliWWMessage  GetConnection()
	{
		try
		{
			synchronized(m_connPool)
			{
				if (m_connPool.empty())
				{
					m_connPool.wait();					
				}
				return m_connPool.pop();
			}
		}
		catch(EmptyStackException e)
		{		
			e.printStackTrace();
			return null;
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void ReleaseConnection(AliWWMessage conn)
	{
		try
		{
			synchronized(m_connPool)
			{
				m_connPool.push(conn);
				m_connPool.notify();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param strFromId 消息的发送者的长ID，中文站为 "cnalichn"+ID，
	 * 淘宝为"cntaobao"+ID。跨站发送消息必须事先经过审批。
	 * @param strToId 消息的接受者的长ID。同上。
	 * @param strMessage 消息内容，为GBK编码，内容的正确性由调用方保证。
	 * @param saveType 如果接收者不在线，是否存离线消息，下次登录收到。1--存，0--不存。
	 * @param strRetMessage 方法调用的返回错误码描述信息。
	 * @return
	 */
	public int SendWWMessage(
			String strFromId,
			String strToId,
			String strMessage,
			int saveType,
			Ice.StringHolder strRetMessage)
	{
		AliWWMessage sender = GetConnection();
		if (null == sender)
		{
			return -16;
		}
		
		int iRet = sender.SendWWMessage(
				strFromId,
				strToId,
				strMessage,
				saveType,
				strRetMessage);
		
		ReleaseConnection(sender);
		return iRet;
	}
	
	/**
	 * @param strToId 消息的接受者的长ID,中文站为 "cnalichn"+ID，
	 * 淘宝为"cntaobao"+ID。跨站发送消息必须事先经过审批。
	 * @param strMessage 消息内容。格式为(时间||标题||内容) "20091127173030||系统通知||您的交易成功完成。"
	 * @param saveType 是否存离线。0--不存，1--存。接收者如果当前不在线，下一次登录时收到。
	 * @param strRetMessage 方法调用的返回错误码描述信息。
	 * @return
	 */
	public int SendNotifyMessage(
			String strToId, 
			String strMessage, 
			int saveType, 
			Ice.StringHolder strRetMessage)
	{
		AliWWMessage sender = GetConnection();
		if (null == sender)
		{
			return -16;
		}
		
		int iRet = sender.SendNotifyMessage(
				strToId,
				strMessage,
				saveType,
				strRetMessage);
		
		ReleaseConnection(sender);
		return iRet;
	}
	
	/**
	 * @param adParam 业务参数，具体说明请参考文档。
	 * @param strToId 接受者长ID。
	 * @param strRetMessage  方法调用的返回错误码描述信息。
	 * @return
	 */
	 
	public int SendTipMessage(
			STADPUSH adParam, 
			String strToId, 
			Ice.StringHolder strRetMessage)
	{
		AliWWMessage sender = GetConnection();
		if (null == sender)
		{
			return -16;
		}
		
		int iRet = sender.SendTipMessage(
				adParam,
				strToId,
				strRetMessage);
		
		ReleaseConnection(sender);
		return iRet;
		
}



	
	
	
	
	
	
	
	
	
	
	

}
