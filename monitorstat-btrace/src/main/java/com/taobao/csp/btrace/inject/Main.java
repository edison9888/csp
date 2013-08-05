
package com.taobao.csp.btrace.inject;

import com.sun.tools.attach.VirtualMachine;


/**
 * ���� ��ȡ Ӧ�ý��̣�����agent ͨ�� attach ע�뵽 jvm �У�
 * @author xiaodu
 * @version 2011-8-14 ����09:19:26
 */
public class Main {
	    public static void main(String[] args) {
//	    	String pid = "5260";
//	    	String agentPath = "D:\\taobao-SVNROOT\\coremonitor\\monitorstat-btrace\\target\\monitorstat-btrace-1.0.0-SNAPSHOT.jar";
//	    	String agentArgs = "127.0.0.1";
	    	
	    	String pid = args[0].trim();
	    	String agentPath = args[1].trim();
	    	String agentArgs = args[2].trim();
	    	
	    	try {
	            VirtualMachine vm = null;
	            vm = VirtualMachine.attach(pid);	            
	            vm.loadAgent(agentPath, agentArgs+","+agentPath);
	        } catch (Exception re) {
	        	System.out.println("pid:"+pid+" agentPath:"+agentPath+" agentArgs:"+agentArgs);
	        	re.printStackTrace();
	        	return ;
	        }
	        System.out.println("BTRACE INJECT SUCCESS");
	    }
}
