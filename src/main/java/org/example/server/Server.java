package org.example.server;


import org.example.common.Request;
import org.example.common.Response;
import org.example.common.commands.Command;
import org.example.common.model.entity.City;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;


public class Server {
    private static final int PORT = 80805;
    private static final TreeSet<City> collection = new TreeSet<>();
    private static final String SAVE_FILE = "collection.dat";

    public static void main(String[] args) {
        loadCollection();

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false); // Неблокирующий режим

            Selector selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен " + PORT);

            while (true) {
                selector.select(); // Блокируемся до готовности каналов
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isAcceptable()) {
                        acceptClient(serverChannel, selector);
                    }
                    if (key.isReadable()) {
                        processClientRequest(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        } finally {
            saveCollection();
        }
    }

    private static void acceptClient(ServerSocketChannel serverChannel, Selector selector)
            throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("Новый клиент подключен");
    }

    private static void processClientRequest(SelectionKey key) {
        try (SocketChannel channel = (SocketChannel) key.channel();
             ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel));
             ObjectOutputStream out = new ObjectOutputStream(Channels.newOutputStream(channel))) {

            Request request = (Request) in.readObject();
            Response response = handleCommand(request);
            out.writeObject(response);

        } catch (Exception e) {
            System.err.println("Ошибка обработки запроса: " + e.getMessage());
        }
    }

    private static Response handleCommand(Request request) {
        try {
            Command command = CommandFactory.getCommand(request.getCommandName());
            return command.execute(collection, request);
        } catch (IllegalArgumentException e) {
            return new Response("Ошибка: " + e.getMessage());
        }
    }

    private static void loadCollection() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            Object obj = in.readObject();
            if (obj instanceof TreeSet) {
                collection.addAll((TreeSet<City>) obj);
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки городов: " + e.getMessage());
        }
    }

    private static void saveCollection() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(collection);
        } catch (Exception e) {
            System.err.println("Ошибка сохранения колекции: " + e.getMessage());
        }
    }
}