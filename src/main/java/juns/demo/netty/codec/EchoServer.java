package juns.demo.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class EchoServer {
    public void bind(int port) throws InterruptedException {
        // 配置服务端的NIO 线程组
        // 其实就是Reactor线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 关联服务端通道
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            socketChannel.pipeline()
                                    // 指定分隔符解码器
//                                    .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))// 设置最大Frame长度是为了防止内存溢出
                                    // 测试下定长解码器
                                    .addLast(new FixedLengthFrameDecoder(20)) // 这种定长的应用场景是什么? Http的包体就是采用这个方式
                                    .addLast(new StringDecoder())
                                    .addLast(new EchoServerHandler());
                        }
                    });

            // 同步等待绑定成功
            ChannelFuture f = b.bind(port).sync();
            System.out.println("Time server listen on " + port);
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出, 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 1000;
        try {
            new EchoServer().bind(port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
