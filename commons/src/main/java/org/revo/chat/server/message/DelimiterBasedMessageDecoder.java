package org.revo.chat.server.message;

import org.revo.chat.server.utils.Defines;

import java.util.ArrayList;
import java.util.function.Function;

public class DelimiterBasedMessageDecoder implements Function<String, Message> {
    ArrayList<String> lines = new ArrayList<>();

    @Override
    public Message apply(String s) {
        if (s.trim().equals(Defines.delimiter.trim())) {
            Message decode = decode(this.lines);
            this.lines = new ArrayList<>();
            return decode;
        } else {
            lines.add(s);
            return Message.EMPTY;
        }
    }

    static private Message decode(ArrayList<String> lines) {
        if (lines.size() == 0) return null;
        Message message = new Message();
        for (String line : lines) {
            if (line.startsWith(Message.PATH_KEY + Defines.propertyDelimiter)) {
                message.setPath(line.replace(Message.PATH_KEY + Defines.propertyDelimiter, ""));
            }
            if (line.startsWith(Message.STATUS_KEY + Defines.propertyDelimiter)) {
                message.setStatus(line.replace(Message.STATUS_KEY + Defines.propertyDelimiter, ""));
            }
            if (line.startsWith(Message.PAYLOAD_KEY + Defines.propertyDelimiter)) {
                message.setPayload(line.replace(Message.PAYLOAD_KEY + Defines.propertyDelimiter, ""));
            }
        }

        return message;
    }
}
