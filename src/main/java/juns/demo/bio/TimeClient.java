package juns.demo.bio;

import java.io.*;
import java.net.Socket;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 1000;
        try (Socket socket = new Socket("127.0.0.1", port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {
            out.println("QUERY TIME ORDER");
            System.out.println("Send order 2 server succeed.");
            String resp = in.readLine();
            System.out.println("Now is : " + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
