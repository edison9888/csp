package com.taobao.csp.depend.util;


public class JspFormatUtil implements ConstantParameters {

	public static String getControlString(int i) {
		if(i==ConstantParameters.CSP_DEPEND_MAP_NO_CONTROL) {
			return "δ����";
		} else if(i==ConstantParameters.CSP_DEPEND_MAP_SS){
			return "SS����";
		} else if(i==ConstantParameters.CSP_DEPEND_MAP_TMD){
			return "TMD����";
		} else {
			return "wrong type";
		}
	}

	public static String getKeyStatusString(int i) {
		if(i==ConstantParameters.CSP_DEPEND_MAP_STATUS_AUTO) {
			return "�Զ�";
		} else if(i==ConstantParameters.CSP_DEPEND_MAP_STATUS_CONFIG){
			return "����";
		} else {
			return "wrong type";
		}
	}

	public static String getKeyLevel(int i) {
		return "P" + i + "��";
	}

	public static void main(String[] args) {
		//System.out.println(MethodUtil.getOneDayPre());
		//System.out.println("sss");
	}


	public static  String changeHtmlToText(String html){
		if(html == null)
			return html = "";
		StringBuffer buf = new StringBuffer();
		char ch = ' ';
		for( int i=0; i<html.length(); i++ ) {
			ch = html.charAt(i);
			if( ch == '<' ) {
				buf.append( "&lt;" );
			}
			else if( ch == '>' ) {
				buf.append( "&gt;" );
			}
			else {
				buf.append( ch );
			}
		}
		return buf.toString();
	}
}
