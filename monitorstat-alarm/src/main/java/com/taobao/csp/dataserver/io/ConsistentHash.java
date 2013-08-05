package com.taobao.csp.dataserver.io;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.util.Util;

/**
 * һ���Թ�ϣ
 * ������/ɾ���ڵ��ʱ���ܾ����ٵ�Ӱ������
 * 
 * @author bishan.ct
 *
 */
public class ConsistentHash {
	private static final Logger logger = Logger.getLogger(ConsistentHash.class);
    // ��Ҫ���Ƶ�����ڵ����  
    private final int numberOfReplicas;  
    private final static long circleLength=1<<15;
    private final static long circleMarker=circleLength-1;
    
    // hashcodeѭ��Ȧ  
    private volatile SortedMap<Long, String> circle = new TreeMap<Long, String>();  
  
    //32������ڵ�
    private static ConsistentHash instance=new ConsistentHash(128);
    
    public static ConsistentHash getInstance(){
    	return instance;
    }
    private Set<String> nodes;
    /** 
     * ���캯�� 
     *  
     * @param hashFunction 
     * @param numberOfReplicas 
     * @param nodes 
     *            ��ʵ�ڵ��� 
     */  
    private ConsistentHash(int numberOfReplicas) {  
       this.numberOfReplicas = numberOfReplicas;
    }  
  
    public synchronized void addAll(Set<String> nodes) {  
    	if(this.nodes==null){
    		this.nodes=new HashSet<String>();
    	}
    	this.nodes.addAll(nodes);
    	reset();
    }  
    
    public synchronized void reset() {  
    	if(nodes==null){
    		return;
    	}
    	SortedMap<Long, String> tmpCircle = new TreeMap<Long, String>();  
    	for(String node:nodes){
	        for (int i = 0; i < numberOfReplicas; i++) {
	        	 long hash = -1;
	     		try {
	     			hash = Util.hash((node+"#"+ i).getBytes("GBK"));
	     		} catch (UnsupportedEncodingException e) {
	     			logger.error("hash ͳһ�ɼ�GBK�ֽ�,��ȡGBK�ֽڱ���ʧ��!");
	     			hash = Util.hash((node+"#"+ i).getBytes());
	     		}
	     		hash=hashCompress(hash);
	        	tmpCircle.put(hash, node);  
	        }
    	}
        
        circle=tmpCircle;
    }
    
    /** 
     * ���ӽڵ� 
     *  
     * @param node 
     */  
    public synchronized void add(String node) {  
    	if(nodes==null){
    		nodes=new HashSet<String>();
    	}
    	nodes.add(node);
        
        reset();
    }
  
    /** 
     * �Ƴ��ڵ� 
     *  
     * @param node 
     */  
    public synchronized void remove(String node) {  
    	if(nodes==null){
    		return;
    	}
    	nodes.remove(node);
    	reset();
    }  
    
    private static long hashCompress(long hashCode){
    	return hashCode&circleMarker;
    }
  
    /** 
     * ���ݶ����key�õ�˳ʱ�뷽��ĵ�һ��node 
     *  
     * @param key 
     * @return 
     */  
    public String get(String key) {  
        if (circle.isEmpty()) {  
            return null;  
        }  
        long hash = -1;
		try {
			hash = Util.hash(key.getBytes("GBK"));
		} catch (UnsupportedEncodingException e) {
			logger.error("hash ͳһ�ɼ�GBK�ֽ�,��ȡGBK�ֽڱ���ʧ��!");
			hash = Util.hash(key.getBytes());
		}
		hash=hashCompress(hash);
		
        if (!circle.containsKey(hash)) {  
            // �õ�circle��hashcodeֵ���ڵ���hash�Ĳ���ӳ��  
            SortedMap<Long, String> tailMap = circle.tailMap(hash);  
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();  
        }  
        return circle.get(hash);  
    }  
	
    public static void main(String[] args){
    	HashMap<String,Integer> nodes=new HashMap<String,Integer>();
    	nodes.put("172.23.210.104:16512",0);
    	nodes.put("172.24.52.85:16512",0);
    	nodes.put("172.23.152.138:16512",0);
    	nodes.put("172.24.52.87:16512",0);
    	nodes.put("172.24.52.125:16512",0);
    	nodes.put("172.23.210.118:16512",0);
    	nodes.put("172.24.52.63:16512",0);
    	nodes.put("172.24.52.57:16512",0);
    	nodes.put("172.24.53.42:16512",0);
    	
    	getInstance().addAll(nodes.keySet());
    	
    	String[] keys={"DETAIL`HSF","LOGIN`HSF","MBIS`HSF"};
    	for(int i=0;i<1000000;i++){
    		for(String k:keys){
    			String key="DETAIL`HSF"+i;
        		int coiunt=nodes.get(getInstance().get(key));
        		coiunt++;
        		nodes.put(getInstance().get(key),coiunt);
    		}
    		//System.out.println(getInstance().get(key));
    	}
    	
    	for(Map.Entry<String, Integer> k:nodes.entrySet()){
    		System.out.println(k.getKey()+":"+k.getValue());
    	}
    }
    
}
