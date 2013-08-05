'''
Created on 2011-3-23

@author: xiaodu
'''
import os
import time
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
def sortfunc(x,y):
        return cmp(y[1],x[1])
def analyseApache():
    filePathTime=time.strftime("/%Y/%m/%Y-%m-%d")
    apachePath = "/home/admin/cai/logs/cronolog"+filePathTime+"-taobao-access_log"
    fileHandle=open(apachePath,"r")
    linelist = fileHandle.readlines();
    refMap = {}
    all_v = 0
    for line in linelist:        
        all_v=all_v+1
        result = line.split(" ")
        ref = result[9]
        url = ref.split("?")
        if len(url)==2:
            ref=url[0]
        refMap[ref] = refMap.get(ref,0)+1
    items = refMap.items()
    items.sort(sortfunc)
    for item in items[:10] :
        print item[0]+" "+str(item[1])+ " %.2f"%((item[1]*100.0/(all_v)))+"%"
    

if __name__ == '__main__':
    analyseApache()