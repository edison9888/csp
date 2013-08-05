<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="com.taobao.monitor.web.vo.AuctionData" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.taobao.monitor.web.schedule.GetAuctionsOnlineNumber" %>
<%@ page import="com.taobao.monitor.web.vo.AuctionData" %>
<%!
	
	public static void jsonListArray(final StringBuffer contents,
			LinkedList<AuctionData> datas, String name) {
		contents.append(name).append(":[");
		if (datas != null && datas.size() > 0) {
			for (AuctionData data : datas) {
				contents.append("{id:\"").append(data.getId()).append("\"")
						.append(",time:\"").append(data.getDate()).append("\"")
						.append(",number:").append(data.getNumber()).append("},");
			}
			contents.deleteCharAt(contents.length() - 1);
		}
		contents.append("]");
	}
%>
<%
	String id = (String)request.getParameter("id");
	if(id == null){
		return;
	}
    GetAuctionsOnlineNumber onlineNumber = GetAuctionsOnlineNumber.getInstance();
    
    StringBuffer value = new StringBuffer();
    value.append("Auction.Count.result({");
    jsonListArray(value, onlineNumber.getAuctionsNumberMap().get(id), "oneMinuteList");
    value.append("});");
    response.getWriter().write(value.toString());


%>