package es.udc.pa.pa006.cines.model.util;

import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;

public class SessionDto {

	private SessionMovie session;

	private Long cinemaId;

	private Long roomId;

	private Long movieId;
	
	private int hour;
	
	private int minutes;

	public SessionDto() {
	}

	public SessionDto(SessionMovie session, Long cinemaId, Long roomId,
			Long movieId) {
		super();
		this.session = session;
		this.cinemaId = cinemaId;
		this.roomId = roomId;
		this.movieId = movieId;
	}

	public SessionMovie getSession() {
		return session;
	}

	public void setSession(SessionMovie session) {
		this.session = session;
	}

	public Long getCinemaId() {
		return cinemaId;
	}

	public void setCinemaId(Long cinemaId) {
		this.cinemaId = cinemaId;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	@Override
	public String toString() {
		return "SessionDto [session=" + session + ", cinemaId=" + cinemaId
				+ ", roomId=" + roomId + ", movieId=" + movieId + "]";
	}

}
