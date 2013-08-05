package com.taobao.monitor.web.vo;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rentong
 * Date: 2010-6-30
 * Time: 19:01:34
 * To change this template use File | Settings | File Templates.
 */
public class WebWWData {
    private long number;
    private String date;
    
    public WebWWData(){}
    
    public WebWWData(long number,String date){
    	this.number = number;
    	this.date = date;
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
