'''
Created on 2011-3-9

@author: xiaodu
'''

def get_taile():
    file=open('/home/admin/logs/monitor/monitor-app-org.jboss.mx.loading.UnifiedClassLoader3.log')
    content = file.read()
    m={}
    for l in content.split('\02'):
        for k in l.split('\01'):
            m[k]=1
            break
    for i in m:
        print i  
    

if __name__ == '__main__':
    get_taile()