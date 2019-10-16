package juns.demo.netty.codec.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * Created by 01380763 on 2019/10/14.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        try{

            MessagePack msgpack = new MessagePack();
            // 序列化为byte[]
            // MessagePack序列化对象时, 需要在对象上添加主键@Message, 否则报错
            byte[] raw = msgpack.write(o);
            byteBuf.writeBytes(raw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
