package test2;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

/**
 * @author a.rusu
 */
public class SessionStateListenerImpl implements SessionStateListener {
    @Override
    public void onStateChange(SessionState sessionState, SessionState sessionState1, Session session) {
        System.out.println(sessionState.toString());
    }
}
