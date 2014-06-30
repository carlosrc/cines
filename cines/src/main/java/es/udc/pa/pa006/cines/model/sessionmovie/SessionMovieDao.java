package es.udc.pa.pa006.cines.model.sessionmovie;

import java.util.Calendar;
import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface SessionMovieDao extends GenericDao<SessionMovie, Long> {
	
	public List<SessionMovie> findSessionsByCinema(Calendar date, long cinemaId);
	
	public List<SessionMovie> findOtherSessionsMovie(long cinemaId, long roomId, Calendar dateSession, int movieDuration);

}
