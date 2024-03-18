package ru.jdk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Server extends JFrame {

    public static final int POX_X = 500;
    public static final int POX_Y = 550;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private JButton btnStart;
    private JButton btnStop;
    private JTextArea log;
    private boolean isServerWorking;
    private static final List<Client> connectedClient = new ArrayList<>();
    private static final Stack<String> clientsLog = new Stack<>();


    private JPanel createButtons(){
        JPanel panelButtons = new JPanel(new GridLayout(1,2));
        btnStart = new JButton("Start");
        btnStop = new JButton("Stop");
        panelButtons.add(btnStop);
        panelButtons.add(btnStart);
        return panelButtons;
    }

    private JScrollPane createLog(){
        log = new JTextArea();
        log.setEditable(false);

        return new JScrollPane(log);
    }

    public Server(){
        isServerWorking = false;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POX_X, POX_Y, WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Chat server");

        add(createButtons(), BorderLayout.SOUTH);
        btnStop.addActionListener(e -> {
            isServerWorking = false;
            sendResponseToDisconnectionForClients();
            log.append("Server stopped \n");
            for (Client client : connectedClient) {
                log.append(client.getLogin() + " disconnect to server \n");
            }
            System.out.println("Server stopped " + isServerWorking + '\n');
        });

        btnStart.addActionListener(e -> {
            isServerWorking = true;
            sendResponseToSuccessConnectionForClients();
            log.append("Server started \n");
            for (Client client : connectedClient) {
                log.append(client.getLogin() + " connect to server \n");
            }
            System.out.println("Server started " + isServerWorking + '\n');
        });

        add(createLog(), BorderLayout.CENTER);

        try(FileInputStream logPuller = new FileInputStream("chat/serverLog.chat")) {
            int read;
            while ((read = logPuller.read()) != -1){
                log.append(String.valueOf((char) read));
            }
        } catch (Exception e) {}

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(isServerWorking){
                    log.append("Server stopped \n");
                    for (Client client : connectedClient) {
                        log.append(client.getLogin() + " disconnect to server \n");
                    }
                }
                SaverLog.saveLog(Arrays.asList(log.getText()), "server");
                SaverLog.saveLog(clientsLog, "clients");
                super.windowClosing(e);
            }
        });

        setVisible(true);
    }


    public void addClient(Client client){
        connectedClient.add(client);
    }

    public void removeClient(String clientLogin){
        for (int i = 0; i < connectedClient.size(); i++) {
            if(connectedClient.get(i).getLogin().equals(clientLogin)){
                connectedClient.remove(i);
            }
        }
    }

    public void sendMessage(String sender, String message){
        if(!isServerWorking || stringIsEmpty(message)){
            return;
        }
        clientsLog.push(sender + ": " + message + '\n');
        for (Client client : connectedClient) {
            client.getLog().append(clientsLog.peek());
        }
        log.append(sender + ": " + message + '\n');
    }
    private void sendResponseToSuccessConnectionForClients(){
        for (Client client : connectedClient) {
            client.sendResponseConnectedStatus(true);

            try(FileInputStream logPuller = new FileInputStream("chat/clientsLog.chat")) {
                int read;
                while ((read = logPuller.read()) != -1){
                    client.getLog().append(String.valueOf((char) read));
                    clientsLog.add(String.valueOf((char) read));
                }
            } catch (Exception e) {}
        }
    }

    private void sendResponseToDisconnectionForClients() {
        for (Client client : connectedClient) {
            client.sendResponseConnectedStatus(false);
        }
    }

    public void sendMessageToServer(String message){
        log.append(message);
    }

    public boolean stringIsEmpty(String str){
        if (str.isEmpty()) return true;

        char[] strArray = str.toCharArray();
        int countNotEmpty = 0;
        for (char elem : strArray) {
            if(elem != ' ') countNotEmpty++;
        }
        return countNotEmpty < 1;
    }

}
