package juns.demo.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by 01380763 on 2019/10/15.
 */
public class HttpFileServer {
    private static final String DEFAULT_URL = "/src/main/java/juns/demo/";

    public void run(int port, String url) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 关联服务端通道
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast("http-decoder", new HttpRequestDecoder()) // 请求消息解码器
                                    // 将多个消息转换为FullHttpRequest 或者 FullHttpResponse
                                    // 否则的话HTTP解码器会在每个HTTP消息中生成多个消息对象
                                    // 1,HttpRequest/HttpResponse 2,HttpContent 3,LastHttpContent
                                    .addLast("http-aggregator", new HttpObjectAggregator(65536))

                                    // 这里对下面两个处理器调换了顺序, 否则会报错,
                                    // 因为Chunked处理后的message类型不能被HttpResponseEncoder处理
                                    // 所以要先经过ChunkedWriteHandler 处理

                                    // 为了支持异步发送大的码流(例如大的文件流), 但不占用过多的内存
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    // HTTP响应编码器
                                    .addLast("http-encoder", new HttpResponseEncoder())

                                    .addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            // 同步等待绑定成功
            ChannelFuture f = b.bind(port).sync();
            System.out.println("HTTP 文件目录服务器启动, 网址是 http://localhost:" + port + url);
            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出, 释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 1000;

        new HttpFileServer().run(port, DEFAULT_URL);

    }
}
