'''
Created on 2011-4-2

@author: xiaodu
'''
import os
import re
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
def compress_mysql(path):
    c = checkDate(path)
    if c == True:
        myisampack = 'myisampack '+path
        result = exec_cmd(myisampack)       
        if result[0] == 0 :
            print path+' compress success'
            #print result[1]
            repairTable(path)
        else:
            print path+' compress fail'
            #print result[2]
        
def checkDate(path):
    myTime = time.strftime('%Y%m%d',time.localtime(time.time() - 24*60*60*2) )
    reg = re.search('(\d\d\d\d\d\d\d\d)', path)
    if reg:
        m = reg.group(1)
        if int(m)<=int(myTime):
            return True
        else:
            return False
    else:
        return False
    
def repairTable(path):
    m = re.search('(ms_monitor_data_\\d\\d\\d\\d\\d\\d\\d\\d)', path)
    if m != None:
        tablename = m.group(1)
        repairTableExe = "mysql --default-character-set=gbk monitor_time -e 'repair table "+tablename+"'"
        print repairTableExe
        result = exec_cmd(repairTableExe)
        if result[0] == 0 :
            print tablename+' repair success'
            #print result[1]
        else:
            print tablename+' repair fail'
            #print result[2]
         
        
    
    
        
        
    
def findMysqlMYI():
    ls="ls -Ll /home/data/mysql/monitor_time/ms_monitor_data_2011*.MYI"
    result = exec_cmd(ls)
    myiItem=[]
    if result[0] == 0 :
        myiFiles = result[1].split("\n")
        for myi in myiFiles:
            f = myi.split(" ")
            myiItem.append(f[len(f)-1]) 
        return myiItem
    else:
        print 'can`t get myi files'


if __name__ == '__main__':
    items = findMysqlMYI()
    if items:
        for p in items:
            print p
            compress_mysql(p)
    
