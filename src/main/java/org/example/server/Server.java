package org.example.server;

import org.example.common.Request;
import org.example.common.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    private static final int PORT = 8080;
    private final CityCollection cityCollection;
    private final CommandProcessor processor;

    public Server() {
        this.cityCollection = new CityCollection();
        this.processor = new CommandProcessor(cityCollection);
        loadInitialData();
    }

    private void loadInitialData() {
//        String filename = System.getenv("CITY_DATA_FILE");
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

                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        ConnectionAcceptor.accept(selector, serverChannel);
                    } else if (key.isReadable()) {
                        handleClientRequest(key);
                    }
                }
                selector.selectedKeys().clear();
            }
        }
    }

    private void handleClientRequest(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        try {
            Request request = RequestReader.readRequest(client);
            Response response = processor.process(request);
            ResponseSender.sendResponse(client, response);
        } catch (Exception e) {
            ResponseSender.sendResponse(client, new Response("ERROR", e.getMessage()));
            client.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
    }
}