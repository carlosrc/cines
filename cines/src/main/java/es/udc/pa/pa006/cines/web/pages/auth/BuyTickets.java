/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.auth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.management.InvalidAttributeValueException;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa006.cines.model.buyservice.BuyService;
import es.udc.pa.pa006.cines.model.buyservice.MovieAlreadyStartedException;
import es.udc.pa.pa006.cines.model.buyservice.NoAvaliableSeatsException;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.web.pages.NotFound;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class BuyTickets {

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Long sessionId;

	@Property
	private SessionMovie session;
	@Property
	private int tickets;

	@Property
	private String cardNumber;

	@Property
	private int expirationDateMonth;

	@Property
	private int expirationDateYear;

	@Property
	private String dateSession;

	@Property
	private Long buyId;

	@Component
	private Form buyTicketsForm;

	@Component(id = "tickets")
	private TextField ticketsField;

	@Component(id = "expirationDateMonth")
	private TextField dateField;

	@Inject
	private Messages messages;

	@Inject
	private Locale locale;

	@Inject
	private BuyService buyService;

	@Inject
	private MovieService movieService;

	@InjectPage
	private BuyCreated buyCreated;

	@InjectPage
	private NotFound notFound;

	Object onActivate(String sessionId) {
		try {
			this.sessionId = Long.parseLong(sessionId);
		} catch (NumberFormatException e) {
			return NotFound.class;
		}
		try {
			session = movieService.findSessionMovie(this.sessionId);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
			dateSession = sdf.format(session.getDateSession().getTime());
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		}
		return null;
	}

	Object onValidateFromBuyTicketsForm() {

		if (!buyTicketsForm.isValid()) {
			return null;
		}
		Calendar expirationDate = new GregorianCalendar(expirationDateYear,
				expirationDateMonth, 0);
		try {
			buyId = buyService.buyTickets(tickets, cardNumber, expirationDate,
					sessionId, userSession.getUserProfileId());
		} catch (InstanceNotFoundException e) {
			return notFound;
		} catch (NoAvaliableSeatsException e) {
			buyTicketsForm.recordError(ticketsField,
					messages.format("error-noAvaliableSeats"));
		} catch (MovieAlreadyStartedException e) {
			buyTicketsForm.recordError(messages
					.format("error-movieAlreadyStarted"));
		} catch (InvalidAttributeValueException e) {
			buyTicketsForm.recordError(messages
					.format("error-invalidExpirationDate"));
		}
		return null;
	}

	Object[] onPassivate() {
		return new Object[] { sessionId };
	}

	Object onSuccess() {
		buyCreated.setBuyId(buyId);
		return buyCreated;
	}

}