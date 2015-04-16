
#### 常用
##### 协议
 Thrift可以让你选择客户端与服务端之间传输通信协议的类别，在传输协议上总体上划分为文本(text)和二进制(binary)传输协议, 为节约带宽，提供传输效率，一般情况下使用二进制类型的传输协议为多数，但有时会还是会使用基于文本类型的协议，这需要根据项目/产品中的实际需求：

* TBinaryProtocol – 二进制编码格式进行数据传输。
* TCompactProtocol – 这种协议非常有效的，使用Variable-Length Quantity (VLQ) 编码对数据进行压缩。
* TJSONProtocol – 使用JSON的数据编码协议进行数据传输。
* TSimpleJSONProtocol – 这种节约只提供JSON只写的协议，适用于通过脚本语言解析
* TDebugProtocol – 在开发的过程中帮助开发人员调试用的，以文本的形式展现方便阅读。

##### 传输层

* TSocket- 使用堵塞式I/O进行传输，也是最常见的模式。
* TFramedTransport- 使用非阻塞方式，按块的大小，进行传输，类似于Java中的NIO。
* TFileTransport- 顾名思义按照文件的方式进程传输，虽然这种方式不提供Java的实现，但是实现起来非常简单。
* TMemoryTransport- 使用内存I/O，就好比Java中的ByteArrayOutputStream实现。
* TZlibTransport- 使用执行zlib压缩，不提供Java的实现。

##### 服务端类型

* TSimpleServer -  单线程服务器端使用标准的堵塞式I/O。
* TThreadPoolServer -  多线程服务器端使用标准的堵塞式I/O。
* TNonblockingServer – 多线程服务器端使用非堵塞式I/O，并且实现了Java中的NIO通道。

