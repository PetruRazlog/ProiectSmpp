package test2;

import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.*;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.*;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("InfiniteLoopStatement")
public class Server implements Runnable, ServerMessageReceiverListener {

    public static void main(String[] args) {
        Server server = new Server(8081);
        server.run();

    }

    private final Logger logger = Logger.getLogger(Server.class);
    private final int port;
    private final MessageIDGenerator messageIDGenerator =
            new RandomMessageIDGenerator();

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        try {
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port);

            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                serverSession.setMessageReceiverListener(this);
                BindRequest request = serverSession.waitForBind(5000);
                if ("test".equals(request.getSystemId()) &&
                        "test".equals(request.getPassword())) {
                    request.accept("test");

                } else if (!"test".equals(request.getPassword())) {

                    request.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                } else if (!"test".equals(request.getSystemId())) {
                    request.reject(SMPPConstant.STAT_ESME_RINVSYSID);
                }
            TimeUnit.MILLISECONDS.sleep(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession smppServerSession) {
        MessageId messageId = messageIDGenerator.newMessageId();
        OptionalParameter.Message_payload payload =
                (OptionalParameter.Message_payload) submitSm.getOptionalParameter(
                        OptionalParameter.Tag.MESSAGE_PAYLOAD);
        String valueAsString = payload.getValueAsString();

        logger.info("id  = " + messageId +
                    " Length = " +valueAsString.length()  +
                    " MSG= " + valueAsString );

        DeliveryReceipt deliveryReceipt =
                new DeliveryReceipt(
                        Integer.valueOf(messageId.getValue(),16).toString(),
                        1,
                        1,
                        new Date(),
                        new Date(),
                        DeliveryReceiptState.DELIVRD,
                        "EROARE",
                        "BLA-BLA-BLA-BLA");

        new Thread(() -> sendDLR(smppServerSession, deliveryReceipt)).start();
        return messageId;
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

    public void sendDLR(SMPPServerSession session, DeliveryReceipt deliveryReceipt) {
        try {
            TimeUnit.SECONDS.sleep(2);

            session.deliverShortMessage(
                    "CMT",
                    TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN,
                    "1",
                    TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN,
                    "2",
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
                PDUException |
                        ResponseTimeoutException |
                        InvalidResponseException |
                        NegativeResponseException |
                        IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}