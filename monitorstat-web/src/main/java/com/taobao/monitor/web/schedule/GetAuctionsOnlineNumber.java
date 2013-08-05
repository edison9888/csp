package com.taobao.monitor.web.schedule;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.monitor.web.vo.AuctionData;

public class GetAuctionsOnlineNumber extends TimerTask {
	
	private static GetAuctionsOnlineNumber auctionsOnline = new GetAuctionsOnlineNumber();
	private String url = "http://110.75.13.100/noLogin.jsp";
//	private String url = "http://10.232.127.110:8080/noLogin.jsp";
	private SimpleDateFormat gmtcreateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Map<String, LinkedList<AuctionData>> auctionsNumberMap 
		= new ConcurrentHashMap<String, LinkedList<AuctionData>>();
	private long totalNumber;
	
	private GetAuctionsOnlineNumber(){
	}

	public static GetAuctionsOnlineNumber getInstance(){
		return auctionsOnline;
	}

	public Map<String, LinkedList<AuctionData>> getAuctionsNumberMap() {
		return auctionsNumberMap;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	public long getTotalNumber() {
		return totalNumber;
	}

	@Override
	public void run() {
		String contents = getOnlieNumberString();
//		String contents = "1500001692827,1;1500001937751,1;1500002369206,1;1500001965953,1;1500001676581,2;1500001965919,1;1500002454929,3;1500002456568,2";
		if(contents.equals("") || contents.length() < 1){
			;
		}else{
			formatNumberStringToMap(contents);
		}
		
	}
	
	private void formatNumberStringToMap(String contents) {
		Date now = new Date();
		now.setSeconds((now.getSeconds() / 5) * 5);
		String date = gmtcreateFormat.format(now);
		totalNumber = 0;
		Map<String, LinkedList<AuctionData>> templeMap 
			= new ConcurrentHashMap<String, LinkedList<AuctionData>>();
		String[] auctions = contents.split(";");
		for(String data : auctions){
			String[] info = data.split(",");
			AuctionData auctionData = new AuctionData();
			auctionData.setId(info[0]);
			auctionData.setDate(date);
			long number = Long.parseLong(info[1]);
			auctionData.setNumber(number);
			totalNumber += number;
			if(auctionsNumberMap.containsKey(info[0])){
				auctionsNumberMap.get(info[0]).addFirst(auctionData);
				if(auctionsNumberMap.get(info[0]).size() > 12){
					auctionsNumberMap.get(info[0]).removeLast();
				}
				templeMap.put(info[0], auctionsNumberMap.get(info[0]));
			}else{
				LinkedList<AuctionData> queue = new LinkedList<AuctionData>();
				queue.addFirst(auctionData);
				templeMap.put(info[0], queue);
			}
		}
		auctionsNumberMap.clear();
		auctionsNumberMap = templeMap;
	}

	public String getOnlieNumberString() {
		String onlineNumbers = "";
        try {
            URL url = new URL(this.url);

            URLConnection httpConnection = url.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setConnectTimeout(3000);

            InputStream is = httpConnection.getInputStream();

            StringBuffer contents = new StringBuffer();
            byte[] buf = new byte[128];
            int size = 0;
            while ((size = is.read(buf)) > 0) {
                if (buf[size - 1] == '\n') {
                    size = size - 1;
                }
                contents.append(new String(buf, 0, size));
            }
            onlineNumbers = contents.toString();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return onlineNumbers;
    }
	
	public static void main(String[] args) {
//		Timer timer = new Timer();
//        timer.scheduleAtFixedRate(GetAuctionsOnlineNumber.getInstance(), 0, 1000 * 5);
	
        GetAuctionsOnlineNumber.getInstance().run();
        try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		GetAuctionsOnlineNumber onlineNumber = GetAuctionsOnlineNumber.getInstance();

	    StringBuffer value = new StringBuffer();
	    value.append("Auctions.Count.result({");
	    GetAuctionsOnlineNumber.jsonMapArray(value, onlineNumber.getAuctionsNumberMap(), "nowList");
	    value.append("});");
	    
	    System.out.println(value.toString());
	    
	    /*
	    StringBuffer value2 = new StringBuffer();
	    value2.append("Auction.Count.result({");
	    GetAuctionsOnlineNumber.jsonListArray(value2, onlineNumber.getAuctionsNumberMap().get("1500001937751"), "auctionList");
	    value2.append("});");
	    System.out.println(value2.toString());*/
	    

		
	}
	
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


	
	
}
