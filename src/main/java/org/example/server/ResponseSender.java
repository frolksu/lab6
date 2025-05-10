package org.example.server;

import org.example.common.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSender {
    public static void sendResponse(SocketChannel client, Response response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
        }
        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        while (buffer.hasRemaining()) {
            client.write(buffer);
        }
    }
}
