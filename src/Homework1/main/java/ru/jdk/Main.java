package ru.jdk;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        new Client("127.0.0.1", "8080", "Johnny","Silverhand", server);
        new Client("127.0.0.1", "8081", "Vi","2077", server);
    }
}