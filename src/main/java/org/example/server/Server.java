package org.example.server;

import org.example.common.CityFileSaver;
import org.example.common.Request;
import org.example.common.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server {
    private static final String DATA_FILE = "cities.csv"; // Добавляем константу
    private static CityCollection cityCollection;
    private static final int PORT = 8080;
    private final CommandProcessor processor;

    public Server() {
        this.cityCollection = new CityCollection();
        this.processor = new CommandProcessor(cityCollection);
        CommandFactory.init(cityCollection);
        loadInitialData();
    }

    private void loadInitialData() {
//        String filename = System.getenv("DATA_FILE");
        String filename = "cities.csv"; // Для теста
        if (filename != null) {
            CityFileLoader.loadCities(cityCollection, filename);
        } else {
            System.out.println("Файл не указан. Коллекция пуста.");
        }
    }

    public void start() throws IOException {

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {

            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                selector.select();

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isAcceptable()) {
                        ConnectionAcceptor.accept(selector, serverChannel);
                    }
                    else if (key.isReadable()) {
                        handleClientRequest(key);
                    }
                }
            }
        }
    }

    private void handleClientRequest(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            Request request = RequestReader.readRequest(client);
            System.out.println("Получен запрос: " + request.getCommandName());

            Response response = processor.process(request);
            System.out.println("Ответ: " + response.getData() + " / " + response.getMessage());

            ResponseSender.sendResponse(client, response);
        } catch (Exception e) {
            System.err.println("Ошибка обработки запроса: " + e);
            ResponseSender.sendResponse(client, new Response("ERROR", e.getMessage()));
            client.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nСервер завершает работу...");
            CityFileSaver.saveCities(cityCollection, DATA_FILE);
        }));
        new Server().start();
    }
}