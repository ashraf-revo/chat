package org.revo.chat.server;

import org.reactivestreams.Publisher;
import org.revo.chat.server.message.DelimiterBasedMessageDecoder;
import org.revo.chat.server.message.Message;
import org.revo.chat.server.session.Session;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.publisher.UnicastProcessor;
import reactor.core.scheduler.Schedulers;

import java.net.Socket;
import java.util.UUID;
import java.util.function.Supplier;

class ServerHandler implements Supplier<Publisher<Void>> {
    private final UnicastProcessor<Request<Message>> processor;
    private final Session session;

    public ServerHandler(UnicastProcessor<Request<Message>> processor, Socket socket) {
        this.processor = processor;
        this.session = new Session(socket, UUID.randomUUID().toString());
    }

    public void accept(Message message) {
        processor.onNext(new Request<>(message, this.session));
    }

    @Override
    public Publisher<Void> get() {
        return Flux.generate(() -> new MessageBuffer(new DelimiterBasedMessageDecoder(), this.session.getSocket())
                , (MessageBuffer messageBuffer, SynchronousSink<Message> emitter) -> {
                    if (messageBuffer.getBufferedReader().hasNext() && !this.session.getSocket().isClosed()) {
                        emitter.next(messageBuffer.getConverter()
                                .apply(messageBuffer.getBufferedReader().nextLine()));
                    } else {
                        emitter.complete();
                    }
                    return messageBuffer;
                }).filter(it -> it != Message.EMPTY)
                .doOnNext(this::accept).subscribeOn(Schedulers.parallel()).then();
    }

    public Session getSession() {
        return session;
    }
}
