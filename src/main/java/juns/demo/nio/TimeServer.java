package juns.demo.nio;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 1000;

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
