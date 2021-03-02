package test2;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * @author a.rusu
 */
public class MessageReceiverListenerImpl implements MessageReceiverListener {


    @Override
    public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
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
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session session) throws ProcessRequestException {
        return null;
    }
}
