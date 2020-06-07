package org.revo.chat.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.revo.chat.server.session.Session;

@Getter
@AllArgsConstructor
public class Request<T> {
    private final T payload;
    private final Session session;
}
