package org.revo.chat;

import org.junit.Before;
import org.junit.Test;
import org.revo.chat.server.message.DelimiterBasedMessageEncoder;
import org.revo.chat.server.message.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatServerTest {

    @Before
    public void init() {
//            try {
////                ChatServer.main(new String[]{});
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


    }

    @Test
    public void testRun() throws IOException {

//        Socket socket = new Socket("localhost", 9888);
//        PrintWriter wtr = new PrintWriter(socket.getOutputStream());
//
//        Message message = new Message();
//        message.setPath("LOGIN");
//        message.setPayload("fsdfas");
//        message.setStatus("200");
//        new DelimiterBasedMessageEncoder().apply(message).forEach(wtr::print);
//        wtr.flush();

    }
}
