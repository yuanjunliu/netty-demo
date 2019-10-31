package juns.demo.netty.protocol.http.xml.pojo;

/**
 * Created by 01380763 on 2019/10/17.
 *
 * Supported shipment methods. The "INTERNATIONAL" shipment methods can only be
 * used for orders with shipping addresses outside the U.S., and one of these
 * methods is required in this case.
 */
public enum Shipping {
    STANDARD_MAIL,
    PRIORITY_MAIL,
    INTERNATIONAL_MAIL,
    DOMESTICT_EXPRESS,
    INTERNATIONAL_EXPRESS
}
