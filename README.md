# netty-demo

## 基础概念

- 7层网络模型

![](https://images0.cnblogs.com/blog/405501/201503/131300141675737.png)

[参考](https://www.cnblogs.com/cyyljw/p/10578056.html)
- Socket是什么?

```text
1.在TCP 在TCP/IP协议中，“IP地址+TCP或UDP端口号”唯一标识网络通讯中的一个进程，“IP地址+端口号”就称为socket。
2.在TCP协议中，建立连接的两个进程各自有一个socket来标识，那么这两个socket组成的socket pair就唯一标识一个连接。socket本身有“插座”的意思，因此用来描述网络连接的一对一关系。
3.TCP/IP协议最早在BSD UNIX上实现，为TCP/IP协议设计的应用层编程接口称为socket接口
TCP4层模型与socket接口之间关系如下图
```
![](https://img-blog.csdn.net/20170207231402155?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemVyb193aXR0eQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

- TCP 状态

![](https://img-blog.csdn.net/20170208150054638?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemVyb193aXR0eQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

[参考](https://blog.csdn.net/zero_witty/article/details/54920148)

- Java线程状态

```text
1. 初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
2. 运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。
线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取CPU的使用权，此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
3.阻塞(BLOCKED)：表示线程阻塞于锁。
4.等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
5.超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。
6. 终止(TERMINATED)：表示该线程已经执行完毕。
```
![](https://img-blog.csdn.net/2018070117435683?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3BhbmdlMTk5MQ==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

[参考](https://blog.csdn.net/qq_22771739/article/details/82529874)

## JDK

- 同步io使用的是socket套接字api
- nio是jdk1.4引入的
- 异步io使用的是SocketChannel 套接字通道api

### NIO

#### Buffer

- bio是面向流的, 数据都是直接放到stream中
- nio增加了缓冲区Buffer的概念,所有数据都是先放到Buffer中

    `流也有缓冲流啊? 流和buffer有什么区别?`
    

#### Channel

- Channel是一个通道, 通道和流的不同之处在于通道是双向的

    `Channel是不是把socket封装了下, 把输入\输出流给组合到一起了?`

#### Selector 多路复用器

- Selector是Java NIO编程的基础
- Selector不断轮询注册在其上的Channel

```
NIO只是非阻塞IO
还是需要我们编写循环代码去处理
```
### AIO

- JDK1.7 引入新的异步通道的概念, 这时的NIO称为 NIO 2.0
- 提供了异步文件通道和一般套接字通道
- NIO 2.0 的异步套接字通道是真正的异步非阻塞IO, 对应Unix网络编程的事件驱动I/O (AIO)
- AIO 都是异步回调, 内部会使用jdk的线程池去处理回调

## Netty

```
TCP粘包/拆包
一个完整的包可能会被TCP拆分成多个包进行发送, 也有可能将多个小包封装成一个大的数据包发
```

