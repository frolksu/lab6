package org.example.client;

import org.example.common.Request;
import org.example.common.Response;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 80805;
    private static final int MAX_RECONNECT_ATTEMPTS = 3;
    private static final int RECONNECT_DELAY_MS = 2000;
    private static final Deque<String> commandHistory = new ArrayDeque<>(5);

    public static void main(String[] args) {
        try (Scanner consoleScanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("Введите команду (help для справки): ");
                String input = consoleScanner.nextLine();

                if (input.isEmpty()) continue;
                if (input.equalsIgnoreCase("exit")) break;

                processCommand(input);
            }
        }
    }


    private static void processCommand(String input) {
        try {
            Request request = parseCommand(input);
            Response response = sendRequestWithRetry(request);
            handleResponse(response);
            if (!input.equals("history")) {
                if (commandHistory.size() >= 5) commandHistory.removeFirst();
                commandHistory.addLast(input.split(" ")[0]);
            }
            if (input.equals("history")) {
                System.out.println("Последние команды: " + commandHistory);
                return;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private static Response sendRequestWithRetry(Request request) throws IOException, ClassNotFoundException {
        int attempts = 0;
        while (attempts < MAX_RECONNECT_ATTEMPTS) {
            try (SocketChannel socketChannel = SocketChannel.open(
                    new InetSocketAddress(SERVER_HOST, SERVER_PORT))) {

                sendRequest(socketChannel, request);
                return receiveResponse(socketChannel);

            } catch (ConnectException e) {
                attempts++;
                if (attempts < MAX_RECONNECT_ATTEMPTS) {
                    System.err.printf("Сервер недоступен. Попытка %d/%d...%n",
                            attempts, MAX_RECONNECT_ATTEMPTS);
                    try {
                        Thread.sleep(RECONNECT_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Прервано ожидание подключения");
                    }
                } else {
                    throw new ConnectException("Не удалось подключиться после " + attempts + " попыток");
                }
            }
        }
        throw new ConnectException("Сервер недоступен");
    }

    private static Request parseCommand(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = parts.length > 1 ? parts[1] : null;

        return new Request(command, argument);
    }

    private static void sendRequest(SocketChannel channel, Request request) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Channels.newOutputStream(channel));
        oos.writeObject(request);
        oos.flush();
    }

    private static Response receiveResponse(SocketChannel channel) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(channel));
        return (Response) ois.readObject();
    }

    private static void handleResponse(Response response) {
        if (response.getMessage() != null) {
            System.out.println(response.getMessage());
        }
        if (response.getData() != null) {
            System.out.println(response.getData());
        }
    }
}