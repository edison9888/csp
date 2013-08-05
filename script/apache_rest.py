#!/usr/bin/python
# -*- coding: utf-8 -*-
'''
Created on 2011-3-2
查找出在某分钟内的响应时间最多的前5个url
@author: xiaodu
'''
import sys
import time

class ApacheUrlStatus:
    def __init__(self,url):
        self.url=url
        self.number=0
        self.myrest=0
        self.codeMap={}
        self.pagesize=0
    def sumOne(self):
        self.number=self.number+1
    def sumRest(self,rest):
        if rest.isdigit() == True:
            self.myrest=self.myrest+int(rest)
    def sumStat(self,statCode):
        if statCode.isdigit() == True:
            self.codeMap[statCode]=self.codeMap.get(statCode,0)+1
    def sumPageSize(self,size):
        if size.isdigit() == True:
            self.pagesize=self.pagesize+int(size)
        
    def printString(self):
        print self.url+":"
        print "          all visit number: %s"%(self.number)
        print "          average response time: %.2f ms"%(self.myrest/self.number/1000)
        print "          average page size: %.2f btye"%(self.pagesize/self.number)
        for c in self.codeMap:
            print "          http"+c+"state :"+str(self.codeMap[c])

def sortfunc(x,y):
    return cmp(y.myrest/y.number,x.myrest/x.number)
    
def sortMapValuefunc(mapArg):
    items=[]
    for k in mapArg:
        if mapArg[k].number >=5:
            items.append(mapArg[k])
    items.sort(sortfunc)
    for item in items[:5] :
        item.printString()

def analyseApache(timeArg):
    filePathTime=formatDateTime(timeArg,"/%Y/%m/%Y-%m-%d")
    findLineTime=formatDateTime(timeArg,":%H:%M:") 
    fileHandle=open("/home/admin/cai/logs/cronolog"+filePathTime+"-taobao-access_log","r")
    linelist = fileHandle.readlines();
    urlStatusMap={}
    for line in linelist:
        if line.find(findLineTime)>0:
            result = line.split("\"")
            rest=result[0]
            url=result[1]
            stat_str=result[2]
            urlShortStr = url.split("?")
            r_result = rest.split(" ")
            s_result = stat_str.strip().split(" ")
            if urlStatusMap.has_key(urlShortStr[0]):
                item=urlStatusMap[urlShortStr[0]]
            else:
                item=ApacheUrlStatus(urlShortStr[0])
                urlStatusMap[urlShortStr[0]]=item
            item=urlStatusMap.get(urlShortStr[0])
            item.sumOne()
            item.sumRest(r_result[1])
            if len(s_result) == 2:
                item.sumStat(s_result[0])
                item.sumPageSize(s_result[1])
    fileHandle.close()
    sortMapValuefunc(urlStatusMap)
    
    
def formatDateTime(timeArg,newformat):
        tmpTime = time.strptime(timeArg,"%Y%m%d%H%M")
        return time.strftime(newformat,tmpTime)
if __name__ == "__main__":
        if len(sys.argv) !=2:
                print "need datatime format:yyyyMMddHHmm"
        else:
                analyseApache(sys.argv[1])