package juns.demo.netty.protocol.http.xml;

import org.jibx.binding.Compile;

/**
 * Created by 01380763 on 2019/10/24.
 */
public class compile {
    public static void main(String[] args) {
        Compile.main(new String[]{"-v", "binding.xml"});
    }
}
