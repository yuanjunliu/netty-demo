package juns.demo.netty.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 01380763 on 2019/10/15.
 */
public class TestSubscribeReqProto {
    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(1);
        builder.setProductName("netty book");
        builder.setUserName("lilinfeng");
        List<String> address = new ArrayList<>();
        address.add("Nanjing yuhuatai");
        address.add("Beijing LiuLiChang");
        address.add("Shenzhen HongSHulin");
        builder.addAllAddress(address);

        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode : " + req.toString());
        System.out.println("Assert equal : --> " + req2.equals(req));
    }
}
