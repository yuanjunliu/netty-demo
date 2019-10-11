package juns.demo.bio;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 1000;
        // ServerSocket 在java.net包
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port: " + port);
            Socket socket = null;
            while (true) {
                // 线程会阻塞在这, 只到有连接进来
                socket = server.accept();
                System.out.println("accept conn from " + socket.getInetAddress());
                new Thread(new TimeServerHandle(socket)).start();
            }
        } finally {
            IOUtils.closeQuietly(server);
        }
    }

}
