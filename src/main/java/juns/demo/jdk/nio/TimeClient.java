package juns.demo.jdk.nio;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 1000;
        new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
    }
}
