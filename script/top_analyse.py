'''
Created on 2011-3-8
top message
@author: xiaodu
'''
import os
import re
def average_array(a):
    if len(a) <=0:
        return 'error'
    aver=0
    for item in a:
        aver=aver+item
    return str(aver/len(a))
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
def top_analyse():
    results = exec_cmd("top -n 3 -b -i")
    if results[0]>0:
        print 'top get error:'+results[2]
        return
    result = results[1]   
    load=[]
    cpu=[]
    swap=[]
    for t in result.split('\n'):
        if t.find('top') >-1:
            m = re.search(r"load average:\s+([\d\.]+),\s+([\d\.]+),\s+([\d\.]+)", t)
            if m:
                load.append(float(m.group(1)))
            continue
        if t.find('Cpu')>-1:
            c = re.search(r"\s+([\d\.]+)%id,",t)
            if c:
                cpu.append(100-float(c.group(1)))
            continue
        if t.find('Swap')>-1:
            s = re.search(r"(\d+)k\s+total,\s+(\d+)k\s+used",t)
            if s:
                swap.append((float(s.group(2))/float(s.group(1)))*100)
            continue
    print '----------------------------------------------------'
    print '|  load:'+average_array(load)+"  cpu:"+average_array(cpu)+"%  swap:"+average_array(swap)+"%"
    print '----------------------------------------------------'     
    
if __name__ == '__main__':
    #fileHandle = open('D:\\taobao-SVNROOT\\coremonitor\\monitorstat-analyse\\target\\gg')
    #top_analyse(fileHandle.read())    
    top_analyse()
    
    
