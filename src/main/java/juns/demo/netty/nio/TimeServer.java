package juns.demo.netty.nio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import juns.demo.netty.nio.resolve_halfpack.*;
import juns.demo.netty.nio.resolve_halfpack.TimeServerHandler;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class TimeServer {
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
                    .childHandler(new ChildChannelHandler()); // 绑定子处理器

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

    private class ChildChannelHandler extends ChannelInitializer {

        @Override
        protected void initChannel(Channel channel) throws Exception {
            // 1, 单个包
//            channel.pipeline().addLast(new TimeServerHandler());
            // 2, 测试粘包问题
//            channel.pipeline().addLast(new juns.demo.netty.nio.halfpack.TimeServerHandler());
            // 3, 解决了粘包问题
            channel.pipeline()
                    .addLast(new LineBasedFrameDecoder(1024))
                    .addLast(new StringDecoder())
                    .addLast(new juns.demo.netty.nio.resolve_halfpack.TimeServerHandler());
        }
    }

    public static void main(String[] args) {
        int port = 1000;
        try {
            new TimeServer().bind(port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
