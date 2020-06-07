package org.revo.chat.server.utils;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String username;
    private String password;
}
