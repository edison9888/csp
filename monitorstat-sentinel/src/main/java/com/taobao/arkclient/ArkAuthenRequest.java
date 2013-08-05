package com.taobao.arkclient;

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

import com.taobao.arkclient.Service.ArkAuthenService;
import com.taobao.arkclient.Service.CodeEncryptService;
import com.taobao.arkclient.Service.EncryptService;
import com.taobao.arkclient.Service.IdEncryptService;
import com.taobao.arkclient.Service.RequestService;
import com.taobao.arkclient.authen.entity.CodeDefine;
import com.taobao.arkclient.authen.entity.SecretKeyIV;
import com.taobao.arkclient.authen.entity.UserProfile;

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
		//�ٳ���name��COOKIENAME��cookie
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (StringUtils.equals(COOKIENAME, cookie.getName())) {
					httpcookie = cookie;
					break;
				}
			}
		}

		//���COOKIENAME��Cookie���д���
		if (httpcookie != null && StringUtils.isNotBlank(httpcookie.getValue())) {
			String cv = httpcookie.getValue().replace("|", "=");
			String[] content = cv.split("&");
			String secretid = content[1].replace(COOKIEGUID + "=", "");
			//����
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
			//���cookiecode���ڣ���cookiecode���н���
			if (StringUtils.isNotBlank(cookiecode)) {
				try {
					cookiecode = CodeEncryptService.Decrypt(cookiecode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				//���ܳ�����ֵ�����ض���״̬����
				//ǿ�ƽ�������ΪĬ��״̬����
				if (!StringUtils.equals(String.valueOf(CodeDefine.DEFAULT), cookiecode)
						&& !StringUtils.equals(String.valueOf(CodeDefine.MIDDLE), cookiecode)
						&& !StringUtils.equals(String.valueOf(CodeDefine.FINAL), cookiecode)) {
					cookiecode = String.valueOf(CodeDefine.DEFAULT);
				}
			}
			SecretKeyIV secretkeyiv = getSecretKeyIv(request, secretid);
			if (secretkeyiv == null) {
				//��ù���ʱһ�µĻ�������
				secretkeyiv = requestSecretKeyIv(secretid);
				if (secretkeyiv.getErrorcode() == 0) {
					//����Ӧ�û����е���Կ��
					setSecretKeyIv(request, secretid, secretkeyiv);
				} else {
					//���Cookie��ͬʱ�ض��򵽴���
					processSecretError(httpcookie, response, secretkeyiv.getErrorcode());
					return;
				}
			}
			//��Կ����ڱ�־
			if (!secretkeyiv.isIsexpired()) {
				switch (Integer.parseInt(cookiecode)) {
				//"0"
				case CodeDefine.DEFAULT:
					//���ܺʹ����û���ݣ���ӵ�����ͷ
					String decryptjson = decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					//��Կ����Ϣ����ʱ���ѹ��ڣ���ARK Server�����µ���Կ��
					//���δ������ֱ�ӽ����û���ݵĽ��ܺ���ݵĴ���
					if (secretkeyiv.getSecretexp().compareTo(convertTz()) <= 0) {
						System.out.println("��Կ����Ϣ����");
						//�жϷ��ص��û�JSON���Ƿ�Ϊ��(��������жϺ���Ҳ����)
						//��Ϊ���������������SecretKeyIV����
						if (StringUtils.isNotBlank(decryptjson)) {
							secretkeyiv = requestSecretKeyIv(secretid, secretkeyiv.isIsexpired());
							//�����������������������������ض���
							if (secretkeyiv.getErrorcode() == 0) {
								//����Ӧ�û����е���Կ��
								setSecretKeyIv(request, secretid, secretkeyiv);
								//��Cookie��Codeֵд��Cookie
								//httpcookie.Values.Set(COOKIECODE,"1");
								setCookieValue(httpcookie, COOKIECODE,
										CodeEncryptService.Encrypt(String.valueOf(CodeDefine.MIDDLE)));
								response.addCookie(httpcookie);
							} else {
								//���Cookie��ͬʱ�ض��򵽴���
								processSecretError(httpcookie, response, secretkeyiv.getErrorcode());
								return;
							}
						}
					}
					break;
				//"1"
				case CodeDefine.MIDDLE:
					//���ܺʹ����û���ݣ���ӵ�����ͷ
					decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					break;
				//"2"
				case CodeDefine.FINAL:
					secretkeyiv = requestSecretKeyIv(secretid);
					//�����������������������������ض���
					if (secretkeyiv.getErrorcode() == 0) {
						//����Ӧ�û����е���Կ��
						setSecretKeyIv(request, secretid, secretkeyiv);
						setCookieValue(httpcookie, COOKIECODE,
								CodeEncryptService.Encrypt(String.valueOf(CodeDefine.DEFAULT)));
						response.addCookie(httpcookie);
						//context.Response.Flush();
					} else {
						//���Cookie��ͬʱ�ض��򵽴���
						processSecretError(httpcookie, response, secretkeyiv.getErrorcode());
						return;
					}

					//���ܺʹ����û���ݣ���ӵ�����ͷ
					//(�˴������Ե���λ�ã���Ҫ�µ���Կ����Cookie)
					decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					break;
				}

			} else {
				switch (Integer.parseInt(cookiecode)) {
				//"0"
				case CodeDefine.DEFAULT:
					//���ܺʹ����û���ݣ���ӵ�����ͷ
					decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					break;
				//"1"
				case CodeDefine.MIDDLE:
					//���ܺʹ����û���ݣ���ӵ�����ͷ
					String decryptjson = decryptUserAddContext(request, response, secretkeyiv, httpcookie);
					//�жϷ��ص��û�JSON���Ƿ�Ϊ��
					//��Ϊ���������������SecretKeyIV����
					if (StringUtils.isNotBlank(decryptjson)) {
						SecretKeyIV newkeyiv = requestSecretKeyIv(secretid, secretkeyiv.isIsexpired());
						//�����������������������������ض���
						if (newkeyiv.getErrorcode() == 0) {
							//����Ӧ�û����е���Կ��Ĺ��ڱ�־Ϊfalse
							secretkeyiv.setIsexpired(false);
							setSecretKeyIv(request, secretid, secretkeyiv);
							//���µ�SecretKeyIV������Cookie��Ϣ��ͬʱдCookieCode��
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
				//jason���������ض�����  xp
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
		//���JSON����Ϊ��ʱ������û���Ϣ��Ӧ����ȥ
		//����ֱ�Ӳ����κβ���
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
			//����������ʱ��ֱ������û���Cookie��Ϣ
			//���Կ��Ǵ۸�Request.Cookie����Ϣ����Ч���д����ԣ�
			processSecretError(httpcookie, response, 1100);
		}

		return decryptjson;
	}

	private SecretKeyIV getSecretKeyIv(HttpServletRequest request, String secretId) {
		//ʹ��session?,��������ϵͳ�Ƕ������Ⱥ������ط�����������
		if (request.getSession().getAttribute(secretId) != null) {
			return (SecretKeyIV) request.getSession().getAttribute(secretId);
		} else {
			return null;
		}
	}

	private void setSecretKeyIv(HttpServletRequest request, String secretId, SecretKeyIV secret) {
		request.getSession().setAttribute(secretId, secret);
		//��ԿKEYʱ�䣬�����õķ���װ������
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
		//�ж�Ticket�Ƿ�Ϊ�գ����Ϊ������Ҫ��SSO������֤
		//�����Ϊ����ֻ��Ҫ�����û���ݵĻ�ȡ
		if (ticket == null || ticket == "") {
			//�ض�����������֤
			//���ʱ���б�Ҫ���лỰʱ��ƾ��Cookie���������
			//����ᵼ���޷����������û���ݵ�����
			//context.Response.Cookies.Remove(COOKIENAME);
			try {
				response.sendRedirect(RequestService.ProcessLoginUrl(request, response));
				throw new RedirectException();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//�����û���ݵ�Profile
			//�˴��ķ��յ�Ƚϴ�
			String jsonResult = RequestService.RequestUrl(RequestService.ProcessProfileUrl(ticket), null);
			//jsonResult����Ч���ж�
			JSONObject decryptjs = new JSONObject(jsonResult);
			UserProfile user = new UserProfile();
			//�����жϣ���ֵ��ȡ  xp
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
				//������Կ��Ļ�����Ϣ
				setSecretKeyIv(request, user.getSecretid(), user.getSecretkey(), user.getSecretiv(),
						user.getSecretexp());

				//������������������û��������Ϣ
				request.setAttribute(USERITEM, user.getWorkid() + "&" + user.getEmail() + "&" + user.getDomainuser()
						+ "&" + user.getWangwang());

				//д��Client��Cookie��ȥ��
				Cookie cookie;
				try {

					String cookieV = COOKIEAUTHEN + "="
							+ EncryptService.Encrypt(jsonResult, user.getSecretkey(), user.getSecretiv()) + "&"
							+ COOKIEGUID + "=" + IdEncryptService.Encrypt(user.getSecretid()) + "&" + COOKIECODE + "="
							+ CodeEncryptService.Encrypt(String.valueOf(CodeDefine.DEFAULT));

					//=����cookie�ǲ���ȫ�ģ��Ͱ汾���������ܲ�֧��
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
				//ʹ�����Ѿ�ʹ�ù���Ticket����������UserProfile��ʱ�򷵻�1001����
				//���ʱ����Ҫ��������ArkServer�����Ticket
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

	//�õ�uctʱ��
	public static Date convertTz() {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		return cal.getTime();
	}
}
