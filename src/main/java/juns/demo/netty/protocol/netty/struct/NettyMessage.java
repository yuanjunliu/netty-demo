package juns.demo.netty.protocol.netty.struct;

import lombok.Data;

/**
 * Created by 01380763 on 2019/10/31.
 */
@Data
public class NettyMessage {
    private Header header;
    private Object body;
}
