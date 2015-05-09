

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


If, instead, you have a module called module.ko that is generated from two source files (called, say, file1.c and file2.c), the correct incantation would be:

    
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

     

   
**module.h** contains a great many definitions of symbols and functions needed by loadable modules. You need **init.h** to specify your initialization and cleanup functions. Most modules also include **moduleparam.h** to enable the passing of parameters to the module at load time.

    MODULE_LICENSE("GPL");
    MODULE_AUTHOR (stating who wrote the module),
    MODULE_DESCRIPTION (a human-read- able statement of what the module does), 
    MODULE_VERSION (for a code revision num- ber; see the comments in <linux/module.h> for the conventions to use in creating version strings), 
    MODULE_ALIAS (another name by which this module can be known), 
    MODULE_DEVICE_TABLE (to tell user space about which devices the module supports).
    
    
The actual definition of the initialization function always looks like:

    static int __init initialization_function(void) {




The Cleanup Function


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

     
So hellop would declare its parameters and make them available to insmod as follows:








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



