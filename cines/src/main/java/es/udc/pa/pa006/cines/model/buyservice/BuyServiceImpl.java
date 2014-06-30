package es.udc.pa.pa006.cines.model.buyservice;

import java.util.Calendar;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa006.cines.model.buy.Buy;
import es.udc.pa.pa006.cines.model.buy.BuyDao;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovieDao;
import es.udc.pa.pa006.cines.model.userprofile.UserProfile;
import es.udc.pa.pa006.cines.model.userprofile.UserProfileDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("buyService")
@Transactional
public class BuyServiceImpl implements BuyService {

	@Autowired
	private BuyDao buyDao;

	@Autowired
	private UserProfileDao userProfileDao;

	@Autowired
	private SessionMovieDao sessionMovieDao;

	@Transactional(readOnly = true)
	public Buy findBuy(long buyId) throws InstanceNotFoundException {
		return buyDao.find(buyId);
	}

	@Transactional(readOnly = true)
	public BuyBlock findBuysByUser(long userId, int startIndex, int count) {
		List<Buy> buys = buyDao.findBuysByUser(userId, startIndex, count + 1);

		boolean existMoreBuys = buys.size() == (count + 1);

		if (existMoreBuys) {
			buys.remove(buys.size() - 1);
		}

		return new BuyBlock(buys, existMoreBuys);
	}

	public void deliverTickets(long buyId) throws InstanceNotFoundException,
			TicketsAlreadyDelivered, MovieAlreadyStartedException {
		Buy buy = buyDao.find(buyId);
		if (buy.getDelivered()) {
			throw new TicketsAlreadyDelivered(buyId);
		}
		if (Calendar.getInstance()
				.after(buy.getSessionMovie().getDateSession())) {
			throw new MovieAlreadyStartedException(buy.getSessionMovie()
					.getDateSession(), Calendar.getInstance());
		}
		buy.setDelivered(true);
	}

	public long buyTickets(int tickets, String cardNumber,
			Calendar expirationDate, long sessionId, long userId)
			throws InstanceNotFoundException, NoAvaliableSeatsException,
			MovieAlreadyStartedException, InvalidAttributeValueException {
		UserProfile user = userProfileDao.find(userId);
		SessionMovie session = sessionMovieDao.find(sessionId);
		if (Calendar.getInstance().after(session.getDateSession())) {
			throw new MovieAlreadyStartedException(session.getDateSession(),
					Calendar.getInstance());
		}
		if (Calendar.getInstance().after(expirationDate)) {
			throw new InvalidAttributeValueException(expirationDate.toString());
		}
		Buy buy = new Buy(tickets, cardNumber, expirationDate);
		if (session.getAvaliableSeats() < tickets) {
			throw new NoAvaliableSeatsException(tickets,
					session.getAvaliableSeats());
		}
		session.setAvaliableSeats(session.getAvaliableSeats() - tickets);
		buy.setUserProfile(user);
		buy.setSessionMovie(session);
		buyDao.save(buy);
		return buy.getBuyId();
	}

}
