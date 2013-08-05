
/**
 * monitorstat-monitor
 */
package com.taobao.monitor.web.cache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xiaodu
 *
 * ÉÏÎç11:05:02
 */
public class IpRegionCache {
	
	private static IpRegion[] ipRegions = null;
	
	
	static{
		readerIPText();
	}
	
		
	public static IpRegion getIpRegion(String ip){
		
		try {
			return binarySearchRegion(getIPtoLong(ip));
		} catch (UnknownHostException e) {
		}
		return null;
	}
	
	
	
	private static synchronized void readerIPText(){
		
		if(ipRegions != null){
			return ;
		}
		
		List<IpRegion> regionList = new ArrayList<IpRegion>();
		
		
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("ipbase"),"GBK"));
			while((line=reader.readLine())!=null){
				String[] infos = line.split(",");
				if(infos.length==7){
					String startip = infos[2];
					String endip = infos[3];
					String network = infos[4];
					String province = infos[5];
					String city = infos[6];
					
					IpRegion ip = new IpRegion();
					ip.setCity(city);
					ip.setEndIp(Long.parseLong(endip));
					ip.setNetwork(network);
					ip.setProvince(province);
					ip.setStartIp(Long.parseLong(startip));
					regionList.add(ip);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collections.sort(regionList, new Comparator<IpRegion>() {

			@Override
			public int compare(IpRegion o1, IpRegion o2) {
				
				if(o1.getStartIp()>o2.getStartIp()){
					return 1;
				}else if(o1.getStartIp()<o2.getStartIp()){
					return -1;
				}
				return 0;
			}
		});
		
		ipRegions = regionList.toArray(new IpRegion[]{});
	}
	
	
	
	private static  IpRegion binarySearchRegion(long ip){
		
		int high = ipRegions.length-1;
		int low = 0;
		while(low<=high){
			 int middle = (low + high) >>> 1;
			if(ip <=ipRegions[middle].getEndIp()&&ip>=ipRegions[middle].getStartIp()){
				return ipRegions[middle];
			}else if(ip <ipRegions[middle].getStartIp()){
				high = middle-1;
			}else if(ip >ipRegions[middle].getEndIp()){
				low = middle+1;
			}
			
		}
		return null;
	}
	
	
	private static long getIPtoLong(String ip) throws UnknownHostException {   
		InetAddress ipObj = InetAddress.getByName(ip);
        byte[] b = ipObj.getAddress();   
        long l = b[0] << 24L & 0xff000000L | b[1] << 16L & 0xff0000L   
                | b[2] << 8L & 0xff00 | b[3] << 0L & 0xff; 
        return l;   
    } 
	
	
	public static void main(String[] args){
		for(int i=0;i<10000;i++){
			IpRegion r = IpRegionCache.getIpRegion("218.57.175.63");
			IpRegion r1 = IpRegionCache.getIpRegion("122.227.209.17");
			
		}
		for(int i=0;i<10000;i++){
			IpRegion r = IpRegionCache.getIpRegion("218.57.175.63");
			IpRegion r1 = IpRegionCache.getIpRegion("122.227.209.17");
			
		}
		long time = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			IpRegion r = IpRegionCache.getIpRegion("123.5.183.126");
			IpRegion r1 = IpRegionCache.getIpRegion("122.227.209.17");
		}
		System.out.println(System.currentTimeMillis()- time);
		IpRegion r = IpRegionCache.getIpRegion("210.12.156.0");
		System.out.println(r.getNetwork()+""+r.getProvince()+r.getCity());
	}
}
