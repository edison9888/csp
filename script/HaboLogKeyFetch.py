'''
Created on 2011-5-9

@author: xiaodu
'''

import sys
def analyseFile(path):
    file.newlines='\02'
    f=file(path,"r")
    linelist = f.readlines()
    m={}
    for line in linelist:
        str = line.split('\01')
        m[str[0]]
    for k in m:
        print k 
    
        



if __name__ == '__main__':
    analyseFile(sys.argv[1])