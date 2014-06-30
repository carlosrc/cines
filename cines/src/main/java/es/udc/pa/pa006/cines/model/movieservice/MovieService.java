package es.udc.pa.pa006.cines.model.movieservice;

import java.util.Calendar;
import java.util.List;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.model.room.Room;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.util.BillboardDto;
import es.udc.pa.pa006.cines.model.util.SessionDto;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface MovieService {

	public List<BillboardDto> findBillboard(Calendar date, long cinemaId)
			throws InvalidDateBillboardException;

	public List<Province> getProvinces();

	public List<Cinema> findCinemaByProvince(long provinceId);

	public Cinema findCinema(long cinemaId) throws InstanceNotFoundException;

	public Movie findMovie(long movieId) throws InstanceNotFoundException;

	public List<Movie> findMoviesByDate(Calendar date);

	public List<Room> findRoomsByCinema(long cinemaId);

	public SessionMovie findSessionMovie(long sessionMovieId)
			throws InstanceNotFoundException;

	public SessionMovie createSessionMovie(SessionMovie sessionMovie)
			throws DuplicateInstanceException;

	public void createBillboard(List<SessionDto> newSessions)
			throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidSessionMovieCreateException;

}
