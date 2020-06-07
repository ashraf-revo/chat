package org.revo.chat.server.session;

import org.revo.chat.server.Request;
import org.revo.chat.server.message.Message;
import org.revo.chat.utils.Defines;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {
    private static final Map<String, Session> sessionsBySessionIdRegistry = new ConcurrentHashMap<>();
    private static final Map<String, String> sessionsByUsernameRegistry = new ConcurrentHashMap<>();


    public static void save(Request<Message> request, String username) {
        request.getSession().put(Defines.USERNAME_KEY, username).put(Defines.IS_AUTH_KEY, "true");
        sessionsByUsernameRegistry.put(username, request.getSession().getSessionId());
    }

    public static Session getSession(Request<Message> request) {
        return sessionsBySessionIdRegistry.get(request.getSession().getSessionId());
    }

    public static String getUsername(Request<Message> request) {
        return request.getSession().getSessionInfo().get(Defines.USERNAME_KEY);
    }

    public static Session getSession(String username) {
        if (sessionsByUsernameRegistry.containsKey(username)) {
            String sessionId = sessionsByUsernameRegistry.get(username);
            if (sessionsBySessionIdRegistry.containsKey(sessionId)) {
                return sessionsBySessionIdRegistry.get(sessionId);
            }

        }
        return null;
    }

    public static Map<String, String> getSessionInfo(Request<Message> request) {
        if (request.getSession()!=null&&request.getSession().getSessionId() != null && !request.getSession().getSessionId().isEmpty())
            return sessionsBySessionIdRegistry.get(request.getSession().getSessionId()).getSessionInfo();
        else
            return new HashMap<>();
    }

    public static boolean isAuth(Request<Message> request) {
        String isAuth = SessionRegistry.getSessionInfo(request).get(Defines.IS_AUTH_KEY);
        return isAuth != null && isAuth.equals("true");
    }

    public static Session createSession(Session session) {
        sessionsBySessionIdRegistry.put(session.getSessionId(), session);
        return session;
    }

    public static void close(Request<Message> request) {
        try {
            request.getSession().getSocket().close();
            sessionsBySessionIdRegistry.remove(request.getSession().getSessionId());
            if (request.getSession().getSessionInfo().containsKey(Defines.USERNAME_KEY))
                sessionsByUsernameRegistry.remove(request.getSession().getSessionInfo().get(Defines.USERNAME_KEY));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Set<String> activeUsers() {
        return sessionsByUsernameRegistry.keySet();
    }


    /*
     * @param username if null will broadcast to all active user
     * */
    public static void sendTo(String username, String message) {
        if (username == null || username.trim().isEmpty()) {
            SessionRegistry.activeUsers().forEach(it -> sendTo(it, message));
        } else {
            Session session = SessionRegistry.getSession(username);
            if (session != null) {
                Message send = new Message();
                send.setPath("SEND");
                send.setPayload(message);
                session.send(send);
            }
        }
    }


}
