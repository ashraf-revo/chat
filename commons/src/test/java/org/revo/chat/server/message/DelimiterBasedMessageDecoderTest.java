package org.revo.chat.server.message;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class DelimiterBasedMessageDecoderTest {
    @Test
    public void decodeTextToMessage() {
        DelimiterBasedMessageDecoder delimiterBasedMessageDecoder = new DelimiterBasedMessageDecoder();
        assertThat(delimiterBasedMessageDecoder.apply("PATH : LOGIN\r\n"), is(Message.EMPTY));
        assertThat(delimiterBasedMessageDecoder.apply("STATUS : 200\r\n"), is(Message.EMPTY));
        Message before = delimiterBasedMessageDecoder.apply("PAYLOAD : payload\r\n");
        assertThat(before, is(Message.EMPTY));
        assertThat(before.getPath(), nullValue());
        assertThat(before.getStatus(), nullValue());
        assertThat(before.getPayload(), is(""));
        Message apply = delimiterBasedMessageDecoder.apply("~&\r\n");
        assertThat(apply, not(Message.EMPTY));
        assertThat(apply.getPath(), is("LOGIN\r\n"));
        assertThat(apply.getStatus(), is("200\r\n"));
        assertThat(apply.getPayload(), is("payload\r\n"));
    }
}
