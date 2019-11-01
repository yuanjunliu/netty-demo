package juns.demo.netty.protocol.netty.server;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import juns.demo.netty.protocol.netty.MessageType;
import juns.demo.netty.protocol.netty.struct.Header;
import juns.demo.netty.protocol.netty.struct.NettyMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 01380763 on 2019/11/1.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private static final Log LOG = LogFactory.getLog(LoginAuthRespHandler.class);

    // 已连接信息
    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    // IP白名单
    private List<String> whitekList = Lists.newArrayList("127.0.0.1", "192.168.1.104");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        // 如果是握手请求消息，处理，其它消息透传
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeIndex = ((InetSocketAddress) ctx.channel().remoteAddress()).getHostName();
            NettyMessage loginResp;
            // 重复登陆，拒绝
            if (nodeCheck.containsKey(nodeIndex)) {
                loginResp = buildResponse((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                if (!whitekList.contains(ip)) {
                    loginResp = buildResponse((byte) -1);
                } else {
                    loginResp = buildResponse((byte) 0);
                    nodeCheck.put(address.toString(), true);
                }
            }
            LOG.info("The login response is : " + loginResp + " body [" + loginResp.getBody() + "]");
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(result);
        return message;
    }
}
