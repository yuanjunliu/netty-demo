package juns.demo.netty.protocol.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juns.demo.netty.protocol.netty.MessageType;
import juns.demo.netty.protocol.netty.struct.Header;
import juns.demo.netty.protocol.netty.struct.NettyMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 01380763 on 2019/11/1.
 */
public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
    private static final Log LOG = LogFactory.getLog(HeartBeatReqHandler.class);
    private volatile ScheduledFuture<?> heartbeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null) {
            ctx.fireChannelRead(msg);
        }
        // 握手成功，主动发送心跳消息
        if (message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            heartbeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx),
                    0, 5000, TimeUnit.MILLISECONDS);
        } else if (message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
            LOG.info("Client receive server heart beat message : ---> " + message);
        }
    }

    private class HeartBeatTask implements Runnable {
        private ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heatBeat = buildHeatBeat();
            LOG.info("Client send heart beat message to server : ---> " + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private NettyMessage buildHeatBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (heartbeat != null) {
            heartbeat.cancel(true);
        }
        ctx.fireExceptionCaught(cause);
    }
}
