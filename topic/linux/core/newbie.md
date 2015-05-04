

### http://kernelnewbies.org/


####IRC

Start your favorite IRC client

* /server irc.oftc.net

* /join #kernelnewbies

* Ask what you want to know without asking permission to ask and keep the discussion ontopic (ie about kernel programming).

Alternatively try this URL: irc://irc.oftc.net/kernelnewbies (should work with many browser / IRC client combinations)



### http://kernelnewbies.org/KernelJanitors


### Hacking
#### http://kernelnewbies.org/FirstKernelPatch

###### Install some packages

    sudo apt-get install vim libncurses5-dev gcc make git exuberant-ctags
    
###### Setup your Linux kernel code repository

    mkdir -p git/kernels; cd git/kernels

    git clone -b staging-next git://git.kernel.org/pub/scm/linux/kernel/git/gregkh/staging.git

    cd staging

###### Setting up your kernel configuration

    cp /boot/config-`uname -r`* .config
    
    make olddefconfig
    
If you need to make changes, you can run:

    make menuconfig
    
###### Building the kernel

    make

    sudo make modules_install install
    
    sudo vim /etc/default/grub
    GRUB_TIMEOUT=10
    
    sudo update-grub2

###### Email software

    sudo apt-get install esmtp
    
    sudo apt-get install mutt
    
    sudo apt-get install git-email
    
###### Optional tools

    sudo apt-get install gitk
    
###### Make ctags
    
    make tags




### Linux Device Drivers, Third Edition
#### http://lwn.net/Kernel/LDD3/