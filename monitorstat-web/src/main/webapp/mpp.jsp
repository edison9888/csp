<%@ page contentType="text/html;charset=GBK" language="java" %>
<%@ page import="com.taobao.monitor.web.vo.WebWWData" %>
<%@ page import="java.util.List" %>
<%@ page import="com.taobao.monitor.web.schedule.GetMppOnlineNumber" %>
<%!
    public static void jsonObject(final StringBuffer contents, WebWWData data, String name) {
        if (data == null || data.getDate() == null) {
            contents.append(name).append(":{").append("number:").append(0).append(",time:\"").append("\"}");
        } else {
            contents.append(name).append(":{").append("number:").append(data.getNumber()).append(",time:\"").append(data.getDate()).append("\"}");
        }
    }

    public static void jsonArray(final StringBuffer contents, List<WebWWData> datas, String name) {
        contents.append(name).append(":[");
        if (datas != null && datas.size() > 0) {
            for (WebWWData data : datas) {
                contents.append("{number:").append(data.getNumber()).append(",time:\"").append(data.getDate()).append("\"},");
            }
            contents.deleteCharAt(contents.length() - 1);
        }

        contents.append("]");

    }
%>
<%
    GetMppOnlineNumber onlineNumber = GetMppOnlineNumber.getInstance();
/*
    Date now = new Date();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
*/
    StringBuffer value = new StringBuffer();
    value.append("WW.Count.result({");
    jsonObject(value, onlineNumber.getNow(), "now");
    value.append(",");
    jsonObject(value, onlineNumber.getHistoryMax(), "historyMax");
    value.append(",");
    jsonObject(value, onlineNumber.getHistoryNow(), "historyNow");
    value.append(",");
    jsonObject(value, onlineNumber.getHistoryWeekMax(), "historyWeekMax");
    value.append(",");
    jsonObject(value, onlineNumber.getLastweekDayMax(), "lastweekDayMax");
    value.append(",");
    jsonObject(value, onlineNumber.getLastweekNow(), "lastweekNow");
    value.append(",");
    jsonObject(value, onlineNumber.getTodayMax(), "todayMax");
    value.append(",");
    jsonObject(value, onlineNumber.getYestodayMax(), "yestodayMax");
    value.append(",");
    jsonObject(value, onlineNumber.getYestodayNow(), "yestodayNow");
    value.append(",");
    jsonArray(value, onlineNumber.getOneMinuteList(), "oneMinuteList");
    value.append(",");
    jsonArray(value, onlineNumber.getTwoWeeksList(), "twoWeeksList");

    value.append("});");
    response.getWriter().write(value.toString());


%>