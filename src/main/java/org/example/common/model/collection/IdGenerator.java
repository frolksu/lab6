package org.example.common.model.collection;

public class IdGenerator {
    private static long nextId = 1;

    public static synchronized long getNextId() {
        return nextId++;
    }

    public static synchronized void updateId(long loadedId) {
        if (loadedId >= nextId) {
            nextId = loadedId + 1;
        }
    }
}