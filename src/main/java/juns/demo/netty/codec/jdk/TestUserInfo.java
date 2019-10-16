package juns.demo.netty.codec.jdk;

import juns.demo.netty.codec.UserInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by 01380763 on 2019/10/12.
 */
public class TestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(info);
        os.flush();
        os.close();

        byte[] b = bos.toByteArray();
        System.out.println("The jdk serializable length is : " + b.length);
        bos.close();

        System.out.println("--------------------------------------------");
        System.out.println("The byte array serializable length is : " + info.codeC().length);
    }
}
