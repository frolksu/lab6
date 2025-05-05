package org.example.client;

import org.example.common.Request;
import org.example.common.Response;

import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 80805;

    public static void main(String[] args) {
        try (Scanner consoleScanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("Введите команду (help для справки):");
                String input = consoleScanner.nextLine().trim();
                if (input.equals("exit")) break;

                try (SocketChannel socketChannel = SocketChannel.open(
                        new InetSocketAddress(SERVER_HOST, SERVER_PORT))) {

                    Request request = parseCommand(input);
                    sendRequest(socketChannel, request);
                    Response response = receiveResponse(socketChannel);
                    System.out.println(response.getMessage());
                    if (response.getData() != null) {
                        System.out.println(response.getData());
                    }
                } catch (ConnectException e) {
                    System.err.println("Сервер недоступен. Попробуйте позже.");
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        }
    }

    private static Request parseCommand(String input) {
        String[] parts = input.split(" ", 2);
        String cmd = parts[0];
        Object[] args = (parts.length > 1) ? new Object[]{parts[1]} : new Object[0];
        return new Request(cmd, args);
    }

    private static void sendRequest(SocketChannel channel, Request request) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(Channels.newOutputStream(channel));
        oos.writeObject(request);
    }

    private static Response receiveResponse(SocketChannel channel) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(channel));
        return (Response) ois.readObject();
    }
}