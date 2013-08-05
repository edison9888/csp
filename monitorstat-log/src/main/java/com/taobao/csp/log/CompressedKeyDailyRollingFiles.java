
/**
 * monitorstat-log
 */
package com.taobao.csp.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

/**
 * @author xiaodu
 *
 * 下午2:49:05
 */
public class CompressedKeyDailyRollingFiles extends DailyRolingFiles{

	private SimpleDateFormat sdf;
	
	private String fullLogFileName;
	
	private String fullCompressKeyFileName;

	private String backLogFilename; //备份文件的名称
	
	private String backKeyFilename;

	private BufferedWriter logFilewriter = null;
	
	private BufferedWriter keyFileWriter = null;

	private int bufferSize = 8 * 1024;

	private long nextCheck = System.currentTimeMillis() - 1;

	private Date now = new Date();
	
//	private boolean compress = false;
	
	private String KEY_COMPRESS_POSTFIX = "_INDEX_COMPRESS";
	
	private String KEY_INDEX_POSTFIX = "!";
	
	private int compressIndex = -1;
	
	private int MIN_USE_COMPRESS_KEY_LEN = 8;
	
	private int MAX_COMPRESS_INDEX = 999;
	
	private Map<String,Integer> keyCompressMap = new ConcurrentHashMap<String, Integer>();
	
	private char KEY_COMPRESS_SEPARATOR = 1;
	
	
	/**
	 * 构建日滚得文件
	 * @param fileName 需要一个完整的文件路径
	 */
	public CompressedKeyDailyRollingFiles(String filePath,String fileName) {
		super(filePath,fileName);
		this.fullLogFileName = filePath+fileName;
		this.fullCompressKeyFileName = filePath+fileName+KEY_COMPRESS_POSTFIX;
		init();
	}
	
	
	public void setMaxCompressKeySize(int size){
		if(size >1)
			MAX_COMPRESS_INDEX = size;
	}


	public synchronized void writerInfo(Keys key,NoteLog values) {
		if (logFilewriter != null) {
			if (isRolling()) {
				dailyLogRolling();
				dailyKeyRolling();
			}
			try {
				
				StringBuilder sb = new StringBuilder();
				sb.append(getFormatTime()).append(MonitorConstants.SPLITTER_CHAR);
				if(key.getKeys() != null){
					for(String k:key.getKeys()){
						String tmp = k;
						if(tmp!=null){
							tmp = tmp
									.replaceAll(MonitorConstants.SPLITTER_CHAR, "").replaceAll("\n","");
						}else{
							tmp ="null";
						}
						
						Integer index = getKeyCompressIndex(tmp);
						if(index != null){
							tmp = KEY_INDEX_POSTFIX+index.toString();
						}
						sb.append(tmp).append(MonitorConstants.SPLITTER_CHAR);
					}
				}else{
					sb.append("key&empty");
				}
				String valueString = values.getString();
				sb.append(valueString);
				
				logFilewriter.write(sb.toString());
				logFilewriter.newLine();
				logFilewriter.flush();
			} catch (IOException e) {
			}
		}
	}
	

	private void init() {
		if (this.fullLogFileName != null) {
			
			
			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(YYYY_MM_DD);
			try {
				logFilewriter = new BufferedWriter(new FileWriter(this.fullLogFileName,true), bufferSize);
			} catch (IOException e) {
			}
			File logfile = new File(this.fullLogFileName);
			backLogFilename = this.fullLogFileName + sdf.format(new Date(logfile.lastModified()));
			
			try {
				keyFileWriter = new BufferedWriter(new FileWriter(this.fullCompressKeyFileName,true), bufferSize);
			} catch (IOException e) {
			}
			File keyfile = new File(fullCompressKeyFileName);
			backKeyFilename = this.fullCompressKeyFileName+ sdf.format(new Date(keyfile.lastModified()));
			readOldCompressIndex();
		}
	}
	/**
	 * 判断是否可以滚动文件
	 * @return
	 */
	private boolean isRolling() {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(n);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, 1);
			now.setTime(n);
			nextCheck = calendar.getTimeInMillis();
			return true;
		}
		return false;
	}
	/**
	 * 替换文件
	 */
	private void dailyLogRolling() {
		String newFilename = this.fullLogFileName + sdf.format(now);
		// 如果isRolling() 并没有到达第二天 需要用这个判断
		if (backLogFilename.equals(newFilename)) {
			return;
		}
		if (logFilewriter != null) {
			try {
				logFilewriter.close();
			} catch (IOException e) {
			}
		}

		File target = new File(backLogFilename);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(this.fullLogFileName);
		boolean result = file.renameTo(target);
		if(!result){
			
		}
		try {
			logFilewriter =createFile(this.fullLogFileName);
		} catch (IOException e) {
		}
		backLogFilename = newFilename;
	}
	
	
	
	  public  BufferedWriter createFile(String fileName) throws IOException {
		FileOutputStream ostream = null;
		try {
			ostream = new FileOutputStream(fileName, true);
		} catch (FileNotFoundException ex) {
		}
		OutputStreamWriter retval = null;
		try {
			retval = new OutputStreamWriter(ostream, GBK);
		} catch (IOException e) {
		}
		if (retval == null) {
			retval = new OutputStreamWriter(ostream);
		}
		return  new BufferedWriter(retval,bufferSize);
	  }
	
	
	private void dailyKeyRolling() {
		String newFilename = this.fullCompressKeyFileName + sdf.format(now);
		// 如果isRolling() 并没有到达第二天 需要用这个判断
		if (backKeyFilename.equals(newFilename)) {
			return;
		}
		if (keyFileWriter != null) {
			try {
				keyFileWriter.close();
			} catch (IOException e) {
			}
		}

		File target = new File(this.backKeyFilename);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(this.fullCompressKeyFileName);
		boolean result = file.renameTo(target);
		if(!result){
			
		}
		try {
			keyFileWriter = createFile(this.fullCompressKeyFileName);
		} catch (IOException e) {
		}
		 this.backKeyFilename = newFilename;
		 
		 keyCompressMap.clear();
		 compressIndex = -1;
		 
	}
	
	private synchronized Integer getKeyCompressIndex(String keyName){
		
		
		if(keyName.length() <MIN_USE_COMPRESS_KEY_LEN ){
			return null;
		}
		
		if(keyName != null ){
			Integer index = keyCompressMap.get(keyName);
			if(index != null){
				return index;
			}else{
				if(compressIndex > MAX_COMPRESS_INDEX){
					return null;
				}
				compressIndex++;
				try {
					immediatelyWriteKey(keyName,compressIndex);
				} catch (IOException e) {
					return null;
				}
				keyCompressMap.put(keyName, compressIndex);
				return compressIndex;
			}
		}
		return null;
	}
	
	
	private void immediatelyWriteKey(String keyName,int index) throws IOException{
		
		if(keyFileWriter!= null){
			keyFileWriter.write(keyName);
			keyFileWriter.write(KEY_COMPRESS_SEPARATOR);
			keyFileWriter.write(""+index);
			keyFileWriter.newLine();
			keyFileWriter.flush();
		}else{
			throw new IOException("keyFileWriter is not exist");
		}
		
	}
	
	private void readOldCompressIndex(){
		String compressFilePath = this.fullCompressKeyFileName;
		File file = new File(compressFilePath);
		if(file.exists()){
			BufferedReader reader =  null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String line = null;
				while((line =reader.readLine())!= null){
					String[] keyInfo = StringUtils.split(line,KEY_COMPRESS_SEPARATOR);
					String keyName = keyInfo[0];
					String keyIndex = keyInfo[1];
					
					Integer index = Integer.parseInt(keyIndex);
					if(compressIndex <index){
						compressIndex = index;
					}
					keyCompressMap.put(keyName, index);
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}finally{
				if(reader != null){
					try {
						reader.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.log.DailyRolingFiles#close()
	 */
	@Override
	public void close() {
		
		if (logFilewriter != null) {
			try {
				logFilewriter.close();
			} catch (IOException e) {
			}
		}
		if (keyFileWriter != null) {
			try {
				keyFileWriter.close();
			} catch (IOException e) {
			}
		}
		
	}


	

}
