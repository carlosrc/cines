package es.udc.pa.pa006.cines.model.sessionmovie;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa006.cines.model.movie.Movie;
import es.udc.pa.pa006.cines.model.room.Room;
import es.udc.pa.pa006.cines.web.util.Constants;

@Entity
@BatchSize(size=Constants.BATCHSIZE)
public class SessionMovie {

    private Long sessionMovieId;
    private Calendar dateSession;
    private int avaliableSeats;
    private Double price;
    private Movie movie;
    private Room room;
    private long version;

    public SessionMovie() {
    }

    public SessionMovie(Calendar dateSession) {
	this.dateSession = dateSession;
	this.price = 0.0;
    }


    @SequenceGenerator( // It only takes effect for
    name = "SessionMovieIdGenerator", // databases providing identifier
    sequenceName = "SessionMovieSeq")
    // generators.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SessionMovieIdGenerator")
    public Long getSessionMovieId() {
	return sessionMovieId;
    }

    public void setSessionMovieId(Long sessionMovieId) {
	this.sessionMovieId = sessionMovieId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getDateSession() {
	return dateSession;
    }

    public void setDateSession(Calendar dateSession) {
	this.dateSession = dateSession;
    }

    public int getAvaliableSeats() {
    return avaliableSeats;
    }

    public void setAvaliableSeats(int avaliableSeats) {
    this.avaliableSeats = avaliableSeats;
    }

    public Double getPrice() {
    return price;
    }
    
    public void setPrice(Double price) {
    this.price = price;
    }
    
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId")
    public Movie getMovie() {
	return movie;
    }

    public void setMovie(Movie movie) {
	this.movie = movie;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "roomId")
    public Room getRoom() {
	return room;
    }

    public void setRoom(Room room) {
	this.room = room;
    }
	
    @Version
	public long getVersion() {
	return version;
	}
    
    public void setVersion(long version) {
    	this.version = version;
    }

	@Override
	public String toString() {
		return "SessionMovie [sessionMovieId=" + sessionMovieId
				+ ", dateSession=" + dateSession + ", avaliableSeats="
				+ avaliableSeats + ", price=" + price + ", movie=" + movie
				+ ", room=" + room + ", version=" + version + "]";
	}

}
