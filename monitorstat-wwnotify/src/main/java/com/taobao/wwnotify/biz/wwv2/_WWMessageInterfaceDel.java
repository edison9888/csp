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

public interface _WWMessageInterfaceDel extends Ice._ObjectDel
{
    int SendWWMessage(String strCaller, String strServiceType, String strFromId, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int SendNotifyMessage(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int SendNotifyMessageEx(String strCaller, String strServiceType, String strToId, String strMessage, int saveType, java.util.Map<java.lang.String, java.lang.String> htmlMsg, Ice.StringHolder strRetMessage, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
