package org.example.server;


import org.example.common.Request;
import org.example.common.Response;
import org.example.common.commands.Command;
import org.example.common.model.entity.City;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 80805;
    private static final String SAVE_FILE = "collection.dat";
    private static final CityCollection collection = new CityCollection();
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static volatile boolean isRunning = true;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(Server::shutdown));

        loadCollection();
        startServer();
    }

    private static void startServer() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + PORT);

            while (isRunning) {
                selector.select(1000);
                processSelectedKeys(selector);
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        } finally {
            saveCollection();
        }
    }

    private static void processSelectedKeys(Selector selector) throws IOException {
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

        while (keys.hasNext()) {
            SelectionKey key = keys.next();
            keys.remove();

            if (key.isAcceptable()) acceptNewClient(key);
            if (key.isReadable()) processRequest(key);
        }
    }

    private static void acceptNewClient(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(key.selector(), SelectionKey.OP_READ);
        System.out.println("Новое подключение: " + client.getRemoteAddress());
    }

    private static void processRequest(SelectionKey key) {
        executor.submit(() -> {
            try (SocketChannel channel = (SocketChannel) key.channel();
                 ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel));
                 ObjectOutputStream out = new ObjectOutputStream(Channels.newOutputStream(channel))) {

                Request request = (Request) in.readObject();
                Response response = processCommand(request);
                out.writeObject(response);

            } catch (Exception e) {
                System.err.println("Ошибка обработки запроса: " + e.getMessage());
            } finally {
                key.cancel();
            }
        });
    }

    private static Response processCommand(Request request) {
        try {
            Command command = CommandFactory.createCommand(request);
            return command.execute(collection);
        } catch (IllegalArgumentException e) {
            return new Response("ERROR", e.getMessage());
        } catch (Exception e) {
            return new Response("ERROR", "Внутренняя ошибка сервера");
        }
    }

    private static synchronized void loadCollection() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            TreeSet<City> loaded = (TreeSet<City>) in.readObject();
            collection.getCities().addAll(loaded);
            System.out.println("Загружено " + loaded.size() + " городов");
        } catch (Exception e) {
            System.err.println("Ошибка загрузки коллекции: " + e.getMessage());
        }
    }

    private static synchronized void saveCollection() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(collection);
            System.out.println("Коллекция сохранена (" + collection.getCities().size() + " городов)");
        } catch (Exception e) {
            System.err.println("Ошибка сохранения коллекции: " + e.getMessage());
        }
    }

    private static void shutdown() {
        isRunning = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
        System.out.println("Сервер остановлен");
    }
}