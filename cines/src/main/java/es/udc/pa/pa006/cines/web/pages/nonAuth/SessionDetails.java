/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.nonAuth;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.web.pages.NotFound;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class SessionDetails {
	
	@Property
	@SessionState(create = false)
	private UserSession userSession;
	
	private Long sessionId;
	private SessionMovie session;

	@Inject
	private MovieService movieService;

	@Inject
	private Locale locale;

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public SessionMovie getSession() {
		return session;
	}

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

	Object onActivate(String sessionId) {
		try {
			this.sessionId = Long.parseLong(sessionId);
		} catch (NumberFormatException e) {
			return NotFound.class;
		}

		try {
			session = movieService.findSessionMovie(this.sessionId);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		}
		return null;

	}

	Long onPassivate() {
		return sessionId;
	}
}