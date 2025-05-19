package org.example.server;

import org.example.common.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ResponseSender {
    public static void sendResponse(SocketChannel client, Response response) throws IOException {
        if (client.isBlocking()) {
            client.configureBlocking(false);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
            oos.flush();
        }

        byte[] data = baos.toByteArray();
        int dataLength = data.length;

        ByteBuffer buffer = ByteBuffer.allocate(dataLength + 4);
        buffer.putInt(dataLength);
        buffer.put(data);
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write(buffer);
        }

        System.out.println("Отправлено: " + response.getData());
    }
}