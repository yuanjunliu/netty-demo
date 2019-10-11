# netty-demo
netty-demo

- 同步io使用的是socket套接字api
- nio是jdk1.4引入的
- 异步io使用的是SocketChannel 套接字通道api

### Buffer

- bio是面向流的, 数据都是直接放到stream中
- nio增加了缓冲区Buffer的概念,所有数据都是先放到Buffer中

    `流也有缓冲流啊? 流和buffer有什么区别?`
    

### Channel

- Channel是一个通道, 通道和流的不同之处在于通道是双向的

    `Channel是不是把socket封装了下, 把输入\输出流给组合到一起了?`

### Selector 多路复用器

- Selector是Java NIO编程的基础

- Selector不断轮询注册在其上的Channel