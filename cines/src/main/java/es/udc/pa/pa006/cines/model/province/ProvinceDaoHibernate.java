package es.udc.pa.pa006.cines.model.province;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("provinceDao")
public class ProvinceDaoHibernate extends GenericDaoHibernate<Province, Long>
	implements ProvinceDao {

    @SuppressWarnings("unchecked")
    @Override
    public List<Province> findAllProvinces() {
	return getSession().createQuery(
		"SELECT p FROM Province p ORDER BY p.name").list();
    }

}