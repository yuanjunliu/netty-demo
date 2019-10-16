package juns.demo.netty.codec.messagepack;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juns.demo.netty.codec.UserInfo;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    private final int sendNumber;

    public EchoClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] infos = new UserInfo[sendNumber];
        for (int i = 0; i < sendNumber; i++) {
            UserInfo info = new UserInfo();
            info.setUserID(12);
            info.setUserName("ABCDEFG--->" + i);
            infos[i] = info;
        }
        for (UserInfo infoE : infos) {
            ctx.write(infoE);
        }
        ctx.flush();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive the msgpack message : " + msg);
//        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
