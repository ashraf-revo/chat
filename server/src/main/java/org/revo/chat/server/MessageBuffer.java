package org.revo.chat.server;

import lombok.Getter;
import org.revo.chat.server.message.Message;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Function;

@Getter
class MessageBuffer {
    private final Function<String, Message> converter;
    private final Scanner bufferedReader;

    public MessageBuffer(Function<String, Message> converter, Socket socket) throws IOException {
        this.converter = converter;
        this.bufferedReader = new Scanner(new InputStreamReader(socket.getInputStream()));
    }
}
