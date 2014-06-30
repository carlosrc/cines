/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.auth.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.movieservice.InvalidSessionMovieCreateException;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.model.room.Room;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.util.SessionDto;
import es.udc.pa.pa006.cines.web.pages.Index;
import es.udc.pa.pa006.cines.web.pages.NotFound;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMIN)
public class MakeBillboard {

	@Persist
	@Property
	private String province;

	@Persist
	@Property
	private String cinema;

	@Property
	private String cinemas = "";

	@Property
	private Cinema favoriteCinema = null;

	@Property
	private Date today;

	@Property
	private SessionDto session;

	@Property
	private List<SessionDto> sessionDtos = new ArrayList<SessionDto>();

	@Persist
	@Property
	private String rooms;

	@Property
	private String movies;

	@Component
	private Form selectCinemaForm;

	@Component
	private Form makeBillboardForm;

	@Inject
	private Messages messages;

	@Inject
	private MovieService movieService;

	@InjectComponent
	private Zone cinemaZone, roomZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectPage
	private Index index;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	@Persist
	private String date;
	
	void onPrepareForRender() {
		sessionDtos.clear();
	}

	void onActivate() {
		if (date != null) {
			try {
				this.today = sdf.parse(date);
			} catch (ParseException e) {
				this.today = Calendar.getInstance().getTime();
			}
		} else {
			this.today = Calendar.getInstance().getTime();
		}

		loadCinemas(province);
		loadRooms(cinema);
		loadMovies();
	}

	public String getProvinces() {
		List<Province> provinces = movieService.getProvinces();
		String result = "";
		for (Province province : provinces) {
			result += province.getProvinceId() + "=" + province.getName() + ",";
		}
		return result;
	}

	boolean loadCinemas(String province) {
		Long provinceIdLong = null;
		try {
			provinceIdLong = Long.parseLong(province);
		} catch (NumberFormatException e) {
			return false;
		}

		cinemas = "";
		rooms = "";
		movies = "";
		if (province != null) {
			List<Cinema> cinemasObject = movieService
					.findCinemaByProvince(provinceIdLong);
			for (Cinema cinema : cinemasObject) {
				cinemas += cinema.getCinemaId() + "=" + cinema.getName() + ",";
			}
			return true;
		} else {
			cinemas = null;
		}
		return true;
	}

	void onValueChangedFromProvince(String province) {
		if (loadCinemas(province)) {
			if (request.isXHR()) {
				ajaxResponseRenderer.addRender(cinemaZone);
			}
		}
	}

	boolean loadRooms(String cinema) {
		rooms = "";
		Long cinemaIdLong = null;
		try {
			cinemaIdLong = Long.parseLong(cinema);
		} catch (NumberFormatException e) {
			return false;
		}

		if (cinemaIdLong != null) {
			List<Room> roomsObject = movieService
					.findRoomsByCinema(cinemaIdLong);
			for (Room room : roomsObject) {
				rooms += room.getRoomId() + "=" + room.getName() + ",";
			}
			return true;
		}
		return true;
	}

	void loadMovies() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		movies = "";
		List<Movie> moviesObject = movieService.findMoviesByDate(cal);
		for (Movie movie : moviesObject) {
			movies += movie.getMovieId() + "=" + movie.getTitle() + ",";
		}
	}

	public ValueEncoder<SessionDto> getSessionDtoEncoder() {
		return new ValueEncoder<SessionDto>() {
			public String toClient(SessionDto value) {
				return String.valueOf(sessionDtos.indexOf(value));
			}

			public SessionDto toValue(String clientValue) {
				return sessionDtos.get(Integer.parseInt(clientValue));
			}
		};
	}

	@OnEvent(value = EventConstants.ADD_ROW, component = "sessions")
	Object onAddRowFromSessions() {
		SessionDto sessionDto = new SessionDto();
		sessionDtos.add(sessionDto);
		return sessionDto;
	}

	@OnEvent(value = EventConstants.REMOVE_ROW, component = "sessions")
	void onRemoveRowFromPhones(SessionDto sessionToDelete) {
		sessionDtos.set(sessionDtos.indexOf(sessionToDelete), null);
	}

	void onSuccessFromSelectCinemaForm() {
		date = sdf.format(today);
		sessionDtos.clear();
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(roomZone);
		}
	}

	Object onSuccessFromMakeBillboardForm() {
		List<SessionDto> toRemove = new ArrayList<SessionDto>();
		for (SessionDto aux : sessionDtos) {
			if (aux == null) {
				toRemove.add(aux);
			} else {
				Calendar cal = Calendar.getInstance();

				aux.setCinemaId(Long.parseLong(cinema));
				SimpleDateFormat sdfComplete = new SimpleDateFormat(
						"HH:mm dd-MM-yyyy");
				String completeDate = aux.getHour() + ":" + aux.getMinutes()
						+ " " + date;
				try {
					cal.setTime(sdfComplete.parse(completeDate));
				} catch (ParseException e) {
					makeBillboardForm.recordError(messages
							.format("error-InvalidFormatDateException"));
					return null;
				}
				aux.setSession(new SessionMovie(cal));
			}
		}

		sessionDtos.removeAll(toRemove);

		try {
			movieService.createBillboard(sessionDtos);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		} catch (DuplicateInstanceException e) {
			makeBillboardForm.recordError(messages
					.format("error-DuplicateInstanceException"));
			return null;
		} catch (InvalidSessionMovieCreateException e) {
			makeBillboardForm.recordError(messages
					.format("error-InvalidSessionMovieCreateException"));
			return null;
		}

		sessionDtos.clear();
		return null;
	}

}