

##### python UTF8 注释

    #! /usr/bin/env python
    # -*- coding: utf-8 -*-


##### python main
    
    if __name__ == "__main__":

##### 正则

    # encoding: UTF-8
    import re
     
    # 将正则表达式编译成Pattern对象
    pattern = re.compile(r'hello')
     
    # 使用Pattern匹配文本，获得匹配结果，无法匹配时将返回None
    match = pattern.match('hello world!')
     
    if match:
        # 使用Match获得分组信息
        print match.group()
     
    ### 输出 ###
    # hello

#### mysql

    import MySQLdb
     
    try:
        conn=MySQLdb.connect(host='localhost',user='root',passwd='root',db='test',port=3306)
        cur=conn.cursor()
        cur.execute('select * from user')
        cur.close()
        conn.close()
    except MySQLdb.Error,e:
         print "Mysql Error %d: %s" % (e.args[0], e.args[1])



#### dict

##### 遍历
    
    for (d,x) in dict.items():
        print "key:"+d+",value:"+str(x)

    for d,x in dict.items():
        print "key:"+d+",value:"+str(x)