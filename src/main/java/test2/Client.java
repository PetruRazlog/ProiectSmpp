package test2;

import org.jsmpp.bean.*;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.*;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.jsmpp.util.TimeFormatter;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Client implements Runnable, MessageReceiverListener, SessionStateListener {

    private static final TimeFormatter TIME_FORMATTER =
            new AbsoluteTimeFormatter();
    static String message = getMsg();
    private static final byte[] empty_arr = new byte[0];
    static SMPPSession session = new SMPPSession();

    @Override
    public void run() {
        session.setMessageReceiverListener(this);
        session.addSessionStateListener(this);

        try {

            OptionalParameter payload =
                    new OptionalParameter.Message_payload(
                            message.getBytes()
                    );

            session.connectAndBind(
                    "localhost",
                    8081,
                    new BindParameter(
                            BindType.BIND_TRX,
                            "test",
                            "test",
                            "",
                            TypeOfNumber.INTERNATIONAL,
                            NumberingPlanIndicator.ISDN,
                            ""
                    ),
                    2000
            );

            TimeUnit.SECONDS.sleep(2);
            session.submitShortMessage(
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
                    empty_arr,
                    payload
            );


//            session.unbindAndClose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new Client().run();



    }

    private static String getMsg() {
        return "Java ".repeat(99);
    }

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) {
//        System.out.println(session.getSessionState());
//        System.out.println(session.getMessageReceiverListener());
        try {
            System.out.println(deliverSm.getShortMessageAsDeliveryReceipt().getText());
        } catch (InvalidDeliveryReceiptException e) {
            System.out.println("MESAJ NU A FOST LIVRAT.");
        }
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {

    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) {
        return null;
    }

    @Override
    public void onStateChange(SessionState sessionState, SessionState sessionState1, Session session) {
        System.out.println(sessionState.toString());
    }
}