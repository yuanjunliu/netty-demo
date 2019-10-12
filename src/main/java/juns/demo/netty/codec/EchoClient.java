package juns.demo.netty.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class EchoClient {

    public void connect(int port, String host) throws InterruptedException {
        // 配置客户端NIO线程组, 不管是服务端还是客户端, 这个线程组是一样的
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 这个Bootstrap跟服务端不一样了, 服务端是ServerBootstrap
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class) // 绑定的Channel
                    .option(ChannelOption.TCP_NODELAY, true)
                    // 绑定一个处理器(可以绑定多个处理器吗?)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            socketChannel.pipeline()
                                    .addLast(new DelimiterBasedFrameDecoder(1024, delimiter))
                                    .addLast(new StringDecoder())
                                    .addLast(new EchoClientHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture f = b.connect(host, port).sync();

            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 1000;
        try {
            new EchoClient().connect(port, "127.0.0.1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
