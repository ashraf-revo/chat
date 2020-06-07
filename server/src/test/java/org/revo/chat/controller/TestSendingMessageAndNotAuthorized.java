package org.revo.chat.controller;

import org.junit.Test;
import org.revo.chat.server.Request;
import org.revo.chat.server.message.Message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestSendingMessageAndNotAuthorized extends MockSession{
    @Test
    public void whenSendMessageToMeAndNotAuthorized() {
        Message message = new Message();
        message.setPath("SEND");
        String payload = "TOME";
        message.setPayload("revo-->" + payload);
        MainController.onSEND.accept(new Request<>(message, getMockSession()));
        Message response = readData(getOutputStream());
        assertThat(response, nullValue());
    }

}
