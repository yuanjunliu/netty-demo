package juns.demo.netty.codec.jdk;

import juns.demo.netty.codec.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class PerformTestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to netty");
        int loop = 1000000;

        long startTime = System.currentTimeMillis();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(bos)) {
            for (int i = 0; i < loop; i++) {
                os.writeObject(info);
                os.flush();
                os.close();

                byte[] b = bos.toByteArray();
                bos.close();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("The jdk serializable cost time is : " + (endTime - startTime) + " ms");

            System.out.println("----------------------------------------------------");
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < loop; i++) {
                byte[] b = info.codeC(buffer);
            }
            endTime = System.currentTimeMillis();
            System.out.println("The byte array serializable cost time is : " + (endTime - startTime) + " ms");
        }
    }
}
