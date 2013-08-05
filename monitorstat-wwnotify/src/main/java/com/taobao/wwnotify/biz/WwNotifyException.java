package com.taobao.wwnotify.biz;

/**
 * 发送旺旺提醒消息的异常
 * @author wzm2162
 * @version $Id: TbClientException.java 2007-3-27 下午02:18:55 wzm2162
 */
public class WwNotifyException extends Exception {
    private static final long serialVersionUID = 4052213654872400925L;

    public WwNotifyException() {
    }

    public WwNotifyException(String e) {
        super(e);
    }

    public WwNotifyException(Exception e) {
        super(e);
    }

    public WwNotifyException(String s, Exception e) {
        super(s, e);
    }
}
