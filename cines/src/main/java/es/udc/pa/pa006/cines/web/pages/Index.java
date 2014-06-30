package es.udc.pa.pa006.cines.web.pages;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.movieservice.InvalidDateBillboardException;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.util.BillboardDto;
import es.udc.pa.pa006.cines.web.pages.nonAuth.SelectCinema;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.CookiesManager;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Index {

	private Long cinemaId = 1L;

	@Property
	private List<BillboardDto> billboardDtos;

	@Property
	private BillboardDto billboardDto;

	@Property
	private Cinema cinema;

	private SessionMovie sessionMovie;

	@Property
	private String sessionTime;

	@Property
	private String date;

	@Inject
	private MovieService movieService;

	@Inject
	private Locale locale;

	@Inject
	private Cookies cookies;

	@Component
	private Form dateForm;

	@InjectComponent
	private Zone billboardZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private Messages messages;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");

	public SessionMovie getSessionMovie() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sessionTime = sdf.format(sessionMovie.getDateSession().getTime());
		return sessionMovie;
	}

	public void setSessionMovie(SessionMovie sessionMovie) {
		this.sessionMovie = sessionMovie;
	}

	public Long getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

	Object onActivate() {
		return prepare(null);
	}

	Object prepare(String date) {
		this.cinemaId = CookiesManager.getFavoriteCinema(cookies);
		if (cinemaId == null) {
			return SelectCinema.class;
		}

		try {
			cinema = movieService.findCinema(cinemaId);
		} catch (InstanceNotFoundException e1) {
			return NotFound.class;
		}

		Calendar cal = Calendar.getInstance();
		if (date == null) {
			date = sdf.format(cal.getTime());
		}

		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			dateForm.recordError(messages
					.get("error-InvalidFormatDateBillboardException"));
			return null;
		}

		Calendar today = Calendar.getInstance();
		if (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
				&& cal.get(Calendar.MONTH) == today.get(Calendar.MONTH)
				&& cal.get(Calendar.DAY_OF_MONTH) == today
						.get(Calendar.DAY_OF_MONTH)) {
			cal = Calendar.getInstance();
		} else {
			cal.set(Calendar.HOUR_OF_DAY, 3);
			cal.set(Calendar.MINUTE, 1);
		}

		try {
			billboardDtos = movieService.findBillboard(cal, this.cinemaId);
		} catch (InvalidDateBillboardException e) {
			dateForm.recordError(messages
					.get("error-InvalidDateBillboardException"));
		}
		return null;
	}

	public String getDates() throws ParseException {
		String result = "";
		for (int i = 0; i < 7; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, i);
			result += sdf.format(cal.getTime()) + ",";
		}
		return result;
	}

	void onValueChangedFromDate(String date) {
		if (date != null) {
			prepare(date);
			if (request.isXHR()) {
				ajaxResponseRenderer.addRender(billboardZone);
			}
		}
	}

}
