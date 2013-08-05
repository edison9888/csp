#!/usr/bin/python
# -*- coding: utf-8 -*-
'''
Created on 2011-2-9
获取apache 在某分钟呢的一个ip 和url的前5名，访问情况
@author: xiaodu
'''
import sys
import time

class DataStruct:
        def __init__(self,key,value):
                self.key=key
                self.value=value
        def printStruct(self):
                print self.key,self.value



def formatDateTime(timeArg,newformat):
        tmpTime = time.strptime(timeArg,"%Y%m%d%H%M")
        return time.strftime(newformat,tmpTime)
def sortfunc(x,y):
        return cmp(y.value,x.value)

def sortMapValuefunc(mapArg):
        items=[]
        for k in mapArg:
                item= DataStruct(k,mapArg[k])
                items.append(item)
        items.sort(sortfunc)
        for item in items[:5] :
                item.printStruct()


def analyseApacheUrl(timeArg):
        filePathTime=formatDateTime(timeArg,"/%Y/%m/%Y-%m-%d")
        findLineTime=formatDateTime(timeArg,":%H:%M:") 
        ipMap={}
        urlShortMap={}
        fileHandle=open("/home/admin/cai/logs/cronolog"+filePathTime+"-taobao-access_log","r")
        linelist = fileHandle.readlines();
        for line in linelist:
                if line.find(findLineTime)>0:
                        result = line.split(" ")
                        ipMap[result[0]]=ipMap.get(result[0],0)+1                        
                        url = result[6]
                        urlShortStr = url.split("?")
                        urlShortMap[urlShortStr[0]]=urlShortMap.get(urlShortStr[0],0)+1                        
        fileHandle.close()
        sortMapValuefunc(ipMap)
        sortMapValuefunc(urlShortMap)


if __name__ == "__main__":
        if len(sys.argv) !=2:
                print "need datatime format:yyyyMMddHHmm"
        else:
                analyseApacheUrl(sys.argv[1])