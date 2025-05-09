package org.example.server;

import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.io.*;


import org.example.common.Request;
import org.example.common.Response;
import org.example.common.commands.Command;

public class Server {
    private static final int PORT = 8080;
    private static CityCollection cityCollection = new CityCollection();

    public static void main(String[] args) {
        // 1. Загрузка коллекции из файла

//        String filename = System.getenv("CITY_DATA_FILE");
        String filename = "cities.csv"; // Для теста
        if (filename != null) {
            CityFileLoader.loadCities(cityCollection, filename);
        } else {
            System.out.println("Файл не указан. Коллекция пуста.");
        }
        // 2. Настройка NIO
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + PORT);

            // 3. Главный цикл обработки запросов
            while (true) {
                selector.select(); // Блокируется до новых событий

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        acceptClient(selector, serverChannel);
                    } else if (key.isReadable()) {
                        processClientRequest(key);
                    }
                }
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private static void acceptClient(Selector selector, ServerSocketChannel server) throws IOException {
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("Новый клиент: " + client.getRemoteAddress());
    }

    private static void processClientRequest(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            // 1. Чтение запроса
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            int bytesRead = client.read(buffer);

            if (bytesRead == -1) {
                client.close();
                return;
            }

            // 2. Десериализация запроса
            Request request = deserializeRequest(buffer);

            // 3. Выполнение команды
            Command command = CommandFactory.createCommand(request);
            Response response = command.execute(cityCollection);
            sendResponse(client, response);

        } catch (Exception e) {
            sendError(client, e);
        }
    }

    private static Request deserializeRequest(ByteBuffer buffer) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Request) ois.readObject();
        }
    }

    private static void sendResponse(SocketChannel client, Response response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);

        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        client.write(buffer);
    }

    private static void sendError(SocketChannel client, Exception e) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(("ERROR: " + e.getMessage()).getBytes());
        client.write(buffer);
        client.close();
    }
}