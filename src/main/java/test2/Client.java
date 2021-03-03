package test2;

import org.jsmpp.bean.*;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Client implements Runnable{

    private static final TimeFormatter TIME_FORMATTER =
            new AbsoluteTimeFormatter();
    static String message = getMsg();
    private static final byte[] empty_arr = new byte[0];

    @Override
    public void run() {
        SMPPSession session = new SMPPSession();
        session.setMessageReceiverListener(new MessageReceiverListenerImpl());
        session.addSessionStateListener(new SessionStateListenerImpl());

        try {

            OptionalParameter payload =
                    new OptionalParameter.Message_payload(
                            message.getBytes()
                    );

            session.connectAndBind(
                    "localhost",
                    8081,
                    BindType.BIND_TRX,
                    "admin",
                    "admi",
                    "unfn",
                    TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.ISDN,
                    "",
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
}