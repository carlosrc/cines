package es.udc.pa.pa006.cines.model.province;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Immutable;

import es.udc.pa.pa006.cines.model.cinema.Cinema;

@Entity
@Immutable
public class Province {

    private Long provinceId;
    private String name;
    private Set<Cinema> cinemas;

    public Province() {
    }

    public Province(String name, Set<Cinema> cinemas) {
    	this.name = name;
    	this.cinemas = cinemas;
    }

    @SequenceGenerator( // It only takes effect for
    name = "ProvinceIdGenerator", // databases providing identifier
    sequenceName = "ProvinceSeq")
    // generators.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "ProvinceIdGenerator")
    public Long getProvinceId() {
	return provinceId;
    }

    public void setProvinceId(Long provinceId) {
	this.provinceId = provinceId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @OneToMany(mappedBy = "province")
    public Set<Cinema> getCinemas() {
	return cinemas;
    }

    public void setCinemas(Set<Cinema> cinemas) {
	this.cinemas = cinemas;
    }

}
