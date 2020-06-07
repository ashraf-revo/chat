package org.revo.chat;

import org.revo.chat.server.Server;
import org.revo.chat.services.EnvLoader;

import java.io.IOException;

import static org.revo.chat.controller.MainController.gard;
import static org.revo.chat.controller.MainController.when;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server(EnvLoader.getEnv()).gard(gard).when(when());
        server.subscribe();
    }
}
