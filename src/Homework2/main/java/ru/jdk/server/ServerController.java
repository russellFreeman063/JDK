package ru.jdk.server;

import ru.jdk.client.ClientController;
import ru.jdk.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class ServerController {
    private ServerView serverView;
    private boolean isWork;
    private List<ClientController> clientControllerList;
    private Repository<String> repository;

    public ServerController(ServerView serverView, Repository<String> repository) {
        this.serverView = serverView;
        this.repository = repository;
        clientControllerList = new ArrayList<>();
        serverView.setServerController(this);
    }

    public String getHistory() {
        return repository.load();
    }

    public void start(){
        if (isWork){
            showOnWindow("Сервер уже был запущен");
        } else {
            isWork = true;
            showOnWindow("Сервер запущен!");
        }
    }

    public void stop(){
        if (!isWork){
            showOnWindow("Сервер уже был остановлен");
        } else {
            isWork = false;
            while (!clientControllerList.isEmpty()){
                disconnect(clientControllerList.get(clientControllerList.size() - 1));
            }
            showOnWindow("Сервер остановлен!");
        }
    }

    public boolean connect(ClientController clientController){
        if (!isWork){
            return false;
        }
        clientControllerList.add(clientController);
        showOnWindow(clientController.getName() + " подключился");
        return true;
    }

    public void disconnect(ClientController clientController){
        clientControllerList.remove(clientController);
        if (clientController != null){
            clientController.disconnectFromServer();
            showOnWindow(clientController.getName() + " отключился");
        }
    }


    public void message(String text){
        if (!isWork){
            return;
        }
        showOnWindow(text);
        answer(text);
        saveInHistory(text);
    }



    private void answer(String text){
        for (ClientController clientController : clientControllerList){
            clientController.answerFromServer(text);
        }
    }

    private void showOnWindow(String text){
        serverView.showMessage(text + "\n");
    }

    private void saveInHistory(String text){
        repository.save(text);
    }
}
