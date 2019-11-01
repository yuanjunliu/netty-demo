package juns.demo.netty.protocol.netty;

/**
 * Created by 01380763 on 2019/11/1.
 */
public enum MessageType {
    SERVICE_REQ((byte) 0),
    SERvICE_RESP((byte) 1),
    ONE_WAY((byte) 2),
    LOGIN_REQ((byte) 3),
    LOGIN_RESP((byte) 4),
    HEARTBEAT_REQ((byte) 5),
    HEARTBEAT_RESP((byte) 6);

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
