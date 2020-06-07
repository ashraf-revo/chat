package org.revo.chat.server.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Env {
    private Integer port;
    private List<User> users;
}
