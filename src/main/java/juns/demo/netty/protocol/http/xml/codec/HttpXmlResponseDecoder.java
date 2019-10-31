package juns.demo.netty.protocol.http.xml.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.List;

/**
 * Created by 01380763 on 2019/10/17.
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<FullHttpResponse>  {
    public HttpXmlResponseDecoder(Class<?> clazz) {
        super(clazz);
    }

    public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse(msg, decode0(
                ctx, msg.content()));
        out.add(resHttpXmlResponse);
    }
}
