package com.example.gblocalchat;

import com.example.gblocalchat.Client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;

public class HelloController {
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

    public void updateClientList(String[] clients) {
        clientlist.getItems().clear();
        clientlist.getItems().addAll(clients);

    }
}