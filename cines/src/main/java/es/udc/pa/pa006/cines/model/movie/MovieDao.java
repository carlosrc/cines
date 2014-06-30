package es.udc.pa.pa006.cines.model.movie;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface MovieDao extends GenericDao<Movie, Long> {

	public List<Movie> findMoviesByDate(Calendar date);
	
}
