package com.example.gblocalchat;

import com.example.gblocalchat.Client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HelloController {
    @FXML
    private VBox registerBox;
    @FXML
    private TextField registerLoginField;
    @FXML
    private TextField registerPasswordField;
    @FXML
    private TextField registerNickField;
    @FXML
    private HBox loginBox;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox messagebox;
    @FXML
    private ListView<String> clientlist;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea messageArea;

    final ChatClient chatClient;

    File history = new File("historyOfChat.txt");


    JdbcBaseHandler base = new JdbcBaseHandler();

    public HelloController() {
        chatClient = new ChatClient(this);
    }

    @FXML
    protected void onHelloButtonClick() {
        final String message = messageField.getText();
        if (message != null && !message.isEmpty()) {
            chatClient.sendMessage(message);
            messageField.clear();
            messageField.requestFocus();
        }
    }

    public void addMessage(String message) {
        messageArea.appendText(message + "\n");
    }

    public void btnAuthClick(ActionEvent actionEvent) {
        chatClient.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());

    }

    public void selectClient(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2){
            final String message = messageField.getText();
            final String client = clientlist.getSelectionModel().getSelectedItem();
            messageField.setText("/w " + client + " " + message);
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }

    public void setAuth(boolean isAuthSucces) {
        loginBox.setVisible(!isAuthSucces);
        messagebox.setVisible(isAuthSucces);
    }

    public void setRegisterWindowVisible(boolean isAuthSucces) {
        loginBox.setVisible(!isAuthSucces);
        registerBox.setVisible(isAuthSucces);
    }

    public void updateClientList(String[] clients) {
        clientlist.getItems().clear();
        clientlist.getItems().addAll(clients);

    }

    public void btnRegisterClick(ActionEvent actionEvent) {
        setRegisterWindowVisible(true);
    }

    public void btnLoginWindowClick(ActionEvent actionEvent) {
        setRegisterWindowVisible(false);
    }

    public void btnRegisterNewAccount(ActionEvent actionEvent) {
        try {
            base.connect();
            base.insert(registerLoginField.getText(), registerPasswordField.getText(), registerNickField.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Если файла нет, то создаем его. И, как вы показывали по паттерну "Декоратор" создаем PrintWriter,
     * а потом создаем буферизированный поток куда передаем наш PrintWriter. Берем текст из messageArea и заносим
     * текст в файл.
     */
    public void saveHistory(){
        try {
            if (!history.exists()) {
                System.out.println("не существует.\n Создаю файл...");
                history.createNewFile();
            }
            PrintWriter writer = new PrintWriter(new FileWriter(history, false));
            BufferedWriter bufWriter = new BufferedWriter(writer);
            bufWriter.write(messageArea.getText());
            bufWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Нужно подумать над тем, как именно последние 100 строк показывать...но это не первая проблема, я вообще не
     * уверен, что тут сделано все правильно, да и вообще кажется, что я придумал новый паттерн
     * "Китайский завод велосипедов".
     */
    public void loadHistory(){
        try {
            List<String> historyList;
            FileInputStream in = new FileInputStream(history);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            //Опять идея оказалась умнее меня и мой индусский код заменила на одну строчку...
            historyList = bufferedReader.lines().collect(Collectors.toList());
            IntStream.range(2, historyList.size()).forEach(i -> messageArea.appendText(historyList.get(i) + "\n"));
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}