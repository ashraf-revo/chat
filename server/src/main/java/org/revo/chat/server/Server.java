package org.revo.chat.server;

import org.revo.chat.server.message.Message;
import org.revo.chat.server.session.SessionRegistry;
import org.revo.chat.server.utils.Env;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.publisher.UnicastProcessor;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Server extends Flux<Void> implements Closeable {
    private final UnicastProcessor<Request<Message>> processor = UnicastProcessor.create();
    private final ServerSocket serverSocket;
    Flux<Socket> generate;
    private List<Route> routes;
    private Disposable disposable;
    private Predicate<Request<Message>> predicate = messageRequest -> true;

    public Server(Env env) throws IOException {
        this.serverSocket = new ServerSocket(env.getPort());
        this.generate = Flux.generate(() -> this.serverSocket, (ServerSocket r1, SynchronousSink<Socket> r2) -> {
            try {
                r2.next(r1.accept());
                System.out.println("one connected");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r1;
        });

    }


    @Override
    public void close() throws IOException {
        this.disposable.dispose();
        this.serverSocket.close();
    }


    @Override
    public void subscribe(CoreSubscriber<? super Void> coreSubscriber) {
        this.disposable = this.processor.publish().autoConnect()
                .filter(predicate).subscribe(it ->
                        this.routes.stream().filter(itx -> itx.getPath().trim().equals(it.getPayload().getPath().trim()))
                                .forEach(v -> {
                                    try {
                                        v.getFunction().accept(it);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                })
                );
        this.generate.flatMap(r -> {
            ServerHandler serverHandler = new ServerHandler(processor, r);
            SessionRegistry.createSession(serverHandler.getSession());
            return serverHandler.get();
        }).subscribe(coreSubscriber);
    }

    /*
     * @param routes array for server path based routes
     * */
    public Server when(Route... routes) {
        this.routes = Arrays.asList(routes);
        return this;
    }

    /*
     * @param predicate filter run before any route
     * */
    public Server gard(Predicate<Request<Message>> predicate) {
        this.predicate = predicate;
        return this;
    }
}


