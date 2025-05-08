package org.example.server;

import java.io.IOException;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;

/**
 * для теста!!! необходимо доработать
 */

public class Server {
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Сервер запущен...");

        while (true) {
            selector.select(); // Ждём событий
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();

                if (!key.isValid()) { // Проверяем, валиден ли ключ
                    continue;
                }

                if (key.isAcceptable()) { // Новый клиент
                    SocketChannel client = serverChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Клиент подключен: " + client.getRemoteAddress());
                }

                if (key.isReadable()) { // Данные от клиента
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);

                    try {
                        int bytesRead = client.read(buffer);

                        if (bytesRead == -1) { // Клиент отключился
                            System.out.println("Клиент отключился: " + client.getRemoteAddress());
                            key.cancel();
                            client.close();
                            continue;
                        }

                        buffer.flip();
                        String message = new String(buffer.array(), 0, bytesRead);
                        System.out.println("Получено: " + message);

                        // Отправляем эхо-ответ
                        buffer.rewind();
                        client.write(buffer);
                    } catch (IOException e) {
                        System.err.println("Ошибка при чтении: " + e.getMessage());
                        key.cancel();
                        client.close();
                    }
                }
            }
        }
    }
}