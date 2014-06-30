/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.auth;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa006.cines.model.buy.Buy;
import es.udc.pa.pa006.cines.model.buyservice.BuyBlock;
import es.udc.pa.pa006.cines.model.buyservice.BuyService;
import es.udc.pa.pa006.cines.web.pages.NotFound;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.Constants;
import es.udc.pa.pa006.cines.web.util.UserSession;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UserBuys {

	private final static int BUYS_PER_PAGE = Constants.BATCHSIZE;

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	private int startIndex = 0;
	private BuyBlock buyBlock;
	private Buy buy;

	@Property
	private String buyDate;

	@Property
	private String dateSession;

	@Inject
	private BuyService buyService;

	@Inject
	private Locale locale;

	@InjectComponent
	private Zone tableZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRederer;

	public List<Buy> getBuys() {
		return buyBlock.getBuys();
	}

	public Buy getBuy() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
		buyDate = sdf.format(buy.getBuyDate().getTime());
		dateSession = sdf.format(buy.getSessionMovie().getDateSession()
				.getTime());
		return buy;
	}

	public void setBuy(Buy buy) {
		this.buy = buy;
	}

	Object[] onPassivate() {
		return new Object[] { startIndex };
	}

	Object onActivate(String startIndex) {
		try {
			this.startIndex = Integer.parseInt(startIndex);
		} catch (NumberFormatException e) {
			return NotFound.class;
		}
		buyBlock = buyService.findBuysByUser(userSession.getUserProfileId(),
				this.startIndex, BUYS_PER_PAGE);
		return null;
	}

	public Object[] getPreviousLinkContext() {
		if (startIndex - BUYS_PER_PAGE >= 0) {
			return new Object[] { startIndex - BUYS_PER_PAGE };
		} else {
			return null;
		}
	}

	public Object[] getNextLinkContext() {
		if (buyBlock.getExistMoreBuys()) {
			return new Object[] { startIndex + BUYS_PER_PAGE };
		} else {
			return null;
		}
	}

	void onChangePage(String startIndex) {
		onActivate(startIndex);
		renderAjax();
	}

	private void renderAjax() {
		if (request.isXHR()) {
			ajaxResponseRederer.addRender("tableZone", tableZone);
		}
	}

}