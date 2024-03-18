package ru.jdk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {

    private final Server SERVER;
    private boolean isConnected = false;

    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    private JTextArea log;
    private JTextField tfIPAddress;
    private JTextField tfPort;
    private JTextField tfLogin;
    private JPasswordField tfPassword;
    private JTextField tfMessage;
    private JButton btnSend;

    private JPanel createTop(String IP, String port, String login, String password){
        JPanel panel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField(IP);
        tfPort = new JTextField(port);
        tfLogin = new JTextField(login);
        tfPassword = new JPasswordField(password);
        panel.add(tfIPAddress);
        panel.add(tfPort);
        panel.add(tfLogin);
        panel.add(tfPassword);
        return panel;
    }

    private JPanel createButtons(){
        JPanel panelButton = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        btnSend = new JButton("Send");
        panelButton.add(tfMessage, BorderLayout.CENTER);
        panelButton.add(btnSend, BorderLayout.EAST);
        return panelButton;
    }

    private JScrollPane createLog(){
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    public Client(String ip, String port, String login, String password, Server SERVER) {
        this.SERVER = SERVER;
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat Client: " + login);
        add(createTop(ip, port, login, password), BorderLayout.NORTH);
        add(createButtons(), BorderLayout.SOUTH);
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    sendMessageToServer();
                }
                super.keyPressed(e);
            }
        });
        btnSend.addActionListener(e -> sendMessageToServer());

        add(createLog());
        if(SERVER != null){
            SERVER.addClient(this);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SERVER.sendMessageToServer(tfLogin.getText() + " disconnect to server \n");
                SERVER.removeClient(tfLogin.getText());

                super.windowClosing(e);
            }
        });

        setVisible(true);
    }

    public String getLogin() {
        return tfLogin.getText();
    }

    public JTextArea getLog() {
        return log;
    }

    public void sendResponseConnectedStatus(boolean isConnected){
        this.isConnected = isConnected;
        String statusText = isConnected ? "Connection success \n \n" : "Connection failed \n \n";
        log.append(statusText);
    }

    public void sendMessageToServer(){
        if(isConnected){
            SERVER.sendMessage(tfLogin.getText(), tfMessage.getText());
            tfMessage.setText(null);
        }
    }

}
