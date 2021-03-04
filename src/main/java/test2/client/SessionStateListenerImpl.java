package test2.client;

import org.apache.log4j.Logger;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

import java.text.MessageFormat;

/**
 * @author a.rusu
 */
public class SessionStateListenerImpl implements SessionStateListener {

    private static SessionStateListenerImpl sessionStateListenerImpl;

    public static SessionStateListenerImpl getInstance(){
        if (sessionStateListenerImpl == null){
             sessionStateListenerImpl = new SessionStateListenerImpl();
        }
        return sessionStateListenerImpl;
    }

    private final Logger logger = Logger.getLogger(Client.class);

    @Override
    //TODO показать oldstate и newState
    public void onStateChange(SessionState sessionState, SessionState sessionState1, Session session) {
        logger.info(MessageFormat.format("Sessions state: old state {0}, new state {1}, sessionId {2}", sessionState1, sessionState, session.getSessionId()));
    }
}
