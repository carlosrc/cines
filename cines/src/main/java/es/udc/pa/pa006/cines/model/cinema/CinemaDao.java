package es.udc.pa.pa006.cines.model.cinema;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface CinemaDao extends GenericDao<Cinema, Long> {

	public List<Cinema> findCinemasByProvince(long provinceId);
	
	
	
}
