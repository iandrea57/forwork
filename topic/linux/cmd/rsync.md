
##### rsync --daemon

    [root@YZSJHL27-71 10.2.23.234-backup]# cat rsyncd/rsyncd.conf 
    uid = root
    gid = root
    log file=/data11/10.2.23.234-backup/rsyncd/rsyncd.log
    pid file=/data11/10.2.23.234-backup/rsyncd/rsyncd.pid
    lock file=/data11/10.2.23.234-backup/rsyncd/rsyncd.lock
    read only = no
    [data11]
    path = /data11

    rsync --daemon --config=/data11/10.2.23.234-backup/rsyncd/rsyncd.conf 

from root@10.2.23.234:/data

    rsync -ztrvl * root@10.4.27.71::data11/10.2.23.234-backup/data/