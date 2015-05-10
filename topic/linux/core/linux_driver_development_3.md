

##### ch02

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
