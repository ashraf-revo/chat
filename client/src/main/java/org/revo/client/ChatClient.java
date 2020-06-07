package org.revo.client;

import org.revo.chat.server.message.DelimiterBasedMessageDecoder;
import org.revo.chat.server.message.DelimiterBasedMessageEncoder;
import org.revo.chat.server.message.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private PrintWriter wtr;
    private Scanner scanner;
    private final DelimiterBasedMessageDecoder delimiterBasedMessageDecoder = new DelimiterBasedMessageDecoder();
    private final DelimiterBasedMessageEncoder delimiterBasedMessageEncoder = new DelimiterBasedMessageEncoder();

    public static ChatClient connect(String address, int port, Consumer<Message> on, Consumer<Exception> error,Runnable complete) {
        ChatClient chatClient = new ChatClient();
        try {
            chatClient.socket = new Socket(address, port);
            chatClient.wtr = new PrintWriter(chatClient.socket.getOutputStream());
            chatClient.scanner = new Scanner(chatClient.socket.getInputStream());
            new Thread(() -> {
                while (!chatClient.socket.isClosed() && chatClient.scanner.hasNext()) {
                    Message apply = chatClient.delimiterBasedMessageDecoder.apply(chatClient.scanner.nextLine());
                    if (apply != null && apply != Message.EMPTY) {
                        on.accept(apply);
                    }
                }
                complete.run();
            }).start();
        } catch (IOException e) {
            error.accept(e);
        }

        return chatClient;
    }

    public synchronized void send(Message message) {
        if (message == null || message == Message.EMPTY) return;
        delimiterBasedMessageEncoder.apply(message).forEach(it -> {
            wtr.print(it);
        });
        wtr.flush();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
