package test2.server;

import org.apache.log4j.Logger;
import org.jsmpp.SMPPConstant;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("InfiniteLoopStatement")
public class Server implements Runnable {

    private final int port;
    private final Logger logger = Logger.getLogger(Server.class);

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        ArrayList<User> users = new ArrayList<>();

        users.add(new User("test", "test"));
        users.add(new User("admin", "admin"));
        users.add(new User("petru", "razlog"));
        users.add(new User("bill", "gates"));
        users.add(new User("domnul", "atotputernic"));

        try {
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port);

            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                serverSession.setMessageReceiverListener(ServerMessageReceiverListenerImpl.getInstance());
                BindRequest request = serverSession.waitForBind(5000);
                boolean result = users
                        .stream()
                        .anyMatch(user -> user.getUsername().equalsIgnoreCase(request.getSystemId())
                                && user.getParola().equalsIgnoreCase(request.getPassword()));

                if (result) {
                    logger.info("Client logged in!!!");
                    request.accept(request.getSystemId());
                }
                else {
                    logger.error("Warning. Unauthorized user!!!");
                    request.reject(SMPPConstant.STAT_ESME_RBINDFAIL);
                }

            TimeUnit.MILLISECONDS.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8081);
        server.run();

    }

}