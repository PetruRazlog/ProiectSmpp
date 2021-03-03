package test2;

import org.apache.log4j.Logger;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * @author a.rusu
 */
public class MessageReceiverListenerImpl implements MessageReceiverListener {

    private final Logger logger = Logger.getLogger(Server.class);

    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) {
        try {
            //TODO: Добавить logger.info вместо sout
            logger.info(deliverSm.getShortMessageAsDeliveryReceipt());
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
