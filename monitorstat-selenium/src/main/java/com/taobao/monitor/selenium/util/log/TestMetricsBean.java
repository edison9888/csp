/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import org.apache.commons.lang.ArrayUtils;

import com.taobao.monitor.selenium.dao.model.UseCase;

/**
 * ����������ڼ��ռ���ָ��ͻ�����Ϣ���û�����Ȱ汾��.
 * Ҳ���ܼ�����commandsProcessed��verificationsProcessed��
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-24 - ����05:25:10
 * @version 1.0
 */
public class TestMetricsBean {
	
	/** ��¼������case��һ�β��Ե�id */
	private long usecaseResultId;
	
	/** ��¼������UC_ID */
	private int usecaseId;
	
    private long startTimeStamp;

    private long endTimeStamp;

    private String startDate;

    private String endDate;
    
	private long duration;

    private static final String LOGGING_SELENIUM_REVISION = "$Revision: 96 $";

    private static final int REVISION_PREFIX_LENGTH = "$Revision: ".length();

    private long commandsProcessed = 0;

    private long failedCommands = 0;

    private long verificationsProcessed = 0;

    private String userAgent;

    private String seleniumCoreVersion;

    private String seleniumCoreRevision;

    private String seleniumRcVersion;

    private String seleniumRcRevision;

    private String lastFailedCommandMessage;

    String[] commandsExcludedFromLogging = {};
    
	private String useCaseAlias;

	private UseCase useCase;
	
	/** ����ִ�н��״̬���ɹ�/ʧ�ܡ� */
	private int finallyState;

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public long getCommandsProcessed() {
        return commandsProcessed;
    }

    public void setCommandsProcessed(long commandsProcessed) {
        this.commandsProcessed = commandsProcessed;
    }

    /**
     * Increment commandsProcessed by one.
     * �����ݵ��÷�: setCommandsProcessed(getCommandsProcessed()+1)
     */
    public void incCommandsProcessed() {
        this.commandsProcessed++;
    }

    public long getFailedCommands() {
        return failedCommands;
    }

    public void setFailedCommands(long failedCommands) {
        this.failedCommands = failedCommands;
    }

    /**
     * Increment failedCommands by one.
     *
     * �����ݵ��÷�: setFailedCommands(getFailedCommands()+1)
     */
    public void incFailedCommands() {
        this.failedCommands++;
    }

    /**
     * ��֤startTimeStamp and endTimeStamp��־��Χ��Ȼ�����ķѵ�ʱ��.
     * @return duration��λ[millis]. ���ڶ�ʧ�����ֵ������·���0
     */
    public long getTestDuration() {
        long testDuration = 0;
        if (startTimeStamp > 0 && endTimeStamp > startTimeStamp) {
            testDuration = endTimeStamp - startTimeStamp;
        }
        return testDuration;
    }

    public long getVerificationsProcessed() {
        return verificationsProcessed;
    }

    public void setVerificationsProcessed(long verificationsProcessed) {
        this.verificationsProcessed = verificationsProcessed;
    }

    /**
     * Increment verificationsProcessed by one.
     *
     * �����ݵ��÷�: setVerificationsProcessed(getVerificationsProcessed()+1)
     */
    public void incVerificationsProcessed() {
        this.verificationsProcessed++;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getSeleniumCoreVersion() {
        return seleniumCoreVersion;
    }

    public void setSeleniumCoreVersion(String seleniumCoreVersion) {
        this.seleniumCoreVersion = seleniumCoreVersion;
    }

    public String getSeleniumCoreRevision() {
        return seleniumCoreRevision;
    }

    public void setSeleniumCoreRevision(String seleniumCoreRevision) {
        this.seleniumCoreRevision = seleniumCoreRevision;
    }

    public String getSeleniumRcVersion() {
        return seleniumRcVersion;
    }

    public void setSeleniumRcVersion(String seleniumRcVersion) {
        this.seleniumRcVersion = seleniumRcVersion;
    }

    public String getSeleniumRcRevision() {
        return seleniumRcRevision;
    }

    public void setSeleniumRcRevision(String seleniumRcRevision) {
        this.seleniumRcRevision = seleniumRcRevision;
    }

    public String getLastFailedCommandMessage() {
        return lastFailedCommandMessage;
    }

    public void setLastFailedCommandMessage(String lastFailedCommandMessage) {
        this.lastFailedCommandMessage = lastFailedCommandMessage;
    }

    public String getLoggingSeleniumRevision() {
        return LOGGING_SELENIUM_REVISION.substring(REVISION_PREFIX_LENGTH, LOGGING_SELENIUM_REVISION.length() - 2);
    }

    public String[] getCommandsExcludedFromLogging() {
        return (String[]) ArrayUtils.clone(commandsExcludedFromLogging);
    }

    public void setCommandsExcludedFromLogging(String[] commandsExcludedFromLogging) {
        this.commandsExcludedFromLogging = (String[]) ArrayUtils.clone(commandsExcludedFromLogging);
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
	public int getUsecaseId() {
		return usecaseId;
	}

	/**
	 * @param usecaseId the usecaseId to set
	 */
	public void setUsecaseId(int usecaseId) {
		this.usecaseId = usecaseId;
	}
    /**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the useCaseAlias
	 */
	public String getUseCaseAlias() {
		return useCaseAlias;
	}

	/**
	 * @param useCaseAlias the useCaseAlias to set
	 */
	public void setUseCaseAlias(String useCaseAlias) {
		this.useCaseAlias = useCaseAlias;
	}

	/**
	 * @return the finallyState
	 */
	public int getFinallyState() {
		return finallyState;
	}

	/**
	 * @param finallyState the finallyState to set
	 */
	public void setFinallyState(int finallyState) {
		this.finallyState = finallyState;
	}

	/**
	 * @return the useCase
	 */
	public UseCase getUseCase() {
		return useCase;
	}

	/**
	 * @param useCase the useCase to set
	 */
	public void setUseCase(UseCase useCase) {
		this.useCase = useCase;
	}
}
