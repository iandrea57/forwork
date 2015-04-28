

#### 编码
##### 修改系统默认编码

    vi /etc/sysconfig/i18n
        LANG="en_US.UTF-8"

    vi /etc/profile
        export LC_ALL="zh_CN.UTF-8"
        export LANG="zh_CN.UTF-8"