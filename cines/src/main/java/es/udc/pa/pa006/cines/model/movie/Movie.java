package es.udc.pa.pa006.cines.model.movie;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Immutable;

import es.udc.pa.pa006.cines.web.util.Constants;

@Entity
@Immutable
@BatchSize(size = Constants.BATCHSIZE)
public class Movie {

    private Long movieId;
    private String title;
    private String summary;
    private int duration;
    private Calendar initDate;
    private Calendar endDate;

    public Movie() {
    }

    public Movie(String tittle, String summary, int duration,
	    Calendar initDate, Calendar endDate) {
	this.title = tittle;
	this.summary = summary;
	this.duration = duration;
	this.initDate = initDate;
	this.endDate = endDate;
    }

    @SequenceGenerator( // It only takes effect for
    name = "MovieIdGenerator", // databases providing identifier
    sequenceName = "MovieSeq")
    // generators.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "MovieIdGenerator")
    public Long getMovieId() {
	return movieId;
    }

    public void setMovieId(Long movieId) {
	this.movieId = movieId;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    public int getDuration() {
	return duration;
    }

    public void setDuration(int duration) {
	this.duration = duration;
    }

    @Temporal(TemporalType.DATE)
    public Calendar getInitDate() {
	return initDate;
    }

    public void setInitDate(Calendar initDate) {
	this.initDate = initDate;
    }

    @Temporal(TemporalType.DATE)
    public Calendar getEndDate() {
	return endDate;
    }

    public void setEndDate(Calendar endDate) {
	this.endDate = endDate;
    }

}
