package es.udc.pa.pa006.cines.model.buy;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("buyDao")
public class BuyDaoHibernate extends GenericDaoHibernate<Buy, Long> implements
	BuyDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Buy> findBuysByUser(long userId, int startIndex, int count) {
	return getSession()
		.createQuery(
			"SELECT b FROM Buy b WHERE "
				+ "b.userProfile.userProfileId = :userId ORDER BY b.buyDate DESC")
		.setParameter("userId", userId).setFirstResult(startIndex)
		.setMaxResults(count).list();
    }

}