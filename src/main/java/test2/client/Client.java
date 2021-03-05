package test2.client;

import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@SuppressWarnings({"TryWithIdenticalCatches", "InfiniteLoopStatement"})
public class Client {

    private final Logger logger = Logger.getLogger(Client.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    static String clientShortMsg = "TEST SHORT MESSAGE IN SUBMIT_SM METHOD";
    private static Integer counter = 0;

    public void start(){
        SMPPSession session = new SMPPSession();
        session.setMessageReceiverListener(MessageReceiverListenerImpl.getInstance());
        session.addSessionStateListener(SessionStateListenerImpl.getInstance());


        ClientConfig clientConfig =
                new ClientConfig("admin", "admin","localhost",8081);

        bind(clientConfig,session);
    }

    public void bind(ClientConfig clientConfig, SMPPSession smppSession) {
        final ScheduledExecutorService bindExecutorService = Executors.newSingleThreadScheduledExecutor();

        bindExecutorService.scheduleAtFixedRate(() -> {
            logger.info("Connecting to "+ clientConfig.getHost() +
                    " port "+ clientConfig.getPort() +
                    " systemId " + clientConfig.getSystemId());
            try {
                smppSession.connectAndBind(
                        clientConfig.getHost(),
                        clientConfig.getPort(),
                        BindType.BIND_TRX,
                        clientConfig.getSystemId(),
                        clientConfig.getPassword(),
                        "unifun",
                        TypeOfNumber.INTERNATIONAL,
                        NumberingPlanIndicator.ISDN,
                        "",
                        2000
                );

                while(true){
                    sendMessage(smppSession, clientConfig, counter++);
                    logger.info("Connected to "+ clientConfig.getHost() +
                            " port "+ clientConfig.getPort() +
                            " systemId " + clientConfig.getSystemId());}
            } catch (IOException e) {
                logger.info("Bind unsuccessful on socket " + clientConfig.getHost()+ ";"+ clientConfig.getPort());
            }
        }, 0 ,2, TimeUnit.SECONDS);
    }


    private void sendMessage(SMPPSession session, ClientConfig clientConfig, int counter) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //try to send submit
        String remoteId = null;

        try {
            remoteId = session.submitShortMessage(
                    "CMT",
                    TypeOfNumber.ALPHANUMERIC,
                    NumberingPlanIndicator.UNKNOWN,
                    "TestSource",
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.ISDN,
                    "123456789",
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
                    (clientShortMsg+" nr: "+counter).getBytes()
            );
        } catch (PDUException e) {
            e.printStackTrace();
        } catch (ResponseTimeoutException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (NegativeResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Submitted message with ID= "+ remoteId+
                " to "                       + clientConfig.getHost() +
                " port "                     + clientConfig.getPort() +
                " systemId "                 + clientConfig.getSystemId());
    }

    public static void main(String[] args) {
        Client client = new Client();
        new Thread(client::start).start();

    }

}