
package com.taobao.monitor.stat.analyse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 
 * @author xiaodu
 * @version 2010-6-11 ÏÂÎç01:49:46
 */
public class FilterAnalyse extends Analyse{

	public FilterAnalyse(String appName) {
		super(appName);
	}

	@Override
	public void analyseLogFile(ReportContentInterface content) {
		
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(""));
			String line = null;
			while((line=reader.readLine())!=null){
				String time = parseCollectTime(line);
				TimeFilterNode root = new TimeFilterNode();
				if(time!=null){
					//2010-06-11 00:00:06,115 WARN  timer.TimerFilter - Response of GET /browse/50010388/.htm returned in 1,059ms					
					root.setUrl("");
					continue;
				}
				//Detail: 0 [1,059ms (6ms), 100%] - process HTTP request
				//  +---0 [0ms] - Preparing request context: BufferedRequestContextImpl(27217920)
				//  +---1 [0ms] - Preparing request context: LazyCommitRequestContextImpl(4022539)
			    //  +---1 [0ms] - Preparing request context: ParserRequestContextImpl(28197986)
			    //  +---1 [0ms] - Preparing request context: SetLocaleRequestContextImpl(754405)
			    //  |   `---1 [0ms] - Parsing request parameters				
				line = line.trim();
				
				if(line.startsWith("+")){
					TimeFilterNode node = new TimeFilterNode();					
					root.addChildNode(node);
				}
				
				
				
				
				
				
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private String parseCollectTime(String line){		
		Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			return matcher.group(1);
		}
		return null;
	}
	/**
	 * //Detail: 0 [1,059ms (6ms), 100%] - process HTTP request
	 * @param line
	 * @return
	 */
	private String parseProcessHttpRequest(String line){		
		Pattern pattern = Pattern.compile("Detail:.*\\[([\\d,])ms\\s*\\([\\d,]+ms\\),\\s*[\\d\\.,]+%\\]");
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			return matcher.group(1);
		}
		return null;
	}
	
	
	
	private class TimeFilterTree{		
		private TimeFilterTree root;		
		private TimeFilterTree parentNode;
				
	}
	
	private class TimeFilterNode{
		
		private TimeFilterNode parentNode;
		private TimeFilterNode root;
		
		private String url;
		
		private String excuteDesc;
		private String excuteAim;
		private String useTime;
		
		private List<TimeFilterNode> nodeList = null;
		
		
		public void addChildNode(TimeFilterNode node){
			
		}
		
		
		public void parseNode(BufferedReader reader){
			try {
				String line = reader.readLine();
				if(line.startsWith("+")){
					this.getParentNode().parseNode(reader);
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public TimeFilterNode getParentNode() {
			return parentNode;
		}
		public void setParentNode(TimeFilterNode parentNode) {
			this.parentNode = parentNode;
		}
		public TimeFilterNode getRoot() {
			return root;
		}
		public void setRoot(TimeFilterNode root) {
			this.root = root;
		}
		public String getExcuteDesc() {
			return excuteDesc;
		}
		public void setExcuteDesc(String excuteDesc) {
			this.excuteDesc = excuteDesc;
		}
		public String getExcuteAim() {
			return excuteAim;
		}
		public void setExcuteAim(String excuteAim) {
			this.excuteAim = excuteAim;
		}
		public String getUseTime() {
			return useTime;
		}
		public void setUseTime(String useTime) {
			this.useTime = useTime;
		}
		
		
		
	}
	
	
	

	@Override
	protected void insertToDb(ReportContentInterface content) {
		// TODO Auto-generated method stub
		
	}

}
