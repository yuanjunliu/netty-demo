package juns.demo.jdk.aio;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by 01380763 on 2019/10/11.
 */
public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private AsynchronousSocketChannel client;
    private String host;
    private int port;
    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            client = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(client);
    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        for (int i = 0; i < 10; i++) {
            byte[] req = ("QUERY TIME ORDER" + i).getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
            writeBuffer.put(req);
            writeBuffer.flip();
            client.write(writeBuffer, writeBuffer,
                    new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {

                            if (buffer.hasRemaining()) {
                                client.write(buffer, buffer, this);
                            } else {
                                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                                client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                                    @Override
                                    public void completed(Integer result, ByteBuffer buffer) {
                                        buffer.flip();
                                        byte[] bytes = new byte[buffer.remaining()];
                                        buffer.get(bytes);
                                        String body;
                                        try {
                                            body = new String(bytes, "UTF-8");
                                            System.out.println("REQ:" + new String(req) + "  " + "Now is : " + body);
//                                            latch.countDown();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void failed(Throwable exc, ByteBuffer attachment) {
                                        IOUtils.closeQuietly(client);
                                    }
                                });
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            IOUtils.closeQuietly(client);
                            latch.countDown();
                        }
                    });
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        exc.printStackTrace();
        IOUtils.closeQuietly(client);
        latch.countDown();
    }
}
