/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package adapter;

import com.taobao.monitor.selenium.adapter.CaseAdapter;
import com.taobao.monitor.selenium.util.log.LogSelenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-30 - 上午10:50:18
 * @version 1.0
 */
public class UseCaseAdapterTest implements CaseAdapter{

	/**
	 * 
	 * 2011-5-30 - 上午10:56:10
	 */
	@Override
	public void execute(LogSelenium selenium) {
		System.out.println("测试用户自定义适配器！");
		selenium.setContext("loggingSeleniumSuccessSample()");
		selenium.open("/");
		selenium.waitForPageToLoad("30000");
		boolean logstate = selenium.isElementPresent("css=a.user-nick");;
		if(logstate){
			selenium.logComment("登陆成功！");
		}else{
			selenium.click("link=请登录");
			selenium.waitForPageToLoad("30000");
			
			//selenium.click("//form[@id='J_StaticForm']/div[1]/span");
			//verifyTrue(selenium.isChecked("J_SafeLoginCheck"));
			selenium.click("J_SafeLoginCheck");
			selenium.type("TPL_username_1", "selenium_test");
			selenium.type("TPL_password", "lztaomin@1986");
			selenium.click("//button[@type='submit']");
			selenium.waitForPageToLoad("30000");
			String userName = selenium.getText("link=selenium_test");//class=user-nick
			//boolean logstate = selenium.isElementPresent("css=a.user-nick");
			if(userName!=null && !userName.equals("")){
				selenium.logComment("登陆成功！");
			}else{
				selenium.logComment("登陆失败！");
				//assertTrue("登录失败！", selenium.isElementPresent("css=a.user-nick"), selenium);
			}
			selenium.type("q", "电脑");
			selenium.click("//form[@id='J_TSearchForm']/button");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=【电保包】联想笔记本电脑/联想 Z465A-NNI(酷黑)/四核/");
			//selenium.waitForPageToLoad("30000");
			String[] titles = selenium.getAllWindowTitles();  
	        selenium.selectWindow("title="+titles[titles.length-1]);//选择最后打开的一个窗口
	        try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        selenium.click("J_LinkBuy");
	        selenium.close();//关闭最后打开的一个窗口  
	        selenium.selectWindow("null");//重新聚集在最开始的一个窗口（只有两个窗口的时候） 
			//selenium.waitForPageToLoad("3000");
		   selenium.stop();
		}
	}

}
