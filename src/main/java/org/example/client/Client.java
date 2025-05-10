package org.example.client;

import org.example.common.Request;
import org.example.common.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключено к серверу. Введите команду (help для справки):");

            while (true) {
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) break;

                try {
                    Request request = RequestBuilder.buildRequest(input);
                    oos.writeObject(request);
                    oos.flush();

                    Object response = ois.readObject();
                    if (response instanceof Response) {
                        System.out.println("Сервер: " + ((Response) response).getMessage());
                    }
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }
}