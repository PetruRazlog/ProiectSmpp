package test2.client;

import org.apache.log4j.Logger;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import test2.server.Server;

/**
 * @author a.rusu
 */
public class MessageReceiverListenerImpl implements MessageReceiverListener {

    private static MessageReceiverListenerImpl messageReceiverListenerImpl;

    public static MessageReceiverListenerImpl getInstance(){
        if(messageReceiverListenerImpl == null){
            messageReceiverListenerImpl = new MessageReceiverListenerImpl();
        }
        return messageReceiverListenerImpl;
    }

    private final Logger logger = Logger.getLogger(Server.class);

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) {
        try {
            DeliveryReceipt deliveryReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
            logger.info(
                    "Receiving from " + deliverSm.getSourceAddr() +
                    " to " + deliverSm.getDestAddress() +
                    "delivery: " + deliveryReceipt);
        } catch (InvalidDeliveryReceiptException e) {
            logger.error("Error. Undelivered message!!!", e);

        }
    }

    @Override
    public void onAcceptAlertNotification(AlertNotification alertNotification) {

    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) {
        return null;
    }
}
