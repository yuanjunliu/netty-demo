package juns.demo.netty.protocol.http;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * Created by 01380763 on 2019/10/23.
 */
public class SimpleHttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 本示例模拟了通过HTTP长连接对http服务器发起多次请求的场景
        // http服务器(不管是tomcat还是netty)一般对一个连接都只会使用一个线程去处理, 所以本例中多次请求的结果也是按顺返回的
        // 对于keep-alive的连接, http服务器一般不会主动关闭
        // tcp协议是全双工的, http协议是半双工的, websocket协议是全双工的
        // 所以http请求发出后, 如果没有返回, 这个连接是不能用的, 因为会被阻塞在read上, 这是http协议所规定的

        // 所以优化大量调用http接口的方法:
        // 连接池, 异步?
        // 如果使用netty单个连接, 其实跟同步是一个效果
        // 如果使用多连接异步是会很快, 但是无法对应业务数据

        for (int i = 1; i < 10; i++) {
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, "/MarioManagePlatform/flowQuality/test.do");
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            headers.set(HttpHeaderNames.HOST, "localhost:8080");
            headers.set(HttpHeaderNames.COOKIE, "JSESSIONID=12cymvmdt1rhkcfjn8y4ts717");
            HttpHeaders.setContentLength(request, 0);
            ChannelFuture f = ctx.writeAndFlush(request);
            f.addListener(future -> {
                if (!future.isSuccess()) {
                    future.cause().printStackTrace();
                }
            });
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                FullHttpResponse msg) throws Exception {
        System.out.println("The client receive response of http body is : "
                + msg.content().toString(CharsetUtil.UTF_8));
    }
}
