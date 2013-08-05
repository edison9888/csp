package com.taobao.wwnotify.biz.ww;

import java.io.UnsupportedEncodingException;


public class MessageDemo {
	public static int main()
	{
		int iRet = AliWWMessageMgr.Instance().Init();
		if (iRet != 0)
		{
			System.out.println("initialize AliWWMessageMgr failed.");
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

}
