package org.revo.chat.server.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {
    private String path;
    private String status;
    private String payload = "";

    public static final String PATH_KEY = "PATH";
    public static final String STATUS_KEY = "STATUS";
    public static final String PAYLOAD_KEY = "PAYLOAD";
    public static final Message EMPTY = new Message();
}
