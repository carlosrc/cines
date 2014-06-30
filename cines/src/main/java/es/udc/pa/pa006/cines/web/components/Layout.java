package es.udc.pa.pa006.cines.web.components;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.pa.pa006.cines.web.pages.Index;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.CookiesManager;
import es.udc.pa.pa006.cines.web.util.UserSession;

@Import(stylesheet = { "context:/css/bootstrap.min.css",
		"context:/css/styles.css" }, library = {
		"context:js/jquery-1.11.1.min.js", "context:js/bootstrap.min.js" })
public class Layout {
	@Property
	@Parameter(required = false, defaultPrefix = "message")
	private String menuExplanation;

	@Property
	@Parameter(required = true, defaultPrefix = "message")
	private String pageTitle;

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private Cookies cookies;

	@AuthenticationPolicy(AuthenticationPolicyType.ALL_AUTHENTICATED_USERS)
	Object onActionFromLogout() {
		userSession = null;
		CookiesManager.removeCookies(cookies);
		return Index.class;
	}

}