package es.udc.pa.pa006.cines.model.movieservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.cinema.CinemaDao;
import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.movie.MovieDao;
import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.model.province.ProvinceDao;
import es.udc.pa.pa006.cines.model.room.Room;
import es.udc.pa.pa006.cines.model.room.RoomDao;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovieDao;
import es.udc.pa.pa006.cines.model.util.BillboardDto;
import es.udc.pa.pa006.cines.model.util.SessionDto;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("movieService")
@Transactional
public class MovieServiceImpl implements MovieService {

	@Autowired
	private ProvinceDao provinceDao;

	@Autowired
	private CinemaDao cinemaDao;

	@Autowired
	private MovieDao movieDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private SessionMovieDao sessionMovieDao;

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<BillboardDto> findBillboard(Calendar date, long cinemaId)
			throws InvalidDateBillboardException {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.SECOND, 0);
		Calendar maxDate = Calendar.getInstance();
		maxDate.add(Calendar.DAY_OF_YEAR, 6);

		if (date.before(today) || date.after(maxDate)) {
			throw new InvalidDateBillboardException(date);
		}

		// Obtenemos la lista de sesiones de todas las peliculas
		List<SessionMovie> sesiones = sessionMovieDao.findSessionsByCinema(
				date, cinemaId);

		// Inicializamos el Mapa
		HashMap<Movie, List<SessionMovie>> mapaSesiones = new HashMap<Movie, List<SessionMovie>>();
		List<BillboardDto> dtoSesiones = new ArrayList<BillboardDto>();

		// Para cada SessionMovie, la agrupamos en su pelicula
		for (int i = 0; i < sesiones.size(); i++) {
			SessionMovie s = sesiones.get(i);
			if (mapaSesiones.containsKey(s.getMovie())) {
				mapaSesiones.get(s.getMovie()).add(s);
			} else {
				List<SessionMovie> tmpSesiones = new ArrayList<SessionMovie>();
				tmpSesiones.add(s);
				mapaSesiones.put(s.getMovie(), tmpSesiones);
			}
		}

		java.util.Iterator<Entry<Movie, List<SessionMovie>>> it = mapaSesiones
				.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry) it.next();
			List<SessionMovie> sessionMovies = (List<SessionMovie>) pairs
					.getValue();
			// Ordenamos las sesiones por fecha
			Collections.sort(sessionMovies, new SessionComparator());
			dtoSesiones.add(new BillboardDto((Movie) pairs.getKey(),
					sessionMovies));
			it.remove();
		}
		// Ordenamos la lista por título de película
		Collections.sort(dtoSesiones, new BillboardComparator());

		return dtoSesiones;
	}

	public class BillboardComparator implements Comparator<BillboardDto> {
		public int compare(BillboardDto object1, BillboardDto object2) {
			return object1.getMovie().getTitle()
					.compareTo(object2.getMovie().getTitle());
		}
	}

	public class SessionComparator implements Comparator<SessionMovie> {
		public int compare(SessionMovie object1, SessionMovie object2) {
			return object1.getDateSession().compareTo(object2.getDateSession());
		}
	}

	@Transactional(readOnly = true)
	public List<Province> getProvinces() {
		return provinceDao.findAllProvinces();
	}

	@Transactional(readOnly = true)
	public List<Cinema> findCinemaByProvince(long provinceId) {
		return cinemaDao.findCinemasByProvince(provinceId);
	}

	public Cinema findCinema(long cinemaId) throws InstanceNotFoundException {
		return cinemaDao.find(cinemaId);
	}

	@Transactional(readOnly = true)
	public Movie findMovie(long movieId) throws InstanceNotFoundException {
		return movieDao.find(movieId);
	}

	@Transactional(readOnly = true)
	public List<Movie> findMoviesByDate(Calendar date) {
		return movieDao.findMoviesByDate(date);
	}

	@Transactional(readOnly = true)
	public List<Room> findRoomsByCinema(long cinemaId) {
		return roomDao.findRoomsByCinema(cinemaId);
	}

	@Override
	public SessionMovie createSessionMovie(SessionMovie sessionMovie)
			throws DuplicateInstanceException {
		sessionMovieDao.save(sessionMovie);
		return sessionMovie;
	}

	private boolean existsOtherSessionMovie(SessionDto sessionDto,
			int movieDuration) {
		List<SessionMovie> sessionMovies = sessionMovieDao
				.findOtherSessionsMovie(sessionDto.getCinemaId(), sessionDto
						.getRoomId(), sessionDto.getSession().getDateSession(),
						movieDuration);
		return !sessionMovies.isEmpty();
	}

	@Override
	public void createBillboard(List<SessionDto> newSessions)
			throws InstanceNotFoundException, DuplicateInstanceException,
			InvalidSessionMovieCreateException {
		for (SessionDto sessionDto : newSessions) {
			Cinema cinema = cinemaDao.find(sessionDto.getCinemaId());
			Room room = roomDao.find(sessionDto.getRoomId());
			Movie movie = movieDao.find(sessionDto.getMovieId());
			sessionDto.getSession().setRoom(room);
			sessionDto.getSession().setAvaliableSeats(room.getCapacity());
			sessionDto.getSession().setPrice(cinema.getPrice());
			sessionDto.getSession().setMovie(movie);
			sessionDto.getSession().getRoom().setCinema(cinema);

			if (existsOtherSessionMovie(sessionDto, movie.getDuration())) {
				throw new InvalidSessionMovieCreateException(sessionDto);
			} else {
				createSessionMovie(sessionDto.getSession());
			}
		}
	}

	@Transactional(readOnly = true)
	public SessionMovie findSessionMovie(long sessionMovieId)
			throws InstanceNotFoundException {
		return sessionMovieDao.find(sessionMovieId);
	}
}
