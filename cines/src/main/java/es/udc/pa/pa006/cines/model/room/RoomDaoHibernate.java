package es.udc.pa.pa006.cines.model.room;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("roomDao")
public class RoomDaoHibernate extends GenericDaoHibernate<Room, Long> implements
		RoomDao {

	@SuppressWarnings("unchecked")
	public List<Room> findRoomsByCinema(long cinemaId) {
		return getSession()
				.createQuery(
						"SELECT r FROM Room r WHERE "
								+ "r.cinema.cinemaId = :cinemaId ORDER BY r.name")
				.setParameter("cinemaId", cinemaId).list();
	}

}