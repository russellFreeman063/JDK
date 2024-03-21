package ru.jdk.client;

public interface ClientView {

    void showMessage(String message);

    void disconnectedFromServer();

    void setClientController(ClientController clientController);
}
