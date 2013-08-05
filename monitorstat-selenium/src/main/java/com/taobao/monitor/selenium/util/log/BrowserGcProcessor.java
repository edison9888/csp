/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.openqa.selenium.net.Urls;

import com.thoughtworks.selenium.DefaultRemoteCommand;
import com.thoughtworks.selenium.SeleniumException;

/**
 * 浏览器gc回收处理器：回收所有没有释放掉的过期的浏览器.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-6-14 - 上午11:08:05
 * @version 1.0
 */
public class BrowserGcProcessor {
	
	private String pathToServlet;
	
	private String browserStartCommand;
	
	private String sessionId;
    
	private String rcServerLocation;
	
	private String commandName = "testComplete";
	
    public BrowserGcProcessor(String serverHost, int serverPort, String browserStartCommand,
    		String sessionId) {
        rcServerLocation = serverHost +
        ":"+ Integer.toString(serverPort);
        this.pathToServlet = "http://" + rcServerLocation + "/selenium-server/driver/";
        this.browserStartCommand = browserStartCommand;
        this.sessionId = sessionId;
    }
    
    /**
     * 封装selenium 命令
     * 
     * @param command
     * @return
     * 2011-6-14 - 下午04:07:37
     */
	private String buildCommandBody(String command) {
        StringBuffer sb = new StringBuffer();
        sb.append(command);
        if (sessionId != null) {
            sb.append("&sessionId=");
          sb.append(Urls.urlEncode(sessionId));
        }
        return sb.toString();
    }
    
    // for testing
    protected HttpURLConnection getHttpUrlConnection(URL urlForServlet) throws IOException {
      return (HttpURLConnection) urlForServlet.openConnection();
    }
    
    // for testing
    protected Writer getOutputStreamWriter(HttpURLConnection conn) throws IOException {
      return new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())); 
    }
    
    // for testing
    protected Reader getInputStreamReader(HttpURLConnection conn) throws IOException {
      return new InputStreamReader(conn.getInputStream(), "UTF-8");
    }
    
    // for testing
    protected int getResponseCode(HttpURLConnection conn) throws IOException {
      return conn.getResponseCode();
    }
    
    /**
     * 提交selenium 命令到selenium server执行并返回结果
     * 
     * @param command
     * @return
     * @throws IOException
     * 2011-6-14 - 下午04:12:13
     */
    protected String getCommandResponseAsString(String command) throws IOException {
        String responseString = null;
        int responsecode = HttpURLConnection.HTTP_MOVED_PERM;
        HttpURLConnection uc = null;
        Writer wr = null;
        Reader rdr = null;
        while (responsecode == HttpURLConnection.HTTP_MOVED_PERM) {
            URL result = new URL(pathToServlet); 
            String body = buildCommandBody(command);
            try {
                uc = getHttpUrlConnection(result);
                uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                uc.setInstanceFollowRedirects(false);
                uc.setDoOutput(true);
                wr = getOutputStreamWriter(uc);;
                wr.write(body);
                wr.flush();
                responsecode = getResponseCode(uc);
                if (responsecode == HttpURLConnection.HTTP_MOVED_PERM) {
                    pathToServlet = uc.getRequestProperty("Location");
                } else if (responsecode != HttpURLConnection.HTTP_OK) {
                    throwAssertionFailureExceptionOrError(uc.getResponseMessage());
                } else {
                    rdr = getInputStreamReader(uc);
                    responseString = stringContentsOfInputStream(rdr);
                }
            } finally {
              closeResources(uc, wr, rdr);
            }
        }
        return responseString;
    }
    
    protected String throwAssertionFailureExceptionOrError(String message) {
        throw new SeleniumException(message);
    }
    
    /**
     * 释放资源
     * 
     * @param conn
     * @param wr
     * @param rdr
     * 2011-6-14 - 下午04:11:30
     */
    protected void closeResources(HttpURLConnection conn, Writer wr, Reader rdr) {
        try {
          if (null != wr) {
            wr.close();
          }
        } catch (IOException ioe) {
          // ignore
        }
    }
    
    private String stringContentsOfInputStream(Reader rdr) throws IOException {
        StringBuffer sb = new StringBuffer();
        int c;
        try {
          while ((c = rdr.read()) != -1) {
              sb.append((char) c);
          }
          return sb.toString();
        } finally {
          rdr.close();
        }
    }
    
    /**
     * 执行关闭浏览器
     * 
     * @param commandName
     * @param args
     * @return
     * 2011-6-14 - 下午04:07:54
     */
    public String closeBrowser() {
        DefaultRemoteCommand command = new DefaultRemoteCommand(commandName, null);
        String result = executeCommandOnServlet(command.getCommandURLString());
        if (result == null) {
            throw new NullPointerException("Selenium Bug! result must not be null");
        }
        if (!result.startsWith("OK")) {
            return throwAssertionFailureExceptionOrError(result);
        }
        return result;
    }
    
    /** Sends the specified command string to the bridge servlet */  
    public String executeCommandOnServlet(String command) {
        InputStream is = null;
        try {
            return getCommandResponseAsString(command);
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                throw new SeleniumException(e.getMessage(),e);
            }
            e.printStackTrace();
            throw new UnsupportedOperationException("Catch body broken: IOException from " + command + " -> " + e, e);
        }
    }
    
    public static void main(String[] args) {
    	BrowserGcProcessor gcProcessor = new BrowserGcProcessor("10.13.42.54", 
    			4444, "*firefox /usr/lib/firefox-3.0.18/firefox", "e81e8e3146314178894b8ce9f9329bd0");
    	try {
    		gcProcessor.closeBrowser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
