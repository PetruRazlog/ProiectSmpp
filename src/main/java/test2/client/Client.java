package test2.client;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Client implements Runnable {

    private final Logger logger = Logger.getLogger(Client.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    static String clientShortMsg = "TEST SHORT MESSAGE IN SUBMIT_SM METHOD";

    @Override
    public void run() {
        SMPPSession session = new SMPPSession();
        session.setMessageReceiverListener(MessageReceiverListenerImpl.getInstance());
        session.addSessionStateListener(SessionStateListenerImpl.getInstance());
        long messageId = 0;

        ClientConfig clientConfig =
                new ClientConfig("admin", "admin","localhost",8081);
        try {

            logger.info("Connecting to "+ clientConfig.getHost() +
                        " port "+ clientConfig.getPort() +
                        " systemId " + clientConfig.getSystemId());
            session.connectAndBind(
                    clientConfig.getHost(),
                    clientConfig.getPort(),
                    BindType.BIND_TRX,
                    clientConfig.getSystemId(),
                    clientConfig.getPassword(),
                    "unfn",
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.ISDN,
                    "",
                    2000
            );

            logger.info("Connected to "+ clientConfig.getHost() +
                        " port "+ clientConfig.getPort() +
                        " systemId " + clientConfig.getSystemId());

            TimeUnit.SECONDS.sleep(2);
            //try to send sumbit
            String remoteId = session.submitShortMessage(
                    "CMT",
                    TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN,
                    "sourceAddress",
                    TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN,
                    "destAddress",
                    new ESMClass(
                            MessageMode.DEFAULT,
                            MessageType.SMSC_DEL_RECEIPT,
                            GSMSpecificFeature.DEFAULT
                    ),
                    (byte) 0,
                    (byte) 1,
                    TIME_FORMATTER.format(
                            new Date()
                    ),
                    null,
                    new RegisteredDelivery(
                            SMSCDeliveryReceipt.DEFAULT
                    ),
                    (byte) 0,
                    new GeneralDataCoding(
                            Alphabet.ALPHA_DEFAULT,
                            MessageClass.CLASS1,
                            false
                    ),
                    (byte) 0,
                    clientShortMsg.getBytes()
            );

            logger.info("Submitted message with ID= "+ messageId+
                        " to "                       + clientConfig.getHost() +
                        " port "                     + clientConfig.getPort() +
                        " systemId "                 + clientConfig.getSystemId());
            messageId = Long.parseLong(remoteId);

//            session.unbindAndClose();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ResponseTimeoutException e) {
            e.printStackTrace();
        } catch (NegativeResponseException e) {
            e.printStackTrace();
        } catch (PDUException e) {
            e.printStackTrace();
        }
        logger.info("Sent message with ID= "+ messageId+
                " to "                       + clientConfig.getHost() +
                " port "                     + clientConfig.getPort() +
                " systemId "                 + clientConfig.getSystemId());
    }

    public void taskScheduling(Client client){
        ScheduledExecutorService executorService =
                Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> scheduledFuture =
                executorService.scheduleAtFixedRate(client, 0, 2, TimeUnit.SECONDS);

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.taskScheduling(client);
        client.run();

    }

}