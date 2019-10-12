# netty-demo

## 基础概念

- 7层网络模型
- 网络编程API Socket
- TCP 状态

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

