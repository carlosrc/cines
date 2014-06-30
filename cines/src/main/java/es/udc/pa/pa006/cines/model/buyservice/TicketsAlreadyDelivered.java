package es.udc.pa.pa006.cines.model.buyservice;

@SuppressWarnings("serial")

public class TicketsAlreadyDelivered extends Exception {

    private long buyId;

    public TicketsAlreadyDelivered(long buyId) {
	super("Tickets Already Delivered exception => buy id = " + buyId);
	this.buyId = buyId;
    }

    public long getBuyId() {
	return buyId;
    }

}
