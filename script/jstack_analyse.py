#!/usr/bin/python
# -*- coding: utf-8 -*-
'''
Created on 2011-3-7
jstack 
need admin
@author: xiaodu
'''
import os
import time
import string

class DataStruct:
    def __init__(self,key,value):
        self.key=key
        self.value=value
    def printString(self):
        print 'count:'+str(self.value)+'\n',self.key
def sortfunc(x,y):
        return cmp(y.value,x.value)

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
        raise  Exception(result[2])
    else:
        return string.replace(result[1],'\n','')

def get_jstackMessage(pid):
     jstack = "/opt/taobao/java/bin/jstack"
     if os.path.exists(jstack) is False:
         jstack = 'jstack'
     result = exec_cmd("%s %s"%(jstack,pid))
     #print result
     if result[0]>0:
         raise Exception(result[2])
     else:
         return result[1]
def analyse_jstackMessage(results):
    threadMap = {}
    threadStack = ''
    threadState = ''
    for result in results:        
        for thr in result.split('\n'):
            if thr.find('\"') != -1:#thread start string
                if len(threadStack) >2:
                    if threadMap.has_key(threadState):                        
                       pass
                    else:
                        threadMap[threadState]={}
                    if len(threadState) >2:
                        threadMap[threadState][threadStack]=threadMap[threadState].get(threadStack,0)+1   
                threadStack = ''
                threadState = ''                         
                continue
            else:
                if thr.find('java.lang.Thread.State') !=-1:#thread state
                    threadState=thr
                else:
                    threadStack=threadStack+thr+'\n'
    return threadMap
def show_message(threadMap):
    for state in threadMap:
        items=[]
        for k in threadMap[state]:
            item= DataStruct(k,threadMap[state][k])
            items.append(item)
        items.sort(sortfunc)
        if len(items) >0:
            print 'Thread state:-----------------'+state+'--------------------'
            for item in items[:3] :
                item.printString()
                
     
def dump_jstack():
    frequency = 6;
    interval = 0.5;
    results=[]
    try:
        
        pid = get_appPid()
        print 'pid:'+pid
        while frequency>0:
            frequency=frequency-1
            results.append(get_jstackMessage(pid))
            time.sleep(interval)
        threadMap = analyse_jstackMessage(results)
        show_message(threadMap)
    except Exception,e:
        print e,'excute error'
        pass
             
 
if __name__ == '__main__':
    dump_jstack()
