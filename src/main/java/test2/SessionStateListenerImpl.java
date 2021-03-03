package test2;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

/**
 * @author a.rusu
 */
public class SessionStateListenerImpl implements SessionStateListener {
    @Override
    //TODO: добавить logger и написать нормальный логер с какого на какой статк поменялось и ид сессей
    public void onStateChange(SessionState sessionState, SessionState sessionState1, Session session) {
        System.out.println(sessionState.toString());
    }
}
