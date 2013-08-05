/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;


/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午03:52:57
 * @version 1.0
 */
public class LogBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 记录隶属于case的一次测试的id */
	private long usecaseResultId = 13076844219800L;
	
	/** 记录隶属于UC_ID */
	private long usecaseId;

	/** command名字 */
    private String commandName = "";

    private String[] args;

    private String pageElement;
    
    private String elementValue;
    
    private String result = "";

    private String callingClass = "";

    private boolean commandSuccessful;

    private boolean waitInvolved;

    private long cmdStartMillis;

    private long cmdEndMillis;

    private String sourceMethod;

    private boolean excludeFromLogging = false;

    private long waitDeltaMillis = 0;
    
    private String responseRc;
    
    private String responseSelenium;
    
    private long costTime;

    private List<LogBean> children = new ArrayList<LogBean>();
    
    private Date gmtCreate;
    
    public void addChild(LogBean loggingBean) {
        children.add(loggingBean);
    }

    public List<LogBean> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return getChildren().size() > 0;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String[] getArgs() {
        return (String[]) ArrayUtils.clone(args);
    }

    public void setArgs(String[] args) {
        this.args = (String[]) ArrayUtils.clone(args);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCallingClass() {
        return callingClass;
    }

    public void setCallingClass(String callingClass) {
        this.callingClass = callingClass;
    }

    public boolean isCommandSuccessful() {
        return commandSuccessful;
    }

    public void setCommandSuccessful(boolean commandSuccessful) {
        this.commandSuccessful = commandSuccessful;
    }

    /**
     * 提取 remote-control 综合结果的部分.
     * 
     * @return the remote-control 综合结果的部分
     */
    public String getSrcResult() {
        String[] results = this.result.split(",");
        String srcResult = "";
        if (results.length > 0) {
            srcResult = results[0];
        }
        return srcResult;
    }

    /**
     * 提取selenium-core 综合结果的部分
     * 
     * @return the selenium-core 综合结果的部分
     */
    public String getSelResult() {
        int firstCommaIndex = this.result.indexOf(",");
        return this.result.substring(firstCommaIndex + 1);
    }

    @Override
    public String toString() {
        return "commandName=" + commandName + ", args=" + ArrayUtils.toString(args);
    }

    public long getCmdStartMillis() {
        return cmdStartMillis;
    }

    public void setCmdStartMillis(long cmdStartMillis) {
        this.cmdStartMillis = cmdStartMillis;
    }

    public long getCmdEndMillis() {
        return cmdEndMillis;
    }

    public void setCmdEndMillis(long cmdEndMillis) {
        this.cmdEndMillis = cmdEndMillis;
    }

    public boolean isWaitInvolved() {
        return waitInvolved;
    }

    public void setWaitInvolved(boolean waitInvolved) {
        this.waitInvolved = waitInvolved;
    }

    public String getSourceMethod() {
        return sourceMethod;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    /**
     * 返回的时间取决于命令的类型
     * 
     * 当前主要分为2种情况：
     * 
     * 1) 等待花费的时间
     * 
     * 2) 但一指令执行时间
     * 
     * @return time (millis) 单一指令或者等待花费的时间总和
     */
    public long getDeltaMillis() {
        if (getWaitDeltaMillis() > 0) {
            return getWaitDeltaMillis();
        } else {
            return this.cmdEndMillis - this.cmdStartMillis;
        }
    }

    public boolean isExcludeFromLogging() {
        return excludeFromLogging;
    }

    public void setExcludeFromLogging(boolean excludeFromLogging) {
        this.excludeFromLogging = excludeFromLogging;
    }

    public long getWaitDeltaMillis() {
        return waitDeltaMillis;
    }

    public void setWaitDeltaMillis(long waitDeltaMillis) {
        this.waitDeltaMillis = waitDeltaMillis;
    }

	/**
	 * @return the usecaseResultId
	 */
	public long getUsecaseResultId() {
		return usecaseResultId;
	}

	/**
	 * @param usecaseResultId the usecaseResultId to set
	 */
	public void setUsecaseResultId(long usecaseResultId) {
		this.usecaseResultId = usecaseResultId;
	}

	/**
	 * @return the usecaseId
	 */
	public long getUsecaseId() {
		return usecaseId;
	}

	/**
	 * @param usecaseId the usecaseId to set
	 */
	public void setUsecaseId(long usecaseId) {
		this.usecaseId = usecaseId;
	}

	/**
	 * @return the pageElement
	 */
	public String getPageElement() {
		return pageElement;
	}

	/**
	 * @param pageElement the pageElement to set
	 */
	public void setPageElement(String pageElement) {
		this.pageElement = pageElement;
	}

	/**
	 * @return the elementValue
	 */
	public String getElementValue() {
		return elementValue;
	}

	/**
	 * @param elementValue the elementValue to set
	 */
	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}

	/**
	 * @return the responseRc
	 */
	public String getResponseRc() {
		return responseRc;
	}

	/**
	 * @param responseRc the responseRc to set
	 */
	public void setResponseRc(String responseRc) {
		this.responseRc = responseRc;
	}

	/**
	 * @return the responseSelenium
	 */
	public String getResponseSelenium() {
		return responseSelenium;
	}

	/**
	 * @param responseSelenium the responseSelenium to set
	 */
	public void setResponseSelenium(String responseSelenium) {
		this.responseSelenium = responseSelenium;
	}

	/**
	 * @return the costTime
	 */
	public long getCostTime() {
		return costTime;
	}

	/**
	 * @param costTime the costTime to set
	 */
	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	/**
	 * @return the gmtCreate
	 */
	public Date getGmtCreate() {
		return gmtCreate;
	}

	/**
	 * @param gmtCreate the gmtCreate to set
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
}
