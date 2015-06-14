




##### 外网连

    ssh pi@110.96.190.63



##### Raspberry - MAC上使用USB转串口线连接访问树莓派

1. 先确定是否已经有了驱动。

    ls /dev/tty.usb*

    执行结果如果为空，那说明还没驱动。

2. 安装驱动。

    先去下载：[PL2303 Mac OS X Driver Download](http://www.prolific.com.tw/US/ShowProduct.aspx?p_id=229&pcid=41)

    安装完重启，再次执行ls /dev/tty.usb*，此时应该就有结果了。

3. 使用screen连接pi

    打开终端
    
    检查是否有screen命令：screen -v

    如果提示还没有该命令，安装之：brew install screen

    安装完毕执行：screen /dev/tty.usbserial 115200

    Notice：

    执行完会进入一个空界面，此时按Enter键，就会出现Raspberry Pi的登录提示了。

    Pi的默认用户名是：pi 默认密码是：raspberry

    这个应该是跟所使用的raspberry系统的不同而不同，我的是官网上下的镜像，Linux raspberrypi 3.6.11+
将树莓派进行 shutdown 之后，如果想再次连接，需要将数据线拔掉，然后再插上。此时可以再次连接，否则可能会出现 could not find PTY的错误提示。

    终端退出的时候不会自动断开与树莓派的连接：
    
    这时如果直接拔掉USB串口板，会造成系统重启。需要执行：ps -x|grep tty，得到串口连接的进程号，然后：kill 进程号。

    如果只是不小心给关了，需要再次连接，同样需要kill一下，然后再screen进行连接，否则也可能会出现could not find PTY的错误提示。


![](http://img04.taobaocdn.com/imgextra/i4/406368984/T2lSPNXnxaXXXXXXXX_!!406368984.jpg)

