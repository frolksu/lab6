package org.example.client;

import org.example.common.Request;
import org.example.common.Response;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключено к серверу. Введите команду (help для справки):");

            while (true) {
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) break;

                try {
                    Request request = RequestBuilder.buildRequest(input);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(request);
                        oos.flush();
                    }

                    byte[] data = baos.toByteArray();
                    dos.write(data);
                    dos.flush();

                    // Чтение ответа от сервера
                    int respLen = dis.readInt();
                    byte[] respData = new byte[respLen];
                    dis.readFully(respData);  // Читаем полный ответ от сервера

                    // Десериализация ответа
                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(respData))) {
                        Object response = ois.readObject();
                        if (response instanceof Response) {
                            System.out.println("Сервер: " + ((Response) response).getData());
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка подключения: " + e.getMessage());
        }
    }
}