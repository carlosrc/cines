package es.udc.pa.pa006.cines.model.movieservice;

import es.udc.pa.pa006.cines.model.util.SessionDto;

@SuppressWarnings("serial")
public class InvalidSessionMovieCreateException extends Exception {

	private SessionDto sessionDto;

	public InvalidSessionMovieCreateException(SessionDto sessionDto) {
		super("Invalid date. Can't create session movie in this date => date = "
				+ sessionDto.getSession().getDateSession());
		this.sessionDto = sessionDto;
	}

	public SessionDto getSessionDto() {
		return sessionDto;
	}

}
