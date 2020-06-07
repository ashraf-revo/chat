package org.revo.chat.controller;

import org.revo.chat.server.Request;
import org.revo.chat.server.Route;
import org.revo.chat.server.message.Message;
import org.revo.chat.server.session.SessionRegistry;
import org.revo.chat.server.utils.Util;
import org.revo.chat.services.EnvLoader;
import org.revo.chat.services.Impl.InMemoryUserServiceImpl;
import org.revo.chat.services.UserService;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.revo.chat.server.Route.route;

public class MainController {
    private final static UserService userService = new InMemoryUserServiceImpl(EnvLoader.getEnv().getUsers());

    public static Predicate<Request<Message>> gard = request -> {
        if (request.getPayload().getPath().equals("LOGIN")) {
            return true;
        } else {
            if (SessionRegistry.isAuth(request)) {
                return true;
            } else {
                SessionRegistry.close(request);
                return false;
            }
        }
    };

    public static Route[] when() {
        return new Route[]{
                route("LOGIN", (s) -> {
                    String[] split = s.getPayload().getPayload().split(":");
                    userService.findByUsernameAndPasswordMatch(split[0], split[1]).ifPresent(it ->
                            SessionRegistry.save(s, it.getUsername()));
                }),
                route("SEND", (s) -> {
                    if (s.getPayload().getPayload().contains("-->")) {
                        String[] split = s.getPayload().getPayload().split("-->");
                        SessionRegistry.sendTo(split[0], split[1]);
                        Util.dumb(SessionRegistry.getUsername(s), split[1] + "\n", true);
                        if (s.getPayload().getPayload().contains("-->Bye Bye")) {
                            SessionRegistry.close(s);
                        }
                    }

                }),
                route("ME", onME),
                route("OPTIONS", (s) -> System.out.println("options"))
        };
    }

    private static final Consumer<Request<Message>> onME = (s) -> {
        Message message = new Message();
        message.setStatus("200");
        message.setPath("ME");
        message.setPayload(SessionRegistry.getUsername(s));
        s.getSession().send(message);
    };

}
