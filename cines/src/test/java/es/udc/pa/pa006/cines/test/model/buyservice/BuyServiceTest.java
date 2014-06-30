package es.udc.pa.pa006.cines.test.model.buyservice;

import static es.udc.pa.pa006.cines.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa006.cines.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.management.InvalidAttributeValueException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa006.cines.model.buy.Buy;
import es.udc.pa.pa006.cines.model.buyservice.BuyBlock;
import es.udc.pa.pa006.cines.model.buyservice.BuyService;
import es.udc.pa.pa006.cines.model.buyservice.MovieAlreadyStartedException;
import es.udc.pa.pa006.cines.model.buyservice.NoAvaliableSeatsException;
import es.udc.pa.pa006.cines.model.buyservice.TicketsAlreadyDelivered;
import es.udc.pa.pa006.cines.model.cinema.Cinema;
import es.udc.pa.pa006.cines.model.cinema.CinemaDao;
import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.movie.MovieDao;
import es.udc.pa.pa006.cines.model.movieservice.InvalidSessionMovieCreateException;
import es.udc.pa.pa006.cines.model.movieservice.MovieService;
import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.model.province.ProvinceDao;
import es.udc.pa.pa006.cines.model.room.Room;
import es.udc.pa.pa006.cines.model.room.RoomDao;
import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.userprofile.UserProfile;
import es.udc.pa.pa006.cines.model.userservice.UserProfileDetails;
import es.udc.pa.pa006.cines.model.userservice.UserService;
import es.udc.pa.pa006.cines.model.util.SessionDto;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class BuyServiceTest {

    private final long NON_EXISTENT_BUY_ID = -1;
    private final int avaliableSeats = 80;
    private final Double price = 8.0;
    private final int tickets = 2;
    private final String cardNumber = "123344556667";

    @Autowired
    private BuyService buyService;

    @Autowired
    private UserService userService;

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

    private UserProfile user;
    private Province coruna = new Province("A Coruña", null);
    private Cinema marineda = new Cinema("Marineda City", price, coruna);
    private Movie movie1;
    private Room room1 = new Room("sala 1", avaliableSeats, marineda);
    private SessionDto sessionMovie;
    private Calendar expirationDate;

    @Before
    public void setUp() throws Exception {

	expirationDate = Calendar.getInstance();
	expirationDate.add(Calendar.YEAR, 1);

	Calendar initDate = Calendar.getInstance();
	initDate.add(Calendar.DAY_OF_YEAR, -5);

	Calendar endDate = Calendar.getInstance();
	initDate.add(Calendar.DAY_OF_YEAR, 5);

	user = userService.registerUser("user1", "password",
		new UserProfileDetails("12345678A", "name", "last name",
			"user@user.com"));

	provinceDao.save(coruna);
	cinemaDao.save(marineda);
	movie1 = new Movie("12 años de exclavitud",
		"Descripción de 12 años de exclavitud", 133, initDate, endDate);
	movieDao.save(movie1);
	roomDao.save(room1);

	Calendar dateSession = Calendar.getInstance();
	dateSession.add(Calendar.DAY_OF_YEAR, 1);
	dateSession.set(Calendar.HOUR_OF_DAY, 17);

	SessionMovie sm = new SessionMovie(dateSession);

	sessionMovie = new SessionDto(sm, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	List<SessionDto> lista = new ArrayList<SessionDto>();
	lista.add(sessionMovie);
	movieService.createBillboard(lista);
    }

    @Test
    public void testCreateAndFindBuy() throws InstanceNotFoundException,
	    NoAvaliableSeatsException, MovieAlreadyStartedException, InvalidAttributeValueException {

	Long buyId = buyService.buyTickets(tickets, cardNumber, expirationDate,
		sessionMovie.getSession().getSessionMovieId(),
		user.getUserProfileId());

	Buy buy = buyService.findBuy(buyId);

	assertEquals(buyId, buy.getBuyId());
	assertEquals(tickets, buy.getTickets());
	assertFalse(buy.getDelivered());
	assertEquals(cardNumber, buy.getCardNumber());
	assertEquals(expirationDate, buy.getExpirationDate());

	assertEquals(sessionMovie.getSession().getAvaliableSeats(),
		room1.getCapacity() - buy.getTickets());

    }

    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentBUy() throws InstanceNotFoundException {

	buyService.findBuy(NON_EXISTENT_BUY_ID);

    }

    @Test(expected = NoAvaliableSeatsException.class)
    public void testNoAvaliableSeats() throws NoAvaliableSeatsException,
	    InstanceNotFoundException, MovieAlreadyStartedException, InvalidAttributeValueException {

	buyService.buyTickets(100, cardNumber, expirationDate, sessionMovie
		.getSession().getSessionMovieId(), user.getUserProfileId());

    }

    @Test(expected = MovieAlreadyStartedException.class)
    public void testMovieAlreadyStarted() throws MovieAlreadyStartedException,
	    InstanceNotFoundException, DuplicateInstanceException,
	    NoAvaliableSeatsException, InvalidSessionMovieCreateException, InvalidAttributeValueException {
	Calendar dateSession = Calendar.getInstance();
	dateSession.add(Calendar.HOUR_OF_DAY, -2);

	SessionMovie sm1 = new SessionMovie(dateSession);

	SessionDto sessionStart = new SessionDto(sm1, marineda.getCinemaId(),
		room1.getRoomId(), movie1.getMovieId());

	List<SessionDto> lista = new ArrayList<SessionDto>();
	lista.add(sessionStart);
	movieService.createBillboard(lista);

	buyService.buyTickets(tickets, cardNumber, expirationDate, sessionStart
		.getSession().getSessionMovieId(), user.getUserProfileId());

    }

    @Test
    public void testFindBuysByUser() throws InstanceNotFoundException,
	    NoAvaliableSeatsException, MovieAlreadyStartedException, InvalidAttributeValueException {

	Long buyId1 = buyService.buyTickets(tickets, cardNumber,
		expirationDate, sessionMovie.getSession().getSessionMovieId(),
		user.getUserProfileId());

	Long buyId2 = buyService.buyTickets(tickets, cardNumber,
		expirationDate, sessionMovie.getSession().getSessionMovieId(),
		user.getUserProfileId());

	BuyBlock resultBlock = buyService.findBuysByUser(
		user.getUserProfileId(), 0, 10);

	assertEquals(2, resultBlock.getBuys().size());
	assertFalse(resultBlock.getExistMoreBuys());
	assertEquals(buyId1, resultBlock.getBuys().get(0).getBuyId());
	assertEquals(buyId2, resultBlock.getBuys().get(1).getBuyId());

	BuyBlock resultBlock2 = buyService.findBuysByUser(
		user.getUserProfileId(), 0, 1);
	assertTrue(resultBlock2.getExistMoreBuys());

    }

    @Test
    public void testDeliverTickets() throws InstanceNotFoundException,
	    NoAvaliableSeatsException, MovieAlreadyStartedException,
	    TicketsAlreadyDelivered, InvalidAttributeValueException {
	Long buyId = buyService.buyTickets(tickets, cardNumber, expirationDate,
		sessionMovie.getSession().getSessionMovieId(),
		user.getUserProfileId());

	Buy buy = buyService.findBuy(buyId);
	assertFalse(buy.getDelivered());

	buyService.deliverTickets(buyId);

	assertTrue(buy.getDelivered());

    }

    @Test(expected = TicketsAlreadyDelivered.class)
    public void testTicketsAlreadyDelivered() throws TicketsAlreadyDelivered,
	    InstanceNotFoundException, NoAvaliableSeatsException,
	    MovieAlreadyStartedException, InvalidAttributeValueException {
	Long buyId = buyService.buyTickets(tickets, cardNumber, expirationDate,
		sessionMovie.getSession().getSessionMovieId(),
		user.getUserProfileId());

	Buy buy = buyService.findBuy(buyId);
	assertFalse(buy.getDelivered());

	buyService.deliverTickets(buyId);

	assertTrue(buy.getDelivered());

	buyService.deliverTickets(buyId);

    }
}
