package org.revo.chat.server.message;


import org.revo.chat.server.utils.Defines;
import org.revo.chat.server.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DelimiterBasedMessageEncoder implements Function<Message, List<String>> {

    @Override
    public List<String> apply(Message message) {
        ArrayList<String> lines = new ArrayList<>();
        if (Util.nonNullEmpty(message.getPath())) {
            lines.add(Message.PATH_KEY + Defines.propertyDelimiter + message.getPath() + Defines.LineDelimiter);
        }
        if (Util.nonNullEmpty(message.getStatus())) {
            lines.add(Message.STATUS_KEY + Defines.propertyDelimiter + message.getStatus() + Defines.LineDelimiter);
        }
        if (Util.nonNullEmpty(message.getPayload())) {
            lines.add(Message.PAYLOAD_KEY + Defines.propertyDelimiter + message.getPayload() + Defines.LineDelimiter);
        }
        lines.add(Defines.delimiter);
        return lines.size()>1?lines:null;
    }

}
