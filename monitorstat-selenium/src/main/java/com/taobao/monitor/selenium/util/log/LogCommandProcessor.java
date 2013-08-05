/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.HttpCommandProcessor;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午03:28:25
 * @version 1.0
 */
public class LogCommandProcessor implements CommandProcessor {
	static final String AUTO_SCREENSHOT_WAIT_TIMEOUT_FILENAME_PART = "WaitTimeout";
	
	private CommandProcessor realCommandProcessor;
	
	/** 日志格式化处理接口 */
	private LogResultsFormatter formatter;

    final TestMetricsBean seleniumTestMetrics = new TestMetricsBean();

    static final String SELENIUM_RC_OK_RESULT = "OK";

    static final String SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA = SELENIUM_RC_OK_RESULT + ",";

    static final String SELENIUM_CORE_BOOLEAN_RESULT_TRUE = "true";

    static final String SELENIUM_CORE_BOOLEAN_RESULT_FALSE = "false";

    static final String WAIT_CLASS_NAME = "Wait";

    boolean logMethodsAsComments = true;

    String[] excludeCommandsFromLog = {"getHtmlSource",};

    List<LogBean> logEventsQueue = new ArrayList<LogBean>();

    /** 回调接口 */
    private LogNotifier callbackNotifier = null;
    
    /**  回调实例 */
    private Object callbackInstance = null;

    /**
     * Constructor.
     *
     * @param commandProcessor
     *        selenium的commandProcessor
     * @param myFormatter
     *        自定义的日志格式化处理器.
     */
    public LogCommandProcessor(CommandProcessor commandProcessor, LogResultsFormatter myFormatter) {
        this.formatter = myFormatter;
        this.realCommandProcessor = commandProcessor;
    }
    
    /**
     * 创建HttpCommandProcessor.
     *
     * @param serverHost -
     *        Selenium Server 主机地址
     * @param serverPort -
     *        Selenium Server 主机监听端口
     * @param browserStartCommand -
     *        命令字符串用来启动浏览器, e.g. "*firefox" or "c:\\program files\\internet
     *        explorer\\iexplore.exe"
     * @param browserUrl -
     *        测试的URL域名。我们将在启动浏览器指向这个selenium资源
     *        网址，例如： “http://www.google.com“将发送到浏览器
     *        “http://www.google.com/selenium-server/core/RemoteRunner.html“
     * @param myFormatter
     *        日志格式化实现接口，按需求自己扩展.
     */
    public LogCommandProcessor(String serverHost, int serverPort, String browserStartCommand, String browserUrl,
            LogResultsFormatter myFormatter) {
        this.formatter = myFormatter;
        this.realCommandProcessor = new HttpCommandProcessor(serverHost, serverPort, browserStartCommand, browserUrl);
    }
    
    /**
     * 扩展doCommand,处理特殊的commands. 包括所有的除了realcommandProcessor自身外的其他命令
     * Extends doCommand, handling special logging commands. Delegates all other commands to the realcommandProcessor.
     * @param commandName
     * @param args
     * @return
     * 2011-5-24 - 下午05:39:11
     */
	public String doCommand(String commandName, String[] args) {
        String result = "";
        long cmdStartMillis = System.currentTimeMillis();
        if (SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_COMMENT.getName().equals(commandName)) {
            String comment = args[0] != null ? args[0] : "";
            String extraInfo = "";
            if (args.length > 1) {
                extraInfo = args[1] != null ? args[1] : "";
            }
            logComment(comment, extraInfo, cmdStartMillis);
        } else if (SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_AUTO_SCREENSHOT.getName().equals(commandName)) {
            String baseName = args[0] != null ? args[0] : "";
            doAutomaticScreenshot(baseName);
        } else if (SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_ASSERTION.getName().equals(commandName)) {
            // FIXME handle less than 3 args
            String[] loggingArgs = new String[] {args[1], args[2]};
            doExceptionLogging(args[0], loggingArgs, "", null, cmdStartMillis);
        } else if (SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_RESOURCE.getName().equals(commandName)) {
            result = LogCommandProcessor.SELENIUM_RC_OK_RESULT;
            long now = System.currentTimeMillis();
            LogBean b = new LogBean();
            b.setCallingClass(getRealCallingClassWithLineNumberAsString(getCurrentCallingClassAsStackTraceElement()));
            b.setWaitInvolved(isWaitInvolved());
            b.setSourceMethod("RESOURCE");
            b.setCommandName("captureScreenshot");
            b.setResult(result);
            b.setArgs(args);
            b.setCommandSuccessful(true);
            b.setCmdStartMillis(now);
            b.setCmdEndMillis(now);
            logEventsQueue.add(b);
        } else {
            this.seleniumTestMetrics.incCommandsProcessed();
            try {
                result = this.realCommandProcessor.doCommand(commandName, args);
            } catch (RuntimeException e) {
                doExceptionLogging(commandName, args, "", e, cmdStartMillis);
                throw e;
            }
            doLog(commandName, args, result, cmdStartMillis);
        }
        return result;
	}
	
	/**
	 * 
	 * @param commandName
	 * @param args
	 * @return
	 * 2011-5-26 - 下午06:27:04
	 */
    public boolean getBoolean(String commandName, String[] args) {
        // TODO: both?
        long cmdStartMillis = System.currentTimeMillis();
        this.seleniumTestMetrics.incCommandsProcessed();
        this.seleniumTestMetrics.incVerificationsProcessed();
        boolean result = false;
        try {
            result = this.realCommandProcessor.getBoolean(commandName, args);
        } catch (RuntimeException e) {
            doExceptionLogging(commandName, args, "", e, cmdStartMillis);
            throw (e);
        }
        doLog(commandName, args, LogCommandProcessor.SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA + result, cmdStartMillis);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean[] getBooleanArray(String commandName, String[] args) {
        long cmdStartMillis = System.currentTimeMillis();
        this.seleniumTestMetrics.incCommandsProcessed();
        boolean[] results;
        try {
            results = this.realCommandProcessor.getBooleanArray(commandName, args);
        } catch (RuntimeException e) {
            doExceptionLogging(commandName, args, "", e, cmdStartMillis);
            throw e;
        }
        doLog(commandName, args, LogCommandProcessor.SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA
                + ArrayUtils.toString(results), cmdStartMillis);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public Number getNumber(String commandName, String[] args) {
        long cmdStartMillis = System.currentTimeMillis();
        this.seleniumTestMetrics.incCommandsProcessed();
        Number result;
        try {
            result = this.realCommandProcessor.getNumber(commandName, args);
        } catch (RuntimeException e) {
            doExceptionLogging(commandName, args, "", e, cmdStartMillis);
            throw e;
        }
        doLog(commandName, args, LogCommandProcessor.SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA + result, cmdStartMillis);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public Number[] getNumberArray(String commandName, String[] args) {
        long cmdStartMillis = System.currentTimeMillis();
        this.seleniumTestMetrics.incCommandsProcessed();
        Number[] results;
        try {
            results = this.realCommandProcessor.getNumberArray(commandName, args);
        } catch (RuntimeException e) {
            doExceptionLogging(commandName, args, "", e, cmdStartMillis);
            throw e;
        }
        doLog(commandName, args, LogCommandProcessor.SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA
                + ArrayUtils.toString(results), cmdStartMillis);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public String getString(String commandName, String[] args) {
        long cmdStartMillis = System.currentTimeMillis();
        this.seleniumTestMetrics.incCommandsProcessed();
        String result;
        try {
            result = this.realCommandProcessor.getString(commandName, args);
        } catch (RuntimeException e) {
            doExceptionLogging(commandName, args, "", e, cmdStartMillis);
            throw e;
        }
        doLog(commandName, args, LogCommandProcessor.SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA
                + ArrayUtils.toString(result), cmdStartMillis);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getStringArray(String commandName, String[] args) {
        long cmdStartMillis = System.currentTimeMillis();
        String[] results;
        try {
            results = this.realCommandProcessor.getStringArray(commandName, args);
        } catch (RuntimeException e) {
            doExceptionLogging(commandName, args, "", e, cmdStartMillis);
            throw e;
        }
        doLog(commandName, args, LogCommandProcessor.SELENIUM_RC_OK_RESULT_PREFIX_WITH_COMMA
                + ArrayUtils.toString(results), cmdStartMillis);
        return results;
    }
    
    /**
     * 
     * 2011-5-24 - 下午05:48:57
     */
    public void start() {
        this.realCommandProcessor.start();
        this.seleniumTestMetrics.setStartTimeStamp(System.currentTimeMillis());
        logExecutionEnvironment();
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        seleniumTestFinished();
        this.realCommandProcessor.stop();
        seleniumTestMetrics.setCommandsExcludedFromLogging(excludeCommandsFromLog);
        // TODO: looks a bit awkward (instantiating and not care about the instance)
        new EventQueuePostProcessor(formatter, logEventsQueue, seleniumTestMetrics, logMethodsAsComments);
    }

    public String[] getExcludedCommands() {
        return excludeCommandsFromLog.clone();
    }

    public void setExcludedCommands(String[] excludedCommands) {
        this.excludeCommandsFromLog = excludedCommands.clone();
    }

    /**
     * 封装日志数据
     * 
     * @param commandName
     * @param args
     * @param result
     * @param cmdStartMillis
     * @param cmdEndMillis
     * @return
     * 2011-5-25 - 下午06:27:37
     */
    public static LogBean presetLogBean(String commandName, String[] args, String result, long cmdStartMillis,
            long cmdEndMillis) {
        LogBean LogBean = new LogBean();
        LogBean.setCommandName(commandName);
        LogBean.setArgs(args);
        LogBean.setResult(result);
        LogBean.setCmdStartMillis(cmdStartMillis);
        LogBean.setCmdEndMillis(cmdEndMillis);
        return LogBean;
    }

    /**
     * 获取环境变量
     * 
     * 2011-5-25 - 下午06:28:04
     */
    void logExecutionEnvironment() {
        final String userAgent = getEvalNoException("navigator.userAgent");
        this.seleniumTestMetrics.setUserAgent(userAgent);

        // SeleniumCore
        final String seleniumCoreVersion = getEvalNoException("window.top.Selenium.coreVersion");
        this.seleniumTestMetrics.setSeleniumCoreVersion(seleniumCoreVersion);
        final String seleniumCoreRevision = getEvalNoException("window.top.Selenium.coreRevision");
        this.seleniumTestMetrics.setSeleniumCoreRevision(seleniumCoreRevision);
        // SeleniumRC
        final String seleniumRcVersion = getEvalNoException("window.top.Selenium.rcVersion");
        this.seleniumTestMetrics.setSeleniumRcVersion(seleniumRcVersion);
        final String seleniumRcRevision = getEvalNoException("window.top.Selenium.rcRevision");
        this.seleniumTestMetrics.setSeleniumRcRevision(seleniumRcRevision);
    }

    /**
     * Get an Property via Selenium getEval catching any Exception beeing thrown.
     * @param jsExpr an JavaScript Expressing returning an String
     * @return result of calling getEval or "UNKNOWN" if any Exception is issued by Selenium
     */
    String getEvalNoException(String jsExpr) {
        String propertyValue;
        try {
            propertyValue = this.realCommandProcessor.getString("getEval",
                new String[] {jsExpr,});
        } catch (Exception exc) {
            propertyValue = "UNKNOWN";
        }
        return propertyValue;
    }

    /**
     * 处理异常
     * 
     * @param commandName
     * @param args
     * @param result
     * @param exception
     * @param cmdStartMillis
     * 2011-5-25 - 下午06:28:33
     */
    void doExceptionLogging(String commandName, String[] args, String result, Throwable exception, long cmdStartMillis) {
        Boolean screenshot = true;
        /*
        if (null!=callbackNotifier) {
            screenshot = callbackNotifier.errorLogging(callbackInstance, commandName, args, result, exception, cmdStartMillis);
        }
        if (screenshot) {
            doAutomaticScreenshot("Error");
        }
        */
        String resultContent;
        String errorMessage;
        if (null != exception) {
            resultContent = "ERROR," + result + " " + exception.getClass().getName() + " - " + exception.getMessage();
            errorMessage = exception.getMessage();
        } else {
            resultContent = "ERROR," + result;
            if (args.length > 0) {
                errorMessage = "ERROR: " + args[0];
            } else {
                errorMessage = "INTERNAL ERROR: real error-msg could not be determined";
            }
        }
        doLog(commandName, args, resultContent, cmdStartMillis);
        this.seleniumTestMetrics.incFailedCommands();
        this.seleniumTestMetrics.setLastFailedCommandMessage(errorMessage);
    }

    /**
     * 
     * 
     * @param commandName
     * @param args
     * @param result
     * @param cmdStartMillis
     * 2011-5-26 - 下午06:28:51
     */
    void doLog(String commandName, String[] args, String result, long cmdStartMillis) {
        LogBean currentCommand = LogCommandProcessor.presetLogBean(commandName, args, result, cmdStartMillis, System
                .currentTimeMillis());
        currentCommand.setExcludeFromLogging(isCommandExcludedFromLogging(commandName));
        currentCommand.setCallingClass(getRealCallingClassWithLineNumberAsString(getCurrentCallingClassAsStackTraceElement()));
        currentCommand.setWaitInvolved(isWaitInvolved());
        String sourceMethodName = "unknown";
        StackTraceElement classOrNull = getCurrentCallingClassAsStackTraceElement();
        if (null != classOrNull) {
            sourceMethodName = classOrNull.getMethodName();
        }
        currentCommand.setSourceMethod(sourceMethodName);
        logEventsQueue.add(currentCommand);
    }

    void logComment(String comment, String extraInfo, long cmdStartMillis) {
        doLog(SeleniumCommandExtensions.COMMAND_EXTENSION_LOG_COMMENT.getName(), new String[] {comment, extraInfo}, "",
                cmdStartMillis);
    }
    
    /**
     * 实用程序来检查，如果当前命令是被排除在外
     * 
     * @param commandName
     * @return
     * 2011-5-26 - 下午06:30:46
     */
    boolean isCommandExcludedFromLogging(final String commandName) {
        return Arrays.asList(excludeCommandsFromLog).contains(commandName);
    }

    StackTraceElement getCurrentCallingClassAsStackTraceElement() {
        return StackTraceUtils.getCurrentCallingClassAsStackTraceElement(Thread.currentThread().getStackTrace(),
                "DefaultSelenium");
    }

    String getRealCallingClassWithLineNumberAsString(StackTraceElement currentCallingClassAsStackTraceElement) {
        return StackTraceUtils.stackTraceElementWithLinenumberAsString(currentCallingClassAsStackTraceElement);
    }

    boolean isWaitInvolved() {
        return StackTraceUtils.isClassInStackTrace(Thread.currentThread().getStackTrace(), "."
                + LogCommandProcessor.WAIT_CLASS_NAME);
    }

    void seleniumTestFinished() {
        this.seleniumTestMetrics.setEndTimeStamp(System.currentTimeMillis());
        // Wait timed out?
        if (logEventsQueue.size() > 0) {
            LogBean lastBeanInQueue = logEventsQueue.get(logEventsQueue.size() - 1);
            if (lastBeanInQueue.isWaitInvolved()) {
                lastBeanInQueue.setResult("ERROR,wait timed out");
                doAutomaticScreenshot(LogCommandProcessor.AUTO_SCREENSHOT_WAIT_TIMEOUT_FILENAME_PART);
                this.seleniumTestMetrics.setLastFailedCommandMessage(lastBeanInQueue.getResult());
                this.seleniumTestMetrics.incFailedCommands();
            }
        }
    }

    void doAutomaticScreenshot(String baseFileName) {
    	/*
        final String autoScreenshotFullPath = formatter.generateFilenameForAutomaticScreenshot(baseFileName);
        Boolean internal = true;
        if (null!=callbackNotifier) {
            final String pathFile = formatter.generateFilenameForAutomaticScreenshot(
                    LogCommandProcessor.AUTO_SCREENSHOT_WAIT_TIMEOUT_FILENAME_PART);
            internal = callbackNotifier.makeScreenshot(callbackInstance, pathFile);
        }
        if (internal) {
            doCommand("captureScreenshot", new String[] {autoScreenshotFullPath});
        }
        */
    }

    public boolean isTestFailed() {
        return seleniumTestMetrics.getFailedCommands() > 0;
    }

    /**
     * 设置回调为想停止的通知
     * 
     * @param callbackLogNotifier
     * @param callbackLoggingInstance
     * 2011-5-25 - 下午06:29:43
     */
    public void setCallbackNotifier(LogNotifier callbackLogNotifier, Object callbackLoggingInstance) {
        this.callbackNotifier = callbackLogNotifier;
        this.callbackInstance = callbackLoggingInstance;
    }

    public boolean isLogMethodsAsComments() {
        return logMethodsAsComments;
    }

    public void setLogMethodsAsComments(boolean logMethodsAsComments) {
        this.logMethodsAsComments = logMethodsAsComments;
    }

	/**
	 * @return
	 * 2011-5-24 - 下午06:48:10
	 */
	@Override
	public String getRemoteControlServerLocation() {
		return realCommandProcessor.getRemoteControlServerLocation();
	}

	/**
	 * @param extensionJs
	 * 2011-5-24 - 下午06:48:10
	 */
	@Override
	public void setExtensionJs(String extensionJs) {
		realCommandProcessor.setExtensionJs(extensionJs);	
	}

	/**
	 * @param optionsString
	 * 2011-5-24 - 下午06:48:10
	 */
	@Override
	public void start(String optionsString) {
		realCommandProcessor.start(optionsString);	
	}

	/**
	 * @param optionsObject
	 * 2011-5-24 - 下午06:48:10
	 */
	@Override
	public void start(Object optionsObject) {
		realCommandProcessor.start(optionsObject);	
	}

}
