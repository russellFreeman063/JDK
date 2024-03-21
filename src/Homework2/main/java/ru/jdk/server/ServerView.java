package ru.jdk.server;

public interface ServerView {
    void showMessage(String message);
    void setServerController(ServerController serverController);
}
