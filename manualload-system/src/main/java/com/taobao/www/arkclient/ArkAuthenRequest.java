package com.taobao.www.arkclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.taobao.www.arkclient.Service.ArkAuthenService;
import com.taobao.www.arkclient.Service.CodeEncryptService;
import com.taobao.www.arkclient.Service.EncryptService;
import com.taobao.www.arkclient.Service.IdEncryptService;
import com.taobao.www.arkclient.Service.RequestService;
import com.taobao.www.arkclient.authen.entity.CodeDefine;
import com.taobao.www.arkclient.authen.entity.SecretKeyIV;
import com.taobao.www.arkclient.authen.entity.UserProfile;

public class ArkAuthenRequest {

	private final static String COOKIENAME = "ArkAppAuthen";
	private final static String COOKIEAUTHEN = "Authen";
	private final static String COOKIEGUID = "Scid";
	private final static String COOKIECODE = "Code";
	private final static String USERITEM = "Ark:User";
	private final static String TICKETKEY = "ticket";
	private final static String HOSTNAME = "localhost";
	private static String localName = null;

	public ArkAuthenRequest(HttpServletRequest request, HttpServletResponse response) throws JSONException, RedirectException {
		Cookie httpcookie = null;
		Cookie[] cookies = request.getCookies();
		String ticket = request.getParameter(TICKETKEY);
		//抠出来name是COOKIENAME的cookie
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (StringUtils.equals(COOKIENAME, cookie.getName())) {
					httpcookie = cookie;
					break;
				}
			}
		}

		//针对COOKIENAME的Cookie进行处理
		if (httpcookie != null && StringUtils.isNotBlank(httpcookie.getValue())) {
			String cv = httpcookie.getValue().replace("|", "=");
			String[] content = cv.split("&");
			String secretid = content[1].replace(COOKIEGUID + "=", "");
			//解密
			if (StringUtils.isNotBlank(secretid)) {
				try {
					secretid = IdEncryptService.Decrypt(secretid);
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			} else {
				processSecretError(httpcookie, response, 1013);
				return;
			}

			String cookiecode = content[2].replace(COOKIECODE + "=", "");
			//如果cookiecode存在，对cookiecode进行解码
			if (StringUtils.isNotBlank(cookiecode)) {
				try {
					cookiecode = CodeEncryptService.Decrypt(cookiecode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				//解密出来的值不是特定的状态代码
				//强制将其修正为默认状态代码
				if (!StringUtils.equals(String.valueOf(CodeDefine.DEFAULT), cookiecode)
						&& !StringUtils.equals(String.valueOf(CodeDefine.MIDDLE), cookiecode)
						&& !StringUtils.equals(String.valueOf(CodeDefine.FINAL), cookiecode)) {
					cookiecode = String.valueOf(CodeDefine.DEFAULT);
				}
			}
			SecretKeyIV secretkeyiv = getSecretKeyIv(request, secretid);
			if (secretkeyiv == null) {
				//获得过期时一致的缓存内容
				secretkeyiv = requestSecretKeyIv(secretid);
				if (secretkeyiv.getErrorcode() == 0) {
					//设置应用缓存中的密钥类
					setSecretKeyIv(request, secretid, secretkeyiv);
				} else {
					//清除Cookie，同时重定向到错误
					processSecretError(httpcookie, response, secretkeyiv.getErrorcode());
					return;
				}
			}
			//密钥类过期标志
			if (!secretkeyiv.isIsexpired()) {
				switch (Integer.parseInt(cookiecode)) {
				//"0"
				case CodeDefine.DEFAULT:
					//解密和传递用户身份，添加到请求头
					String decryptjson = decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					//密钥类信息过期时间已过期，向ARK Server请求新的密钥类
					//如果未过期则直接进行用户身份的解密和身份的传递
					if (secretkeyiv.getSecretexp().compareTo(convertTz()) <= 0) {
						System.out.println("密钥类信息过期");
						//判断返回的用户JSON串是否为空(如果不加判断好像也可以)
						//不为空则进行重新请求SecretKeyIV操作
						if (StringUtils.isNotBlank(decryptjson)) {
							secretkeyiv = requestSecretKeyIv(secretid, secretkeyiv.isIsexpired());
							//请求正常的情况，错误的情况则进行重定向
							if (secretkeyiv.getErrorcode() == 0) {
								//设置应用缓存中的密钥类
								setSecretKeyIv(request, secretid, secretkeyiv);
								//将Cookie的Code值写回Cookie
								//httpcookie.Values.Set(COOKIECODE,"1");
								setCookieValue(httpcookie, COOKIECODE,
										CodeEncryptService.Encrypt(String.valueOf(CodeDefine.MIDDLE)));
								response.addCookie(httpcookie);
							} else {
								//清除Cookie，同时重定向到错误
								processSecretError(httpcookie, response, secretkeyiv.getErrorcode());
								return;
							}
						}
					}
					break;
				//"1"
				case CodeDefine.MIDDLE:
					//解密和传递用户身份，添加到请求头
					decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					break;
				//"2"
				case CodeDefine.FINAL:
					secretkeyiv = requestSecretKeyIv(secretid);
					//请求正常的情况，错误的情况则进行重定向
					if (secretkeyiv.getErrorcode() == 0) {
						//设置应用缓存中的密钥类
						setSecretKeyIv(request, secretid, secretkeyiv);
						setCookieValue(httpcookie, COOKIECODE,
								CodeEncryptService.Encrypt(String.valueOf(CodeDefine.DEFAULT)));
						response.addCookie(httpcookie);
						//context.Response.Flush();
					} else {
						//清除Cookie，同时重定向到错误
						processSecretError(httpcookie, response, secretkeyiv.getErrorcode());
						return;
					}

					//解密和传递用户身份，添加到请求头
					//(此处不可以调换位置，需要新的密钥解密Cookie)
					decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					break;
				}

			} else {
				switch (Integer.parseInt(cookiecode)) {
				//"0"
				case CodeDefine.DEFAULT:
					//解密和传递用户身份，添加到请求头
					decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					break;
				//"1"
				case CodeDefine.MIDDLE:
					//解密和传递用户身份，添加到请求头
					String decryptjson = decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					//判断返回的用户JSON串是否为空
					//不为空则进行重新请求SecretKeyIV操作
					if (StringUtils.isNotBlank(decryptjson)) {
						SecretKeyIV newkeyiv = requestSecretKeyIv(secretid, secretkeyiv.isIsexpired());
						//请求正常的情况，错误的情况则进行重定向
						if (newkeyiv.getErrorcode() == 0) {
							//设置应用缓存中的密钥类的过期标志为false
							secretkeyiv.setIsexpired(false);
							setSecretKeyIv(request, secretid, secretkeyiv);
							//用新的SecretKeyIV来加密Cookie信息，同时写CookieCode。
							setCookieValue(httpcookie, COOKIEAUTHEN, EncryptService.Encrypt(decryptjson,
									newkeyiv.getSecretkey(), newkeyiv.getSecretiv()));
							setCookieValue(httpcookie, COOKIECODE,
									CodeEncryptService.Encrypt(String.valueOf(CodeDefine.FINAL)));
							response.addCookie(httpcookie);

						} else {
							try {
								response.sendRedirect(RequestService.ProcessErrorUrl(secretkeyiv.getErrorcode()));
								throw new RedirectException();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					break;
				}
			}
		} else {
			try {
				processProfile(response, request, ticket);
			} catch (JSONException e) {
				//jason解析报错，重定向处理。  xp
				try {
					response.sendRedirect(RequestService.ProcessLoginUrl(request, response));
					throw new RedirectException();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private String decryptUserAddContext(HttpServletRequest request, HttpServletResponse response,
			SecretKeyIV secretkeyiv, Cookie httpcookie) throws RedirectException {
		String cv = httpcookie.getValue().replace("|", "=");
		String[] content = cv.split("&");

		String encryptjson = content[0].replace(COOKIEAUTHEN + "=", "");

		String decryptjson = EncryptService.Decrypt(encryptjson, secretkeyiv.getSecretkey(), secretkeyiv.getSecretiv());
		//如果JSON串不为空时则添加用户信息到应用中去
		//否则直接不做任何操作
		if (StringUtils.isNotBlank(decryptjson)) {
			try {
				JSONObject decryptjs = new JSONObject(decryptjson);
				UserProfile user = new UserProfile();
				user.setWorkid(decryptjs.getString("WorkId"));
				user.setEmail(decryptjs.getString("Email"));
				user.setDomainuser(decryptjs.getString("DomainUser"));
				user.setWangwang(decryptjs.getString("WangWang"));
				request.setAttribute(USERITEM, user.getWorkid() + "&" + user.getEmail() + "&" + user.getDomainuser()
						+ "&" + user.getWangwang());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			//如果出错，这个时候直接清除用户的Cookie信息
			//可以考虑篡改Request.Cookie的信息（有效性有待测试）
			processSecretError(httpcookie, response, 1100);
		}

		return decryptjson;
	}

	private SecretKeyIV getSecretKeyIv(HttpServletRequest request, String secretId) {
		//使用session?,如果接入的系统是多机器集群，这个地方可能有问题
		if (request.getSession().getAttribute(secretId) != null) {
			return (SecretKeyIV) request.getSession().getAttribute(secretId);
		} else {
			return null;
		}
	}

	private void setSecretKeyIv(HttpServletRequest request, String secretId, SecretKeyIV secret) {
		request.getSession().setAttribute(secretId, secret);
		//密钥KEY时间，将配置的分钟装换成秒
		request.getSession().setMaxInactiveInterval(ArkAuthenService.getSecretCache() * 60);
	}

	private void setSecretKeyIv(HttpServletRequest request, String secretId, String key, String iv, Date expitime) {
		SecretKeyIV secretkeyiv = new SecretKeyIV();
		secretkeyiv.setSecretkey(key);
		secretkeyiv.setSecretiv(iv);
		secretkeyiv.setSecretexp(expitime);

		setSecretKeyIv(request, secretId, secretkeyiv);
	}

	private SecretKeyIV requestSecretKeyIv(String secretId) throws JSONException {
		String secretString = RequestService.RequestUrl(RequestService.ProcessSecretUrl(secretId), null);
		JSONObject decryptjs = new JSONObject(secretString);
		SecretKeyIV secretkeyiv = new SecretKeyIV();
		secretkeyiv.setErrorcode(decryptjs.getInt("ErrorCode"));
		secretkeyiv.setIsexpired(decryptjs.getBoolean("IsExpired"));
		Calendar dt = new GregorianCalendar();
		dt.setTimeInMillis(Long.parseLong(decryptjs.getString("SecretExp").substring(6, 19)));

		String timeZone = "UTC" + decryptjs.getString("SecretExp").substring(19, 24);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		dt.setTimeZone(tz);
		secretkeyiv.setSecretexp(dt.getTime());
		secretkeyiv.setSecretiv(decryptjs.getString("SecretIV"));
		secretkeyiv.setSecretkey(decryptjs.getString("SecretKey"));
		return secretkeyiv;
	}

	private SecretKeyIV requestSecretKeyIv(String secretId, boolean isexpired) throws JSONException {
		String secretString = RequestService.RequestUrl(RequestService.ProcessSecretUrl(secretId, isexpired), null);

		JSONObject decryptjs = new JSONObject(secretString);
		SecretKeyIV secretkeyiv = new SecretKeyIV();
		secretkeyiv.setErrorcode(decryptjs.getInt("ErrorCode"));
		secretkeyiv.setIsexpired(decryptjs.getBoolean("IsExpired"));
		Calendar dt = new GregorianCalendar();
		dt.setTimeInMillis(Long.parseLong(decryptjs.getString("SecretExp").substring(6, 19)));

		String timeZone = "UTC" + decryptjs.getString("SecretExp").substring(19, 24);
		TimeZone tz = TimeZone.getTimeZone(timeZone);
		dt.setTimeZone(tz);
		secretkeyiv.setSecretexp(dt.getTime());
		secretkeyiv.setSecretiv(decryptjs.getString("SecretIV"));
		secretkeyiv.setSecretkey(decryptjs.getString("SecretKey"));
		return secretkeyiv;
	}

	private void processProfile(HttpServletResponse response, HttpServletRequest request, String ticket)
			throws JSONException, RedirectException {
		//判断Ticket是否为空，如果为空则需要到SSO进行验证
		//如果不为空则只需要进行用户身份的获取
		if (ticket == null || ticket == "") {
			//重定向进行身份验证
			//这个时候有必要进行会话时子凭据Cookie的清除操作
			//否则会导致无法重新生成用户身份的问题
			//context.Response.Cookies.Remove(COOKIENAME);
			try {
				response.sendRedirect(RequestService.ProcessLoginUrl(request, response));
				throw new RedirectException();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//请求用户身份的Profile
			//此处的风险点比较大
			String jsonResult = RequestService.RequestUrl(RequestService.ProcessProfileUrl(ticket), null);
			//jsonResult的有效性判断
			JSONObject decryptjs = new JSONObject(jsonResult);
			UserProfile user = new UserProfile();
			//增加判断，有值再取  xp
			if (decryptjs.has("ErrorCode"))
				user.setErrorcode(decryptjs.getInt("ErrorCode"));
			if (decryptjs.has("IsExpired"))
				user.setIsexpired(decryptjs.getBoolean("IsExpired"));
			Calendar dt = new GregorianCalendar();
			if (decryptjs.has("SecretExp")) {
				dt.setTimeInMillis(Long.parseLong(decryptjs.getString("SecretExp").substring(6, 19)));
				String timeZone = "UTC" + decryptjs.getString("SecretExp").substring(19, 24);
				TimeZone tz = TimeZone.getTimeZone(timeZone);
				dt.setTimeZone(tz);
				user.setSecretexp(dt.getTime());
			}
			if (decryptjs.has("SecretId") && decryptjs.has("SecretIV") && decryptjs.has("SecretKey")
					&& decryptjs.has("WorkId") && decryptjs.has("Email") && decryptjs.has("DomainUser")
					&& decryptjs.has("WangWang"))

			{
				user.setSecretid(decryptjs.getString("SecretId"));
				user.setSecretiv(decryptjs.getString("SecretIV"));
				user.setSecretkey(decryptjs.getString("SecretKey"));
				user.setWorkid(decryptjs.getString("WorkId"));
				user.setEmail(decryptjs.getString("Email"));
				user.setDomainuser(decryptjs.getString("DomainUser"));
				user.setWangwang(decryptjs.getString("WangWang"));

			}
			if (user.getErrorcode() == 0) {
				//设置密钥类的缓存信息
				setSecretKeyIv(request, user.getSecretid(), user.getSecretkey(), user.getSecretiv(),
						user.getSecretexp());

				//往上下文请求中添加用户的身份信息
				request.setAttribute(USERITEM, user.getWorkid() + "&" + user.getEmail() + "&" + user.getDomainuser()
						+ "&" + user.getWangwang());

				//写到Client的Cookie中去。
				Cookie cookie;
				try {

					String cookieV = COOKIEAUTHEN + "="
							+ EncryptService.Encrypt(jsonResult, user.getSecretkey(), user.getSecretiv()) + "&"
							+ COOKIEGUID + "=" + IdEncryptService.Encrypt(user.getSecretid()) + "&" + COOKIECODE + "="
							+ CodeEncryptService.Encrypt(String.valueOf(CodeDefine.DEFAULT));

					//=号在cookie是不安全的，低版本服务器可能不支持
					cookie = new Cookie(COOKIENAME, cookieV.replace("=", "|"));

					cookie.setDomain(request.getLocalAddr());
					response.setHeader("Set-Cookie", COOKIENAME + "=" + cookie.getValue() + ";HttpOnly");

					//                cookie.HttpOnly = true;

					if (localName == null) {
						localName = request.getLocalName();
					}
					if (!localName.equalsIgnoreCase(HOSTNAME)) {
						cookie.setDomain(localName);
					}

					response.addCookie(cookie);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				//使用了已经使用过的Ticket，导致请求UserProfile的时候返回1001错误
				//这个时候需要重新请求ArkServer来获得Ticket
				try {
					response.sendRedirect(RequestService.ProcessLoginUrl(request, response));
					throw new RedirectException();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void processSecretError(Cookie httpcookie, HttpServletResponse response, int errorcode) throws RedirectException {
		httpcookie.setValue("");
		response.addCookie(httpcookie);

		try {
			response.sendRedirect(RequestService.ProcessErrorUrl(errorcode));
			throw new RedirectException();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Cookie setCookieValue(Cookie cookie, String title, String content) {
		Cookie cook = cookie;
		String[] con = cookie.getValue().split("&");
		for (String c : con) {
			if (c.contains(title)) {
				cook.getValue().replace(c, title + "=" + content);
			}
		}
		return cook;
	}

	//得到uct时间
	public static Date convertTz() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}
}
