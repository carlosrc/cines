package es.udc.pa.pa006.cines.model.util;

import java.util.List;

import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;

public class BillboardDto {
	
	private Movie movie;
	
	private List<SessionMovie> sessionMovies;

	public BillboardDto(Movie movie, List<SessionMovie> sessionMovies) {
		super();
		this.movie = movie;
		this.sessionMovies = sessionMovies;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public List<SessionMovie> getSessionMovies() {
		return sessionMovies;
	}

	public void setSessionMovies(List<SessionMovie> sessionMovies) {
		this.sessionMovies = sessionMovies;
	}

	@Override
	public String toString() {
		return "BillboardDto [movie=" + movie + ", sessionMovies="
				+ sessionMovies + "]";
	}
	
	

}
