#### root登录

* 开启root账号
 
    sudo passwd root
    
* vi /etc/lightdm/lightdm.conf
     
    [SeatDefaults]   
     
      greeter-session=unity-greeter
      user-session=ubuntu
      greeter-show-manual-login=true  #手工输入登陆系统的用户名和密码
      allow-guest=false  #不允许guest登录