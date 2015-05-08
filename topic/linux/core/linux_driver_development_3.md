

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
    
