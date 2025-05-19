package org.example.client;

import org.example.common.Request;
import org.example.common.Response;
import org.example.common.model.entity.City;

import java.io.*;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Client {
    private static final int HISTORY_SIZE = 5;
    private static final Deque<String> commandHistory = new ArrayDeque<>();
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
                    if (input.equalsIgnoreCase("history")) {
                        showHistory();
                        continue;
                    }
                    addToHistory(input);

                    if (input.startsWith("update ")) {
                        Request checkRequest = RequestBuilder.buildRequest(input);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                            oos.writeObject(checkRequest);
                            oos.flush();
                        }

                        byte[] data = baos.toByteArray();
                        dos.write(data);
                        dos.flush();

                        int respLen = dis.readInt();
                        byte[] respData = new byte[respLen];
                        dis.readFully(respData);

                        Response checkResponse;
                        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(respData))) {
                            checkResponse = (Response) ois.readObject();
                        }

                        if (checkResponse.getMessage().equals("ERROR")) {
                            System.out.println(checkResponse.getData());
                            continue;
                        }

                        City existingCity = (City) checkResponse.getData();
                        City updatedCity = new City(existingCity.getId(),existingCity.getName(), existingCity.getCoordinates(), existingCity.getArea(), existingCity.getPopulation(),existingCity.getMetersAboveSeaLevel(),existingCity.getCarCode(),existingCity.getAgglomeration(),existingCity.getStandardOfLiving(),existingCity.getGovernor());updatedCity.setId(existingCity.getId()); // Важно сохранить ID

                        CityFactory.updateCityFields(updatedCity);

                        Request updateRequest = new Request("update", updatedCity);

                        ByteArrayOutputStream updateBaos = new ByteArrayOutputStream();
                        try (ObjectOutputStream oos = new ObjectOutputStream(updateBaos)) {
                            oos.writeObject(updateRequest);
                            oos.flush();
                        }

                        byte[] updateData = updateBaos.toByteArray();
                        dos.write(updateData);
                        dos.flush();

                        int updateRespLen = dis.readInt();
                        byte[] updateRespData = new byte[updateRespLen];
                        dis.readFully(updateRespData);

                        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(updateRespData))) {
                            Response updateResponse = (Response) ois.readObject();
                            System.out.println(updateResponse.getData());
                        }

                        continue;
                    }

                    Request request = RequestBuilder.buildRequest(input);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                        oos.writeObject(request);
                        oos.flush();
                    }

                    byte[] data = baos.toByteArray();
                    dos.write(data);
                    dos.flush();

                    int respLen = dis.readInt();
                    byte[] respData = new byte[respLen];
                    dis.readFully(respData);

                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(respData))) {
                        Object response = ois.readObject();
                        if (response instanceof Response) {
                            System.out.println(((Response) response).getData());
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
    private static void addToHistory(String command) {
        if (commandHistory.size() >= HISTORY_SIZE) {
            commandHistory.removeFirst();
        }
        commandHistory.addLast(command);
    }

    private static void showHistory() {
        if (commandHistory.isEmpty()) {
            System.out.println("История команд пуста");
            return;
        }

        System.out.println("Последние " + HISTORY_SIZE + " команд:");
        int counter = 1;
        for (String cmd : commandHistory) {
            System.out.println(counter++ + ". " + cmd);
        }
    }

}