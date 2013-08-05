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

public final class WWMessageInterfaceHolder
{
    public
    WWMessageInterfaceHolder()
    {
    }

    public
    WWMessageInterfaceHolder(WWMessageInterface value)
    {
        this.value = value;
    }

    public class Patcher implements IceInternal.Patcher
    {
        public void
        patch(Ice.Object v)
        {
            try
            {
                value = (WWMessageInterface)v;
            }
            catch(ClassCastException ex)
            {
                IceInternal.Ex.throwUOE(type(), v.ice_id());
            }
        }

        public String
        type()
        {
            return "::AliIMInterface::WWMessageInterface";
        }
    }

    public Patcher
    getPatcher()
    {
        return new Patcher();
    }

    public WWMessageInterface value;
}
