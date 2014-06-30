package es.udc.pa.pa006.cines.model.room;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface RoomDao extends GenericDao<Room, Long> {

	public List<Room> findRoomsByCinema(long cinemaId);

}
