package org.revo.chat.controller;

import org.junit.Test;
import org.revo.chat.server.Request;
import org.revo.chat.server.message.Message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MainControllerTest extends MockSession {

    @Test
    public void whenLoginWithValidUserShouldReturn200() {
        Message message = new Message();
        message.setPath("LOGIN");
        message.setPayload("revo:revo");
        MainController.onLOGIN.accept(new Request<>(message, getMockSession()));
        Message response = readData(getOutputStream());
        assertThat(response.getStatus().trim(), is("200"));
    }

    @Test
    public void whenLoginWithInValidUserShouldReturn430() {
        Message message = new Message();
        message.setPath("LOGIN");
        message.setPayload("revo:revo11");
        MainController.onLOGIN.accept(new Request<>(message, getMockSession()));
        Message response = readData(getOutputStream());
        assertThat(response.getStatus().trim(), is("403"));
    }




}
