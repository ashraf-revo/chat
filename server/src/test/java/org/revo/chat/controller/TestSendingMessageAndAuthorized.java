package org.revo.chat.controller;

import org.junit.Test;
import org.revo.chat.server.Request;
import org.revo.chat.server.message.Message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestSendingMessageAndAuthorized extends MockSession {
    @Test
    public void whenSendMessageToMeAndAuthorized() {
        whenLoginWithValidUserShouldReturn200();
        Message message = new Message();
        message.setPath("SEND");
        String payload = "TOME";
        message.setPayload("revo-->" + payload);
        MainController.onSEND.accept(new Request<>(message, getMockSession()));
        Message response = readData(getOutputStream());
        assertThat(response.getPayload().trim(), is(payload));
    }

    @Test
    public void whenLoginWithValidUserShouldReturn200() {
        Message message = new Message();
        message.setPath("LOGIN");
        message.setPayload("revo:revo");
        MainController.onLOGIN.accept(new Request<>(message, getMockSession()));
        Message response = readData(getOutputStream());
        assertThat(response.getStatus().trim(), is("200"));
    }

}
