package es.udc.pa.pa006.cines.model.buy;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface BuyDao extends GenericDao<Buy, Long> {

	public List<Buy> findBuysByUser(long userId, int startIndex, int count);
	
}
