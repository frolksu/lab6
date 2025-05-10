package org.example.server;

import org.example.common.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestReader {
    private static final int BUFFER_SIZE = 8192;

    public static Request readRequest(SocketChannel client) throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        client.read(buffer);
        buffer.flip();

        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Request) ois.readObject();
        }
    }
}
