package juns.demo.netty.http.xml.codec;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 01380763 on 2019/10/17.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HttpXmlResponse {
    private FullHttpResponse httpResponse;
    private Object result;

    @Override
    public String toString() {
        return "HttpXmlResponse [httpResponse=" + httpResponse + ", result="
                + result + "]";
    }

}
