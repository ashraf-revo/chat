package org.revo.chat.server;

import org.revo.chat.server.message.Message;

import java.util.Objects;
import java.util.function.Consumer;

public class Route {
    private final String path;
    private final Consumer<Request<Message>> function;

    public Route(String path, Consumer<Request<Message>> function) {
        this.path = path;
        this.function = function;
    }

    public static Route route(String path, Consumer<Request<Message>> function) {
        return new Route(path, function);
    }

    public String getPath() {
        return path;
    }

    public Consumer<Request<Message>> getFunction() {
        return function;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
