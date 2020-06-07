package org.revo.chat.server.message;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class DelimiterBasedMessageEncoderTest {
    @Test
    public void encodeMessageToText() {
        Message message = new Message();
        message.setPath("LOGIN");
        message.setPayload("payload");
        message.setStatus("200");
        List<String> apply = new DelimiterBasedMessageEncoder().apply(message);
        assertThat(apply, hasItem("PATH : LOGIN\r\n"));
        assertThat(apply, hasItem("STATUS : 200\r\n"));
        assertThat(apply, hasItem("PAYLOAD : payload\r\n"));
        assertThat(apply, hasItem("~&\r\n"));
    }
}
