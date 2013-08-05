'''
Created on 2011-4-30

@author: xiaodu
'''
'''
Created on 2011-4-21

@author: xiaodu
'''
import re
import os
   
        
def analysejar(arg,dirname,names):
    for name in names:
        if name.find("jar") >-1:
            print dirname+""+name
        if name == "web.xml":
            print dirname+""+name
def vt100_parse_params(p, d, to_int = True):
    # Process parameters (params p with defaults d)
    # Add prefix to all parameters
    prefix = ''
    if len(p) > 0:
        if p[0] >= '<' and p[0] <= '?':
            prefix = p[0]
            p = p[1:]
        p = p.split(';')
    else:
        p = ''
    # Process parameters
    n = max(len(p), len(d))
    o = []
    for i in range(n):
        value_def = False
        if i < len(p):
            value = prefix + p[i]
            value_def = True
            if to_int:
                try:
                    value = int(value)
                except ValueError:
                    value_def = False
        if (not value_def) and i < len(d):
            value = d[i]
        o.append(value)
    return o
       

# UTF-8 functions
def utf8_decode(d):
    utf8_units_count=0
    utf8_units_received=0
    utf8_char=0
    o = ''
    for c in d:
        char = ord(c)
        if utf8_units_count != utf8_units_received:
            utf8_units_received += 1
            if (char & 0xc0) == 0x80:
                utf8_char = (utf8_char << 6) | (char & 0x3f)
                if utf8_units_count == utf8_units_received:
                    if utf8_char<0x10000:
                        o += unichr(utf8_char)
                    utf8_units_count = utf8_units_received = 0
            else:
                o += '?'
                while utf8_units_received:
                    o += '?'
                    utf8_units_received -= 1
                utf8_units_count = 0
        else:
            if (char & 0x80) == 0x00:
                o += c
            elif (char & 0xe0) == 0xc0:
                utf8_units_count = 1
                utf8_char = char & 0x1f
            elif (char & 0xf0) == 0xe0:
                utf8_units_count = 2
                utf8_char = char & 0x0f
            elif (char & 0xf8) == 0xf0:
                utf8_units_count = 3
                utf8_char = char & 0x07
            else:
                o += '?'
    return o

if __name__ == '__main__':
    print vt100_parse_params("1",[0]);
    print utf8_decode("12!@#$%%^676·Éfff")
    
