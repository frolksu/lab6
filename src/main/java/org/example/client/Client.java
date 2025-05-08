package org.example.client;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;

/**
 * для теста!!! необходимо доработать
 */

public class Client {
    public static void main(String[] args) throws Exception {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress("localhost", 8080));

        ByteBuffer buffer = ByteBuffer.wrap("Тест".getBytes());
        channel.write(buffer);

        buffer.clear();
        channel.read(buffer); // Читаем ответ
        buffer.flip();
        System.out.println("Ответ сервера: " + new String(buffer.array()));

        channel.close(); // Закрываем соединение
    }
}