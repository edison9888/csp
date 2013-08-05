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

public final class WWMessageInterfacePrxHelper extends Ice.ObjectPrxHelperBase implements WWMessageInterfacePrx
{
    public int
    SendNotifyMessage(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage)
    {
        return SendNotifyMessage(strCaller, strServiceType, strToId, strMessage, saveType, strRetMessage, null, false);
    }

    public int
    SendNotifyMessage(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx)
    {
        return SendNotifyMessage(strCaller, strServiceType, strToId, strMessage, saveType, strRetMessage, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    SendNotifyMessage(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("SendNotifyMessage");
                __delBase = __getDelegate(false);
                _WWMessageInterfaceDel __del = (_WWMessageInterfaceDel)__delBase;
                return __del.SendNotifyMessage(strCaller, strServiceType, strToId, strMessage, saveType, strRetMessage, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public int
    SendNotifyMessageEx(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, java.util.Map<java.lang.String, java.lang.String> htmlMsg, Ice.StringHolder strRetMessage)
    {
        return SendNotifyMessageEx(strCaller, strServiceType, strToId, strMessage, saveType, htmlMsg, strRetMessage, null, false);
    }

    public int
    SendNotifyMessageEx(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, java.util.Map<java.lang.String, java.lang.String> htmlMsg, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx)
    {
        return SendNotifyMessageEx(strCaller, strServiceType, strToId, strMessage, saveType, htmlMsg, strRetMessage, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    SendNotifyMessageEx(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, java.util.Map<java.lang.String, java.lang.String> htmlMsg, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("SendNotifyMessageEx");
                __delBase = __getDelegate(false);
                _WWMessageInterfaceDel __del = (_WWMessageInterfaceDel)__delBase;
                return __del.SendNotifyMessageEx(strCaller, strServiceType, strToId, strMessage, saveType, htmlMsg, strRetMessage, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public int
    SendWWMessage(String strCaller, String strServiceType, String strFromId, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage)
    {
        return SendWWMessage(strCaller, strServiceType, strFromId, strToId, strMessage, saveType, strRetMessage, null, false);
    }

    public int
    SendWWMessage(String strCaller, String strServiceType, String strFromId, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx)
    {
        return SendWWMessage(strCaller, strServiceType, strFromId, strToId, strMessage, saveType, strRetMessage, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    SendWWMessage(String strCaller, String strServiceType, String strFromId, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("SendWWMessage");
                __delBase = __getDelegate(false);
                _WWMessageInterfaceDel __del = (_WWMessageInterfaceDel)__delBase;
                return __del.SendWWMessage(strCaller, strServiceType, strFromId, strToId, strMessage, saveType, strRetMessage, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public static WWMessageInterfacePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        WWMessageInterfacePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (WWMessageInterfacePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::AliIMInterface::WWMessageInterface"))
                {
                    WWMessageInterfacePrxHelper __h = new WWMessageInterfacePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static WWMessageInterfacePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        WWMessageInterfacePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (WWMessageInterfacePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::AliIMInterface::WWMessageInterface", __ctx))
                {
                    WWMessageInterfacePrxHelper __h = new WWMessageInterfacePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static WWMessageInterfacePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        WWMessageInterfacePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::AliIMInterface::WWMessageInterface"))
                {
                    WWMessageInterfacePrxHelper __h = new WWMessageInterfacePrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static WWMessageInterfacePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        WWMessageInterfacePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::AliIMInterface::WWMessageInterface", __ctx))
                {
                    WWMessageInterfacePrxHelper __h = new WWMessageInterfacePrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static WWMessageInterfacePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        WWMessageInterfacePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (WWMessageInterfacePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                WWMessageInterfacePrxHelper __h = new WWMessageInterfacePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static WWMessageInterfacePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        WWMessageInterfacePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            WWMessageInterfacePrxHelper __h = new WWMessageInterfacePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _WWMessageInterfaceDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _WWMessageInterfaceDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, WWMessageInterfacePrx v)
    {
        __os.writeProxy(v);
    }

    public static WWMessageInterfacePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            WWMessageInterfacePrxHelper result = new WWMessageInterfacePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
