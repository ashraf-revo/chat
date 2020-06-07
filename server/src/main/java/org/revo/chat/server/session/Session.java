package org.revo.chat.server.session;

import lombok.Getter;
import org.revo.chat.server.message.DelimiterBasedMessageEncoder;
import org.revo.chat.server.message.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Session {
    private final String sessionId;
    private final Socket socket;
    private PrintWriter wtr;
    private final Map<String, String> sessionInfo = new HashMap<>();

    public Session(Socket socket, String sessionId) {
        this.socket = socket;
        this.sessionId = sessionId;
        try {
            this.wtr = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Session put(String key, String value) {
        this.sessionInfo.put(key, value);
        return this;
    }

    public synchronized void send(Message message) {
        if (wtr == null && !socket.isConnected()) return;
        DelimiterBasedMessageEncoder encoder = new DelimiterBasedMessageEncoder();
        encoder.apply(message).forEach(wtr::print);
        wtr.flush();
    }

}
