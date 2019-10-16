package juns.demo.netty.codec.messagepack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import juns.demo.netty.codec.UserInfo;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * Created by 01380763 on 2019/10/14.
 */
public class MsgpackDecode extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        // 从ByteBuf获取要解码的数据, 不再是直接全部获取了
        byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);

        MessagePack messagePack = new MessagePack();
        // 反序列化为Object, 然后放入List
        list.add(messagePack.read(array, UserInfo.class));
    }
}
