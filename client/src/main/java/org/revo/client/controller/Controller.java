package org.revo.client.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.revo.chat.server.message.Message;
import org.revo.client.ChatClient;
import reactor.core.Disposable;

import java.io.IOException;
import java.util.function.Consumer;

public class Controller {
    private ChatClient chatClient;
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
    private Disposable disposable;

    public Controller() {
        try {
            this.chatClient = ChatClient.connect("localhost", 9888);
            this.disposable = this.chatClient.subscribe(this.consumer, err -> Platform.exit(), Platform::exit);
        } catch (IOException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    public void close() {
        disposable.dispose();
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
    private final Consumer<Message> consumer = (s) -> {
        this.textArea.appendText(s.toString() + "\n");
    };

}
