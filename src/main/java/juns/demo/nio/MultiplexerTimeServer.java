package juns.demo.nio;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class MultiplexerTimeServer implements Runnable {
    // 多路IO复用器
    private Selector selector;

    // 服务端通道
    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            // 打开selector
            selector = Selector.open();
            // 打开通道
            servChannel = ServerSocketChannel.open();
            // 设置为非阻塞
            servChannel.configureBlocking(false);
            // 通道里默认就有Socket对象 , 绑定端口
            servChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 把通道注册到Selector, 必须指定SelectionKey
            servChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        // Selector本身并不会循环, 需要我们来循环
        while (!stop) {
            try {
                // 这个方法是阻塞的, 直到以下情况出现
                // 1, 至少有一个通道selected
                // 2, 调用了selector.wakeup()
                // 3, 线程被中断
                // 4, 达到超时时间
                selector.select(1000);

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();// 用iterator, 可以在迭代时删除元素
                    try {
                        // 每个key单独try-catch, 避免影响其他key
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        IOUtils.closeQuietly(selector);
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 处理新接入的请求消息
            if (key.isAcceptable()) {
                // 这个通道是最开始注册进Selector的服务端通道
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 接收到一个跟客户端相连的通道
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);

                // 把这个通道也注册到Selector中
                sc.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                // 从通道读数据, 需要我们先准备好ByteBuffer
                // 为何通道里面不直接预先准备好ByteBuffer??
                // 这里只分配了1024字节, 如果接收的内容超过了这个长度怎么办?
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(readBuffer);
                if (readBytes > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("The time server receive order : " + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date().toString() : "BAD ORDER";
                    doWrite(sc, currentTime);
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    key.cancel();
                    // 关闭当前通道
                    sc.close();
                } else {
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (StringUtils.isNotEmpty(response)) {
            // 往通道写数据时, 也是先把数据放到一个ByteBuffer中, 再把ByteBuffer写入通道
            // 为何不把这一步放到Channel里面呢??
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
        }
    }
}
