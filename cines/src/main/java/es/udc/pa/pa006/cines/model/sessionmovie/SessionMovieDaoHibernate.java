package es.udc.pa.pa006.cines.model.sessionmovie;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("sessionMovieDao")
public class SessionMovieDaoHibernate extends
		GenericDaoHibernate<SessionMovie, Long> implements SessionMovieDao {

	@SuppressWarnings("unchecked")
	public List<SessionMovie> findSessionsByCinema(Calendar date, long cinemaId) {
		Calendar dateLimit = Calendar.getInstance();
		dateLimit.setTime(date.getTime());
		dateLimit.set(Calendar.HOUR_OF_DAY, 23);
		dateLimit.add(Calendar.HOUR_OF_DAY, 4);
		dateLimit.set(Calendar.MINUTE, 00);
		dateLimit.set(Calendar.SECOND, 00);

		return getSession()
				.createQuery(
						"SELECT s FROM SessionMovie s WHERE "
								+ "s.room.cinema.cinemaId = :cinemaId AND "
								+ "(s.dateSession >= :dateFrom AND "
								+ "s.dateSession <= :dateLimit) ORDER BY s.movie.title")
				.setParameter("cinemaId", cinemaId)
				.setCalendar("dateFrom", date)
				.setCalendar("dateLimit", dateLimit).list();
	}

	@SuppressWarnings("unchecked")
	public List<SessionMovie> findOtherSessionsMovie(long cinemaId,
			long roomId, Calendar dateSession, int movieDuration) {
		Calendar dateEnd = Calendar.getInstance();
		dateEnd.setTime(dateSession.getTime());
		dateEnd.add(Calendar.MINUTE, movieDuration);

		Calendar dateAux = Calendar.getInstance();
		dateAux.setTime(dateSession.getTime());
		dateAux.add(Calendar.MINUTE, -movieDuration);

		return getSession()
				.createQuery(
						"SELECT s FROM SessionMovie s WHERE "
								+ "s.room.cinema.cinemaId = :cinemaId AND "
								+ "s.room.roomId = :roomId AND "
								+ "( (s.dateSession BETWEEN :dateSession AND :dateEnd) "
								+ "OR (s.dateSession BETWEEN :dateAux AND :dateSession) )")
				.setParameter("cinemaId", cinemaId)
				.setParameter("roomId", roomId)
				.setParameter("dateSession", dateSession)
				.setParameter("dateEnd", dateEnd)
				.setParameter("dateAux", dateAux).list();
	}
}