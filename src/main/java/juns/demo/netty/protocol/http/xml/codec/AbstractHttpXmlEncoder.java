package juns.demo.netty.protocol.http.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Created by 01380763 on 2019/10/17.
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    IBindingFactory factory;
    StringWriter writer;
    final static Charset UTF_8 = CharsetUtil.UTF_8;

    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws Exception {
        factory = BindingDirectory.getFactory(body.getClass());
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(body, UTF_8.name(), null, writer);
        String xmlStr = writer.toString();
        writer.close();
        return Unpooled.copiedBuffer(xmlStr, UTF_8);
    }

    //    @Skip
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        // 释放资源
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

}
