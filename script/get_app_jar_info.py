'''
Created on 2011-5-5

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
def getTime(p):
    tmp = p.split(" ")
    w = tmp[0]
    m = tmp[1]
    d = tmp[2]
    time = tmp[3]
    y = tmp[4]
    cal = y+"/"+m+"/"+d
    return cal+" "+time;
def getJbossTime():
    comm = "ps -e -o lstart,cmd |grep -w java|grep -v grep|grep 'jboss.server.home.dir\\=/home/admin/\\w*/.default'"
    result = exec_cmd(comm)
    if result[0] >0 :
        return None
    else:       
        return result[1]
def getApacheTime():
    comm = "ps -e -o lstart,cmd |grep -w 'httpd'|grep -v grep"
    result = exec_cmd(comm)
    if result[0] >0 :
        return None
    else:     
        return result[1]
    
            

def getAppName():
    comm = "ps -e -o lstart,cmd |grep -w java|grep -v grep|grep 'jboss.server.home.dir\\=/home/admin/\\w*/.default'"
    result = exec_cmd(comm)
    if result[0] >0 :
        return None
    else:
        m = re.search(r"jboss.server.home.dir\=/home/admin/(\w*)/.default", result[1])
        if m:
            return m.group(1)
        return None    

        
        

def getJars(appName):
    path = "/home/admin/"+appName+"/.default/deploy/"
    listFile(path)
            
            
            
def listFile(path):
    files = os.listdir(path)
    for file in files:
        if file.find("ear") >-1 or file.find("war") >-1:
            if os.path.isdir(path+"/"+file):
                listFile(path+"/"+file)
        if file.find("WEB-INF")>-1:
            if os.path.isdir(path+"/"+file+"/lib"):
                libs = os.listdir(path+"/"+file+"/lib");
                for lib in libs:
                    if lib.find("jar")>-1:
                        s = os.stat(path+"/"+file+"/lib/"+lib);
                        print lib+","+str(s.st_size)+","+time.ctime(s.st_mtime)
            print "\02"
            if os.path.isfile(path+"/"+file+"/web.xml"):
                fileHandle=open(path+"/"+file+"/web.xml","r")
                linelist = fileHandle.readlines();
                for line in linelist:
                    print line
                fileHandle.close()
            print "\02"
            if os.path.isfile(path+"/"+file+"/webx.xml"):
                fileHandle=open(path+"/"+file+"/webx.xml","r")
                linelist = fileHandle.readlines();
                for line in linelist:
                    print line
                fileHandle.close()
        
                
                
        
    



if __name__ == '__main__':
    appname=getAppName()
    if appname:        
        print "appName:"+appname
        print "\02"
        print getJbossTime()
        print "\02"
        print getApacheTime()
        print "\02"
        getJars(appname)
    
    