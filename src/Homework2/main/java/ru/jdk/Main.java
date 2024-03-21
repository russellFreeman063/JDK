package ru.jdk;

import ru.jdk.client.Client;
import ru.jdk.client.ClientController;
import ru.jdk.server.Server;
import ru.jdk.server.ServerController;
import ru.jdk.repository.FileStorage;


public class Main {
    public static void main(String[] args) {
        ServerController serverController = new ServerController(new Server(), new FileStorage());
        new ClientController(new Client(), serverController);
        new ClientController(new Client(), serverController);
    }
}
