package es.udc.pa.pa006.cines.model.cinema;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("cinemaDao")
public class CinemaDaoHibernate extends GenericDaoHibernate<Cinema, Long>
	implements CinemaDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Cinema> findCinemasByProvince(long provinceId) {
	return getSession()
		.createQuery(
			"SELECT c FROM Cinema c WHERE c.province.provinceId = :provinceId ORDER BY c.name")
		.setParameter("provinceId", provinceId).list();
    }

}