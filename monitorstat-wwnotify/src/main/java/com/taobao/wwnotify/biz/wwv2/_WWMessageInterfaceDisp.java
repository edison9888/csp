// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package com.taobao.wwnotify.biz.wwv2;

public abstract class _WWMessageInterfaceDisp extends Ice.ObjectImpl implements WWMessageInterface
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::AliIMInterface::WWMessageInterface",
        "::Ice::Object"
    };

    public boolean
    ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean
    ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[]
    ice_ids()
    {
        return __ids;
    }

    public String[]
    ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String
    ice_id()
    {
        return __ids[0];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[0];
    }

    public static String
    ice_staticId()
    {
        return __ids[0];
    }

    public final int
    SendNotifyMessage(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage)
    {
        return SendNotifyMessage(strCaller, strServiceType, strToId, strMessage, saveType, strRetMessage, null);
    }

    public final int
    SendNotifyMessageEx(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, java.util.Map<java.lang.String, java.lang.String> htmlMsg, Ice.StringHolder strRetMessage)
    {
        return SendNotifyMessageEx(strCaller, strServiceType, strToId, strMessage, saveType, htmlMsg, strRetMessage, null);
    }

    public final int
    SendWWMessage(String strCaller, String strServiceType, String strFromId, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage)
    {
        return SendWWMessage(strCaller, strServiceType, strFromId, strToId, strMessage, saveType, strRetMessage, null);
    }

    public static Ice.DispatchStatus
    ___SendWWMessage(WWMessageInterface __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String strCaller;
        strCaller = __is.readString();
        String strServiceType;
        strServiceType = __is.readString();
        String strFromId;
        strFromId = __is.readString();
        String strToId;
        strToId = __is.readString();
        String strMessage;
        strMessage = __is.readString();
        int saveType;
        saveType = __is.readInt();
        __is.endReadEncaps();
        Ice.StringHolder strRetMessage = new Ice.StringHolder();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.SendWWMessage(strCaller, strServiceType, strFromId, strToId, strMessage, saveType, strRetMessage, __current);
        __os.writeString(strRetMessage.value);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___SendNotifyMessage(WWMessageInterface __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String strCaller;
        strCaller = __is.readString();
        String strServiceType;
        strServiceType = __is.readString();
        String strToId;
        strToId = __is.readString();
        String strMessage;
        strMessage = __is.readString();
        int saveType;
        saveType = __is.readInt();
        __is.endReadEncaps();
        Ice.StringHolder strRetMessage = new Ice.StringHolder();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.SendNotifyMessage(strCaller, strServiceType, strToId, strMessage, saveType, strRetMessage, __current);
        __os.writeString(strRetMessage.value);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___SendNotifyMessageEx(WWMessageInterface __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String strCaller;
        strCaller = __is.readString();
        String strServiceType;
        strServiceType = __is.readString();
        String strToId;
        strToId = __is.readString();
        String strMessage;
        strMessage = __is.readString();
        int saveType;
        saveType = __is.readInt();
        java.util.Map<java.lang.String, java.lang.String> htmlMsg;
        htmlMsg = HtmlMsgMapHelper.read(__is);
        __is.endReadEncaps();
        Ice.StringHolder strRetMessage = new Ice.StringHolder();
        IceInternal.BasicStream __os = __inS.os();
        int __ret = __obj.SendNotifyMessageEx(strCaller, strServiceType, strToId, strMessage, saveType, htmlMsg, strRetMessage, __current);
        __os.writeString(strRetMessage.value);
        __os.writeInt(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "SendNotifyMessage",
        "SendNotifyMessageEx",
        "SendWWMessage",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping"
    };

    public Ice.DispatchStatus
    __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return ___SendNotifyMessage(this, in, __current);
            }
            case 1:
            {
                return ___SendNotifyMessageEx(this, in, __current);
            }
            case 2:
            {
                return ___SendWWMessage(this, in, __current);
            }
            case 3:
            {
                return ___ice_id(this, in, __current);
            }
            case 4:
            {
                return ___ice_ids(this, in, __current);
            }
            case 5:
            {
                return ___ice_isA(this, in, __current);
            }
            case 6:
            {
                return ___ice_ping(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.endWriteSlice();
        super.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is, boolean __rid)
    {
        if(__rid)
        {
            __is.readTypeId();
        }
        __is.startReadSlice();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type AliIMInterface::WWMessageInterface was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type AliIMInterface::WWMessageInterface was not generated with stream support";
        throw ex;
    }
}
