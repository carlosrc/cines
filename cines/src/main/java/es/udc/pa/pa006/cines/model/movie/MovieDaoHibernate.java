package es.udc.pa.pa006.cines.model.movie;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("movieDao")
public class MovieDaoHibernate extends GenericDaoHibernate<Movie, Long>
	implements MovieDao {

	@SuppressWarnings("unchecked")
	public List<Movie> findMoviesByDate(Calendar date) {
		return getSession()
				.createQuery(
						"SELECT m FROM Movie m WHERE "
								+ "initDate <= :date AND endDate >= :date ORDER BY m.title")
				.setParameter("date", date).list();
	}

}