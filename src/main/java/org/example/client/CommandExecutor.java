package org.example.client;

import org.example.common.Request;
import org.example.common.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

public class CommandExecutor {
    private SocketChannel channel;
    private final Deque<String> history = new ArrayDeque<>(5);

    public Response sendRequest(Request request) {
            addToHistory(request.getCommandName());

            try {
                channel = SocketChannel.open(new InetSocketAddress("localhost", 80805));

                ObjectOutputStream oos = new ObjectOutputStream(Channels.newOutputStream(channel));
                oos.writeObject(request);

                ObjectInputStream ois = new ObjectInputStream(Channels.newInputStream(channel));
                Response response = (Response) ois.readObject();

                return response;
            } catch (Exception e) {
                return new Response("ERROR", "Ошибка подключения: " + e.getMessage());
            } finally {
                if (channel != null) {
                    channel.close();
                }
            }
        }
    }

    private void addToHistory(String commandName) {
        if (history.size() >= 5) history.removeFirst();
        history.addLast(commandName);
    }
}
