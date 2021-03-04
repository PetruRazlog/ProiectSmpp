package test2.server;

import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.*;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.MessageId;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class ServerMessageReceiverListenerImpl implements ServerMessageReceiverListener {

    private static ServerMessageReceiverListenerImpl serverMessageReceiverListener;

    public static ServerMessageReceiverListenerImpl getInstance(){
        if (serverMessageReceiverListener == null) {
            serverMessageReceiverListener = new ServerMessageReceiverListenerImpl();
        }
        return serverMessageReceiverListener;
    }

    private final Logger logger = Logger.getLogger(Server.class);
    private final AtomicLong messageIDGenerator = new AtomicLong(0);
    static String serverShortMsg = "TEST MESSAGE SENT FROM SERVER";


    public MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession smppServerSession) {
        var messageId = messageIDGenerator.incrementAndGet();

        logger.info("id  = " + messageId +
                " Length = " +serverShortMsg.length()  +
                " MSG= " + serverShortMsg );

        DeliveryReceipt deliveryReceipt =
                new DeliveryReceipt(
                        Long.toString(messageId),
                        1,
                        1,
                        new Date(),
                        new Date(),
                        DeliveryReceiptState.DELIVRD,
                        null,
                        (serverShortMsg.length()<=20)?serverShortMsg:serverShortMsg.substring(0,20)
                );

        new Thread(() -> sendDLR(smppServerSession, deliveryReceipt)).start();
        try {
            return new MessageId(Long.toString(messageId));
        } catch (PDUStringException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendDLR(SMPPServerSession session, DeliveryReceipt deliveryReceipt) {
        try {
            TimeUnit.SECONDS.sleep(2);

            session.deliverShortMessage(
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
                    (byte)0,
                    (byte)0,
                    new RegisteredDelivery(
                            SMSCDeliveryReceipt.DEFAULT
                    ),
                    new GeneralDataCoding(
                            Alphabet.ALPHA_DEFAULT,
                            MessageClass.CLASS1,
                            false
                    ),
                    deliveryReceipt.toString().getBytes()

            );
        } catch (
                PDUException | ResponseTimeoutException | InvalidResponseException | NegativeResponseException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Message sent to client!!!");
    }

    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession smppServerSession) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }

    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) throws ProcessRequestException {
        throw new ProcessRequestException("No Implemented", 0);
    }
}
