<%@ page contentType="text/html;charset=GBK" language="java" %>
<html>
<head>
    <title>竞拍商品在线人数</title>
    <style type="text/css">
        body {
            font-size: 12px;
        }

        #ww-content {
            width: 840px;
        }

        fieldset {
            width: 400px;
            height: 500px;
        }

        .unit {
            margin-left: 3px;
            width: 200px;
            height: 17px;
            float: left;
            font-weight: normal;
        }

        .line {
            margin-left: 3px;
            width: 402px;
            height: 17px;
            float: left;
            font-weight: bold;
        }

        .data1 {
            width: 820px;
            height: 230px;
            border: 1px solid #DDDDDD;
            float: left;
        }

        .data2 {
            height: 500px;
            width: 410px;
            border-top: 1px solid #DDDDDD;
            border-right: 1px solid #DDDDDD;
            border-bottom: 1px solid #DDDDDD;
            float: left;
        }

        .data3 {
            width: 410px;
            height: 260px;
            border-left: 1px solid #DDDDDD;
            border-right: 1px solid #DDDDDD;
            border-bottom: 1px solid #DDDDDD;
            float: left;
        }

        .data4 {
            height: 260px;
            width: 410px;
            border-right: 1px solid #DDDDDD;
            border-bottom: 1px solid #DDDDDD;
            float: left;
        }

        .monitor {
            border-left: 1px solid #DDDDDD;
            border-right: 1px solid #DDDDDD;
            border-bottom: 1px solid #DDDDDD;
            width: 821px;
            height: 75px;
            float: left;
            display: inline-block;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div id="auction-content">

</div>
<%
	String auction_Id = (String)request.getParameter("id");
%>
<script type="text/javascript">
    var auction_id = <%=auction_Id %>;
    var Auction = {
        Count:{
            result:function(data) {
                var view = document.getElementById('auction-content');
                if (data) {
                    var contents = '';
                    contents += '<div class="data2">';
                    contents += '<div class="line"><strong>'+ data.oneMinuteList[0].id +'</strong> 最近1分钟值：</div>';
                    for (var i = 0; i < data.oneMinuteList.length; i++) {
                        contents += '<div class="unit">时间：' + data.oneMinuteList[i].time + '</div>';
                        contents += '<div class="unit">在线人数：' + data.oneMinuteList[i].number + '</div>';
                    }
                    contents += '</div>';
					
                    view.innerHTML = contents;
                } else {
                    view.innerHTML = '读取数据出错。';
                }
            }
        },
        init:function() {
        	this.reloadOnlieUser();
            window.setInterval(this.reloadOnlieUser, 5000);
        },
        reloadOnlieUser:function() {
            var xmlHttp;
            try {
                // Firefox, Opera 8.0+, Safari
                xmlHttp = new XMLHttpRequest();
            } catch (e) {
                // Internet Explorer
                try {
                    xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
                } catch (e) {
                    try {
                        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                    } catch (e) {
                        alert("您的浏览器不支持AJAX！");
                        return false;
                    }
                }
            }
            xmlHttp.open("GET", "auctions_online_detail.jsp?id=" + auction_id +"&fresh="+ Math.random(), true);
            xmlHttp.send(null);
            xmlHttp.onreadystatechange = function() {
                if (xmlHttp.readyState == 4) {
                    try {
                        eval(xmlHttp.responseText);
                    } catch(e) {
                       // alert("服务器出错了……");
                    }
                }
            }
        }
    };
    Auction.init();
</script>
<script type="text/javascript" src="auctions_online_detail.jsp"></script>
</body>
</html>