package juns.demo.netty.codec.messagepack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 01380763 on 2019/10/14.
 */
public class TestMsgpack {
    public static void main(String[] args) throws IOException {
        List<String> src = new ArrayList<String>();
        src.add("msgpack");
        src.add("kumofs");
        src.add("viver");

        MessagePack msgpack = new MessagePack();
        // Serialize
        byte[] raw = msgpack.write(src);

        // Deserialize directly using a template
        List<String> dst1 = msgpack.read(raw, Templates.tList(Templates.TString));
        System.out.println(dst1.get(0));
        System.out.println(dst1.get(1));
        System.out.println(dst1.get(2));

        // Or, Deserialze to Value then convert type.
        Value dynamic = msgpack.read(raw);
        List<String> dst2 = new Converter(dynamic)
                .read(Templates.tList(Templates.TString));
        System.out.println(dst2.get(0));
        System.out.println(dst2.get(1));
        System.out.println(dst2.get(2));

    }
}
