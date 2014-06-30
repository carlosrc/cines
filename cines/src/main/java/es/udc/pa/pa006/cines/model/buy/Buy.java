package es.udc.pa.pa006.cines.model.buy;

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

import es.udc.pa.pa006.cines.model.sessionmovie.SessionMovie;
import es.udc.pa.pa006.cines.model.userprofile.UserProfile;

@Entity
public class Buy {

	private Long buyId;
	private int tickets;
	private boolean delivered;
	private String cardNumber;
	private Calendar expirationDate;
	private Calendar buyDate;
	private SessionMovie sessionMovie;
	private UserProfile userProfile;

	public Buy() {
	}

	public Buy(int tickets, String cardNumber, Calendar expirationDate) {
		this.tickets = tickets;
		this.delivered = false;
		this.cardNumber = cardNumber;
		this.expirationDate = expirationDate;
		this.buyDate = Calendar.getInstance();
	}

	public Buy(int tickets, String cardNumber, Calendar expirationDate,
			SessionMovie sessionMovie, UserProfile userProfile) {
		this.tickets = tickets;
		this.delivered = false;
		this.cardNumber = cardNumber;
		this.expirationDate = expirationDate;
		this.buyDate = Calendar.getInstance();
		this.sessionMovie = sessionMovie;
		this.userProfile = userProfile;
	}

	@SequenceGenerator( // It only takes effect for
	name = "BuyIdGenerator", // databases providing identifier
	sequenceName = "BuySeq")
	// generators.
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BuyIdGenerator")
	public Long getBuyId() {
		return buyId;
	}

	public void setBuyId(Long buyId) {
		this.buyId = buyId;
	}

	public int getTickets() {
		return tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@Temporal(TemporalType.DATE)
	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "sessionMovieId")
	public SessionMovie getSessionMovie() {
		return sessionMovie;
	}

	public void setSessionMovie(SessionMovie sessionMovie) {
		this.sessionMovie = sessionMovie;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "usrId")
	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getBuyDate() {
		return buyDate;
	}

	public void setBuyDate(Calendar buyDate) {
		this.buyDate = buyDate;
	}

	public boolean getDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

}
