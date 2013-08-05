// **********************************************************************
//
// Copyright (c) 2003-2008 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.0

package com.taobao.wwnotify.biz.ww;

public final class STADPUSH implements java.lang.Cloneable
{
    public String msgType;

    public String sendType;

    public String privileage;

    public String retryTimes;

    public String delayTime;

    public String beginTime;

    public String endTime;

    public String adTopic;

    public String msgContent;

    public String stayTime;

    public String tipPos;

    public String tipStyle;

    public STADPUSH()
    {
    }

    public STADPUSH(String msgType, String sendType, String privileage, String retryTimes, String delayTime, String beginTime, String endTime, String adTopic, String msgContent, String stayTime, String tipPos, String tipStyle)
    {
        this.msgType = msgType;
        this.sendType = sendType;
        this.privileage = privileage;
        this.retryTimes = retryTimes;
        this.delayTime = delayTime;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.adTopic = adTopic;
        this.msgContent = msgContent;
        this.stayTime = stayTime;
        this.tipPos = tipPos;
        this.tipStyle = tipStyle;
    }

    public boolean
    equals(java.lang.Object rhs)
    {
        if(this == rhs)
        {
            return true;
        }
        STADPUSH _r = null;
        try
        {
            _r = (STADPUSH)rhs;
        }
        catch(ClassCastException ex)
        {
        }

        if(_r != null)
        {
            if(msgType != _r.msgType && msgType != null && !msgType.equals(_r.msgType))
            {
                return false;
            }
            if(sendType != _r.sendType && sendType != null && !sendType.equals(_r.sendType))
            {
                return false;
            }
            if(privileage != _r.privileage && privileage != null && !privileage.equals(_r.privileage))
            {
                return false;
            }
            if(retryTimes != _r.retryTimes && retryTimes != null && !retryTimes.equals(_r.retryTimes))
            {
                return false;
            }
            if(delayTime != _r.delayTime && delayTime != null && !delayTime.equals(_r.delayTime))
            {
                return false;
            }
            if(beginTime != _r.beginTime && beginTime != null && !beginTime.equals(_r.beginTime))
            {
                return false;
            }
            if(endTime != _r.endTime && endTime != null && !endTime.equals(_r.endTime))
            {
                return false;
            }
            if(adTopic != _r.adTopic && adTopic != null && !adTopic.equals(_r.adTopic))
            {
                return false;
            }
            if(msgContent != _r.msgContent && msgContent != null && !msgContent.equals(_r.msgContent))
            {
                return false;
            }
            if(stayTime != _r.stayTime && stayTime != null && !stayTime.equals(_r.stayTime))
            {
                return false;
            }
            if(tipPos != _r.tipPos && tipPos != null && !tipPos.equals(_r.tipPos))
            {
                return false;
            }
            if(tipStyle != _r.tipStyle && tipStyle != null && !tipStyle.equals(_r.tipStyle))
            {
                return false;
            }

            return true;
        }

        return false;
    }

    public int
    hashCode()
    {
        int __h = 0;
        if(msgType != null)
        {
            __h = 5 * __h + msgType.hashCode();
        }
        if(sendType != null)
        {
            __h = 5 * __h + sendType.hashCode();
        }
        if(privileage != null)
        {
            __h = 5 * __h + privileage.hashCode();
        }
        if(retryTimes != null)
        {
            __h = 5 * __h + retryTimes.hashCode();
        }
        if(delayTime != null)
        {
            __h = 5 * __h + delayTime.hashCode();
        }
        if(beginTime != null)
        {
            __h = 5 * __h + beginTime.hashCode();
        }
        if(endTime != null)
        {
            __h = 5 * __h + endTime.hashCode();
        }
        if(adTopic != null)
        {
            __h = 5 * __h + adTopic.hashCode();
        }
        if(msgContent != null)
        {
            __h = 5 * __h + msgContent.hashCode();
        }
        if(stayTime != null)
        {
            __h = 5 * __h + stayTime.hashCode();
        }
        if(tipPos != null)
        {
            __h = 5 * __h + tipPos.hashCode();
        }
        if(tipStyle != null)
        {
            __h = 5 * __h + tipStyle.hashCode();
        }
        return __h;
    }

    public java.lang.Object
    clone()
    {
        java.lang.Object o = null;
        try
        {
            o = super.clone();
        }
        catch(CloneNotSupportedException ex)
        {
            assert false; // impossible
        }
        return o;
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeString(msgType);
        __os.writeString(sendType);
        __os.writeString(privileage);
        __os.writeString(retryTimes);
        __os.writeString(delayTime);
        __os.writeString(beginTime);
        __os.writeString(endTime);
        __os.writeString(adTopic);
        __os.writeString(msgContent);
        __os.writeString(stayTime);
        __os.writeString(tipPos);
        __os.writeString(tipStyle);
    }

    public void
    __read(IceInternal.BasicStream __is)
    {
        msgType = __is.readString();
        sendType = __is.readString();
        privileage = __is.readString();
        retryTimes = __is.readString();
        delayTime = __is.readString();
        beginTime = __is.readString();
        endTime = __is.readString();
        adTopic = __is.readString();
        msgContent = __is.readString();
        stayTime = __is.readString();
        tipPos = __is.readString();
        tipStyle = __is.readString();
    }
}
