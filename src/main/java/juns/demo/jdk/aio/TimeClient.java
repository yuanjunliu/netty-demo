package juns.demo.jdk.aio;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeClient {

    public static void main(String[] args) {

        int port = 1000;

        new Thread(new AsyncTimeClientHandler("127.0.0.1", port)).start();
    }
}
