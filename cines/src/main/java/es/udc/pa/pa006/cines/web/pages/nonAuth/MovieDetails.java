/**
 * 
 */
package es.udc.pa.pa006.cines.web.pages.nonAuth;

import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.web.pages.NotFound;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicy;
import es.udc.pa.pa006.cines.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class MovieDetails {

	private Long movieId;
	private Movie movie;

	@Inject
	private MovieService movieService;

	@Inject
	private Locale locale;

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Movie getMovie() {
		return movie;
	}

	public Format getFormat() {
		return NumberFormat.getInstance(locale);
	}

	Object onActivate(String movieId) {
		try {
			this.movieId = Long.parseLong(movieId);
		} catch (NumberFormatException e) {
			return NotFound.class;
		}

		try {
			movie = movieService.findMovie(this.movieId);
		} catch (InstanceNotFoundException e) {
			return NotFound.class;
		}
		return null;

	}

	Long onPassivate() {
		return movieId;
	}
}