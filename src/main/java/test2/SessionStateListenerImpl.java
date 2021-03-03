package test2;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.apache.log4j.Logger;

import java.text.MessageFormat;

/**
 * @author a.rusu
 */
public class SessionStateListenerImpl implements SessionStateListener {
    private final Logger logger = Logger.getLogger(Client.class);
    @Override

    //TODO показать oldstate и newState
    public void onStateChange(SessionState sessionState, SessionState sessionState1, Session session) {
        logger.info(MessageFormat.format("Sessions state: {0}", sessionState));
    }
}
