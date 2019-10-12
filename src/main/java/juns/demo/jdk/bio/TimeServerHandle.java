package juns.demo.jdk.bio;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeServerHandle implements Runnable {
    private Socket socket;

    public TimeServerHandle(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()))) {
            String currentTime = null;
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null)
                    break;
                System.out.println("The time server receive order: " + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
                out.println(currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.closeQuietly(this.socket);
        }
    }
}
