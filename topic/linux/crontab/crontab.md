

##### crontab执行日志
    /var/log/cron

##### crontab错误排查


根据上面各位提示的问题我按照顺序做了修改
1.将shell脚本中的相对路径改为绝对路径，直接执行sh没有问题，用crontab -e进行定时处理，仍然无法进行备份。
2.使用mail命令查看root收到的邮件信息，发现里面的数据没有更新过，全部都是在/var/spool/mail的root文件中的邮件信息，cron的log文件里面描述的出错信息的邮件都应该发送到上面说的root文件中，由此判断系统的邮件功能出现异常。
3.cd到/ect/rc.d/init.d目录，使用sendmail命令准备重启一下mail服务，出现与“服务器时间不一致”错误，找资料使用touch /etc/mail/*错误提示消失，有新的错误：
Starting sendmail: 451 4.0.0 /etc/mail/sendmail.cf: line 91: fileclass: cannot open '/etc/mail/local-host-names': World writable directory
451 4.0.0 /etc/mail/sendmail.cf: line 588: fileclass: cannot open '/etc/mail/trusted-users': World writable derectory [FAILED]
而后使用
[root@redhat etc]# chmod go-w / /etc /etc/mail /usr /var /var/spool /var/spool/mqueue
[root@redhat etc]# chown root / /etc /etc/mail /usr /var /var/spool /var/spool/mqueue
再
sendmail restart
mail服务正常启动，这样我就可以看到我的cron日志文件中描述发送的邮件(定时执行shell时产生的错误信息)写到/var/spool/mail的root文件中了。
4.首先邮件信息中提示/bin/sh: root: command not found，找原因
40  18  *  *  *  root  /usr/local/mysql/CollectionDataBackup/CollectionDataBackup_true.sh 中不需要写root
重新使用crontab -e编辑。
5.再次执行，邮件中的提示信息改变为
/usr/local/mysql/CollectionDataBackup/CollectionDataBackup.sh: line 55: mysqldump: command not found
查找原因
mysqldump -u root --default-character-set=utf8 --opt --extended-insert=false --triggers -R --single-transaction DB back$yesterdayTableName ...
这一命令改为./mysqldump   mysql命令前面也要加上./
6.再次定时执行，ok备份成功。。。让系统在服务器上跑着，临时转做加急的项目...
