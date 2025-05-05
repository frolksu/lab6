package org.example.server;

import org.example.common.Request;
import org.example.common.Response;
import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.logging.*;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final int PORT = 80805;
    private ServerSocketChannel serverChannel;
    private final CityCollection cityCollection;
    private final CommandHandler commandHandler;

    public Server() {
        this.cityCollection = new CityCollection();
        this.commandHandler = new CommandHandler();
        loadInitialData();
    }

    private void loadInitialData() {
        String filename = System.getenv("CITY_DATA_FILE");
        if (filename != null && !filename.isEmpty()) {
            try {
                CityFileLoader.loadCities(cityCollection, filename);
                logger.info("Коллекция успешно загружена из файла: " + filename);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Ошибка загрузки данных из файла", e);
            }
        } else {
            logger.warning("Переменная окружения CITY_DATA_FILE не установлена. Коллекция пуста.");
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);

            logger.info("Сервер запущен на порту " + PORT);
            logger.info("Текущий размер коллекции: " + cityCollection.getCities().size());

            while (true) {
                SocketChannel clientChannel = serverChannel.accept();
                if (clientChannel != null) {
                    handleClient(clientChannel);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критическая ошибка сервера", e);
        } finally {
            saveDataBeforeShutdown();
        }
    }

    private void handleClient(SocketChannel clientChannel) {
        try (ObjectInputStream ois = new ObjectInputStream(
                Channels.newInputStream(clientChannel));
             ObjectOutputStream oos = new ObjectOutputStream(
                     Channels.newOutputStream(clientChannel))) {

            Request request = (Request) ois.readObject();
            Response response = commandHandler.handle(request);
            oos.writeObject(response);

            logger.info("Обработана команда: " + request.getCommandName());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Ошибка обработки клиента", e);
        }
    }

    private void saveDataBeforeShutdown() {
        String filename = System.getenv("CITY_OUTDATA_FILE");
        if (filename != null) {
            try {
                CityFileSaver.saveCities(cityCollection, filename);
                logger.info("Коллекция сохранена в файл: " + filename);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Ошибка сохранения коллекции", e);
            }
        }
    }
}