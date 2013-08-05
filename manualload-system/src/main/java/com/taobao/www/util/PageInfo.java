package com.taobao.www.util;

import java.util.List;

public class PageInfo
{
    private int pagerecord;
    private int curpage=1;
    private int allpage;
    private int allrecords;
    
    private int nextpage;
    private int previouspage;
    
    private List pagedata;
    
	public PageInfo()
	{
		
	}
	public PageInfo(int pagerecord, int curpage, int allrecords, List pagedata) {

		this.pagerecord = pagerecord;
		this.curpage = curpage;
		this.allrecords = allrecords;
		
		this.allpage=(this.allrecords+this.pagerecord-1)/this.pagerecord;
		
		this.pagedata = pagedata;
	}

	public int getAllpage() {
		return allpage;
	}

	public void setAllpage(int allpage) {
		this.allpage = allpage;
	}

	public int getAllrecords() {
		return allrecords;
	}

	public void setAllrecords(int allrecords) {
		this.allrecords = allrecords;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public int getNextpage() {
		return this.curpage+1;
	}

	public void setNextpage(int nextpage) {
		this.nextpage = nextpage;
	}

	public List getPagedata() {
		return pagedata;
	}

	public void setPagedata(List pagedata) {
		this.pagedata = pagedata;
	}

	public int getPagerecord() {
		return pagerecord;
	}

	public void setPagerecord(int pagerecord) {
		this.pagerecord = pagerecord;
	}

	public int getPreviouspage() {
		return this.curpage-1;
	}

	public void setPreviouspage(int previouspage) {
		this.previouspage = previouspage;
	}
    
}
