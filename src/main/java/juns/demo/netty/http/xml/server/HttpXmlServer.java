package juns.demo.netty.http.xml.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import juns.demo.netty.http.xml.codec.HttpXmlRequestDecoder;
import juns.demo.netty.http.xml.codec.HttpXmlResponseEncoder;
import juns.demo.netty.http.xml.pojo.Order;

import java.net.InetSocketAddress;

/**
 * Created by 01380763 on 2019/10/23.
 */
public class HttpXmlServer {
    public void run(final int port) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));

                            ch.pipeline().addLast("http-decoder",
                                    new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator",
                                    new HttpObjectAggregator(65536));
                            ch.pipeline()
                                    .addLast(
                                            "xml-decoder",
                                            new HttpXmlRequestDecoder(
                                                    Order.class, true));
                            ch.pipeline().addLast("http-encoder",
                                    new HttpResponseEncoder());
                            ch.pipeline().addLast("xml-encoder",
                                    new HttpXmlResponseEncoder());
                            ch.pipeline().addLast("xmlServerHandler",
                                    new HttpXmlServerHandler());
                        }
                    });
            ChannelFuture future = b.bind(new InetSocketAddress(port)).sync();
            System.out.println("HTTP订购服务器启动，网址是 : " + "http://localhost:"
                    + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new HttpXmlServer().run(port);
    }
}
