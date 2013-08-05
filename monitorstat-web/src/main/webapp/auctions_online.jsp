<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="com.taobao.monitor.web.vo.AuctionData" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.taobao.monitor.web.schedule.GetAuctionsOnlineNumber" %>
<%@ page import="com.taobao.monitor.web.vo.AuctionData" %>
<%!
    public static void jsonMapArray(final StringBuffer contents,
			Map<String, LinkedList<AuctionData>> datas, String name) {
		contents.append(name).append(":[");
		if (datas != null && datas.size() > 0) {
			for (Entry<String, LinkedList<AuctionData>> data : datas.entrySet()) {
				contents.append("{id:\"").append(data.getValue().getFirst().getId()).append("\"")
						.append(",time:\"").append(data.getValue().getFirst().getDate()).append("\"")
						.append(",number:").append(data.getValue().getFirst().getNumber()).append("},");
			}
			contents.deleteCharAt(contents.length() - 1);
		}
		contents.append("]");
	}
	
	
%>
<%
    GetAuctionsOnlineNumber onlineNumber = GetAuctionsOnlineNumber.getInstance();

    StringBuffer value = new StringBuffer();
    value.append("Auctions.Count.result({");
    
    value.append("totalNumber:").append(onlineNumber.getTotalNumber()).append(",");
    jsonMapArray(value, onlineNumber.getAuctionsNumberMap(), "nowList");
    value.append("});");
    response.getWriter().write(value.toString());


%>