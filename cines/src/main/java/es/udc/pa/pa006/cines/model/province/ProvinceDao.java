package es.udc.pa.pa006.cines.model.province;

import java.util.List;

import es.udc.pojo.modelutil.dao.GenericDao;

public interface ProvinceDao extends GenericDao<Province, Long> {

	public List<Province> findAllProvinces();
	
}
