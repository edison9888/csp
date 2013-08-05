package com.taobao.monitor.web.vo;

/**
 * Created by IntelliJ IDEA.
 * User: Yang Wenting
 * Date: 2011-07-27
 * Time: 19:01:34
 * To change this template use File | Settings | File Templates.
 */
public class AuctionData {
	private String id;
    private long number;
    private String date;
    
    public AuctionData(){}
    
    public AuctionData(String id, long number, String date) {
		this.id = id;
		this.number = number;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
