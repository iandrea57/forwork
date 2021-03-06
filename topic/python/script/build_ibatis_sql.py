#! /usr/bin/env python
# -*- encoding:utf-8 -*-

import re
import MySQLdb


def cols_and_coltype_dict_from_create_sql(create_sql):
    p_cols_types = re.compile(r'`(.+?)` ([a-z]+?)(\([0-9,]+\))? .*,')
    items = p_cols_types.findall(create_sql)
    cols = []
    coltype_dict = {}
    for item in items:
        col = item[0]
        if col not in cols:
            cols.append(col)
            coltype_dict[col] = item[1]
    return (cols, coltype_dict)


def table_from_create_sql(create_sql):
    p_table = re.compile(r'CREATE TABLE `(.+?)`')
    table = p_table.findall(create_sql)[0]
    return table


def col_variable_dict_from_cols(cols):
    col_variable_dict = {}
    for underline in cols:
        hump = ''
        need_hump = False
        for ch in underline:
            if ch == '_':
                need_hump = True
                continue
            if need_hump:
                ch = ch.upper()
                need_hump = False
            hump += ch
        col_variable_dict[underline] = hump
    return col_variable_dict


def dao_from_table_cols_variable(table, cols, col_variable_dict):
    classname_and_objname = classname_and_objname_from_table(table)
    ignore_for_create = ['id', 'last_update_time']
    dao = ''
    dao += '\n    public static final String TABLE = "%s";' % table
    dao += '\n\n    public static final String COL_ALL = " '
    first = True
    for col in cols:
        if first:
            prefix = ''
            first = False
        else:
            prefix = ', '
        dao += prefix + col
    dao += ' ";'

    next_with_preifx = '\n                    + '
    default_variable_dict_for_create = {'createTime' : 'now()'}
    dao += '\n\n    @Insert(""'
    dao += next_with_preifx + '" insert into "'
    dao += next_with_preifx + 'TABLE'
    dao += next_with_preifx + '" set "'
    first = True
    for col in cols:
        if col not in ignore_for_create:
            if first:
                first = False
            else:
                dao += ', "'
            dao += next_with_preifx + '" %s = %s' % (col, mybatis_replace(col_variable_dict[col], default_variable_dict_for_create))
    dao += ' "'
    dao += next_with_preifx + '"")'
    dao += '\n    public void insert(%s %s);' % classname_and_objname

    ignore_for_update = ['id', 'create_time', 'last_update_time']
    dao += '\n\n    @Update(""'
    dao += next_with_preifx + '" update "'
    dao += next_with_preifx + 'TABLE'
    dao += next_with_preifx + '" set "'
    first = True
    for col in cols:
        if col not in ignore_for_update:
            if first:
                first = False
            else:
                dao += ', "'
            dao += next_with_preifx + '" %s = %s' % (col, mybatis_replace(col_variable_dict[col]))
    dao += ' "'
    dao += next_with_preifx + '" where "'
    dao += next_with_preifx + '" id = #{id} "'
    dao += next_with_preifx + '"")'
    dao += '\n    public void update(%s %s);' % classname_and_objname
    return dao


def classname_and_objname_from_table(table):
    first = True
    need_hump = False
    classname = ''
    objname = ''
    for ch in table:
        if ch == '_':
            need_hump = True
            continue
        if need_hump:
            need_hump = False
            ch = ch.upper()
        objname += ch
        if first:
            classname += ch.upper()
            first = False
        else:
            classname += ch
    return (classname, objname)


def mybatis_replace(variable, default_dict = {}):
    if not default_dict.has_key(variable):
        return '#{%s}' % variable
    else:
        return default_dict[variable]


def javaclass_from_table_cols_type_variable(table, cols, col_variable_dict, coltype_dict):
    classname_and_objname = classname_and_objname_from_table(table)
    javaclass = ''
    javaclass += 'public class %s {' % classname_and_objname[0]
    for col in cols:
        javaclass += '\n\n    private %s %s;' % (mysqltype_javatype_dict[coltype_dict[col]], col_variable_dict[col])
    javaclass += '\n\n}'
    return javaclass


def deal_create_sql(create_sql):
    cols_and_coltype_dict = cols_and_coltype_dict_from_create_sql(create_sql)
    cols = cols_and_coltype_dict[0]
    coltype_dict = cols_and_coltype_dict[1]
    col_variable_dict = col_variable_dict_from_cols(cols)

    table = table_from_create_sql(create_sql)

    dao = dao_from_table_cols_variable(table, cols, col_variable_dict)
    print '%s\n\n\n\n' % dao

    javaclass = javaclass_from_table_cols_type_variable(table, cols, col_variable_dict, coltype_dict)
    print '%s\n\n\n\n\n\n' % javaclass

def deal_mysql(host='localhost',port=3306,user='root',passwd='',db='ara',table=None):
    try:
        conn=MySQLdb.connect(host=host,user=user,passwd=passwd,db=db,port=port)
        cur=conn.cursor()
        tables = []
        if table == None:
            cur.execute('show tables')
            items =  cur.fetchall()
            for item in items:
                tables.append(item[0])
        else:
            tables.append(table)

        for t in tables:
            cur.execute('show create table %s' % t)
            create_sql = cur.fetchall()[0][1]
            print_sql_create(create_sql)
            deal_create_sql(create_sql)

        cur.close()
        conn.close()
    except MySQLdb.Error,e:
         print "Mysql Error %d: %s" % (e.args[0], e.args[1])


def print_sql_create(create_sql):
    to_print = ''
    to_print += '\n    /**'
    to_print += '\n     * <pre>'
    to_print += '\n     * ' + create_sql.replace('\n' , '\n     * ')
    to_print += '\n     * </pre>'
    to_print += '\n     */'
    print '\n\n%s\n\n' % to_print


mysqltype_javatype_dict = {
    'tinyint' : 'int',
    'int' : 'int',
    'bigint' : 'long',
    'timestamp' : 'Date',
    'varchar' : 'String',
    'decimal' : 'BigDecimal',
    'char' : 'String',
    'double' : 'double',
    'text' : 'String',
    'date' : 'Date'
}

def test_mysql(host='localhost',port=3306,user='root',passwd='',db='ara',table=None):
    try:
        conn=MySQLdb.connect(host=host,user=user,passwd=passwd,db=db,port=port)
        cur=conn.cursor()
        sql = 'select id, order_id, property_id from invest_order_property'
        cur.execute(sql)
        result = cur.fetchall()
        r_count_map = {}
        r_id_map = {}
        delete_ids = []
        for r in result:
            rid = r[0]
            order_id = r[1]
            property_id = r[2]
            key = str(r[1]) + '-' + str(r[2])
            count = r_count_map.get(key, 0)
            count += 1
            r_count_map[key] = count
            if count > 1:
                delete_ids.append(str(r_id_map[key]))
            else:
                r_id_map[key] = rid 
            

        order_ids = []
        for k,v in r_count_map.items():
            if v > 1:
                print k, v
                order_ids.append(k.split('-')[0])

        in_sql = '( ' + ', '.join(order_ids) + ' )'
        select_sql = 'select id, account_id, product_id, direction, order_type from invest_order where id in ' + in_sql
        cur.execute(select_sql)
        select_result = cur.fetchall()
        for r in select_result:
            print r

        delete_sql = 'delete from invest_order_property where id in ( ' + ', '.join(delete_ids) + ' )'
        cur.execute(delete_sql)
        conn.commit()

        cur.close()
        conn.close()
    except MySQLdb.Error,e:
         print "Mysql Error %d: %s" % (e.args[0], e.args[1])


def test_mysql_2(host='localhost',port=3306,user='root',passwd='',db='ara',table=None):
    try:
        conn=MySQLdb.connect(host=host,user=user,passwd=passwd,db=db,port=port)
        cur=conn.cursor()
        sql = 'select i.order_id from invest_order_property i, invest_order o where i.order_id = o.id and o.direction = 1 group by i.order_id having count(i.property_id) > 1'
        cur.execute(sql)
        result = cur.fetchall()
        r_count_map = {}
        r_id_map = {}
        delete_ids = []
        for r in result:
            print r
            r_sql = 'select id, order_id, property_id from invest_order_property where order_id = %d ' % r[0]
            cur.execute(r_sql)
            r_result = cur.fetchall()
            count = len(r_result)
            print count
            print r_result
            for i in range(0, count - 1):
                r_r = r_result[i]
                delete_sql = 'delete from invest_order_property where id = %d ' % r_r[0]
                cur.execute(delete_sql)
                delete_sql = 'delete from invest_order where id = %d ' % r_r[1]
                cur.execute(delete_sql)
                delete_sql = 'delete from invest_property where id = %d ' % r_r[2]
                cur.execute(delete_sql)
                conn.commit()

        cur.close()
        conn.close()
    except MySQLdb.Error,e:
         print "Mysql Error %d: %s" % (e.args[0], e.args[1])

if __name__ == '__main__':
    # deal_mysql(host='10.4.32.131',port=3306,user='tango_master',passwd='nidaye',db='waltz_base',table='invest_order')
    # test_mysql(host='10.4.32.131',port=3306,user='tango_master',passwd='nidaye',db='waltz_base',table='invest_order')
    test_mysql_2(host='10.4.32.131',port=3306,user='tango_master',passwd='nidaye',db='waltz_base',table='invest_order')
