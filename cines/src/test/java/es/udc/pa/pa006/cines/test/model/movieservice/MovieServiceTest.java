package es.udc.pa.pa006.cines.test.model.movieservice;

import static es.udc.pa.pa006.cines.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa006.cines.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.cinema.CinemaDao;
import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.movie.MovieDao;
import es.udc.pa.pa006.cines.model.movieservice.InvalidDateBillboardException;
import es.udc.pa.pa006.cines.model.movieservice.InvalidSessionMovieCreateException;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.model.province.ProvinceDao;
import es.udc.pa.pa006.cines.model.room.Room;
import es.udc.pa.pa006.cines.model.room.RoomDao;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.util.BillboardDto;
import es.udc.pa.pa006.cines.model.util.SessionDto;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class MovieServiceTest {

    private final long NON_EXISTENT_MOVIE_ID = -1;
    private final int avaliableSeats = 80;
    private final Double price = 8.0;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProvinceDao provinceDao;

    @Autowired
    private CinemaDao cinemaDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private RoomDao roomDao;

    private Province coruna = new Province("A Coruña", null);
    private Province lugo = new Province("Lugo", null);
    private Province pontevedra = new Province("Pontevedra", null);
    private Province ourense = new Province("Ourense", null);

    private Cinema marineda = new Cinema("Marineda City", price, coruna);
    private Cinema rosales = new Cinema("Los Rosales", price, coruna);
    private Cinema espacio = new Cinema("Espacio Coruña", price, coruna);

    private Movie movie1;
    private Movie movie2;

    private Room room1 = new Room("sala 1", avaliableSeats, marineda);
    private Room room2 = new Room("sala 2", avaliableSeats, marineda);

    private SessionMovie sm1;
    private SessionMovie sm2;
    private SessionMovie sm3;
    private SessionMovie sm4;

    @Before
    public void setUp() throws Exception {

	Calendar initDate = Calendar.getInstance();
	initDate.add(Calendar.DAY_OF_YEAR, -5);

	Calendar endDate = Calendar.getInstance();
	initDate.add(Calendar.DAY_OF_YEAR, 5);

	provinceDao.save(coruna);
	provinceDao.save(lugo);
	provinceDao.save(pontevedra);
	provinceDao.save(ourense);

	cinemaDao.save(marineda);
	cinemaDao.save(rosales);
	cinemaDao.save(espacio);

	movie1 = new Movie("12 años de exclavitud",
		"Descripción de 12 años de exclavitud", 120, initDate, endDate);
	movie2 = new Movie("'Gravity", "Descripción de Gravity", 90, initDate,
		endDate);

	movieDao.save(movie1);
	movieDao.save(movie2);

	roomDao.save(room1);
	roomDao.save(room2);

	Calendar date1 = Calendar.getInstance();
	date1.add(Calendar.DAY_OF_YEAR, 1);
	date1.set(Calendar.HOUR_OF_DAY, 16);

	sm1 = new SessionMovie(date1);

	Calendar date2 = Calendar.getInstance();
	date2.add(Calendar.DAY_OF_YEAR, 2);
	date2.set(Calendar.HOUR_OF_DAY, 19);

	sm2 = new SessionMovie(date2);

	Calendar date3 = Calendar.getInstance();
	date3.add(Calendar.DAY_OF_YEAR, 2);
	date3.set(Calendar.HOUR_OF_DAY, 18);

	sm3 = new SessionMovie(date3);

	Calendar date4 = Calendar.getInstance();
	date4.add(Calendar.DAY_OF_YEAR, 2);
	date4.set(Calendar.HOUR_OF_DAY, 21);

	sm4 = new SessionMovie(date4);

    }

    @Test
    public void testGetProvinces() throws DuplicateInstanceException {

	List<Province> result = movieService.getProvinces();

	assertEquals(4, result.size());
	assertEquals(coruna, result.get(0));
	assertEquals(lugo, result.get(1));
	assertEquals(ourense, result.get(2));
	assertEquals(pontevedra, result.get(3));

    }

    @Test
    public void testFindCinemaByProvince() {

	List<Cinema> result = movieService.findCinemaByProvince(coruna
		.getProvinceId());

	assertEquals(3, result.size());
	assertEquals(espacio, result.get(0));
	assertEquals(rosales, result.get(1));
	assertEquals(marineda, result.get(2));

    }

    @Test
    public void testFindMovie() throws InstanceNotFoundException {

	Movie m = movieService.findMovie(movie1.getMovieId());

	assertEquals(movie1, m);

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentMovie() throws InstanceNotFoundException {

	movieService.findMovie(NON_EXISTENT_MOVIE_ID);

    }

    @Test
    public void testCreateAndFindBillboard() throws InstanceNotFoundException,
	    DuplicateInstanceException, InvalidDateBillboardException,
	    InvalidSessionMovieCreateException {

	SessionDto dto1 = new SessionDto(sm1, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());
	SessionDto dto2 = new SessionDto(sm2, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());
	SessionDto dto3 = new SessionDto(sm3, marineda.getCinemaId(),
		room2.getRoomId(), movie2.getMovieId());
	SessionDto dto4 = new SessionDto(sm4, marineda.getCinemaId(),
		room1.getRoomId(), movie2.getMovieId());

	List<SessionDto> lista = new ArrayList<SessionDto>();
	lista.add(dto1);
	lista.add(dto2);
	lista.add(dto3);
	lista.add(dto4);
	movieService.createBillboard(lista);

	Calendar date5 = Calendar.getInstance();
	date5.add(Calendar.DAY_OF_YEAR, 2);
	date5.set(Calendar.HOUR_OF_DAY, 15);

	List<BillboardDto> result = movieService.findBillboard(date5,
		marineda.getCinemaId());

	assertEquals(2, result.size());
	assertEquals(movie2, result.get(0).getMovie());
	assertEquals(movie1, result.get(1).getMovie());

	assertEquals(2, result.get(0).getSessionMovies().size());
	assertEquals(sm3, result.get(0).getSessionMovies().get(0));
	assertEquals(sm4, result.get(0).getSessionMovies().get(1));

	assertEquals(1, result.get(1).getSessionMovies().size());
	assertEquals(sm2, result.get(1).getSessionMovies().get(0));

	assertEquals(marineda.getPrice(),
		result.get(1).getSessionMovies().get(0).getPrice());
	assertEquals(avaliableSeats, result.get(1).getSessionMovies().get(0)
		.getAvaliableSeats());
    }

    @Test(expected = InvalidDateBillboardException.class)
    public void testInvalidDateBillboard()
	    throws InvalidDateBillboardException, InstanceNotFoundException,
	    DuplicateInstanceException, InvalidSessionMovieCreateException {

	SessionDto dto1 = new SessionDto(sm1, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());
	SessionDto dto2 = new SessionDto(sm2, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	List<SessionDto> lista = new ArrayList<SessionDto>();
	lista.add(dto1);
	lista.add(dto2);
	movieService.createBillboard(lista);

	Calendar dateInvalid = Calendar.getInstance();
	dateInvalid.add(Calendar.DAY_OF_YEAR, 7);

	movieService.findBillboard(dateInvalid, marineda.getCinemaId());

    }

    @Test(expected = InvalidSessionMovieCreateException.class)
    public void testInvalidSessionMovieCreateBefore()
	    throws InvalidDateBillboardException, InstanceNotFoundException,
	    DuplicateInstanceException, InvalidSessionMovieCreateException {

	Calendar date1 = Calendar.getInstance();
	date1.add(Calendar.DAY_OF_YEAR, 1);
	date1.set(Calendar.HOUR_OF_DAY, 16);

	SessionMovie s = new SessionMovie(date1);

	SessionDto dto1 = new SessionDto(s, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	Calendar date2 = Calendar.getInstance();
	date2.add(Calendar.DAY_OF_YEAR, 1);
	date2.set(Calendar.HOUR_OF_DAY, 15);

	SessionMovie s1 = new SessionMovie(date2);

	SessionDto dto2 = new SessionDto(s1, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	List<SessionDto> lista = new ArrayList<SessionDto>();
	lista.add(dto1);
	lista.add(dto2);
	movieService.createBillboard(lista);

    }

    @Test(expected = InvalidSessionMovieCreateException.class)
    public void testInvalidSessionMovieCreateAfter()
	    throws InvalidDateBillboardException, InstanceNotFoundException,
	    DuplicateInstanceException, InvalidSessionMovieCreateException {

	Calendar date1 = Calendar.getInstance();
	date1.add(Calendar.DAY_OF_YEAR, 1);
	date1.set(Calendar.HOUR_OF_DAY, 16);

	SessionMovie s = new SessionMovie(date1);

	SessionDto dto1 = new SessionDto(s, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	Calendar date2 = Calendar.getInstance();
	date2.add(Calendar.DAY_OF_YEAR, 1);
	date2.set(Calendar.HOUR_OF_DAY, 17);

	SessionMovie s1 = new SessionMovie(date2);

	SessionDto dto2 = new SessionDto(s1, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	List<SessionDto> lista = new ArrayList<SessionDto>();
	lista.add(dto1);
	lista.add(dto2);
	movieService.createBillboard(lista);

    }

}
