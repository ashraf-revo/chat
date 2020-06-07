package org.revo.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.revo.chat.server.message.Message;
import org.revo.client.ChatClient;

import java.util.function.Consumer;

public class Controller {
    private final ChatClient chatClient;
    private final Consumer<Message> consumer = (s) -> {
        this.textArea.appendText(s.toString() + "\n");
    };
    @FXML
    private TextArea textArea;
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private TextField to;
    @FXML
    private TextField message;

    public Controller() {
        this.chatClient = ChatClient.connect("localhost", 9888, this.consumer, x -> Platform.exit(), Platform::exit);
    }

    public void close() {
        this.chatClient.close();
    }


    @FXML
    public void login() {
        Message login = new Message();
        login.setPath("LOGIN");
        login.setPayload(this.username.getText() + ":" + this.password.getText());
        this.chatClient.send(login);
    }

    @FXML
    public void me() {
        Message me = new Message();
        me.setPath("ME");
        this.chatClient.send(me);
    }

    @FXML
    public void send() {
        Message me = new Message();
        me.setPath("SEND");
        me.setPayload(to.getText() + "-->" + message.getText());
        this.chatClient.send(me);
    }

}
