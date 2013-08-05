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
def analyseApache():
    filePathTime=time.strftime("/%Y/%m/%Y-%m-%d")        
    findLineTime=time.strftime(":%H:%M:",time.localtime(time.time() - 60))
    apachePath = "/home/admin/cai/logs/cronolog"+filePathTime+"-taobao-access_log"
    grepCpmmand = "grep '"+findLineTime+"' "+apachePath+" |awk '{a[$4]++}END{for(i in a){print i,a[i]}}'"    
    result = exec_cmd(grepCpmmand);    
    if result[0] >0 :
        print "get apache recently visit number error:"+result[2]
    else:
        print "时间"+findLineTime+" 流量"
        print result[1]
        

if __name__ == '__main__':
    analyseApache()