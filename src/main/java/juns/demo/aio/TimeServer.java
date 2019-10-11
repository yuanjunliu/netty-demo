package juns.demo.aio;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 1000;
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
