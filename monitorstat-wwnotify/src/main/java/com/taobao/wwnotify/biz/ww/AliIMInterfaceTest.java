package com.taobao.wwnotify.biz.ww;

import java.io.UnsupportedEncodingException;

public class AliIMInterfaceTest extends Thread
{
	public static void main(String[] args)
	{
		AliIMInterfaceTest test = new AliIMInterfaceTest();
		test.test_single();
		//test_multi_thread();
		return;
	}
	
	public int test_single()
	{
		int iRet = AliWWMessageMgr.Instance().Init();
		if (iRet != 0)
		{
			System.out.println("initialize AliWWMessageMgr failed123.");
			return -1;
		}
		
		Ice.StringHolder retHolder = new Ice.StringHolder();
		try {
			iRet = AliWWMessageMgr.Instance().SendWWMessage(
					"cntaobaotest001", 
					"cntaobaotbtest850", 
					new String("ÄãºÃ".getBytes(),"GBK"), 
					0, 
					retHolder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("catch exception");
		}
		
		System.out.println(" iRet is " + new Integer(iRet) + "\nRetMessage " 
				+ retHolder.value);
		
		if (iRet != ErrorNum.E_SUCCESS)
		{
			//do something
		}
		
		//Destroy it if u do not need it any more.
		AliWWMessageMgr.Instance().Destroy();
		return iRet;
	}
	
	public static void test_multi_thread()
	{
		Thread[] ids = new AliIMInterfaceTest[2];
		int iRet = AliWWMessageMgr.Instance().Init();
		if (iRet != 0)
		{
			System.out.println("initialize AliWWMessageMgr failed.");
			return ;
		}
		
		for (int i = 0; i < ids.length; i++)
		{
			ids[i] = new AliIMInterfaceTest();
			ids[i].start();
		}

		for (int i = 0; i < ids.length; i++)
		{
			try
			{
				ids[i].join();
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
				continue;
			}
		}
		
		//Destroy it if u do not need it any more.
		AliWWMessageMgr.Instance().Destroy();
		return;
	}
	
	public void run()
	{
		int iRet = 0;
		long id = Thread.currentThread().getId();
		Ice.StringHolder retHolder = new Ice.StringHolder();
		try
		{
			for (int i = 0; i < 100; ++i)
			{
				
				iRet = AliWWMessageMgr.Instance().SendWWMessage(
						"cntaobaotest001", 
						"cntaobaotest002", 
						"test message", 
						0, 
						retHolder);
				
				if (iRet != ErrorNum.E_SUCCESS)
				{
					System.out.println(
							new Long(id)+ " *** error. iRet is " + new Integer(iRet)
							+ "\nError Message: " + retHolder.value);
				}
				else
				{
				System.out.println(
						new Long(id)+ " success num: " 
						+ new Integer(i) + "\n Return Message: " + retHolder.value);
				}
				
				sleep(1000);
			}
			System.out.println("return");
			return;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
	}
}


