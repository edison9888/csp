package com.taobao.monitor.web.ss;

import java.util.ArrayList;
import java.util.List;

/**
 * xiaoxie 2011-3-3
 */
public class StableSwitchGroup {
    private String             address;
    private String             body;
    private List<StableSwitch> stableSwitchs = new ArrayList<StableSwitch>();

    public StableSwitchGroup(String ip, String body) {
        this.address = ip;
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<StableSwitch> getStableSwitchs() {
        return stableSwitchs;
    }

    public void addStableSwitchs(StableSwitch stableSwitch) {
        stableSwitchs.add(stableSwitch);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    public void setListWithBody(Integer start,Integer interval){
    	String[] strs = body.split(" ");
    	for(Integer i=start;i<strs.length;i+=interval){
    		StableSwitch ss = new StableSwitch();
    		ss.setStableSwitch(strs, i, 1);
    		addStableSwitchs(ss);
    	}
    }

}
