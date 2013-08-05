'''
Created on 2011-3-9

@author: xiaodu
'''
import os
import time
def average_array(a):
    if len(a) <=0:
        return 'error'
    aver=0
    for item in a:
        aver=aver+item
    return aver/len(a)
def exec_cmd(cmd):
    f = os.popen3(cmd)
    try:
        out = f[1].read()
        err = f[2].read()
        if len(err) >1:
            return None
        else:
            return out
    finally:
        f[0].close()
        f[1].close()
        f[2].close()
def net_stream_analyse():
    in_stream="cat /proc/net/dev | grep eth0 | sed 's=^.*:==' | awk '{ print $1 }'"
    out_stream="cat /proc/net/dev | grep eth0 | sed 's=^.*:==' | awk '{ print $9 }'"
    old_in_stream = exec_cmd(in_stream)
    old_out_stream = exec_cmd(out_stream)
    in_array=[]
    out_array=[]
    size=0
    while size <3:
        time.sleep(1)
        c_net_in = exec_cmd(in_stream)
        c_net_out = exec_cmd(out_stream)
        if c_net_in is not None:
            in_array.append(long(c_net_in)-long(old_in_stream))
        if c_net_out is not None:
            out_array.append(long(c_net_out)-long(old_out_stream))
        old_in_stream = c_net_in
        old_out_stream = c_net_out
        size=size+1
    
    print " IN: %.1f M/s OUT: %.1f M/s "%(average_array(in_array)*8/1024/1024,average_array(out_array)*8/1024/1024)

if __name__ == '__main__':
    net_stream_analyse()