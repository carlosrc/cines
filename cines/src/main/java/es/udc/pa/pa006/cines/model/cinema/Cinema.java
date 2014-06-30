package es.udc.pa.pa006.cines.model.cinema;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;

import es.udc.pa.pa006.cines.model.province.Province;
import es.udc.pa.pa006.cines.web.util.Constants;

@Entity
@Immutable
@BatchSize(size=Constants.BATCHSIZE)
public class Cinema {

    private Long cinemaId;
    private String name;
    private Double price;
    private Province province;

    public Cinema() {
    }

    public Cinema(String name, Double price, Province province) {
    	this.name = name;
    	this.price = price;
    	this.province = province;
    }

    @SequenceGenerator( // It only takes effect for
    name = "CinemaIdGenerator", // databases providing identifier
    sequenceName = "CinemaSeq")
    // generators.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "CinemaIdGenerator")
    public Long getCinemaId() {
	return cinemaId;
    }

    public void setCinemaId(Long cinemaId) {
	this.cinemaId = cinemaId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Double getPrice() {
	return price;
    }

    public void setPrice(Double price) {
	this.price = price;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provinceId")
    public Province getProvince() {
	return province;
    }

    public void setProvince(Province province) {
	this.province = province;
    }

}
