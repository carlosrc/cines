package es.udc.pa.pa006.cines.model.buyservice;

import java.util.Calendar;

import javax.management.InvalidAttributeValueException;

import es.udc.pa.pa006.cines.model.buy.Buy;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface BuyService {

	public Buy findBuy(long buyId) throws InstanceNotFoundException;

	public BuyBlock findBuysByUser(long userId, int startIndex, int count);

	public void deliverTickets(long buyId) throws InstanceNotFoundException,
			TicketsAlreadyDelivered, MovieAlreadyStartedException;

	public long buyTickets(int tickets, String cardNumber,
			Calendar expirationDate, long sessionId, long userId)
			throws InstanceNotFoundException, NoAvaliableSeatsException,
			MovieAlreadyStartedException, InvalidAttributeValueException;

}
