package org.example.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.example.common.Request;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключено к серверу. Введите команду (help для справки):");

            while (true) {
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) break;

                Request request = createRequest(input);
                if (request == null) continue;

                // Отправка запроса
                oos.writeObject(request);
                oos.flush();

                // Чтение ответа (текстового)
                String response = reader.readLine();
                if (response == null) {
                    System.out.println("Сервер закрыл соединение");
                    break;
                }
                System.out.println("Сервер: " + response);
            }
        } catch (IOException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }

    private static Request createRequest(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        Object argument = parts.length > 1 ? parts[1] : null;

        return new Request(command, argument);
    }
}