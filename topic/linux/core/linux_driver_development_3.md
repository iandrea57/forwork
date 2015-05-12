

##### ch02

The Hello World Module

    #include <linux/init.h>
    #include <linux/module.h>
    MODULE_LICENSE("Dual BSD/GPL");
    static int hello_init(void)
    {
        printk(KERN_ALERT "Hello, world\n");
        return 0; 
    }
    static void hello_exit(void)
    {
        printk(KERN_ALERT "Goodbye, cruel world\n");
    }
    module_init(hello_init);
    module_exit(hello_exit);
    
You can test the module with the insmod and rmmod utilities, as shown below. Note that only the superuser can load and unload a module.

    % make
    make[1]: Entering directory `/usr/src/linux-2.6.10'
        CC [M]  /home/ldd3/src/misc-modules/hello.o
        Building modules, stage 2.
        MODPOST
        CC      /home/ldd3/src/misc-modules/hello.mod.o
        LD [M]  /home/ldd3/src/misc-modules/hello.ko
    make[1]: Leaving directory `/usr/src/linux-2.6.10' 
    % su
    root# insmod ./hello.ko
    Hello, world
    root# rmmod hello
    Goodbye cruel world 
    root#
   
Once you have everything set up, creating a makefile for your module is straightforward. In fact, for the “hello world” example shown earlier in this chapter, a single line will suffice:
    obj-m := hello.o 

If, instead, you have a module called module.ko that is generated from two source files (called, say, file1.c and file2.c), the correct incantation would be:
     obj-m := module.o     module-objs := file1.o file2.o
    
he make command required to build your module (typed in the directory containing the module source and makefile) would be:

    make -C ~/kernel-2.6 M=`pwd` modules
    
The trick is to write your makefile as follows:

     # If KERNELRELEASE is defined, we've been invoked from the
     # kernel build system and can use its language.
     ifneq ($(KERNELRELEASE),)
         obj-m := hello.o
     # Otherwise we were called directly from the command
     # line; invoke the kernel build system.
     else
         KERNELDIR ?= /lib/modules/$(shell uname -r)/build
         PWD  := $(shell pwd)
     default:
         $(MAKE) -C $(KERNELDIR) M=$(PWD) modules
     endif
    
   

If your module needs to export symbols for other modules to use, the following macros should be used.
     EXPORT_SYMBOL(name);     EXPORT_SYMBOL_GPL(name);
     
just about all module code has the following:     #include <linux/module.h>     #include <linux/init.h>
   
**module.h** contains a great many definitions of symbols and functions needed by loadable modules. You need **init.h** to specify your initialization and cleanup functions. Most modules also include **moduleparam.h** to enable the passing of parameters to the module at load time.

    MODULE_LICENSE("GPL");
    MODULE_AUTHOR (stating who wrote the module),
    MODULE_DESCRIPTION (a human-read- able statement of what the module does), 
    MODULE_VERSION (for a code revision num- ber; see the comments in <linux/module.h> for the conventions to use in creating version strings), 
    MODULE_ALIAS (another name by which this module can be known), 
    MODULE_DEVICE_TABLE (to tell user space about which devices the module supports).
    
    
The actual definition of the initialization function always looks like:

    static int __init initialization_function(void) {         /* Initialization code here */     }     module_init(initialization_function);
The **__init** token in the definition is a hint to the kernel that the given function is used only at initialization time. The module loader drops the initialization function after the module is loaded, making its memory available for other uses. There is asimilartag(**__initdata**)for data used only during initialization. Useof \_\_initand \_\_initdata is optional, but it is worth the trouble.
You may also encounter **__devinit** and **__devinitdata** in the kernel source; these trans- late to \_\_init and \_\_initdata only if the kernel has not been configured for hotplug- gable devices. The use of **module_init** is mandatory. This macro adds a special section to the module’s object code stating where the module’s initialization function is to be found. Without this definition, your initialization function is never called.


The Cleanup Function
    static void __exit cleanup_function(void) {         /* Cleanup code here */     }     module_exit(cleanup_function);The **__exit** modifier marks the code as being for module unload only (by causing the compiler to place it in a special ELF section).Error Handling During Initialization
    struct something *item1;
    struct somethingelse *item2;
    int stuff_ok;
    void my_cleanup(void)
    {
    if (item1)
        release_thing(item1);
    if (item2)
        release_thing2(item2);
        if (stuff_ok) 
            unregister_stuff();
        return; 
    }
    int __init my_init(void) {
        int err = -ENOMEM;
        item1 = allocate_thing(arguments);
        item2 = allocate_thing2(arguments2);
        if (!item2 || !item2)
            goto fail;
        err = register_stuff(item1, item2);
        if (!err)
            stuff_ok = 1;
        else
            goto fail;
        return 0; /* success */
        
        fail: 
            my_cleanup( ); 
            return err;
    }
Note, however, that the cleanup function cannot be marked __exit when it is called by nonexit code, as in the previous example.


Module Parameters

Such a module could then be loaded with a com- mand line such as:
     insmod hellop howmany=10 whom="Mom"
     
So hellop would declare its parameters and make them available to insmod as follows:
     static char *whom = "world";     static int howmany = 1;     module_param(howmany, int, S_IRUGO);     module_param(whom, charp, S_IRUGO);     
##### ch03
Char devices are accessed through names in the filesystem. Those names are called special files or device files or simply nodes of the filesystem tree; they are conventionally located in the **/dev** directory. Special files for char drivers are identified by a “c” in the first column of the output of ls –l. Block devices appear in /dev as well, but they are identified by a “b.”
If you issue the ls –l command, you’ll see two numbers (separated by a comma) in the device file entries before the date of the last modification, where the file length normally appears. These numbers are the major and minor device number for the particular device. 
Traditionally, the major number identifies the driver associated with the device. For example, /dev/null and /dev/zero are both managed by driver 1The minor number is used by the kernel to determine exactly which device is being referred to.
Some major device numbers are statically assigned to the most common devices. A list of those devices can be found in Documentation/devices.txt within the kernel source tree.
The following script, scull_load, is part of the scull distribution. The user of a driver that is distributed in the form of a module can invoke such a script from the system’s rc.local file or call it manually whenever the module is needed.     #!/bin/sh     module="scull"     device="scull"     mode="664"     # invoke insmod with all arguments we got     # and use a pathname, as newer modutils don't look in . by default     /sbin/insmod ./$module.ko $* || exit 1     # remove stale nodes     rm -f /dev/${device}[0-3]     major=$(awk "\\$2==\"$module\" {print \\$1}" /proc/devices)     mknod /dev/${device}0 c $major 0     mknod /dev/${device}1 c $major 1     mknod /dev/${device}2 c $major 2     mknod /dev/${device}3 c $major 3     # give appropriate group/permissions, and change the group.     # Not all distributions have staff, some have "wheel" instead.     group="staff"     grep -q '^staff:' /etc/group || group="wheel"     chgrp $group /dev/${device}[0-3]     chmod $mode  /dev/${device}[0-3]The script can be adapted for another driver by redefining the variables and adjusting the **mknod** lines. The script just shown creates four devices because four is the default in the scull sources.

Here’s the code we use in scull’s source to get a major number:

     if (scull_major) {
         dev = MKDEV(scull_major, scull_minor);
         result = register_chrdev_region(dev, scull_nr_devs, "scull");
     } else {
         result = alloc_chrdev_region(&dev, scull_minor, scull_nr_devs, "scull");
         scull_major = MAJOR(dev);
     }
     if (result < 0) {
         printk(KERN_WARNING "scull: can't get major %d\n", scull_major);
         return result;
     }


File Operations
