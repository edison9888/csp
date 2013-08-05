/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.selenium.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility Methods to support easy usage from an (junit4 driven) selenium test.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-24 - 下午06:54:09
 * @version 1.0
 */
public final class LogUtils {

    private LogUtils() {}

    /**
     * 创建谢文件的工具
     * 
     * @param resultFileNameAndPath
     * @param resultEncoding
     * @param replaceExistingFile
     * @return
     * @throws RuntimeException
     * 2011-5-26 - 下午06:09:46
     */
    public static BufferedWriter createWriter(final String resultFileNameAndPath, final String resultEncoding,
            boolean replaceExistingFile) throws RuntimeException {
        BufferedWriter loggingWriter = null;
        try {
            // check early if we can create and write to this file
            File resultFile = new File(resultFileNameAndPath);
            if (replaceExistingFile && resultFile.exists()) {
                resultFile.delete();
            }
            boolean newFileCreated = resultFile.createNewFile();
            if (!newFileCreated) {
                throw new RuntimeException("Failed to create new file: '"
                        + resultFileNameAndPath
                        + "'. Does this file already exist?");
            }
            // TODO: do we need this check? if the new file could be created previously we should be able to write to it
            if (!resultFile.canWrite()) {
                throw new RuntimeException("Cannot write to file: '" + resultFileNameAndPath + "'");
            }
            // http://www.jorendorff.com/articles/unicode/java.html
            loggingWriter = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(resultFileNameAndPath), resultEncoding));
        } catch (IOException ioExc) {
            // try to close loggingWriter not needed as it could not be created here
            throw new RuntimeException("ERROR while creating file: '" + resultFileNameAndPath + "'", ioExc);
        }
        return loggingWriter;
    }

    
    @Deprecated
    public static BufferedWriter createWriter(final String resultFileNameAndPath, final String resultEncoding)
            throws RuntimeException {
        return createWriter(resultFileNameAndPath, resultEncoding, false);
    }

    /**
     * 
     * 
     * @return
     * 2011-5-26 - 下午06:10:33
     */
    public static String timeStampForFileName() {
        return timeStampForFileName("yyyy-MM-dd_HH-mm");
    }

    public static String timeStampForFileName(final String simpleDateFormat) {
        Date currentDateTime = new Date(System.currentTimeMillis());
        SimpleDateFormat humanReadableFormat = new SimpleDateFormat(simpleDateFormat);
        return humanReadableFormat.format(currentDateTime);
    }

    /**
     * 
     * 
     * @param loggingBean
     * @param presetNumArgs
     * @param defaultValue
     * @return
     * 2011-5-26 - 下午06:10:58
     */
    static String[] getCorrectedArgsArray(LogBean logBean, int presetNumArgs, String defaultValue) {
        String[] currentArgs;
        if (null == logBean || null == logBean.getArgs()) {
            currentArgs = new String[] {};
        } else {
            currentArgs = logBean.getArgs();
        }
        String[] newArgs = new String[presetNumArgs];
        for (int i = 0; i < currentArgs.length; i++) {
            newArgs[i] = currentArgs[i];
        }
        for (int i = currentArgs.length; i < presetNumArgs; i++) {
            newArgs[i] = defaultValue;
        }
        return newArgs;
    }
}
