package org.revo.client;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.revo.chat.server.message.DelimiterBasedMessageDecoder;
import org.revo.chat.server.message.DelimiterBasedMessageEncoder;
import org.revo.chat.server.message.Message;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

public class ChatClient extends Flux<Message> {
    private Socket socket;
    private PrintWriter wtr;
    private Scanner scanner;
    private final DelimiterBasedMessageDecoder delimiterBasedMessageDecoder = new DelimiterBasedMessageDecoder();
    private final DelimiterBasedMessageEncoder delimiterBasedMessageEncoder = new DelimiterBasedMessageEncoder();
    private Flux<Message> messageFlux;

    public static ChatClient connect(String address, int port) throws IOException {
        ChatClient chatClient = new ChatClient();
            chatClient.socket = new Socket(address, port);
            chatClient.wtr = new PrintWriter(chatClient.socket.getOutputStream());
            chatClient.scanner = new Scanner(chatClient.socket.getInputStream());
            chatClient.messageFlux = Flux.generate(() -> chatClient, (ChatClient it, SynchronousSink<Message> r) -> {
                try {
                    if (!chatClient.socket.isClosed() && chatClient.scanner.hasNext()) {
                        Message apply = chatClient.delimiterBasedMessageDecoder.apply(chatClient.scanner.nextLine());
                        r.next(apply);
                    } else
                        r.complete();
                } catch (Exception e) {
                    r.error(e);
                }
                return it;
            })
                    .filter(it->it!=Message.EMPTY)
                    .subscribeOn(Schedulers.parallel());


        return chatClient;
    }

    public synchronized void send(Message message) {
        if (message == null || message == Message.EMPTY) return;
        delimiterBasedMessageEncoder.apply(message).forEach(it -> wtr.print(it));
        wtr.flush();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void subscribe(CoreSubscriber<? super Message> coreSubscriber) {
        this.messageFlux.subscribe(coreSubscriber);
    }
}
