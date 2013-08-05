
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.taobao.csp.alarm.service.UserService;
import com.taobao.csp.time.custom.arkclient.ArkDomain;
import com.taobao.csp.time.web.session.SessionUtil;
import com.taobao.monitor.common.ao.center.CspUserInfoAo;
import com.taobao.monitor.common.po.CspUserInfoPo;

/**
 * @author xiaodu
 *
 * 下午1:37:23
 */
@Controller
@RequestMapping("/user.do")
public class LoginController {
	
	
	@RequestMapping(params = "method=login")
	public ModelAndView login(HttpServletRequest request){
		String mail = ArkDomain.getArkUserEmail(request);
		CspUserInfoPo user = CspUserInfoAo.get().findCspUserInfoByMail(mail);
		if(user == null){//目前没有登录信息记录
			ModelAndView view = new ModelAndView("/time/conf/user_info_update");
			user = new CspUserInfoPo();
			user.setAccept_apps("");
			user.setMail(mail);
			user.setPermission_desc("");
			user.setPhone("");
			user.setPhone_feature("3#1#00:00#23:59$3#2#18:00#23:59$3#3#18:00#23:59$3#4#18:00#23:59$3#5#18:00#23:59$3#6#18:00#23:59$3#7#00:00#23:59$");
			user.setWangwang("");
			user.setWangwang_feature("0#1#00:00#23:59$0#2#00:00#23:59$0#3#00:00#23:59$0#4#00:00#23:59$0#5#00:00#23:59$0#6#00:00#23:59$0#7#00:00#23:59$");
			view.addObject("userinfo", user);
			return view;
		}
		SessionUtil.setCspUserInfo(request, user);
		
		ModelAndView view = new ModelAndView("/index");
		return view;
	}
	
	@RequestMapping(params = "method=update")
	public ModelAndView update(HttpServletRequest request , HttpServletResponse response) throws IOException {
		String selectApp = request.getParameter("selectApp"); // 拼接好告警的应用：
		String wangwang = request.getParameter("wangwang");
		String phone = request.getParameter("phone");
		String mail = request.getParameter("mail");
		
		CspUserInfoPo po = new CspUserInfoPo();
		po.setMail(mail);
		po.setWangwang(wangwang);
		po.setPhone(phone);

		String phoneDesc = "";

		// 次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		for (int i = 1; i <= 7; i++) {
			String phone_week = request.getParameter("phone_week_" + i);
			String phone_num = request.getParameter("phone_num_" + i);
			if (phone_week != null && !"".equals(phone_week.trim())
					&& phone_num != null && !"".equals(phone_num.trim())) {
				phoneDesc += phone_num + "#" + i + "#" + phone_week + "$";
			}
		}
		String wangwangDesc = "";
		// 次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
		for (int i = 1; i <= 7; i++) {
			String ww_week = request.getParameter("wangwang_week_" + i);
			String ww_num = request.getParameter("wangwang_num_" + i);
			if (ww_week != null && !"".equals(ww_week.trim()) && ww_num != null
					&& !"".equals(ww_num.trim())) {
				wangwangDesc += ww_num + "#" + i + "#" + ww_week + "$";
			}
		}
		po.setPhone_feature(phoneDesc);
		po.setWangwang_feature(wangwangDesc);
		po.setAccept_apps(selectApp);
		
		
		String domainmail = ArkDomain.getArkUserEmail(request);
		
		ModelAndView view = new ModelAndView("/time/conf/user_info_update");
		if(!domainmail.equals(mail)){
			view.addObject("msg", "更新失败! 域email和提交上来的email不一致");
			view.addObject("userinfo", po);
			return view;
		}
		
		
		CspUserInfoPo user = CspUserInfoAo.get().findCspUserInfoByMail(mail);
		boolean b = false;
		
		if(user == null){
			b = CspUserInfoAo.get().insertCspUserInfo(po);
		}else{
			b =CspUserInfoAo.get().updateCspUserInfo(po);
		}
		
		if(!b){
			view.addObject("msg", "更新失败!更新库表失败");
			view.addObject("userinfo", po);
			return view;
		}
		
		view.addObject("msg", "更新成功");
		view.addObject("userinfo", po);
		
		user = CspUserInfoAo.get().findCspUserInfoByMail(mail);
		UserService.get().register(UserService.get().createUserDefine(user));
		
		SessionUtil.setCspUserInfo(request, user);
		return view;
	}
	
	
	@RequestMapping(params = "method=display")
	public ModelAndView display(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView view = new ModelAndView("/time/conf/user_info_update");
		String mail = ArkDomain.getArkUserEmail(request);
		CspUserInfoPo user = CspUserInfoAo.get().findCspUserInfoByMail(mail);
		if(user == null){//目前没有登录信息记录
			user = new CspUserInfoPo();
			user.setAccept_apps("");
			user.setMail(mail);
			user.setPermission_desc("");
			user.setPhone("");
			user.setPhone_feature("3#1#00:00#23:59$3#2#18:00#23:59$3#3#18:00#23:59$3#4#18:00#23:59$3#5#18:00#23:59$3#6#18:00#23:59$3#7#00:00#23:59$");
			user.setWangwang("");
			user.setWangwang_feature("0#1#00:00#23:59$0#2#00:00#23:59$0#3#00:00#23:59$0#4#00:00#23:59$0#5#00:00#23:59$0#6#00:00#23:59$0#7#00:00#23:59$");
			
		}
		view.addObject("userinfo", user);
		return view;
	}
	
	

}
