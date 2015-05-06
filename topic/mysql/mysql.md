

##### mysql编码

###### jdbc连接编码

    jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8

###### 修改编码

    show variables like 'character%';

通过my.cnf文件增加两个参数：

    1.在[mysqld]下添加
    default-character-set=utf8（mysql 5.5 版本添加character-set-server=utf8）
    2.在[client]下添加
    default-character-set=utf8

其中 character_set_database 需要命令修改数据库或者表的默认编码

    alter database test default character set = utf8;

    alter table users default character set = utf8;


    
