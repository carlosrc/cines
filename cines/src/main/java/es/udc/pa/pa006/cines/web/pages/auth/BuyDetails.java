/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.auth;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa006.cines.model.buy.Buy;
import es.udc.pa.pa006.cines.model.buyservice.BuyService;
import es.udc.pa.pa006.cines.model.buyservice.MovieAlreadyStartedException;
import es.udc.pa.pa006.cines.model.buyservice.TicketsAlreadyDelivered;
import es.udc.pa.pa006.cines.web.pages.NotFound;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.SALESMAN)
public class BuyDetails {
	private Long buyId;
	private Buy buy;

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private String buyDate;

	@Property
	private String dateSession;

	@Inject
	private Locale locale;

	@Inject
	private BuyService buyService;

	@Component
	private Form deliverTicketsForm;

	@Inject
	private Messages messages;

	@InjectComponent
	private Zone tableZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRederer;

	public void setBuyId(Long buyId) {
		this.buyId = buyId;
	}

	public Long getBuyId() {
		return buyId;
	}

	public void setBuy(Buy buy) {
		this.buy = buy;
	}

	public Buy getBuy() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
		buyDate = sdf.format(buy.getBuyDate().getTime());
		dateSession = sdf.format(buy.getSessionMovie().getDateSession()
				.getTime());
		return buy;
	}

	Object onActivate(String buyId) {
		try {
			this.buyId = Long.parseLong(buyId);
		} catch (NumberFormatException e) {
			return NotFound.class;
		}

		try {
			buy = buyService.findBuy(this.buyId);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		}
		return null;
	}

	Long onPassivate() {
		return buyId;
	}

	@AuthenticationPolicy(AuthenticationPolicyType.SALESMAN)
	Object onValidate() {
		try {
			buyService.deliverTickets(buyId);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		} catch (TicketsAlreadyDelivered e) {
			deliverTicketsForm.recordError(messages
					.get("error-TicketsAlreadyDelivered"));
		} catch (MovieAlreadyStartedException e) {
			deliverTicketsForm.recordError(messages
					.get("error-MovieAlreadyStarted"));
		}
		if (request.isXHR()) {
			ajaxResponseRederer.addRender("tableZone", tableZone);
		}
		return null;
	}

	@AuthenticationPolicy(AuthenticationPolicyType.SALESMAN)
	void onSuccess() {
		if (request.isXHR()) {
			ajaxResponseRederer.addRender("tableZone", tableZone);
		}
	}

}