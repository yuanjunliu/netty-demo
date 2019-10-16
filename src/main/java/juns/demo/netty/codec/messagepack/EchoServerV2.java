package juns.demo.netty.codec.messagepack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class EchoServerV2 {
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
                            socketChannel.pipeline()
                                    .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2))
                                    .addLast("msgpack decoder", new MsgpackDecode())
                                    .addLast("frameEncoder", new LengthFieldPrepender(2))
                                    .addLast("msgpack encoder", new MsgpackEncoder())
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
            new EchoServerV2().bind(port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
