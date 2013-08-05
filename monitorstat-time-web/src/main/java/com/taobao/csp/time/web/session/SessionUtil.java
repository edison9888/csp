
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.time.custom.arkclient.ArkDomain;
import com.taobao.csp.time.web.action.UserCustomController;
import com.taobao.monitor.common.ao.center.CspUserInfoAo;
import com.taobao.monitor.common.po.CspUserInfoPo;

/**
 * @author xiaodu
 *
 * ����2:28:54
 */
public class SessionUtil {
	
	private static String SESSION_KEY="~$session$~";
	private static Logger logger = Logger.getLogger(SessionUtil.class);
	
	public static CspUserInfoPo getCspUserInfo(HttpServletRequest request) throws Exception{
		
//		HttpSession session = request.getSession(true);
//		Object obj = session.getAttribute(SESSION_KEY);
//		if(obj != null&&obj instanceof CspUserInfoPo){
//			return (CspUserInfoPo)obj;
//		}
		//��Ϊ���ؾ�����ԣ�session�������ã���Ҫ��DB����
		String mail = ArkDomain.getArkUserEmail(request);
		System.out.println("----------------getCspUserInfo() mail: "+ mail);
		CspUserInfoPo user = CspUserInfoAo.get().findCspUserInfoByMail(mail);
		if(user == null){//Ŀǰû�е�¼��Ϣ��¼
			user = new CspUserInfoPo();
			user.setAccept_apps("");
			user.setMail(mail);
			user.setPermission_desc("");
			user.setPhone("");
			user.setPhone_feature("3#1#00:00#23:59$3#2#18:00#23:59$3#3#18:00#23:59$3#4#18:00#23:59$3#5#18:00#23:59$3#6#18:00#23:59$3#7#00:00#23:59$");
			user.setWangwang("");
			user.setWangwang_feature("0#1#00:00#23:59$0#2#00:00#23:59$0#3#00:00#23:59$0#4#00:00#23:59$0#5#00:00#23:59$0#6#00:00#23:59$0#7#00:00#23:59$");
			boolean b = CspUserInfoAo.get().insertCspUserInfo(user);
			String msg ="";
			if(!b){
				msg = "����ʧ��!���¿��ʧ��";
				logger.error(msg);
				throw new Exception(msg);
			}else{
				msg = "���³ɹ�!";
			}
		}
		return user;
	}
	
	
	public static void setCspUserInfo(HttpServletRequest request,CspUserInfoPo po){
		HttpSession session = request.getSession(true);
		session.setAttribute(SESSION_KEY, po);
	}

}
