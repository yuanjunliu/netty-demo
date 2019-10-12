package juns.demo.jdk.fnio;

import juns.demo.jdk.bio.TimeServerHandle;
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
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);
            Socket socket = null;
            while (true) {
                // 线程会阻塞在这, 只到有连接进来
                socket = server.accept();
                System.out.println("accept conn from " + socket.getInetAddress());
                singleExecutor.execute(new TimeServerHandle(socket));
            }
        } finally {
            IOUtils.closeQuietly(server);
        }
    }
}
