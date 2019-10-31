package juns.demo.netty.protocol.http.xml;

import juns.demo.netty.protocol.http.xml.pojo.Order;
import juns.demo.netty.protocol.http.xml.pojo.OrderFactory;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by 01380763 on 2019/10/17.
 */
public class TestOrder {
    private IBindingFactory factory;
    private StringWriter writer;
    private StringReader reader;
    private final static String CHARSET_NAME = "UTF-8";

    private String encode2Xml(Order order) throws JiBXException, IOException {
        factory = BindingDirectory.getFactory(Order.class);
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.marshalDocument(order, CHARSET_NAME, null, writer);

        String xmlStr = writer.toString();
        writer.close();

        System.out.println(xmlStr);
        return xmlStr;
    }

    private Order decode2Order(String xmlBody) throws JiBXException {
        reader = new StringReader(xmlBody);
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Order order = (Order) uctx.unmarshalDocument(reader);
        return order;
    }

    public static void main(String[] args) throws JiBXException, IOException {
        TestOrder testOrder = new TestOrder();
        Order order = OrderFactory.create(1L);
        String body = testOrder.encode2Xml(order);
        Order order1 = testOrder.decode2Order(body);
        System.out.println(order1);

    }
}
