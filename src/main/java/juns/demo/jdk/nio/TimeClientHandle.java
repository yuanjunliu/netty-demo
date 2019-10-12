package juns.demo.jdk.nio;

import org.apache.commons.io.IOUtils;

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
public class TimeClientHandle implements Runnable {
    private Selector selector;
    private SocketChannel channel;
    private volatile boolean stop;

    public TimeClientHandle(String host, int port) {
        try {
            // 打开Selector
            selector = Selector.open();
            // 打开客户端通道
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            if (channel.connect(new InetSocketAddress(host, port))) {
                channel.register(selector, SelectionKey.OP_READ);
                doWrite(channel);
            } else {
                channel.register(selector, SelectionKey.OP_CONNECT);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            try {
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
            SocketChannel sc = (SocketChannel) key.channel();

            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite(sc);
                } else {
                    System.exit(1);
                }
            }

            if (key.isReadable()) {
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
                    System.out.println("Now is : " + body);
                    this.stop = true;
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


    private void doWrite(SocketChannel channel) throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("Send order 2 server succeed.");
        }
    }
}
