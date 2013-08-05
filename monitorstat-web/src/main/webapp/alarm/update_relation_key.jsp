<%@page language="java" contentType="text/html; charset=GB18030"  pageEncoding="GB18030"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.monitor.alarm.source.po.KeySourcePo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.alarm.source.change.KeySourceInfoAo"%>
<%	
	String action = request.getParameter("op");
	String strKey = request.getParameter("keyId");
	String strSrcAppId = request.getParameter("sourceAppId");
	try{
		if(action != null){
			int keyId = 0;
			if(StringUtils.isNotBlank(strKey)){
				keyId = Integer.valueOf(strKey);
			}
			
			int sourceAppId = 0;
			if(StringUtils.isNotBlank(strSrcAppId)){
				sourceAppId = Integer.valueOf(strSrcAppId);
			}
			if("delete".equals(action)){
				if(KeySourceInfoAo.get().deleteKeySourcePo(keyId, sourceAppId)){
					out.print("É¾³ý³É¹¦!");
				} else {
					out.print("É¾³ýÊ§°Ü!");
				}
			}
			if("modify".equals(action)){
				String strAppId = request.getParameter("appId");
				int appId = 0;
				if(StringUtils.isNotBlank(strAppId)){
					appId = Integer.valueOf(strAppId);
				}
				KeySourcePo po = new KeySourcePo();
				AppInfoPo appInfopo = AppCache.get().getKey(sourceAppId);
				KeyPo keyPo = KeyAo.get().getKeyByName(keyId);
				if( appInfopo != null && keyPo != null){
					po.setAppId( appId );
					po.setKeyId( String.valueOf(keyPo.getKeyId()) );
					if( StringUtils.isBlank(keyPo.getAliasName()) ){
						po.setKeyName(keyPo.getKeyName());
					} else {
						po.setKeyName(keyPo.getAliasName());
					}
					po.setSourceAppId( sourceAppId );
					po.setSourceAppName(appInfopo.getAppName());
					po.setSourceGroupName(appInfopo.getGroupName());
					if(KeySourceInfoAo.get().safeAddKeySourcePo(po)){
						out.print("ÐÞ¸Ä³É¹¦");
					} else {
						out.print("ÐÞ¸ÄÊ§°Ü");
					}
				} else{
					out.print("appId or keyId error");
				}
			}
		}
	} catch(Exception e){
		out.print("parameter error!action="+action+"+strKey="+strKey+"sourceAppId="+strSrcAppId);
	}
%>