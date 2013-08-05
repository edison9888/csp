#!/usr/bin/python
# -*- coding: utf-8 -*-
'''
Created on 2011-3-7
jmap  messge 
need admin
@author: xiaodu
'''
import os
import string
def exec_cmd(cmd):
    f = os.popen3(cmd)
    try:
        out = f[1].read()
        err = f[2].read()
        return (len(err),out, err)
    finally:
        f[0].close()
        f[1].close()
        f[2].close()
def get_appPid():
    ps="ps -ef|grep java|grep -v grep|grep hsf-tomcat| awk '{print $2}'"
    result = exec_cmd(ps)
    #print replace_str(result[1],'\n','')
    #print '-------------------'
    if result[0]>0:
        return None
    else:
        return string.replace(result[1], '\n', '')

def get_jmap():
    pid=get_appPid()
    if pid is not None:
        jmap='/opt/taobao/java/bin/jmap'
        if os.path.exists(jmap) is False:
            jmap = 'jmap'
        result = exec_cmd("%s -histo:live %s|head -60"%(jmap,pid))
        if result[0]>0:
            return result[2]
        else:
            return result[1]
    return None

if __name__ == '__main__':
    jmap_message = get_jmap()
    if jmap_message is not None:
        print jmap_message
    else:
        print 'excute jmap error'
    

        
    
    
