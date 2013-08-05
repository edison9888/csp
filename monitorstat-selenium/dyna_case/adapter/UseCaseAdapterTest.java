/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package adapter;

import com.taobao.monitor.selenium.adapter.CaseAdapter;
import com.taobao.monitor.selenium.util.log.LogSelenium;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-30 - ����10:50:18
 * @version 1.0
 */
public class UseCaseAdapterTest implements CaseAdapter{

	/**
	 * 
	 * 2011-5-30 - ����10:56:10
	 */
	@Override
	public void execute(LogSelenium selenium) {
		System.out.println("�����û��Զ�����������");
		selenium.setContext("loggingSeleniumSuccessSample()");
		selenium.open("/");
		selenium.waitForPageToLoad("30000");
		boolean logstate = selenium.isElementPresent("css=a.user-nick");;
		if(logstate){
			selenium.logComment("��½�ɹ���");
		}else{
			selenium.click("link=���¼");
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
				selenium.logComment("��½�ɹ���");
			}else{
				selenium.logComment("��½ʧ�ܣ�");
				//assertTrue("��¼ʧ�ܣ�", selenium.isElementPresent("css=a.user-nick"), selenium);
			}
			selenium.type("q", "����");
			selenium.click("//form[@id='J_TSearchForm']/button");
			selenium.waitForPageToLoad("30000");
			selenium.click("link=���籣��������ʼǱ�����/���� Z465A-NNI(���)/�ĺ�/");
			//selenium.waitForPageToLoad("30000");
			String[] titles = selenium.getAllWindowTitles();  
	        selenium.selectWindow("title="+titles[titles.length-1]);//ѡ�����򿪵�һ������
	        try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        selenium.click("J_LinkBuy");
	        selenium.close();//�ر����򿪵�һ������  
	        selenium.selectWindow("null");//���¾ۼ����ʼ��һ�����ڣ�ֻ���������ڵ�ʱ�� 
			//selenium.waitForPageToLoad("3000");
		   selenium.stop();
		}
	}

}
