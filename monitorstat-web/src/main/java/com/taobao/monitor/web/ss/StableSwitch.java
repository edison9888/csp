package com.taobao.monitor.web.ss;

/**
 * xiaoxie 2011-3-3
 */
public class StableSwitch {

    private String address;
    private String key;
    private int    countValve;
    private int    count;
    private int    pass;
    private int    qps;
    private int    block;
    private int    avgValve;
    private int    avg;
    private int    type;
    
    public void setStableSwitch(String[] infos,Integer start,Integer jump){
    	for(Integer i=start;i<infos.length;i++){
    		switch(i+jump){
    		case 0:setAddress(infos[i]);break;
    		case 1:setKey(infos[i]);break;
    		case 2:setCountValve(Integer.parseInt(infos[i]));break;
    		case 3:setCount(Integer.parseInt(infos[i]));break;
    		case 4:setPass(Integer.parseInt(infos[i]));break;
    		case 5:setQps(Integer.parseInt(infos[i]));break;
    		case 6:setBlock(Integer.parseInt(infos[i]));break;
    		case 7:setAvgValve(Integer.parseInt(infos[i]));break;
    		case 8:setAvg(Integer.parseInt(infos[i]));break;
    		case 9:setType(Integer.parseInt(infos[i]));break;
    		}
    	}
    }

    public String getAddress() {
        return address;
    }

    public int getQps() {
        return qps;
    }

    public void setQps(int qps) {
        this.qps = qps;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() { 
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCountValve() {
        return countValve;
    }

    public void setCountValve(int countValve) {
        this.countValve = countValve;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPass() {
        return pass;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public int getAvgValve() {
        return avgValve;
    }

    public void setAvgValve(int avgValve) {
        this.avgValve = avgValve;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String display(){
    	return "key:"+key+"cv:"+countValve+"count:"+count+"pass:"+pass+"qps:"+qps+"block:"+block+"avgvalve:"+avgValve+"avg:"+avg+"type:"+type;
    }
}
