//package com.taobao.monitor.web.ss;
//
//import java.net.InetSocketAddress;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Executors;
//
//
//import org.apache.log4j.Logger;
//import org.jboss.netty.bootstrap.ClientBootstrap;
//import org.jboss.netty.buffer.ChannelBuffer;
//import org.jboss.netty.channel.ChannelHandlerContext;
//import org.jboss.netty.channel.ChannelPipeline;
//import org.jboss.netty.channel.ChannelPipelineFactory;
//import org.jboss.netty.channel.ChannelStateEvent;
//import org.jboss.netty.channel.ExceptionEvent;
//import org.jboss.netty.channel.MessageEvent;
//import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
//import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
//import org.jboss.netty.handler.codec.http.HttpClientCodec;
//import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
//import org.jboss.netty.handler.codec.http.HttpResponse;
//
//import static org.jboss.netty.channel.Channels.*;
//
//
//
//import com.taobao.monitor.common.po.HostPo;
//import com.taobao.monitor.common.util.OpsFreeHostCache;
//
//public class SsdetailReportManagerNetty {
//	private static Logger log = Logger.getLogger(SsdetailReportManagerNetty.class);
//	public static List<StableSwitchGroup> report(String app, String port, String command) {
//    	Long startTime = System.currentTimeMillis();
//        List<StableSwitchGroup> ssgs = new ArrayList<StableSwitchGroup>();
//        if(app == null || port == null || command == null)return ssgs;
//        List<HostPo> hplist = OpsFreeHostCache.get().getHostListNoCache(app);
//        if(hplist == null)return ssgs;
//        List<String> ips = new ArrayList<String>();
//        for(HostPo hp : hplist){
//        	String ip = hp.getHostIp();
//        	ips.add(ip);
//        }
//        ssgs = report(ips,port,command);
//        Long endTime = System.currentTimeMillis();
//        log.info("report RUNTIME:"+(endTime-startTime));
//        return ssgs;
//    }
//	public static List<StableSwitchGroup> report(List<String> ips,String port,String command){
//    	List<StableSwitchGroup> ssgs = new ArrayList<StableSwitchGroup>();
//    	if(ips == null || port == null || command == null)return ssgs;
//    	ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));
//    	bootstrap.setPipelineFactory(new HttpClientPipelineFactory(command));
//    	bootstrap.setOption("connectTimeoutMillis", 100);
//    	for(String ip : ips){
//    		 bootstrap.connect(new InetSocketAddress(ip,Integer.parseInt(port)));
//    	}
//    	while(true){
//    		try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				log.error(e);
//			}
//    		if(HttpResponseHandler.checkResults() == ips.size())break;
//    	}
//    	List<String> results = HttpResponseHandler.getResults();
//    	List<String> retIPs = HttpResponseHandler.getIps();
//    	for(Integer index =0 ;index < ips.size();index++){
//    		StableSwitchGroup ssg = strToSSG(retIPs.get(index),results.get(index));
//    		ssgs.add(ssg);
//    	}
//    	return ssgs;
//    }
//	private static class HttpClientPipelineFactory implements ChannelPipelineFactory{
//		private String command;
//		public  HttpClientPipelineFactory(String command){
//			this.command = command;
//		}
//		public ChannelPipeline getPipeline(){
//			ChannelPipeline pipeline = pipeline();
//			pipeline.addLast("codec", new HttpClientCodec());
//			pipeline.addLast("inflater", new HttpContentDecompressor());
//			pipeline.addLast("handler", new HttpResponseHandler(command));
//			return pipeline;
//		}
//	}
//	private static StableSwitchGroup strToSSG(String ip,String str){
//	    	if(str == null || str.matches("GET ERROR!(.*)"))str="";
//	    	str=str.trim();
//	    	str=cleanwhiteSpace(str);
//	    	StableSwitchGroup ssg = new StableSwitchGroup(ip,str); 
//	    	String[] parseResult = str.split(" ");
//	    	Integer len = parseResult.length;
//	    	for (int i = 9; i < len; i+=9) {
//	            if(parseResult[i].matches("^com_taobao(.*)"))continue;
//	            if(parseResult[i].matches("^HSF_LOG(.*)"))continue;
//	            if(parseResult[i].matches("^([0-9]*)#(.*)"))continue;
//	            StableSwitch ss = new StableSwitch();
//	            ss.setKey(parseResult[i]);
//	            ss.setCountValve(Integer.parseInt(parseResult[i + 1]));
//	            ss.setCount(Integer.parseInt(parseResult[i + 2]));
//	            ss.setPass(Integer.parseInt(parseResult[i + 3]));
//	            ss.setQps(Integer.parseInt(parseResult[i + 4]));
//	            ss.setBlock(Integer.parseInt(parseResult[i + 5]));
//	            ss.setAvgValve(Integer.parseInt(parseResult[i + 6]));
//	            ss.setAvg(Integer.parseInt(parseResult[i + 7]));
//	            ss.setType(Integer.parseInt(parseResult[i + 8]));
//	            ssg.addStableSwitchs(ss);
//	    	}
//	    	return ssg;
//	    }
//	 private static String cleanwhiteSpace(String source) {
//	        StringBuffer buffer = new StringBuffer();
//	        boolean hastwowhitespace = false;
//	        for (int i = 0; i < source.length(); i++) {
//	            char c = source.charAt(i);
//	            if (!Character.isWhitespace(c)) {
//	                if (hastwowhitespace) {
//	                    buffer.append(' ');
//	                    hastwowhitespace = false;
//	                }
//	                buffer.append(c);
//	            } else {
//	                hastwowhitespace = true;
//	                if (source.length() == (i + 1)) {
//	                    buffer.append(' ');
//	                }
//	            }
//	        }
//	        return buffer.toString();
//	    }
//	private static class HttpResponseHandler extends SimpleChannelUpstreamHandler{
//		private static List<String> results = new ArrayList<String>();
//		private static List<String> ips = new ArrayList<String>();
//		private static Logger logger = Logger.getLogger(HttpResponseHandler.class);
//		private String command;
//		public HttpResponseHandler(String command){
//			this.command = command;
//		}
//		public static List<String> getResults(){
//			List<String> ret = results;
//			results = new ArrayList<String>();
//			return ret;
//		}
//		public static List<String> getIps(){
//			List<String>ret = ips;
//			ips = new ArrayList<String>();
//			return ret;
//		}
//		public static Integer checkResults(){
//			return results.size();
//		}
//		public void channelConnected(ChannelHandlerContext ctx,ChannelStateEvent e) throws Exception{
//			e.getChannel().write(command);
//		}
//		public void messageReceived(ChannelHandlerContext ctx,MessageEvent e) throws Exception{
//			HttpResponse response = (HttpResponse)e.getMessage();
//			ChannelBuffer content = response.getContent();
//			if(content.readable()){
//				ips.add(e.getChannel().getRemoteAddress().toString());
//				results.add(content.toString());
//			}
//			e.getChannel().close();
//		}
//		public void exceptionCaught(ChannelHandlerContext ctx,ExceptionEvent e){
//			logger.error(e);
//			ips.add("");
//			results.add("");
//			e.getChannel().close();
//		}
//	}
//}	
