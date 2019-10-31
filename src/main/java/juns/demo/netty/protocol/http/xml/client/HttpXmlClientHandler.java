package juns.demo.netty.protocol.http.xml.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import juns.demo.netty.protocol.http.xml.codec.HttpXmlRequest;
import juns.demo.netty.protocol.http.xml.codec.HttpXmlResponse;
import juns.demo.netty.protocol.http.xml.pojo.OrderFactory;

/**
 * Created by 01380763 on 2019/10/23.
 */
public class HttpXmlClientHandler extends  SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 10; i < 20; i++) {
            HttpXmlRequest request = new HttpXmlRequest(null,
                    OrderFactory.create(i));
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
                                   HttpXmlResponse msg) throws Exception {
        System.out.println("The client receive response of http header is : "
                + msg.getHttpResponse().headers().names());
        System.out.println("The client receive response of http body is : "
                + msg.getResult());
    }
}
