package com.example.gblocalchat;

import com.example.gblocalchat.Client.ChatClient;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelloController {
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
        if(message != null && !message.isEmpty()){
            chatClient.sendMessage(message);
            messageField.clear();
            messageField.requestFocus();
        }
    }

    public void addMessage(String message) {
        messageArea.appendText(message + "\n");
    }
}