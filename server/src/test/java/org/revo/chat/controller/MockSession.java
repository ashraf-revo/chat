package org.revo.chat.controller;

import org.revo.chat.server.message.DelimiterBasedMessageDecoder;
import org.revo.chat.server.message.Message;
import org.revo.chat.server.session.Session;
import org.revo.chat.server.session.SessionRegistry;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class MockSession {
    private ByteArrayOutputStream outputStream;
    private Session mockSession;

    public MockSession() {
        outputStream = new ByteArrayOutputStream();
        mockSession = SessionRegistry.createSession(new Session(new Socket() {
            @Override
            public OutputStream getOutputStream() {
                return outputStream;
            }
        }, "sessionId"));

    }

    public static Message readData(ByteArrayOutputStream outputStream) {
        DelimiterBasedMessageDecoder decoder = new DelimiterBasedMessageDecoder();
        return Arrays.stream(new String(outputStream.toByteArray()).split("\n"))
                .map(decoder).filter(it -> it != null && it != Message.EMPTY).reduce((first, second) -> second).orElse(null);
    }

    public ByteArrayOutputStream getOutputStream() {
        return outputStream;
    }

    public Session getMockSession() {
        return mockSession;
    }
}
