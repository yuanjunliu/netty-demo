package juns.demo.netty.protocol.http.xml.codec;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 01380763 on 2019/10/17.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpXmlRequest {
    private FullHttpRequest request;
    private Object body;

    @Override
    public String toString() {
        return "HttpXmlRequest [request=" + request + ", body =" + body + "]";
    }
}
