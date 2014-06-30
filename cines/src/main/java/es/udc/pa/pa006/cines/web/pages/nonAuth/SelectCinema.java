/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.nonAuth;

import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.web.pages.Index;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pa.pa006.cines.web.util.CookiesManager;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class SelectCinema {

	@Property
	private String province;

	@Property
	private String cinema;

	@Property
	private String cinemas = "";

	@Property
	private Cinema favoriteCinema = null;

	@Inject
	private MovieService movieService;

	@InjectComponent
	private Zone cinemaZone;

	@Inject
	private Request request;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@InjectPage
	private Index index;

	@Inject
	private Cookies cookies;

	public String getProvinces() {
		List<Province> provinces = movieService.getProvinces();
		String result = "";
		for (Province province : provinces) {
			result += province.getProvinceId() + "=" + province.getName() + ",";
		}
		return result;
	}

	void onValueChangedFromProvince(String province) {
		if (province != null) {
			List<Cinema> cinemasObject = movieService.findCinemaByProvince(Long
					.parseLong(province));
			cinemas = "";
			for (Cinema cinema : cinemasObject) {
				cinemas += cinema.getCinemaId() + "=" + cinema.getName() + ",";
			}

			if (request.isXHR()) {
				ajaxResponseRenderer.addRender(cinemaZone);
			}
		} else {
			cinemas = "";
		}
	}

	Object onValueChangedFromCinema(String cinemaId) {
		Long cinemaIdLong = Long.parseLong(cinemaId);
		CookiesManager.removeFavoriteCinemaCookies(cookies);
		CookiesManager.leaveFavoriteCinemaCookies(cookies, cinemaIdLong);
		index.setCinemaId(cinemaIdLong);
		return index;
	}

	void onActivate() {
		Long cinemaId = CookiesManager.getFavoriteCinema(cookies);
		if (cinemaId != null) {
			try {
				favoriteCinema = movieService.findCinema(cinemaId);
			} catch (InstanceNotFoundException e) {
				CookiesManager.removeCookies(cookies);
			}
		}
	}
}