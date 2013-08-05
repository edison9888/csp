package com.taobao.wwnotify.biz.ww;

import java.util.EmptyStackException;
import java.util.Stack;

public class AliWWMessageMgr
{
	
	//��һ��������
	private static AliWWMessageMgr m_instance = null; 
	
	//���ӳ������ӵ�����
	private final int m_connNum = 5; 
	
	//���Ӵ������Ի��������������ֱ���һ�ס�
	private final String m_strConnInfo = "AliWWMessageIAgentId:tcp -h 10.2.225.148 -p 33445 ";
	
	//��һ���������������ӣ�Ҳ������Vector���������ġ�
	private Stack<AliWWMessage> m_connPool;
	
	private boolean m_bInit = false;
	
	//�����߱�ʶ�����ַ����������Ŷ�Ԥ�ȷ���ã����ܴ۸ġ�
	private String m_strCaller = "strCaller"; 
	
	//����ҵ���ʶ�����ַ����������Ŷ�Ԥ�ȷ���ã����ܴ۸ġ�
	private String m_strServiceType = "strServiceType"; 
	
	//ICE���Ӵ���Ϣ��
	private String m_strConnectInfo = null; 
	
	//����һ�������࣬����ʼ�����ӡ������main������Ҫ�����ڲ������ӵĽ�����
	//��ʽ����£����ɵ��õ����ȥ��ʼ������ȻInit()ֻ��Ҫһ�Ρ�
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
	 * @param strFromId ��Ϣ�ķ����ߵĳ�ID������վΪ "cnalichn"+ID��
	 * �Ա�Ϊ"cntaobao"+ID����վ������Ϣ�������Ⱦ���������
	 * @param strToId ��Ϣ�Ľ����ߵĳ�ID��ͬ�ϡ�
	 * @param strMessage ��Ϣ���ݣ�ΪGBK���룬���ݵ���ȷ���ɵ��÷���֤��
	 * @param saveType ��������߲����ߣ��Ƿ��������Ϣ���´ε�¼�յ���1--�棬0--���档
	 * @param strRetMessage �������õķ��ش�����������Ϣ��
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
	 * @param strToId ��Ϣ�Ľ����ߵĳ�ID,����վΪ "cnalichn"+ID��
	 * �Ա�Ϊ"cntaobao"+ID����վ������Ϣ�������Ⱦ���������
	 * @param strMessage ��Ϣ���ݡ���ʽΪ(ʱ��||����||����) "20091127173030||ϵͳ֪ͨ||���Ľ��׳ɹ���ɡ�"
	 * @param saveType �Ƿ�����ߡ�0--���棬1--�档�����������ǰ�����ߣ���һ�ε�¼ʱ�յ���
	 * @param strRetMessage �������õķ��ش�����������Ϣ��
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
	 * @param adParam ҵ�����������˵����ο��ĵ���
	 * @param strToId �����߳�ID��
	 * @param strRetMessage  �������õķ��ش�����������Ϣ��
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
